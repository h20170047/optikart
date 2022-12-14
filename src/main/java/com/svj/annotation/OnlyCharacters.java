package com.svj.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ FIELD, TYPE_PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = OnlyCharactersValidation.class)
public @interface OnlyCharacters {
    String message() default "Entry should only contain characters";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
