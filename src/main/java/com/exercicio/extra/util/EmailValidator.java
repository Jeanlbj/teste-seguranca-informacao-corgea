package com.exercicio.extra.util;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");

    public static boolean isValid(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }
}
