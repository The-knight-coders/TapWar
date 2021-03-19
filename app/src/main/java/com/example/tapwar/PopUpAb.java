package com.example.tapwar;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tapwar.R;

public abstract class PopUpAb {

	protected PopupWindow popupWindow;

	public abstract void onPopup();

	protected void showPopupWindow(final View view, Context c ) {

	}

	public void showPopupWindow(Context c, @Nullable int layoutResId) {
		LayoutInflater inflater = (LayoutInflater) c.getApplicationContext().getSystemService(c.LAYOUT_INFLATER_SERVICE);
		initializePopupWindow(inflater,layoutResId);
	}

	protected void initializePopupWindow(LayoutInflater inflater,int layoutResId){
		View popupView = inflater.inflate(layoutResId, null);
		popupView.setFitsSystemWindows(true);
		//Specify the length and width through constants
		int width = LinearLayout.LayoutParams.MATCH_PARENT;
		int height = LinearLayout.LayoutParams.MATCH_PARENT;
		//Make Inactive Items Outside Of PopupWindow
		boolean focusable = true;
		//Create a window with our parameters
		popupWindow = new PopupWindow(popupView, width, height, focusable);
		//Set the location of the window on the screen
		initializeUI(popupView);
	}

	public void showPopupWindow(final View view, Context c,@Nullable int layoutResId){

		LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(layoutResId, null);
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

	protected abstract void initializeUI(View popupView);

	protected abstract void initializeUI(View popupView, @Nullable View parentView);

	protected void dismissPopup(View v){
		popupWindow.dismiss();
		Toast.makeText(v.getContext(), "Wow, cancel action button", Toast.LENGTH_SHORT).show();
	}

	protected static void dimBehind(PopupWindow popupWindow) {
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