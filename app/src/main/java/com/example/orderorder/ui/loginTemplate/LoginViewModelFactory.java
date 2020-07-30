package com.example.orderorder.ui.loginTemplate;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.orderorder.data.LoginDataSource;
import com.example.orderorder.data.LoginRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {



    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}