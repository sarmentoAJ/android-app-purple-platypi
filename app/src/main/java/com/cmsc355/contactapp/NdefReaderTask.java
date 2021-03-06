package com.cmsc355.contactapp;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static android.widget.Toast.makeText;
import static com.cmsc355.contactapp.App.context;
import static com.cmsc355.contactapp.ConnectActivity.TAG;

/*
 * Created by talre on 11/15/2017.
 */

/* NdefReaderTask is the task that takes the tag and ensures it is in fact
a Ndef Message, and if so, pulls out the Ndef Record and passes that to the reader
 */
class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords(); //this allows more than one record per message, though our app does not need this.
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException exception) {
                    Log.e(TAG, "Unsupported Encoding", exception);
                }
            }
        }

        return null;
    }

    // Read Text reads the string within the Ndef Record and returns it as a string
    private String readText(NdefRecord record) throws UnsupportedEncodingException {


        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            makeText(context, result,
                    Toast.LENGTH_SHORT).show();
        }
    }
}