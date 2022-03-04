package com.example.dac_rfid8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private NfcAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = NfcAdapter.getDefaultAdapter(this);
    }

    /*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String mes = lecturePuce(intent);
    }*/

    public void btnLectureRFID(View v1) {
        Intent i2 = new Intent();
        Log.i(TAG, "btnLectureRFID 1 ");
        String mes = lecturePuce(i2);
        Log.i(TAG, "btnLectureRFID 2 : " + mes);
    }

    private String lecturePuce(Intent intent) {
        String retour="";
        String action = intent.getAction();
        Log.i(TAG, "action " + action);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] messages;
            if (rawMsgs != null) {
                messages = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {

                    messages[i] = (NdefMessage) rawMsgs[i];
                    NdefRecord record = messages[i].getRecords()[i];
                    byte[] id = record.getId();
                    short tnf = record.getTnf();
                    byte[] type = record.getType();
                    String message = getTextData(record.getPayload()).split("/")[1];
                    retour+=message;
                }
            }
        }
        return retour;
    }

    String getTextData(byte[] payload) {
        String texteCode = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] & 0077;
        try
        {
            return new String(payload, langageCodeTaille + 1, payload.length - langageCodeTaille - 1, texteCode);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}