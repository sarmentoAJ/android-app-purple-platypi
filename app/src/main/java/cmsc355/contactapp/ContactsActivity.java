package cmsc355.contactapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        SQLiteDatabase db = openDatabase(ContactsActivity.this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<JSONObject> contactList = getContactList(db);
        //TODONE - replace this line with pulling contacts from database
        //This line still generates random data despite what is in the db
        //TODO: comment next line to see things inside the db
        Contact.GenerateRandomContacts(contactList, 7);
        recyclerView.setAdapter(new ContactsAdapter(contactList));
    }


    public ArrayList<JSONObject> getContactList(SQLiteDatabase db)
    {
        String table = "Contact";
        String[] columns = {"JSON"};
        String selection = DatabaseContract.Contact.COLUMN_JSON+" = ?";
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        JSONObject json = new JSONObject();
        ArrayList contactsJSON = new ArrayList();

        while(cursor.moveToNext())
        {
            String stringForm = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Contact.COLUMN_JSON));
            try
            {
                 json = new JSONObject(stringForm);
            }
            catch (JSONException e)
            {
                Log.d("Contacts Activity", "Could not parse JSON: **"+stringForm+"**");
            }
            contactsJSON.add(json);
        }
        return contactsJSON;
    }//getContactList method

    public SQLiteDatabase openDatabase(Context context)
    {
        DatabaseHelper DbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = DbHelper.getReadableDatabase();
        return db;
    }
}//ContactsActivity class