package com.example.tapwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity" ;
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private TextView logOutButton ,randomButton,inviteButton;
    private AppCompatButton signInButton;
    private View view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logOutButton = findViewById(R.id.logOutButton);
        randomButton = findViewById(R.id.randomButton);
        inviteButton = findViewById(R.id.inviteButton);
        signInButton = findViewById(R.id.signInButton);
        view2 = findViewById(R.id.view2);
        logOutButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        inviteButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        signIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }



    private void updateUI(GoogleSignInAccount account) {
        if(account!=null){
            signInButton.setVisibility(View.INVISIBLE);
            logOutButton.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        }else{

            signInButton.setVisibility(View.VISIBLE);
            logOutButton.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(GoogleSignInOptions gso) {

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        finish();
                        signInButton.setVisibility(View.VISIBLE);
                        logOutButton.setVisibility(View.GONE);
                        view2.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Signout complete", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                UserDetail userDetail = new UserDetail();
                userDetail.setPersonEmail(acct.getEmail());
                userDetail.setPersonName(acct.getDisplayName());
                userDetail.setPersonId(acct.getId());
                userDetail.setPersonPhoto(acct.getPhotoUrl());

                signInButton.setVisibility(View.INVISIBLE);
                logOutButton.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);


            }
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logOutButton:
                signOut(gso);
                break;
            case R.id.signInButton:
                signIn();
                break;
            case R.id.inviteButton:
                inviteFriend();
                break;
            case R.id.randomButton:
                randomPlay();
                break;
        }
    }

    private void randomPlay() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            signIn();
        }else{
            Toast.makeText(this, "random generated", Toast.LENGTH_SHORT).show();
        }
    }

    private void inviteFriend() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            signIn();
        }else{
            Toast.makeText(this, "Invite send", Toast.LENGTH_SHORT).show();
        }
    }
}