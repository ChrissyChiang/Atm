package com.chrissy.atm;

import static com.chrissy.atm.R.id.ed_account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edAccount;
    private EditText edPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edAccount = findViewById(ed_account);
        edPwd = findViewById(R.id.ed_pwd);

    }


    public void login(View view) {
        String a = edAccount.getText().toString();
        String p = edPwd.getText().toString();

        if (a.equals("hi") && p.equals("1234")) {
            setResult(RESULT_OK);
            finish();//結束此activity,回到上一層。
        }else {
            setResult(MainActivity.REQUEST_LOGIN);
            finish();
        }

    }

    public void cancel(View view) {

    }
}