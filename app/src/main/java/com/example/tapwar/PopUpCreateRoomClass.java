package com.example.tapwar;

import android.content.Context;
import android.os.Build;
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


public abstract class PopUpCreateRoomClass implements View.OnClickListener {

    protected TextView roomCodeTextView;
    protected Button shareButton;
    protected PopupWindow popupWindow;
    protected Button cancelRoomButton;

    public abstract void onPopup();

    //PopupWindow display method
    public void showPopupWindow(final View view,Context c) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.create_room_popup, null);

        popupView.setFitsSystemWindows(true);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;
        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        //Set the location of the window on the screen

        initializeUI(popupView,view);

    }

    public void initializeUI(View popupView, View parentView) {
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        dimBehind(popupWindow);

        roomCodeTextView = popupView.findViewById(R.id.roomIdTextView);
        shareButton = popupView.findViewById(R.id.shareButton);
        cancelRoomButton = popupView.findViewById(R.id.cancelRoomButton);
        shareButton.setOnClickListener(this);
        cancelRoomButton.setOnClickListener(this);
    }

    protected void dismissPopup(View v){
        popupWindow.dismiss();
        Toast.makeText(v.getContext(), "Wow, cancel action button", Toast.LENGTH_SHORT).show();
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }

    public void setRoomCode(String roomcode) {
        Log.d("POPUP", "setRoomCode: " + roomcode);
        roomCodeTextView.setText(roomcode);
    }

}
