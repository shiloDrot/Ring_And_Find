package com.example.shilo90.ringandfind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shilo90.ringandfind.R;


public class FragmentSendRingRing extends Fragment {

    public static final int CONTACT_TO_SEND = 4;
    public static FragmentSendRingRing SendRingRingIns;

    public View layout;
    public Button sendSMS;
    public EditText phoneNumber;
    public ImageButton contactSMS;

    private boolean allowSending;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String textSMS = sharedPref.getString("textSMS", "");

        layout = inflater.inflate(R.layout.fragment_fragment_send_ring_ring, container, false);
        sendSMS = (Button) layout.findViewById(R.id.buttonSendSMS);
        contactSMS = (ImageButton) layout.findViewById(R.id.buttonSMScontacts);
        phoneNumber = (EditText) layout.findViewById(R.id.editTextSMS);
        allowSending = true;

        phoneNumber.setText(textSMS);

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowSending) {
                    allowSending = false;
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String text = sharedPref.getString("textSMS", "");
                    if (text.equals("")) {
                        return;
                    }
                    sendSMS(text, "ringring");
                } else {
                    Toast.makeText(getActivity(), "you already sent RingRing", Toast.LENGTH_SHORT).show();
                }

            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("textSMS", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contactSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                getActivity().startActivityForResult(intent, CONTACT_TO_SEND);
            }
        });


        SendRingRingIns = this;
        return layout;
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getActivity(),"Sending RingRing to " +phoneNumber,Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                allowSending = true;
            }
        }, 5000);
    }
}
