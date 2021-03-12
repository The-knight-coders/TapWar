package com.example.tapwar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.tapwar.classes.ServerResponseBody;
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
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView logOutButton ,joinRoomButton,createRoomButton;
    private AppCompatButton signInButton;
    private View view2;
    private WebSocket webSocket;
    private final String SERVER_PATH = "ws://192.168.0.108:3000";
//            shubh : ""ws://192.168.0.108:3000"";
    private PopUpCreateRoomClass popUpCreateRoomClass;
    private PopUpJoinRoomClass popUpJoinRoomClass;
    private GoogleSignInAccount googleSignInAccount;
    private UserDetail userDetail = new UserDetail();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadUI();
        signIn();

    }

    private void loadUI() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logOutButton = findViewById(R.id.logOutButton);
        joinRoomButton = findViewById(R.id.joinRoomButton);
        createRoomButton = findViewById(R.id.createRoomButton);
        signInButton = findViewById(R.id.signInButton);
        view2 = findViewById(R.id.view2);
        logOutButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        createRoomButton.setOnClickListener(this);
        joinRoomButton.setOnClickListener(this);

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
                        signInButton.setVisibility(View.VISIBLE);
                        logOutButton.setVisibility(View.GONE);
                        view2.setVisibility(View.GONE);

                        if (webSocket != null) {
                            // todo implement logout
                        }
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
                userDetail = new UserDetail();
                userDetail.setPersonEmail(acct.getEmail());
                userDetail.setPersonName(acct.getDisplayName());
                userDetail.setPersonId(acct.getId());
                userDetail.setPersonPhoto(acct.getPhotoUrl());

                signInButton.setVisibility(View.INVISIBLE);
                logOutButton.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                loginToServer();
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
            case R.id.joinRoomButton:
                joinRoom(v);
                break;
        }
    }

    private void joinRoom(View v) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            signIn();
        }else{
            popUpJoinRoomClass = new PopUpJoinRoomClass() {
                @Override
                public void onPopup() {
                    this.showPopupWindow(v,getParent());
                }

                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.cancelJoinButton:
                            this.dismissPopup(v);
                            break;
                        case R.id.enterRoomButton:
                            setEnteredCode();
                            ServerResponseBody body = new ServerResponseBody(account.getEmail()
                                    ,getEnteredCode()
                                    ,ServerResponseBody.REQUEST_JOIN_GAME);

                            webSocket.send(body.toJson());
                            Toast.makeText(getApplicationContext(), body.toString(), Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };

            popUpJoinRoomClass.onPopup();

        }
    }

    private void createRoom(View v) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            signIn();
        }else{
            popUpCreateRoomClass = new PopUpCreateRoomClass() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.shareButton) {
                        String message = "Hey Welcome to Tap war \nTo join Out game Copy The Code : " + popUpCreateRoomClass.getRoomCode();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_SEND);
                        i.setType("text/plain");

                        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Joining Code");
                        i.putExtra(android.content.Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(i,"Share"));
                        //As an example, display the message
                        Toast.makeText(getApplicationContext(), "Wow, share action button", Toast.LENGTH_SHORT).show();
                    } else if (view.getId() == R.id.cancelRoomButton) {
                        ServerResponseBody body = new ServerResponseBody(account.getEmail(), popUpCreateRoomClass.getRoomCode(), ServerResponseBody.REQUEST_CANCEL_GAME);
                        webSocket.send(body.toJson());
                        this.dismissPopup(v);
                    }
                }

                @Override
                public void onPopup() {
                    this.showPopupWindow(v,getParent());
                    ServerResponseBody body = new ServerResponseBody(null, null, ServerResponseBody.REQUEST_CREATE_GAME);
                    webSocket.send(body.toJson());
                }
            };

            popUpCreateRoomClass.onPopup();
        }
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request,new SocketListner());

//        loginToServer();
    }

    private void loginToServer() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            ServerResponseBody body = new ServerResponseBody(account.getEmail(), null, ServerResponseBody.REQUEST_LOGIN);
            webSocket.send(body.toJson());
        }
    }


    private class SocketListner extends WebSocketListener {
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            Log.d(TAG, "onMessage: " + text);
            try {
                JSONObject jsonObject = new JSONObject(text);
                String status = jsonObject.get("status").toString();
                if(jsonObject.get("type").toString().equals("Create room") ){
                     onCreateRoom(jsonObject.getJSONObject("game_info"));
                }else if(jsonObject.get("type").toString().equals("Login")){
                    Log.d(TAG, "LOGIN SUCESSFULLY "  + status);
                }else if(jsonObject.get("type").toString().equals("Cancel room")){
                    Log.d(TAG, "You have leave the room : " + status);
                }else if(jsonObject.get("type").toString().equals("Join room")){
                    if(status == "Successful"){
                        Log.d(TAG, "Room has been joined  : " + status);
                    }else{
                        Log.d(TAG, "Try again : " + status);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    //when response came back after create popup window
    private void onCreateRoom(JSONObject gameInfo){

        if(gameInfo.has("game_id")){
            if (popUpCreateRoomClass != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            popUpCreateRoomClass.setRoomCode( gameInfo.get("game_id").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Stuff that updates the UI

                    }
                });

            } else {
                Log.d(TAG, "onMessage: It is null " );
            }
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