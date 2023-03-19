package com.chrissy.atm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACT = 80;
    private static final String TAG = ContactActivity.class.getSimpleName();
    private List<Contact> contactList;

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

        contactList = new ArrayList<>();
        while (cursor.moveToNext()) {
            //取得聯絡人ID
            int contactId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
            //取得聯絡人姓名
            String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            Contact contact = new Contact(contactId, contactName);

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
                    contact.getPhones().add(contactPhone);
                }
            } else {
                Log.d(TAG, "readContacts: " + contactId + " " + contactName);
            }
            contactList.add(contact);
        }
        ContactAdapter adapter = new ContactAdapter(contactList);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
        List<Contact> contactList;

        public ContactAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            Contact contact = contactList.get(position);
            holder.contactName.setText(contact.getName());
            StringBuilder sb = new StringBuilder();
            for (String phone : contact.getPhones()) {
                sb.append(phone);
                sb.append(" ");
            }
            holder.contactPhone.setText(sb.toString());
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public class ContactHolder extends RecyclerView.ViewHolder {

            TextView contactName;
            TextView contactPhone;

            public ContactHolder(@NonNull View itemView) {
                super(itemView);
                contactName = itemView.findViewById(android.R.id.text1);
                contactPhone = itemView.findViewById(android.R.id.text2);
            }
        }//end of ContactHolder
    }//end of ContactAdapter


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            }
        }
    }//end of onRequestPermissionsResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }//end of onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_upload) {
            String accountShaPre = getSharedPreferences("atm", MODE_PRIVATE)
                    .getString("account", null);
            if (accountShaPre != null) {
                FirebaseDatabase.getInstance().getReference("users")
                        .child(accountShaPre).setValue(contactList);
            }

        }
        return super.onOptionsItemSelected(item);
    }//end of onOptionsItemSelected
}