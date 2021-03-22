package com.example.tapwar;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameScreen extends AppCompatActivity {

    private PopUpTimer popUpTimer;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screem);
       linearLayout = findViewById(R.id.linearLayout);

       linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showCustomPopupMenu(v);
           }
       });



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void showCustomPopupMenu(View V)
    {
                popUpTimer = new PopUpTimer() {
            @Override
            public void onPopup() {
                this.showPopupWindow(V,getApplicationContext());
            }
        };

        popUpTimer.onPopup();
    }

}