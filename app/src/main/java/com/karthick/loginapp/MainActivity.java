package com.karthick.loginapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements GoogleSignInApi {

    //FACEBOOK
    private final String TAG = "tag";
    private CallbackManager callbackManager;

    //GOOGLE
    private static final int RC_SIGN_IN = 201;
    private SignInButton googleSignInButton;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FACEBOOK
        LoginButton facebookLoginButton;
        facebookLoginButton = findViewById(R.id.login_facebook_button);

        callbackManager = CallbackManager.Factory.create();

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            //show error message to user
                        } else {
                            try {
                                //check json response
                                String name = json.getString("name");

                                Intent intent = new Intent(MainActivity.this, LoginPageFaceBook.class);
                                intent.putExtra("name", name);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        //GOOGLE
        googleSignInButton = findViewById(R.id.login_google_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    //ACTIVITY RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //FACEBOOK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //GOOGLE
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    //////////////////////////////// GOOGLE /////////////////////////////////

    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(MainActivity.this, LoginPageGoogle.class));
        }
        super.onStart();
    }

    @Override
    public Intent getSignInIntent(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public OptionalPendingResult<GoogleSignInResult> silentSignIn(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public PendingResult<Status> signOut(GoogleApiClient googleApiClient) {
        googleApiClient.disconnect();
        return null;
    }

    @Override
    public PendingResult<Status> revokeAccess(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public GoogleSignInResult getSignInResultFromIntent(Intent intent) {
        return null;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(MainActivity.this, LoginPageGoogle.class));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}