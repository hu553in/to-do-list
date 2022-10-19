package com.github.hu553in.to_do_list.validator;

import com.github.hu553in.to_do_list.constraint.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private boolean nullable;
    private int minLength;
    private int maxLength;

    @Override
    public void initialize(final Password constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        Collection<String> errors = validate(password);
        if (errors.isEmpty()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(String.join(", ", errors))
            .addConstraintViolation();
        return false;
    }

    private Collection<String> validate(final String password) {
        List<String> errors = new ArrayList<>();
        if (password == null) {
            if (!nullable) {
                errors.add("must not be null");
            }
            return errors;
        }
        int passwordLength = password.length();
        if (passwordLength < minLength || passwordLength > maxLength) {
            errors.add("size must be between %d and %d".formatted(minLength, maxLength));
        }
        boolean hasOneDigit = false;
        boolean hasOneUpperCaseLetter = false;
        boolean hasOneLowerCaseLetter = false;
        boolean hasOneSpecialCharacter = false;
        for (int character : password.toCharArray()) {
            if (Character.isDigit(character)) {
                hasOneDigit = true;
            } else if (Character.isLetter(character)) {
                if (Character.isUpperCase(character)) {
                    hasOneUpperCaseLetter = true;
                } else if (Character.isLowerCase(character)) {
                    hasOneLowerCaseLetter = true;
                }
            } else {
                hasOneSpecialCharacter = true;
            }
        }
        if (!hasOneDigit) {
            errors.add("must have at least 1 digit");
        }
        if (!hasOneUpperCaseLetter) {
            errors.add("must have at least 1 upper case letter");
        }
        if (!hasOneLowerCaseLetter) {
            errors.add("must have at least 1 lower case letter");
        }
        if (!hasOneSpecialCharacter) {
            errors.add("must have at least 1 special character");
        }
        return errors;
    }

}
