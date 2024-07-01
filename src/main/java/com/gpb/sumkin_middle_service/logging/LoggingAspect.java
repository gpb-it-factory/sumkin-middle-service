package com.gpb.sumkin_middle_service.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /** Регистрирует факт вызова метода пользователем с указанием переданной метки*/
    @Before(value = "@annotation(ActionAudit)")
    public void logUserAction(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String parameters = new ParametersMapper(
                signature.getParameterNames(), joinPoint.getArgs()).parse();
        log.info("{}. Метод: {}. Параметры: {}",
                getAction(signature), getMethodUri(), parameters);
    }

    /** Выводит в лог факт вызова каждого public метода в классе, обозначенного данной аннотацией*/
    @Before(value = "@within(LogMethodInvocation) || " +
            "@annotation(LogMethodInvocation)")
    public void before(JoinPoint joinPoint) {

        log.info("Метод вызван: {}", getMethodUri());
    }

    /** Выводит в лог факт окончания работы каждого public метода в классе, обозначенного данной аннотацией*/
    @After(value = "@within(LogMethodInvocation) || " +
            "@annotation(LogMethodInvocation)")
    public void after(JoinPoint joinPoint) {

        log.info("Метод завершён: {}", getMethodUri());
    }

    /** Выводит в лог сообщение об ошибке с указанием полного имени метода, в котором она произошла*/
    @AfterThrowing(value = "@within(LogMethodInvocation) || " +
            "@annotation(LogMethodInvocation)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.error("Ошибка во время выполнения метода {}.",
                signature.getDeclaringTypeName() + "." + signature.getMethod().getName() + "()");
    }

    private String getMethodUri() {
        //Get the HttpServletRequest currently bound to the thread.
        HttpServletRequest request;
        if (RequestContextHolder.getRequestAttributes() != null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getMethod() + " '" +
                    (request.getRequestURI()).replace(request.getContextPath(), "") + "'";
        }
        return "unknown";
    }

    private String getAction(MethodSignature signature) {
        Method method = signature.getMethod();
        ActionAudit aspectLogger = method.getAnnotation(ActionAudit.class);
        return aspectLogger.value();
    }
}
