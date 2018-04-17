package com.ss.popupdemo;

import android.app.Activity;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by song on 2016/4/13.
 * 版本21，与版本23接口有点不一样
 */

public class WindowCallBack implements Window.Callback {
    private Activity activity;
    private WindowsFocusListener listener;
    public WindowCallBack(Activity activity){
        this.activity = activity;
    }

    public interface WindowsFocusListener{
        public void onWindowFocusChanged(String tag, boolean hasFocus);
    }

    public void setOnWindowFocusListener(WindowsFocusListener listener) {
        this.listener = listener;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return activity.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return activity.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return activity.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return activity.dispatchTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return activity.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return activity.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public View onCreatePanelView(int featureId) {
        return activity.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return activity.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return activity.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return activity.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return activity.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
    	activity.onWindowAttributesChanged(attrs);
    }

    @Override
    public void onContentChanged() {
    	activity.onContentChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(activity==null||activity.isFinishing()){
            return;
        }

        if(listener==null){
            return;
        }
        listener.onWindowFocusChanged(activity.getLocalClassName(),hasFocus);
        activity.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onAttachedToWindow() {
    	activity.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
    	activity.onDetachedFromWindow();
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
    	activity.onPanelClosed(featureId, menu);
    }

    @Override
    public boolean onSearchRequested() {
        return activity.onSearchRequested();
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return activity.onWindowStartingActionMode(callback);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
    	activity.onActionModeFinished(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
       activity.onActionModeFinished(mode);
    }


}
