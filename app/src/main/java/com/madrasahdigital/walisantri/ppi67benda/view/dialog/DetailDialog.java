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
 * Created by Alhudaghifari on 13:57 24/05/19
 */
public class DetailDialog extends Dialog implements
        android.view.View.OnClickListener {

    OnMyDialogResult mDialogResult;
    private Activity myActivity;
    private TextView tvStatusPresence;
    private TextView tvTanggal;
    private TextView tvDescription;
    private Button btnOk;

    public DetailDialog(Activity context) {
        super(context);
        myActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvStatusPresence = findViewById(R.id.tvStatusPresence);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvDescription = findViewById(R.id.tvDeskripsi);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
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
