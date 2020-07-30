package com.example.orderorder.data;

import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.orderorder.data.model.LoggedInUser;
import com.example.orderorder.ui.loginTemplate.LoginViewModel;
import com.example.orderorder.ui.loginTemplate.LoginViewModelFactory;
import com.example.orderorder.ui.loginTemplate.RegisterActivity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Handler;

import javax.security.auth.callback.Callback;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private Result<LoggedInUser> loginResultRep;

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;


    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {

        this.dataSource = dataSource;

    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if(instance == null){
            instance = new LoginRepository(dataSource);
        }

        return instance;
    }



    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();

    }

    public Result<LoggedInUser> getLoginResult() {
        return dataSource.getLoginResult();
    }



    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }




    public void login(String username, String password, String firstName, String lastName, final RepositoryCallback<LoggedInUser> callback)  {
        // handle login
        //dataSource.login(username, password, firstName, lastName);
        try {
            dataSource.login(username, password, firstName, lastName, new LoginCallback<LoggedInUser>() {
                @Override
                public void onComplete(Result<LoggedInUser> result) {
                    loginResultRep = result;
                    callback.onComplete(loginResultRep);
                    Log.d("getLogResRep", "Repository success");
                }
            });
            /*if (result.getError() == null) {
                setLoggedInUser(result.getData());
            }*/


        } catch (Exception e) {
            Log.d("getLogResRep", "Repository error");
        }



    }



    /*
    public Result<LoggedInUser> login(String username, String password, String firstName, String lastName) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password, firstName, lastName);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }*/

    public void login(String username, String password, final RepositoryCallback<LoggedInUser> callback)  {
        // handle login
        //dataSource.login(username, password, firstName, lastName);
        try {
            dataSource.login(username, password, new LoginCallback<LoggedInUser>() {
                @Override
                public void onComplete(Result<LoggedInUser> result) {
                    loginResultRep = result;
                    callback.onComplete(loginResultRep);
                    Log.d("getLogResRep", "Repository success");
                }
            });
            /*if (result.getError() == null) {
                setLoggedInUser(result.getData());
            }*/


        } catch (Exception e) {
            Log.d("getLogResRep", "Repository error");
        }



    }
}