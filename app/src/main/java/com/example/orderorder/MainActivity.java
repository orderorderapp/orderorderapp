package com.example.orderorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.orderorder.adapters.SubAdapter;
import com.example.orderorder.ui.loginTemplate.LoginActivity;
import com.example.orderorder.ui.main.MainFragment;
import com.example.orderorder.ui.main.NewSubFragment;
import com.example.orderorder.ui.main.PlanFragment;
import com.example.orderorder.ui.userProfile.ProfileActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    //Button loginAgainButton;
    Toolbar toolbar;

    SessionManager sessionManager;
    final int LAUNCH_LOGIN_ACTIVITY = 1000;


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
            startActivityForResult(intent, LAUNCH_LOGIN_ACTIVITY);
            //startActivity(intent);

            sessionManager = new SessionManager(getApplicationContext());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(data != null) {
                String uid = data.getStringExtra(USER_SERVICE);
                sessionManager.setLogin(true);
                sessionManager.setUid(uid);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userProfile:
                Intent intent = new Intent(this, ProfileActivity.class);

                startActivity(intent);
            case R.id.plan:
                Fragment planFragment = new PlanFragment();
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.container, planFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                /*getSupportFragmentManager().beginTransaction().replace(R.id.container, PlanFragment.newInstance("", "")  ).addToBackStack(null)

                        .commitNow();*/
                //.replace(R.id.container, PlanFragment.newInstance("", ""))

            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
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
