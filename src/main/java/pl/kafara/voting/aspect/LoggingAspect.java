package pl.kafara.voting.aspect;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    private void transactionalMethod() {
    }

    @Pointcut("@within(org.springframework.transaction.annotation.Transactional) && !@annotation(org.springframework.transaction.annotation.Transactional)")
    private void transactionalClassOnly() {
    }

    @Pointcut("transactionalMethod() || transactionalClassOnly()")
    private void transactionalLog() {
    }

    private static final ThreadLocal<String> transactionIdThreadLocal = new ThreadLocal<>();

    @Around(value = "transactionalLog()", argNames = "jp")
    private Object logTransaction(ProceedingJoinPoint jp) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "<anonymous>";

        if (authentication != null) {
            try {
                DecodedJWT jwt = JWT.decode((String) authentication.getPrincipal());
                username = jwt.getClaim("username").toString();
            } catch (Exception e) {
                username = "<anonymous>";
            }
        }

        Object target = jp.getTarget();
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(target);
        String callerClass = clazz.getName();
        if (target instanceof Advised) {
            Class<?>[] interfaces = target.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (Repository.class.isAssignableFrom(anInterface)) {
                    callerClass = anInterface.getName();
                    break;
                }
            }
        }

        String callerMethod = jp.getSignature().getName();
        if(transactionIdThreadLocal.get() == null)
            transactionIdThreadLocal.set(UUID.randomUUID().toString());

        String useCaseId = transactionIdThreadLocal.get();
        String txId = UUID.randomUUID().toString();

        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();

        Transactional transactional = method.getAnnotation(Transactional.class);

        if (transactional == null) {
            transactional = jp.getTarget().getClass().getAnnotation(Transactional.class);
        }

        Object obj;
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            try {
                txId = String.valueOf(UUID.fromString(Objects.requireNonNull(TransactionSynchronizationManager.getCurrentTransactionName())));
                log.info("Continuing existing transaction {} with propagation {} in method {}.{}", txId, transactional.propagation(), callerClass, callerMethod);
            } catch (IllegalArgumentException ignored) {
                TransactionSynchronizationManager.setCurrentTransactionName(txId);
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationLogger(txId));
                log.info("Transaction {} started by {} in {}.{}", txId, username, callerClass, callerMethod);
            } catch (NullPointerException ignored) {
                obj = jp.proceed();
                return obj;
            }
            log.info("Transaction {} info: propagation={}, isolation={}, readOnly={}", txId, transactional.propagation(), transactional.isolation(), transactional.readOnly());

            String args = parsArgs(jp);
            if (args != null) {
                log.info("Method {}.{} called by {} called in transaction {}, with useCaseId {} with args: {}", callerClass, callerMethod, username, txId, useCaseId, args);
            } else {
                log.info("Method {}.{} called by {} called in transaction {}, with useCaseId {}", callerClass, callerMethod, username, txId, useCaseId);
            }
            try {
                obj = jp.proceed();
            } catch (Throwable e) {
                log.error("Method {}.{} called by {} failed in transaction {}, withUseCaseId {} due {} with message {}", callerClass, callerMethod, username, txId, useCaseId, e.getClass().getName(), e.getMessage());
                log.error("Exception:", e);
                throw e;
            }
            String returnValue = parseReturnValue(obj);
            if (returnValue != null) {
                log.info("Method {}.{} called by {} returned in transaction {}, with useCaseId {} with: {}", callerClass, callerMethod, username, txId, useCaseId, returnValue);
            } else {
                log.info("Method {}.{} called by {} returned in transaction {}, with useCaseId {}", callerClass, callerMethod, username, txId, useCaseId);
            }
        } else {
            String args = parsArgs(jp);
            if (args != null) {
                log.info("Method {}.{} called by {} with useCaseId: {}, with args: {}", callerClass, callerMethod, username, useCaseId, args);
            } else {
                log.info("Method {}.{} called by {} with useCaseId: {}", callerClass, callerMethod, username, useCaseId);
            }
            try {
                obj = jp.proceed();
            } catch (Throwable e) {
                log.error("Method {}.{} called by {} failed, withUseCaseId {} due {} with message {}", callerClass, callerMethod, username, useCaseId, e.getClass().getName(), e.getMessage());
                log.error("Exception:", e);
                throw e;
            }
            String returnValue = parseReturnValue(obj);
            if (returnValue != null) {
                log.info("Method {}.{} called by {} with useCaseId: {}, returned with: {}", callerClass, callerMethod, username, useCaseId, returnValue);
            } else {
                log.info("Method {}.{} called by {} with useCaseId: {}, returned", callerClass, callerMethod, username, useCaseId);
            }
        }
        return obj;
    }

    private String parsArgs(ProceedingJoinPoint jp) {
        Object[] args = jp.getArgs();
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        if (args == null || args.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < args.length; j++) {
            String parameterName = parameters[j].getName();
            sb.append(parameterName).append(": ");
            if (parameterName.equals("password") || parameterName.equals("token") || parameterName.equals("model")) {
                sb.append("**********");
                continue;
            }
            if (args[j] == null) {
                continue;
            }
            sb.append(args[j].toString());
            if (j < args.length - 1) sb.append(", ");

            if (args[j] instanceof List<?> entities) {
                for (int i = 0; i < entities.size(); i++) {
                    sb.append(entities.get(i).toString());
                    if (i < entities.size() - 1) sb.append(", ");
                }
            }
        }
        if (sb.isEmpty()) {
            return null;
        }
        return sb.toString();
    }

    private String parseReturnValue(Object args) {
        if (args == null) {
            return null;
        }

        return switch (args) {
            case List<?> entities -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < entities.size(); i++) {
                    sb.append(entities.get(i).toString());
                    if (i < entities.size() - 1) sb.append(", ");
                }
                yield sb.toString();
            }
            case Object object -> object.toString();
        };
    }
}
