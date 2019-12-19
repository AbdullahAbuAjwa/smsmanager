package com.example.smsmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsmanager.Adapter.UserAdapter;
import com.example.smsmanager.Adapter.viewAdapter;
import com.example.smsmanager.R;
import com.example.smsmanager.model.Message;
import com.example.smsmanager.model.ViewMessage;

import java.util.ArrayList;

public class viewActivity extends AppCompatActivity {
    RecyclerView view_rv;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    Toolbar toolbar;
    EditText et_send;
    TextView tv_receiver;
    ImageView img_send;
    ImageView img_rec;
    String sendMessage;
    String text;
    ArrayList<ViewMessage> viewMessages = new ArrayList<>();
    viewAdapter viewadapter = new viewAdapter(viewMessages);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        final String sender = intent.getStringExtra("number");
        text = intent.getStringExtra("text");
        view_rv = findViewById(R.id.view_rv);
        toolbar = findViewById(R.id.toolbar_view);
        et_send = findViewById(R.id.view_et_send);
        tv_receiver = findViewById(R.id.view_tv_receiver);
        img_send = findViewById(R.id.view_img_send);
        img_rec = findViewById(R.id.view_img_rec);
        sendMessage = et_send.getText().toString().trim();
        toolbar.setTitle(sender.substring(10, sender.length() - 1));
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view_rv.setHasFixedSize(true);
        view_rv.setLayoutManager(new LinearLayoutManager(this));
        view_rv.setAdapter(viewadapter);
        tv_receiver.setText(text);
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
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(sender, null, et_send.getText().toString(), null, null);
                    viewMessages.add(new ViewMessage(et_send.getText().toString().trim()));
                    Toast.makeText(viewActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                    et_send.setText("");
                } catch (Exception e) {
                    Toast.makeText(viewActivity.this, "" + e, Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.view_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                ArrayList<ViewMessage> newList = new ArrayList<>();
                tv_receiver.setVisibility(View.GONE);
                img_rec.setVisibility(View.GONE);

                for (ViewMessage item : viewMessages) {
                    if (item.getTextMessage().toLowerCase().contains(userInput)) {
                        newList.add(item);
                    }
                }
                if (text.toLowerCase().contains(userInput)) {
                    tv_receiver.setVisibility(View.VISIBLE);
                    img_rec.setVisibility(View.VISIBLE);
                }
                viewadapter.searchList(newList);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_call:
                break;
            case R.id.view_setting:
                break;

        }
        return true;
    }
}