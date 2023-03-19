package com.chrissy.atm;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.chrissy.atm.R.id.ed_account;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText edAccount;
    private EditText edPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission == PERMISSION_GRANTED) {
            takePhoto();

        } else if (permission == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
        findViews();

        // 如果上次成功登入，那帳號會直接出現在edAccount
        String accountPref = getSharedPreferences("atm", MODE_PRIVATE)
                .getString("account", "");// 沒有值的話就不用顯示
        edAccount.setText(accountPref);

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                //頁面跳轉到拍照
                takePhoto();
            }
        }
    }

    private void findViews() {
        edAccount = findViewById(ed_account);
        edPwd = findViewById(R.id.ed_pwd);
        CheckBox cbRemember = findViewById(R.id.cb_rem_account);
        cbRemember.setChecked(getSharedPreferences("atm", MODE_PRIVATE)
                .getBoolean("REMEMBER_ACCOUNT", false));
        // 傾聽核選元件勾選與否
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm", MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_ACCOUNT", true)
                        .apply();
            }
        });
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
                        if (fbPwd != null && fbPwd.equals(pwd)) {
                            // 判斷是否有勾選記住帳號
                            boolean remAccount = getSharedPreferences("atm", MODE_PRIVATE)
                                    .getBoolean("REMEMBER_ACCOUNT", false);

                            if (remAccount) {
                                // 記住使用者帳號
                                getSharedPreferences("atm", MODE_PRIVATE)
                                        .edit()
                                        .putString("account", account)
                                        .apply();

                            } else {
                                // 如果沒有記住的話，先清掉喜好檔 account 的值
                                getSharedPreferences("atm", MODE_PRIVATE)
                                        .edit()
                                        .putString("account", "")
                                        .commit();
                            }


                            setResult(RESULT_OK);
                            finish();//結束此activity,回到上一層。
                        } else {
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
    }

    public void cancel(View view) {

        new AlertDialog.Builder(LoginActivity.this)
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
