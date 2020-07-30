package com.example.orderorder.data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;
import android.view.ActionMode;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.orderorder.data.model.LoggedInUser;
import com.example.orderorder.ui.loginTemplate.LoginActivity;
import com.example.orderorder.ui.loginTemplate.LoginViewModel;
import com.example.orderorder.ui.loginTemplate.LoginViewModelFactory;
import com.example.orderorder.ui.loginTemplate.RegisterActivity;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

import static java.lang.Thread.sleep;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

interface LoginCallback<T> {
    void onComplete(Result<T> result);
}

public class LoginDataSource {


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    private Result<LoggedInUser> loginResult;
    void setLoginResult(Result<LoggedInUser> res){
        loginResult = res;
    }
    Result<LoggedInUser> getLoginResult() {
        Log.d("getLogRes", "ajettu4a");
        try {
            //Log.d("getLogRes1", "Tulos: " + loginResult.toString());
            Log.d("getLogRes", "ajettu4");
        } catch (Exception e) {
            Log.d("getLogRes", "Virhe1::" + e.getMessage());
        }
        return loginResult;
    }

    private static int cores = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(cores +1);

    public static ExecutorService getExecutor() {
        return executor;
    }



    public void stop() {
       executor.shutdown();
    }
    /*
    private void completeLogin(String username, String password, String firstName, String lastName) {
        Log.d("Cores:::::::", Integer.toString(cores));
        login(username, password, firstName, lastName);
    }*/

    public Result<LoggedInUser> getLoginRes(final String username, final String password, final String firstName, final String lastName)  {
        final Executor exec = (Executor) getExecutor();
        //Result<LoggedInUser> logResult = new Result<>();
        Runnable logres = new Runnable() {
            @Override
            public void run() {

                try {
                    Log.d("getLogRes", "sleep1");
                    //login(username, password, firstName, lastName);
                    Thread.sleep(5);
                    Log.d("getLogRes", "sleep2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("getLogRes", "ajettu1sleepvirhe");
                }
                Log.d("getLogRes", "ajettu1");
            }
        };

        /*try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("executor","virhe:" + e.getMessage());
        }*/
        Log.d("getLogRes", "ajettu2");

        //new Thread(logres).start();
        try {
            Log.d("getLogRes", "sleep1");
            //login(username, password, firstName, lastName);
            Thread.sleep(50);
            Log.d("getLogRes", "sleep2");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("getLogRes", "ajettu1sleepvirhe");
        }
        Log.d("getLogRes", "ajettu1");

        return getLoginResult();
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("executor","virhe:" + e.getMessage());
        }*/
        /*
        Future<Result<LoggedInUser>> resultFuture = null;
        resultFuture = executor.submit(new Callable<Result<LoggedInUser>>() {
            @Override
            public Result<LoggedInUser> call()  {
                return getLoginResult();
            }
        });*/


/*

        try {
            Log.d("getLogRes", "ajettu2 viesti"+resultFuture.get().toString());
            return resultFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("getLogRes", "virheajettu3");
            return new Result<>(e);
        } catch (ExecutionException e) {
            Log.d("getLogRes", "virheajettu3");
            e.printStackTrace();
            return new Result<>(e);
        } catch (Exception e) {
            Log.d("getLogRes", "virheajettu3");
            return new Result<>(e);
        }*/



    }

    public void login(final String email, final String password, final String firstName, final String lastName, final LoginCallback<LoggedInUser> callback) {
        Log.d("getLogResF", "Ajettu2 alkaa firebaseFunc");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("getLogResF", "Ajettu2 firebaseFunc");
                        if (task.isComplete()) {
                                    try {
                                        sleep(400);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        Log.d("executor","virhe:" + e.getMessage());
                                    }
                            Log.d("getLogResF", "complete firebaseFunc");
                            if (task.isSuccessful()) {
                                Log.d("getLogResF", "success firebaseFunc");
                                try {
                                    userID = fAuth.getCurrentUser().getUid();
                                    LoggedInUser user =
                                            new LoggedInUser(
                                                    userID, email,
                                                    firstName, lastName);
                                    setLoginResult(new Result(user));
                                    //Tallenna käyttäjä Firestore databaseen:
                                    DocumentReference documentReference = fStore.collection("users").document(userID);
                                    Map<String, Object> newUser = new HashMap<>();
                                    newUser.put("email", email);
                                    newUser.put("firstName", firstName);
                                    newUser.put("lastName", lastName);
                                    documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore","User registered in Firestore");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Firestore","User registration failed in Firestore");
                                        }
                                    });
                                    callback.onComplete(getLoginResult());

