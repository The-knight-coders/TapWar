package com.example.tapwar;

import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tapwar.model.PopUpAb;


public abstract class PopUpCreateRoomClass extends PopUpAb implements View.OnClickListener {

    private TextView roomCodeTextView;
    protected Button shareButton;
    protected Button cancelRoomButton;

    @Override
    public void showPopupWindow(View view, Context c) {
        super.showPopupWindow(view, c, R.layout.create_room_popup);

    }

    @Override
    protected void initializeUI(View popupView, View parentView) {
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        dimBehind(popupWindow);
        roomCodeTextView = popupView.findViewById(R.id.roomIdTextView);
        shareButton = popupView.findViewById(R.id.shareButton);
        cancelRoomButton = popupView.findViewById(R.id.cancelRoomButton);
        shareButton.setOnClickListener(this);
        cancelRoomButton.setOnClickListener(this);

    }

    public void setRoomCode(String roomcode) {
        Log.d("POPUP", "setRoomCode: " + roomcode);
        roomCodeTextView.setText(roomcode);

    }

    public String getRoomCode() {
        return roomCodeTextView.getText().toString();
    }
}
