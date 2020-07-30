package com.example.orderorder.ui.loginTemplate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.orderorder.data.LoginRepository;
import com.example.orderorder.data.RepositoryCallback;
import com.example.orderorder.data.Result;
import com.example.orderorder.data.model.LoggedInUser;
import com.example.orderorder.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String firstName, String lastName) {
        // can be launched in a separate asynchronous job
       // loginRepository.login(username, password, firstName, lastName);
        loginRepository.login(username, password, firstName, lastName, new RepositoryCallback<LoggedInUser>() {
            @Override
            public void onComplete(Result<LoggedInUser> result) {
                if (result.getError() == null) {
                    LoggedInUser data = result.getData();
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }

            }
        });


    }


    public void loginFinTry() {
        Log.d("LogFinTry", "Tuli läpi");
    }


    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        // loginRepository.login(username, password, firstName, lastName);
        username = "1@a.fi";
        password = "999999";
        loginRepository.login(username, password, new RepositoryCallback<LoggedInUser>() {
            @Override
            public void onComplete(Result<LoggedInUser> result) {
                if (result.getError() == null) {
                    LoggedInUser data = result.getData();
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }

            }
        });


    }

    public void loginDataChanged(String username, String password, String firstName, String lastName) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null, null));
        } else if (!isNameValid(firstName)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_name, null, null, null));
        } else if (!isNameValid(lastName)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_name, null, null, null));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null, null));
        } else if (!isNameValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password_login, null, null));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
            return !name.trim().isEmpty();

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }



}