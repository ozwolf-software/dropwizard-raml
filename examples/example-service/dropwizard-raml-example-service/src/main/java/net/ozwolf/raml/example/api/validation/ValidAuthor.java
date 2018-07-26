package net.ozwolf.raml.test.api.validation;

import net.ozwolf.raml.test.core.service.AuthorService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(validatedBy = ValidAuthor.Validator.class)
@Documented
public @interface ValidAuthor {
    String message() default "must be an author that we know of";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidAuthor[] value();
    }

    class Validator implements ConstraintValidator<ValidAuthor, Integer> {
        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null)
                return true;

            return AuthorService.INSTANCE.find(value).isPresent();
        }

        @Override
        public void initialize(ValidAuthor annotation) {

        }
    }
}
