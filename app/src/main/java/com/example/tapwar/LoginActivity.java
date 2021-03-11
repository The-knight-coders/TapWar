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

import com.example.tapwar.classes.UserDetail;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Base64;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity" ;
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private TextView logOutButton ,randomButton,createRoomButton;
    private AppCompatButton signInButton;
    private View view2;
    private WebSocket webSocket;
    private final String SERVER_PATH = "ws://192.168.1.6:3000";
    private PopUpClass popUpClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadUI();
        signIn();
    }

    private void loadUI() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logOutButton = findViewById(R.id.logOutButton);
        randomButton = findViewById(R.id.randomButton);
        createRoomButton = findViewById(R.id.createRoomButton);
        signInButton = findViewById(R.id.signInButton);
        view2 = findViewById(R.id.view2);
        logOutButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        createRoomButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        initiateSocketConnection();
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

//                initiateSocketConnection();

            } else {
                Log.d(TAG, "handleSignInResult: account is null");
            }
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getMessage());
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
            case R.id.createRoomButton:
                createRoom(v);
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

    private void createRoom(View v) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            signIn();
        }else{
            popUpClass = new PopUpClass();

            //Changes made here
            popUpClass.showPopupWindow(v, this);

        }
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request,new SocketListner());

    }


    private class SocketListner extends WebSocketListener {
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);

            Log.d(TAG, "onMessage: " + text);
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(() ->{
                Toast.makeText(LoginActivity.this, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.d(TAG, "onFailure: " + t.getMessage());
        }
    }
}

/**
 * public static final MediaType JSON
 *     = MediaType.parse("application/json; charset=utf-8");
 *
 * OkHttpClient client = new OkHttpClient();
 *
 * String post(String url, String json) throws IOException {
 *   RequestBody body = RequestBody.create(JSON, json); // new
 *   // RequestBody body = RequestBody.create(JSON, json); // old
 *   Request request = new Request.Builder()
 *       .url(url)
 *       .post(body)
 *       .build();
 *   Response response = client.newCall(request).execute();
 *   return response.body().string();
 * }
 **/