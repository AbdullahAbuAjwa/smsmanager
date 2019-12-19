package com.example.smsmanager.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smsmanager.R;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private EditText txtMobile;
    private EditText txtMessage;
    private Button btnSms;
    Toolbar toolbar;
    ImageView img_contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMobile = findViewById(R.id.mblTxt);
        txtMessage = findViewById(R.id.msgTxt);
        img_contact = findViewById(R.id.img_contact);
        btnSms = findViewById(R.id.btnSend);
        toolbar = findViewById(R.id.toolbar_send);
        setSupportActionBar(toolbar);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission_group.SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(), null, txtMessage.getText().toString(), null, null);
                    Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_LONG).show();
                }

            }

        });
        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent contact = new Intent(Intent.ACTION_PICK, ContactsContract.
                        CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contact, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri = data.getData();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor cursor = getContentResolver().query(uri, null, null, null);
            cursor.moveToFirst();
            // int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds
            //     .Phone.NUMBER);
            int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds
                    .Phone.NUMBER);
            String strNumber = cursor.getString(number);
            txtMobile.setText(strNumber);

        }
    }
}