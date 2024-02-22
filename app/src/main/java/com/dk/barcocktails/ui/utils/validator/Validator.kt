package com.dk.barcocktails.ui.utils.validator

import java.util.regex.Pattern

object Validator {
    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[a-zA-Z])" +
                "(?=\\S+$)" +
                ".{6,}" +
                "$"
    )

    fun validateEmail(email: String): Pair<Boolean, ErrorEnum?> {
        return if (email.isEmpty()) {
            Pair(false, ErrorEnum.REQUIRE)
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Pair(false, ErrorEnum.VALID)
        } else {
            Pair(true, null)
        }
    }

    fun validateLoginPassword(password: String): Pair<Boolean, ErrorEnum?> {
        return if (password.isEmpty()) {
            Pair(false, ErrorEnum.REQUIRE)
        } else {
            Pair(true, null)
        }
    }

    fun validatePassword(password: String): Pair<Boolean, ErrorEnum?> {
        return if (password.isEmpty()) {
            Pair(false, ErrorEnum.REQUIRE)
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Pair(false, ErrorEnum.VALID)
        } else {
            Pair(true, null)
        }
    }
}