                                    Log.d("getLogResF", "Ajettu2 firebase Success");

                                } catch (Exception e) {
                                    Log.d("getLogResF", e.getMessage());
                                    setLoginResult(new Result(new IOException("Error logging in: " + e.getMessage())));
                                    callback.onComplete(getLoginResult());


                                }
                            } else {
                                Log.d("getLogResF", task.getException().getMessage());
                                setLoginResult(new Result(new IOException("Error logging in: " + task.getException().getMessage())));
                                callback.onComplete(getLoginResult());

                            }
                        } else {
                            Log.d("getLogResF", "Firebase ei valmistunu");
                            setLoginResult(new Result(new IOException("Error logging in: " + task.getException().getMessage())));
                            callback.onComplete(getLoginResult());
                        }
                    }
                });




    }

    public void login(final String email, final String password, final LoginCallback<LoggedInUser> callback) {
        Log.d("getLogResF", "Ajettu2 alkaa firebaseFunc");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("getLogResF", "Ajettu2 firebaseFunc");
                if (task.isComplete()) {
                    try {
                        sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("executor","virhe:" + e.getMessage());
                    }
                    Log.d("getLogResF", "complete firebaseFunc");
                    if (task.isSuccessful()) {
                        Log.d("getLogResF", "success firebaseFunc");
                        try {
                            userID = fAuth.getCurrentUser().getUid();
                            LoggedInUser user =
                                    new LoggedInUser(
                                            userID, email
                                            );
                            setLoginResult(new Result(user));

                            callback.onComplete(getLoginResult());

                            Log.d("getLogResF", "Ajettu2 firebase Success");

                        } catch (Exception e) {
                            Log.d("getLogResF", e.getMessage());
                            setLoginResult(new Result(new IOException("Error logging in: " + e.getMessage())));
                            callback.onComplete(getLoginResult());


                        }
                    } else {
                        Log.d("getLogResF", task.getException().getMessage());
                        setLoginResult(new Result(new IOException("Error logging in: " + task.getException().getMessage())));
                        callback.onComplete(getLoginResult());

                    }
                } else {
                    Log.d("getLogResF", "Firebase ei valmistunu");
                    setLoginResult(new Result(new IOException("Error logging in: " + task.getException().getMessage())));
                    callback.onComplete(getLoginResult());
                }
            }
        });




    }



    /*public Result<LoggedInUser> login(String username, String password, String firstName, String lastName) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }*/

    public Result<LoggedInUser> login(String email, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result<>(fakeUser);
        } catch (Exception e) {
            return new Result(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}


/*
        fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    result[0] = false;
                    String errorCode = task.getException().toString();
                    Log.d("Firebase", "firebase authentication virhe" + errorCode);
                    try {
                        throw task.getException();
                   /* } catch (FirebaseAuthUserCollisionException e) {
                        Log.d("Firebase e", e.getMessage());
                        res[0] = new Result.Error(new IOException("Error logging in: " + errorCode, e));
                    } catch (Exception e) {
        Log.d("Firebase e", e.getMessage());
        res[0] = new Result.Error(new IOException("Error logging in: " + errorCode, e));
        }
        } else {
        result[0] = true;
        }
        }

        });
 */

/*
        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser user =
                    new LoggedInUser(
                            fAuth.getUid(),
                            firstName, firstName, lastName);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }*/

/* .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {

                    //resultError = callBack.getCallbackErr(password, firstName, lastName);
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        String errorCode = task.getException().toString();
                        Log.d("Firebase e", e.getMessage());
                        resultError = new Result.Error(new IOException("Error logging in: " + errorCode, e));
                    }

                } else {

                }


            }


        })
        */
