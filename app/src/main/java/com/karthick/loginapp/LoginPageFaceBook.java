package com.karthick.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginPageFaceBook extends AppCompatActivity {

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_facebook);

        welcomeText = findViewById(R.id.welcome_text);

        //RECEIVE THE INTENT
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {

            //EXTRACT THE USERNAME FROM RECEIVED INTENT
            String userName = receivedIntent.getStringExtra("name");
            welcomeText.setText("Welcome " + userName);
        }
    }
}
