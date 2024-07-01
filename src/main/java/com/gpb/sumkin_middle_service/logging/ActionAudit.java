package com.gpb.sumkin_middle_service.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionAudit {

    String USER_ACTION = "РАБОТА С ДАННЫМИ ПОЛЬЗОВАТЕЛЯ";
    String ACCOUNT_ACTION = "РАБОТА СО СЧЕТАМИ";

    String value();
}
