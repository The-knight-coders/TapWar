package com.example.tapwar;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class PopUpTimer extends PopUpAb{
    TextView timerText;

    public void showPopupWindow( View v,Context c) {
        super.showPopupWindow(v,c, R.layout.timer_pop_up);

    }



    @Override
    protected void initializeUI(View popupView, View parentView) {
        popupWindow.showAtLocation(parentView,Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        dimBehind(popupWindow);
        timerText = popupView.findViewById(R.id.timerText);

        long duration = TimeUnit.SECONDS.toMillis(3);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH," %02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timerText.setText(sDuration);
            }

            @Override
            public void onFinish() {
                timerText.setTextSize(45);
                timerText.setText("LETS BEGIN!!");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissPopup(popupWindow.getContentView());
                    }
                },1000);

            }
        }.start();
    }
}
