package pl.kafara.voting.unit;

import dev.samstevens.totp.secret.SecretGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kafara.voting.exceptions.ApplicationOptimisticLockException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.*;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.model.users.tokens.ResetPasswordToken;
import pl.kafara.voting.users.dto.ChangePasswordRequest;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.dto.UpdateUserDataRequest;
import pl.kafara.voting.users.repositories.GenderRepository;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.repositories.VoterRoleRequestRepository;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.JwsService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private GenderRepository genderRepository;
    @Mock
    private JwsService jwsService;
    @Mock
    private AESUtils aesUtils;
    @Mock
    private VoterRoleRequestRepository voterRoleRequestRepository;
    @Mock
    SecretGenerator secretGenerator;
    @InjectMocks
    UserService userService;

    String email = "example@email.com";
    String token = "token";
    ResetPasswordFormRequest password = new ResetPasswordFormRequest("password");
    User user = new User();

    @Test
    public void ResetPassword_WhenUserNotFound_ShouldThrowNotFoundException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WhenUserIsBlocked_ShouldThrowAccountNotActiveException() {
        User user = new User();
        user.setBlocked(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(AccountNotActiveException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WhenUserIsNotVerified_ShouldThrowAccountNotActiveException() {
        User user = new User();
        user.setBlocked(false);
        user.setVerified(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(AccountNotActiveException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WhenUserIsNotBlockedAndIsVerified_ShouldNotThrowAnyException() {
        User user = new User();
        user.setBlocked(false);
        user.setVerified(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WithValidToken_ShouldResetPassword() throws NotFoundException, VerificationTokenUsedException, VerificationTokenExpiredException {
        ResetPasswordToken safeToken = new ResetPasswordToken(
                token,
                null,
                user
        );
        when(tokenService.validateResetPasswordToken(token)).thenReturn(safeToken);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(password.password())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> {
            userService.resetPassword(token, password);
        });
    }

    @Test
    public void ResetPassword_WithInvalidToken_ShouldThrowVerificationTokenExpiredException() throws VerificationTokenUsedException, VerificationTokenExpiredException {
        when(tokenService.validateResetPasswordToken(token)).thenThrow(new VerificationTokenExpiredException("Token expired"));

        assertThrows(VerificationTokenExpiredException.class, () -> {
            userService.resetPassword(token, password);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.USER);
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasNoRoles_ShouldThrowUserMustHaveAtLeastOneRoleException() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(UserMustHaveAtLeastOneRoleException.class, () -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasMultipleRoles_ShouldModifyRoles() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.USER);
        roles.add(UserRoleEnum.ADMINISTRATOR);
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.USER), new Role(UserRoleEnum.VOTER))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(new Role(UserRoleEnum.USER)));
        when(roleRepository.findByName(UserRoleEnum.ADMINISTRATOR)).thenReturn(Optional.of(new Role(UserRoleEnum.ADMINISTRATOR)));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));

        assertDoesNotThrow(() -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasVoterRoleAndNewRolesIncludeUserRole_ShouldRetainVoterRole() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.USER);
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.VOTER))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(new Role(UserRoleEnum.USER)));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));

        assertDoesNotThrow(() -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasVoterRoleAndNewRolesDoNotIncludeUserRole_ShouldNotRetainVoterRole() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.ADMINISTRATOR);
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.USER))));
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.VOTER))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(new Role(UserRoleEnum.USER)));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));
        when(roleRepository.findByName(UserRoleEnum.ADMINISTRATOR)).thenReturn(Optional.of(new Role(UserRoleEnum.ADMINISTRATOR)));

        assertDoesNotThrow(() -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasUserRoleAndNewRolesIncludeVoterRole_ShouldRetainUserRole() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.VOTER);
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.USER))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(new Role(UserRoleEnum.USER)));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));

        assertDoesNotThrow(() -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasUserRoleAndNewRolesDoNotIncludeVoterRole_ShouldNotRetainUserRole() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.MODERATOR);
        roles.add(UserRoleEnum.USER);
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.USER))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.MODERATOR)).thenReturn(Optional.of(new Role(UserRoleEnum.MODERATOR)));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(new Role(UserRoleEnum.USER)));
        when(roleRepository.findByName(UserRoleEnum.MODERATOR)).thenReturn(Optional.of(new Role(UserRoleEnum.MODERATOR)));

        assertDoesNotThrow(() -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserHasAdministratorRoleAndNewRolesDoNotIncludeUserRole_ShouldNotRetainAdministratorRole() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.MODERATOR);
        String tagValue = "tagValue";

        User user = new User();
        user.setRoles(new HashSet<>(Set.of(new Role(UserRoleEnum.ADMINISTRATOR))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.MODERATOR)).thenReturn(Optional.of(new Role(UserRoleEnum.MODERATOR)));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(new Role(UserRoleEnum.USER)));
        when(roleRepository.findByName(UserRoleEnum.MODERATOR)).thenReturn(Optional.of(new Role(UserRoleEnum.MODERATOR)));

        assertDoesNotThrow(() -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void BlockUser_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.block(userId, tagValue);
        });
    }

    @Test
    public void UnblockUser_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.unblock(userId, tagValue);
        });
    }

    @Test
    public void ChangePassword_WhenOldPasswordDoesNotMatch_ShouldThrowWrongPasswordException() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("oldPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())).thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> {
            userService.changePassword(changePasswordRequest, userId);
        });
    }

    @Test
    public void ChangePassword_WhenOldPasswordMatches_ShouldChangePassword() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("oldPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(changePasswordRequest.newPassword())).thenReturn("encodedNewPassword");

        assertDoesNotThrow(() -> {
            userService.changePassword(changePasswordRequest, userId);
        });
    }

    @Test
    public void UpdateUser_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        UpdateUserDataRequest userData = new UpdateUserDataRequest("username", "firstName", "lastName", "phoneNumber", "email", "MALE");
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(userData, userId, tagValue);
        });
    }

    @Test
    public void UpdateUser_WhenTagValueIsInvalid_ShouldThrowApplicationOptimisticLockException() {
        UUID userId = UUID.randomUUID();
        UpdateUserDataRequest userData = new UpdateUserDataRequest("username", "firstName", "lastName", "phoneNumber", "email", "MALE");
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(genderRepository.findByName(GenderEnum.valueOf(userData.gender()))).thenReturn(Optional.of(new Gender(GenderEnum.MALE)));
        when(jwsService.verifySignature(tagValue, user.getId(), user.getVersion())).thenReturn(true);

        assertThrows(ApplicationOptimisticLockException.class, () -> {
            userService.updateUser(userData, userId, tagValue);
        });
    }

    @Test
    public void UpdateUser_WhenValidData_ShouldUpdateUser() {
        UUID userId = UUID.randomUUID();
        UpdateUserDataRequest userData = new UpdateUserDataRequest("username", "firstName", "lastName", "phoneNumber", "email", "MALE");
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(genderRepository.findByName(GenderEnum.valueOf(userData.gender()))).thenReturn(Optional.of(new Gender(GenderEnum.MALE)));
        when(jwsService.verifySignature(tagValue, user.getId(), user.getVersion())).thenReturn(false);

        assertDoesNotThrow(() -> {
            userService.updateUser(userData, userId, tagValue);
        });
    }

    @Test
    public void Change2FaState_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.change2FaState(userId, true);
        });
    }

    @Test
    public void Change2FaState_WhenDeactivating_ShouldRemoveTotpSecret() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> {
            userService.change2FaState(userId, false);
        });
    }

    @Test
    public void Change2FaState_WhenActivatingAndAlreadyActive_ShouldThrowTotpAuthorisationException() {
        UUID userId = UUID.randomUUID();
        user.setAuthorisationTotpSecret("existingSecret");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(TotpAuthorisationException.class, () -> {
            userService.change2FaState(userId, true);
        });
    }

    @Test
    public void Change2FaState_WhenActivatingAndNotActive_ShouldSetTotpSecret() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(secretGenerator.generate()).thenReturn("secret");
        when(aesUtils.encrypt("secret")).thenReturn("encryptedSecret");

        assertDoesNotThrow(() -> {
            userService.change2FaState(userId, true);
        });
    }
}
