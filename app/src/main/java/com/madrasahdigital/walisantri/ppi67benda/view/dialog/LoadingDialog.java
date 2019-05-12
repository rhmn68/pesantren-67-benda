package com.neurafarm.sobattania.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.neurafarm.sobattania.R;

/**
 * Created by Alhudaghifari on 7:59 31/01/19
 */
public class LoadingDialog extends Dialog {

    public Activity myActivity;

    public LoadingDialog(Activity context) {
        super(context);
        myActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);

    }
}
