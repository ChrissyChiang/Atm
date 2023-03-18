package com.chrissy.atm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edAcc;
    private EditText edPwd;
    public CheckBox cbRemAcc;
    private boolean remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        findViews();



    }//end of onCreate

    protected void findViews() {
        edAcc = findViewById(R.id.ed_account);
        edPwd = findViewById(R.id.ed_pwd);
        cbRemAcc = findViewById(R.id.cb_rem_acc);
        // checkbox listener
        cbRemAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("ATM",MODE_PRIVATE).edit()
                        .putBoolean("REMEMBER_ACCOUNT",isChecked)
                        .commit();
            }
        });

        cbRemAcc.setChecked(
                getSharedPreferences("ATM",MODE_PRIVATE)
                        .getBoolean("REMEMBER_ACCOUNT",false)
        );


        String account = getSharedPreferences("ATM",MODE_PRIVATE)
                .getString("ACCOUNT","");
        edAcc.setText(account);

    }

    //press login btn
    public void login(View view) {
        String userAcc = edAcc.getText().toString();
        String userPwd = edPwd.getText().toString();

        // login success




        if ("jack".equals(userAcc) && "1234".equals(userPwd)) {
            setResult(RESULT_OK);
            finish();
        }

        //save account after login
        remember = getSharedPreferences("ATM",MODE_PRIVATE)
                .getBoolean("REMEMBER_ACCOUNT",false);

        if (remember){
            // Remember user Account
            getSharedPreferences("ATM",MODE_PRIVATE).edit()
                    .putString("ACCOUNT",userAcc)
                    .commit();
        }else {
            //Do not remember then clean the ACCOUNT in SharedPreferences
            getSharedPreferences("ATM",MODE_PRIVATE).edit()
                    .putString("ACCOUNT","")
                    .commit();
        }


    }



    //press cancel btn
    public void cancel(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Sure to Exit?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

}
