package com.chrissy.atm;

import static com.chrissy.atm.R.id.ed_account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG =LoginActivity.class.getSimpleName();
    private EditText edAccount;
    private EditText edPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // 創建喜好檔(練習)
//        getSharedPreferences("atm",MODE_PRIVATE)
//                .edit()
//                .putInt("LEVEL",3) // 設定關卡第三關
//                .putString("Name","Chrissy")
//                .commit();

        //讀取喜好檔(練習)
//        int level = getSharedPreferences("atm",MODE_PRIVATE)
//                .getInt("LEVEL",0);
//        Log.d(TAG, "onCreate: "+level);

        edAccount = findViewById(ed_account);
        edPwd = findViewById(R.id.ed_pwd);

        // 如果上次成功登入，那帳號會直接出現在edAccount
        String accountPref = getSharedPreferences("atm",MODE_PRIVATE)
                .getString("account","");// 沒有值的話就不用顯示
        edAccount.setText(accountPref);

    }


    public void login(View view) {
        String account = edAccount.getText().toString();
        String pwd = edPwd.getText().toString();

        FirebaseDatabase.getInstance().getReference("users")
                .child(account).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // get password from firebase
                        String fbPwd = (String) snapshot.getValue();
                        // 登入成功
                        if (fbPwd!=null && fbPwd.equals(pwd)){
                            Log.d("LoginActivity","onDataChange: "+fbPwd);
                            // 記住使用者帳號
                            getSharedPreferences("atm",MODE_PRIVATE).edit()
                                    .putString("account",account)
                                    .apply();

                            setResult(RESULT_OK);
                            finish();//結束此activity,回到上一層。
                        }else {
                            // 登入失敗
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登入結果")
                                    .setMessage("登入失敗")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    edAccount.setText("");
                                                    edPwd.setText("");
                                                }
                                            }
                                    )
                                    .show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        if (a.equals("hi") && p.equals("1234")) {
//            setResult(RESULT_OK);
//            finish();//結束此activity,回到上一層。
//        }

    }

    public void cancel(View view) {

    }
}