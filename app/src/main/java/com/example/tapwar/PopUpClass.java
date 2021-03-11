package com.example.tapwar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.startActivity;


public class PopUpClass {

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
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);



        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        dimBehind(popupWindow);
        //Initialize the elements of our window, install the handler

//        TextView test2 = popupView.findViewById(R.id.titleText);
//        test2.setText(R.string.textTitle);

        Button shareButton = popupView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * After the code is generated we can simply pass the game code here
                 */
                String message = " Hey Welcome to Tap war \n To join Out game Copy The Code : txxgjshg121";
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");

                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Joining Code");
                i.putExtra(android.content.Intent.EXTRA_TEXT, message);
                c.startActivity(Intent.createChooser(i,"Share"));

                //As an example, display the message
                Toast.makeText(view.getContext(), "Wow, share action button", Toast.LENGTH_SHORT).show();

            }
        });


        Button cancelRoomButton = popupView.findViewById(R.id.cancelRoomButton);
        cancelRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //As an example, display the message
                popupWindow.dismiss();
                Toast.makeText(view.getContext(), "Wow, cancel action button", Toast.LENGTH_SHORT).show();

            }
        });


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

}
