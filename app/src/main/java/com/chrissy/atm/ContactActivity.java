package com.chrissy.atm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACT = 80;
    private static final String TAG = ContactActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //於一開始先檢查是否有 contact 權限
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            readContacts();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }

    }

    private void readContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI
                , null, null, null, null);
        while (cursor.moveToNext()) {
            //取得聯絡人ID
            int contactId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
            //取得聯絡人姓名
            String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            //取得聯絡人是否有電話 1有 0沒有
            int contactHasPhone = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (contactHasPhone == 1) {
                Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{String.valueOf(contactId)}, null);
                //取得聯絡人電話
                while (cursorPhone.moveToNext()) {
                    String contactPhone = cursorPhone.getString(cursorPhone.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d(TAG, "readContacts: " + contactId + " " + contactName + " " + contactPhone);
                }
            } else {
                Log.d(TAG, "readContacts: " + contactId + " " + contactName);
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            }
        }
    }
}