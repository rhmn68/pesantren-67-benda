package com.madrasahdigital.walisantri.ppi67benda.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;

/**
 * Created by ghifar on 09/03/18.
 */

public class LogoutDialog extends Dialog implements
        View.OnClickListener {

    private Activity myActivity;
    private Button btnLogout;
    private Button btnTidak;

    private TextView tvQuestion;

    OnMyDialogResult mDialogResult;

    public LogoutDialog(Activity context) {
        super(context);
        myActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_logout);
        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnLogout = findViewById(R.id.btn_logout);
        btnTidak = findViewById(R.id.btn_tidak);
        btnLogout.setOnClickListener(this);
        btnTidak.setOnClickListener(this);
        tvQuestion = findViewById(R.id.tv_question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                if (mDialogResult != null) {
                    mDialogResult.finish(true);
                }
                dismiss();

                break;
            case R.id.btn_tidak:
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

    public void setTvQuestion(String question) {
        tvQuestion.setText(question);
    }

    public interface OnMyDialogResult {
        void finish(boolean result);
    }
}