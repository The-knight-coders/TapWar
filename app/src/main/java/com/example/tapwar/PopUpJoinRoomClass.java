package com.example.tapwar;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public abstract class PopUpJoinRoomClass extends PopUpAb implements View.OnClickListener, TextWatcher {

    protected EditText codeEditText;
    protected Button enterRoomButton;
    protected Button cancelJoinButton;
    private String enteredCode;

    @Override
    protected void showPopupWindow(View view, Context c) {
        super.showPopupWindow(view, c, R.layout.join_room_popup);
    }

    @Override
    protected void initializeUI(View popupView, View parentView) {

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

}
