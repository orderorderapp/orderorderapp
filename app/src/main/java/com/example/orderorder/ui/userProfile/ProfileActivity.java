package com.example.orderorder.ui.userProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.orderorder.R;
import com.example.orderorder.SessionManager;
import com.example.orderorder.data.model.LoggedInUser;
import com.example.orderorder.ui.loginTemplate.LoginFormState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    Button updateButton;
    //ProgressBar loadingProgressBar;
    private DocumentReference dref;
    private FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private String userId;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        firstNameEditText = findViewById(R.id.register_first_name);
        lastNameEditText = findViewById(R.id.register_last_name);
        updateButton = findViewById(R.id.registerButton);
        ProgressBar loadingProgressBar = findViewById(R.id.register_Progress);
        sessionManager = new SessionManager(getApplicationContext());



        initDetails();

    }


        private void initDetails() {


            userId = sessionManager.getUid();
            Log.d("userid:", userId);

            dref = fs.collection("users").document(userId);

            dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null) {
                            firstNameEditText.setText(doc.getString("firstName"));
                            lastNameEditText.setText(doc.getString("lastName"));
                            usernameEditText.setText(doc.getString("email"));

                        } else {
                            Toast.makeText(getApplicationContext(),"Virhe tietoja ladattaessa", Toast.LENGTH_LONG);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Virhe tietoja ladattaessa", Toast.LENGTH_LONG);
                    }
                }
            });

            TextWatcher afterTextChangedListener = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // ignore
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // ignore
                }

                @Override
                public void afterTextChanged(Editable s) {
                    loginDataChanged(usernameEditText.getText().toString(),
                            firstNameEditText.getText().toString(),
                            lastNameEditText.getText().toString());
                }
            };

            usernameEditText.addTextChangedListener(afterTextChangedListener);
            firstNameEditText.addTextChangedListener(afterTextChangedListener);
            lastNameEditText.addTextChangedListener(afterTextChangedListener);


            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //loadingProgressBar.setVisibility(View.VISIBLE);
                    updateDetails(
                    usernameEditText.getText().toString(),
                            firstNameEditText.getText().toString(),
                            lastNameEditText.getText().toString()
                    );
                }
            });




        }

    private void updateDetails(String email, String firstName, String lastName) {
        Map<String, Object> info = new HashMap<>();
        info.put("email", email);
        info.put("firstName", firstName);
        info.put("lastName", lastName);
        try {
            dref.update(info);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Tietojen päivitys epäonnistui.",Toast.LENGTH_SHORT);
        }

        finish();
    }

    public void loginDataChanged(String username,  String firstName, String lastName) {
        if (isUserNameValid(username) && isNameValid(firstName) && isNameValid(lastName)  ) {
            updateButton.setEnabled(true);
        } else {
            updateButton.setEnabled(false);
        }
    }

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

}