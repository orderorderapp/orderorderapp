package com.example.orderorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.orderorder.adapters.SubAdapter;
import com.example.orderorder.ui.loginTemplate.LoginActivity;
import com.example.orderorder.ui.main.MainFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    //Button loginAgainButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();*/

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow();
/*
        loginAgainButton = findViewById(R.id.loginAgainButton);

        loginAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {



        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
