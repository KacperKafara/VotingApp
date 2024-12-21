package pl.kafara.voting.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AESUtils {

    @Value("${security.encryption.secret-key}")
    private String secretKey;

    private static Key getKey(String secretKey) throws NoSuchAlgorithmException {
        byte[] key = secretKey.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = java.util.Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(String plainText) {
        if(!ObjectUtils.isEmpty(secretKey)) {
            Cipher cipher;
            try {
                cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, getKey(secretKey));
                return Base64.encodeBase64String(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                throw new RuntimeException("Error while encrypting: " + plainText, e);
            }
        } else {
            throw new RuntimeException("Key String Not Found");
        }
    }

    public String decrypt(String encryptedText) {
        if(!ObjectUtils.isEmpty(secretKey)) {
            Cipher cipher;
            try {
                cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, getKey(secretKey));
                return new String(cipher.doFinal(Base64.decodeBase64(encryptedText)), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException("Error while decrypting: " + encryptedText, e);
            }
        } else {
            throw new RuntimeException("Key String Not Found");
        }
    }
}
