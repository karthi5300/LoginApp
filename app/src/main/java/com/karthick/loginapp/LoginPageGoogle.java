package com.karthick.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginPageGoogle extends AppCompatActivity {

    private TextView welcomeText;
    private Button signOutButton;
    GoogleSignInClient mGoogleSignInClient;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_google);

        welcomeText = findViewById(R.id.welcome_text);
        signOutButton = findViewById(R.id.signout_button);

        //GET THE LOGGED IN ACCOUNT
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //GET USERNAME
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(LoginPageGoogle.this);
        if (signInAccount != null) {
            userName = signInAccount.getDisplayName();
            welcomeText.setText("Welcome " + userName);
        }

        //SIGNOUT
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                startActivity(new Intent((LoginPageGoogle.this), MainActivity.class));
                finish();
            }
        });
    }
}
