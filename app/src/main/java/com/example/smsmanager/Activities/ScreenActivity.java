package com.example.smsmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.smsmanager.model.Message;
import com.example.smsmanager.R;
import com.example.smsmanager.Adapter.UserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ScreenActivity extends AppCompatActivity implements UserAdapter.OnMessageListener {
    private static ScreenActivity inst;
    RecyclerView rv;
    FloatingActionButton fab;
    Toolbar toolbar;
    String text;
    String sender;
    ArrayList<Message> messages = new ArrayList<>();
    UserAdapter myadapter = new UserAdapter(messages, this);

    public static ScreenActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.screen_fab);
        toolbar = findViewById(R.id.toolbar_screen);
        setSupportActionBar(toolbar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(myadapter);
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS")
                == PackageManager.PERMISSION_GRANTED) {
            refreshSmsInbox();
        } else {
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(ScreenActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) {
            return;
        }

        do {
            sender = smsInboxCursor.getString(indexAddress);
            text = smsInboxCursor.getString(indexBody);
            messages.add(new Message("Send from:  " + sender + "              ",
                    text));
            myadapter.notifyDataSetChanged();
        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(final String smsMessage) {
        myadapter.notifyDataSetChanged();
        myadapter.notifyAll();
        // arrayAdapter.insert(smsMessage, 0);
        //arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                ArrayList<Message> newList = new ArrayList<>();
                for (Message item : messages) {
                    if (item.getText().toLowerCase().contains(userInput)) {
                        newList.add(item);
                    }
                }
                myadapter.searchList(newList);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
            case R.id.main_setting:
                break;

        }
        return true;
    }

    @Override
    public void onMessageClick(int position) {
        Message message = messages.get(position);
        Intent intent = new Intent(this, viewActivity.class);
        intent.putExtra("number", message.getName());
        intent.putExtra("text", message.getText());
        startActivity(intent);
    }
}