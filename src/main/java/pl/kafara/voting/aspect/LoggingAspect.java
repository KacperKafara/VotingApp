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
    @Pointcut("@within(transactional)")
    private void transactionalMethods(Transactional transactional) {
    }

    @Around(value = "transactionalMethods(transactional)", argNames = "jp, transactional")
    private Object logTransaction(ProceedingJoinPoint jp, Transactional transactional) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "<anonymous>";

        if(authentication != null) {
            try {
                DecodedJWT jwt =  JWT.decode((String) authentication.getPrincipal());
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

//        String callerClass = jp.getTarget().getClass().getName();
        String callerMethod = jp.getSignature().getName();
        String txId = UUID.randomUUID().toString();

        Object obj;
        if(TransactionSynchronizationManager.isActualTransactionActive()) {
            try {
                txId = String.valueOf(UUID.fromString(Objects.requireNonNull(TransactionSynchronizationManager.getCurrentTransactionName())));
                log.info("Continuing existing transaction {} with propagation {} in method {}.{}", txId, transactional.propagation(), callerClass, callerMethod);
            } catch (IllegalArgumentException ignored) {
                TransactionSynchronizationManager.setCurrentTransactionName(txId);
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationLogger(txId));
                log.info("Transaction {} started by {} in {}.{}", txId, username, callerClass, callerMethod);
            }
            log.info("Transaction {} info: propagation={}, isolation={}, readOnly={}", txId, transactional.propagation(), transactional.isolation(), transactional.readOnly());

            String args = parsArgs(jp);
            if (args != null) {
                log.info("Method {}.{} called by {} called in transaction {} with args: {}", callerClass, callerMethod, username, txId, args);
            } else {
                log.info("Method {}.{} called by {} called in transaction {}", callerClass, callerMethod, username, txId);
            }
            try{
                obj = jp.proceed();
            } catch (Throwable e) {
                log.error("Method {}.{} called by {} failed in transaction {} due {} with message {}", callerClass, callerMethod, username, txId, e.getClass().getName(), e.getMessage());
                throw e;
            }
            String returnValue = parseReturnValue(obj);
            if (returnValue != null) {
                log.info("Method {}.{} called by {} returned in transaction {} with: {}", callerClass, callerMethod, username, txId, returnValue);
            } else {
                log.info("Method {}.{} called by {} returned in transaction {}", callerClass, callerMethod, username, txId);
            }
        } else {
            String args = parsArgs(jp);
            if (args != null) {
                log.info("Method {}.{} called by {} with args: {}", callerClass, callerMethod, username, args);
            } else {
                log.info("Method {}.{} called by {}", callerClass, callerMethod, username);
            }
            try{
                obj = jp.proceed();
            } catch (Throwable e) {
                throw e;
            }
            String returnValue = parseReturnValue(obj);
            if (returnValue != null) {
                log.info("Method {}.{} called by {} returned with: {}", callerClass, callerMethod, username, returnValue);
            } else {
                log.info("Method {}.{} called by {} returned", callerClass, callerMethod, username);
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
            if (parameterName.equals("password") || parameterName.equals("token")) {
                sb.append("**********");
                continue;
            }
            if(args[j] == null) {
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
