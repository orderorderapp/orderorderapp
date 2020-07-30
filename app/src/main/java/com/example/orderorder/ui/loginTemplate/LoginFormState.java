package com.example.orderorder.ui.loginTemplate;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer firstNameError, @Nullable Integer lastNameError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.firstNameError = null;
        this.lastNameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getFirstNameError() {
        return firstNameError;
    }

    @Nullable
    Integer getLastNameError() {
        return lastNameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}