package com.madrasahdigital.walisantri.ppi67benda.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.madrasahdigital.walisantri.ppi67benda.R;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TAG;

/**
 * Created by Alhudaghifari on 8:31 22/05/19
 */
public class SettingDialog extends Dialog implements
        android.view.View.OnClickListener {

    OnMyDialogResult mDialogResult;
    private Activity myActivity;
    private TextView tvLogout;
    private TextView tvAbout;

    public SettingDialog(Activity context) {
        super(context);
        myActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey(TAG + "-SetDial", "1-" + e.getMessage());
            e.printStackTrace();
        }
        setContentView(R.layout.dialog_setting);
        tvLogout = findViewById(R.id.tvLogout);
        tvAbout = findViewById(R.id.tvAbout);
        tvLogout.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAbout:
                if (mDialogResult != null) {
                    mDialogResult.finish("about");
                }
                dismiss();

                break;
            case R.id.tvLogout:
                if (mDialogResult != null) {
                    mDialogResult.finish("logout");
                }
                dismiss();

                break;
            default:
                break;
        }
        dismiss();
    }

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(String buttonClicked);
    }
}
