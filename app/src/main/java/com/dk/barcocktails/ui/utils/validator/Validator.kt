package com.dk.barcocktails.ui.utils.validator

import java.util.regex.Pattern

class Validator() {

    fun check(input: String, type: InputType): Pair<Boolean, ErrorEnum?> {
        return when (type) {
            InputType.EMAIL -> validateEmail(input)
            InputType.PASSWORD -> validatePassword(input)
        }
    }

    private fun validateEmail(email: String): Pair<Boolean, ErrorEnum?> {
        return if (email.isEmpty()) {
            Pair(false, ErrorEnum.REQUIRE)
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Pair(false, ErrorEnum.VALID)
        } else {
            Pair(true, null)
        }
    }

    private fun validatePassword(password: String): Pair<Boolean, ErrorEnum?> {
        return if (password.isEmpty()) {
            Pair(false, ErrorEnum.REQUIRE)
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Pair(false, ErrorEnum.VALID)
        } else {
            Pair(true, null)
        }
    }

    companion object {
        private val PASSWORD_PATTERN: Pattern = Pattern.compile(
            "^" + "(?=.*[a-zA-Z])" + "(?=\\S+$)" + ".{6,}" + "$"
        )
    }
}