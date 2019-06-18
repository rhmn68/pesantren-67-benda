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
 * Created by Alhudaghifari on 0:46 19/06/19
 */
public class InfoDialog extends Dialog implements
        android.view.View.OnClickListener {

    OnMyDialogResult mDialogResult;
    private Activity myActivity;
    private TextView tvInfo;
    private String info;
    private Button btnOk;

    public InfoDialog(Activity context, String info) {
        super(context);
        myActivity = context;
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_info);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvInfo = findViewById(R.id.tvInfo);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        tvInfo.setText(info);
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
