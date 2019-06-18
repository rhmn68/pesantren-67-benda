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
 * Created by Alhudaghifari on 23:46 18/06/19
 */
public class KonfirmasiSantriDialog extends Dialog implements
        android.view.View.OnClickListener {

    OnMyDialogResult mDialogResult;
    private Activity myActivity;
    private TextView tvNis;
    private TextView tvTanggal;
    private TextView tvNama;
    private String nis;
    private String tanggal;
    private String nama;
    private Button btnOk;
    private Button btnBatal;

    public KonfirmasiSantriDialog(Activity context, String nis, String nama, String tanggalLahir) {
        super(context);
        myActivity = context;
        this.nis = "NIS : " + nis;
        this.tanggal = "Tanggal lahir : " + tanggalLahir;
        this.nama = "Nama : " + nama.substring(0,25);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_santri);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvNis = findViewById(R.id.tvNis);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvNama = findViewById(R.id.tvNama);
        btnOk = findViewById(R.id.btnOk);
        btnBatal = findViewById(R.id.btnBatal);
        btnOk.setOnClickListener(this);
        btnBatal.setOnClickListener(this);


        tvNis.setText(nis);
        tvTanggal.setText(tanggal);
        tvNama.setText(nama);
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
        void finish(boolean isConfirmClicked);
    }
}
