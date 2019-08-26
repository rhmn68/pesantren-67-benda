package com.madrasahdigital.walisantri.ppi67benda.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.madrasahdigital.walisantri.ppi67benda.R;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TAG;

/**
 * Created by Alhudaghifari on 16:32 22/06/19
 */
public class KonfirmasiBayarDialog extends Dialog implements
        android.view.View.OnClickListener {

    OnMyDialogResult mDialogResult;
    private Activity myActivity;
    private Button btnOk;
    private Button btnBatal;

    public KonfirmasiBayarDialog(Activity context) {
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
            Crashlytics.setString(TAG + "-KonfirmBDial", "1-" + e.getMessage());
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        setContentView(R.layout.dialog_confirm_payment);
        btnOk = findViewById(R.id.btnOk);
        btnBatal = findViewById(R.id.btnBatal);
        btnOk.setOnClickListener(this);
        btnBatal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if (mDialogResult != null) {
                    mDialogResult.finish(true);
                }
                dismiss();

                break;
            case R.id.btnBatal:
                if (mDialogResult != null) {
                    mDialogResult.finish(false);
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
        void finish(boolean buttonClicked);
    }
}
