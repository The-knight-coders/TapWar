package com.example.tapwar;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public abstract class PopUpJoinRoomClass implements View.OnClickListener, TextWatcher {
    protected PopupWindow popupWindow;
    protected EditText codeEditText;
    protected Button enterRoomButton;
    protected Button cancelJoinButton;
    private String enteredCode;
    public abstract void onPopup();

    public void showPopupWindow(final View view, Context c) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.join_room_popup, null);

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

    private void initializeUI(View popupView, View parentView) {
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        dimBehind(popupWindow);

        codeEditText = popupView.findViewById(R.id.codeEditText);
        enterRoomButton = popupView.findViewById(R.id.enterRoomButton);
        cancelJoinButton = popupView.findViewById(R.id.cancelJoinButton);
        enterRoomButton.setOnClickListener(this);
        cancelJoinButton.setOnClickListener(this);
        codeEditText.addTextChangedListener(this);
    }

    private static void dimBehind(PopupWindow popupWindow) {
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

    protected void dismissPopup(View v){
        popupWindow.dismiss();
        Toast.makeText(v.getContext(), "Wow, cancel action button", Toast.LENGTH_SHORT).show();
    }

    public String getEnteredCode() {
        return enteredCode;
    }

    public void setEnteredCode() {
        String code = codeEditText.getText().toString();
        this.enteredCode = code;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        enterRoomButton.setEnabled(codeEditText.getText().length() > 0 &&
                codeEditText.getText().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {


    }


    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.cancelJoinButton:
//        }
//    }
}
