package com.madrasahdigital.walisantri.ppi67benda.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.Santrus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alhudaghifari on 2:39 14/05/19
 */
public class SantriChooserDialog extends Dialog {

    private ListView listView;
    private ArrayAdapter mAdapter;
    private Activity myActivity;
    private List<Santrus> santriList;
    private List<String> arrayListName;
    OnMyDialogResult mDialogResult;

    public SantriChooserDialog(Activity context, List<Santrus> santri) {
        super(context);
        myActivity = context;
        this.santriList = santri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_santri_chooser);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        listView = findViewById(R.id.listView);
        arrayListName = new ArrayList<>();

        for (int i=0;i<santriList.size();i++) {
            arrayListName.add(santriList.get(i).getFullname());
        }

        mAdapter = new ArrayAdapter(myActivity, R.layout.simple_text_custom, arrayListName);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            String name = ((TextView) v).getText().toString();
            mDialogResult.finish(santriList.get(arg2).getId());
            dismiss();
            Log.d("DIALOG", "posisi : " + arg2);
        }
    };

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(String id);
    }
}