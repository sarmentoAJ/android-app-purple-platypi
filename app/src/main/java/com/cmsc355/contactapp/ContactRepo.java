package com.cmsc355.contactapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactRepo {

    public ContactRepo() {
    }

    static int insertToDatabase(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        String attrString = contact.getAttributes().toString();
        Log.d("ContactRepo insertToDB","Insert to db: " + attrString);
        values.put(Contact.COLUMN_JSON, attrString);

        int contactId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        contactId = (int) db.insert(Contact.TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return contactId;
    }

    static void delete(int contactId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Contact.TABLE_NAME, Contact._ID + "= ?", new String[]{String.valueOf(contactId)});
        DatabaseManager.getInstance().closeDatabase();
    }

    static void deleteAll() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Contact.TABLE_NAME, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    static void update(Contact contact) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_JSON, contact.getAttributes().toString());

        db.update(Contact.TABLE_NAME, values, Contact._ID + "= ?", new String[]{String.valueOf(contact.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    static ArrayList<Contact> searchContacts(String searchQuery) {
        ArrayList<Contact> allContacts = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + Contact.TABLE_NAME + " WHERE " + Contact.COLUMN_NAME + " LIKE ?";
        String[] args = new String[]{"%" + searchQuery + "%"};
        Cursor cursor = db.rawQuery(query, args);
        Contact contact;

        if (cursor.moveToFirst()) {
            do {
                try {
                    contact = new Contact(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)),
                            new JSONObject(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_JSON))),
                            cursor.getInt(cursor.getColumnIndex(Contact._ID)));
                    allContacts.add(contact);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        DatabaseManager.getInstance().closeDatabase();
        return allContacts;
    }

    static Contact getContact(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String query = "SELECT * FROM " + Contact.TABLE_NAME + " WHERE " + Contact._ID + " = ?";
        String[] args = new String[]{"" + id};
        Cursor cursor = db.rawQuery(query, args);
        Contact contact = new Contact();
        if (cursor.moveToFirst()) {
            try {
                contact.setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)));
                Log.d("ContactRepo getContact","setAttributes to: " + cursor.getString(cursor.getColumnIndex(Contact.COLUMN_JSON)));
                contact.setAttributes(new JSONObject(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_JSON))));
                contact.setId(id);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
        return contact;
    }
}
