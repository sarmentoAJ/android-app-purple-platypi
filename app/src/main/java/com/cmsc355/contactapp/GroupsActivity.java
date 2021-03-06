package com.cmsc355.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupsActivity extends NonHomeActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Toolbar groupsToolbar = findViewById(R.id.group_toolbar);
        setSupportActionBar(groupsToolbar);

        recyclerView = findViewById(R.id.group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button newGroupButton = findViewById(R.id.group_new);
        newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, EditGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //want to pass the adapter all the groups and all the contacts related to each group
        //that way, it can generate the correct amount of items in its list, and also put them
        //in the correct order
        ArrayList<ContactGroup> groupsList = Utilities.sortGroupList(App.databaseIoManager.getAllGroups());
        Log.d("Groups","# groups loaded: " + groupsList.size());

        recyclerView.setAdapter(new GroupsAdapter(groupsList));
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
