package com.cmsc355.contactapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class AppInfoActivity extends NonHomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        Toolbar appInfoToolbar = findViewById(R.id.app_info_toolbar);
        setSupportActionBar(appInfoToolbar);


    }

    //adds the home button to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //home button takes you straight home, resets the list of activities for the back button
    //(see https://developer.android.com/guide/components/activities/tasks-and-back-stack.html)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
