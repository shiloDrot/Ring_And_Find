package com.example.shilo90.ringandfind;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.shilo90.ringandfind.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private RadioButton All;
    private RadioButton ONE;
    private Button contact;
    private EditText phone;
    private RadioGroup radioGroup;
    private Spinner cntry;


    private List<String> CountryList;
    private List<String> CountryCodeList;

    public static final int PICK_CONTACT = 1;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorDrawable colorDrawable = new ColorDrawable(Color.rgb(169, 208, 221));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(SMSBroadcastReceiver.NOTIFICATION_ID);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            int power = sharedPref.getInt("power", 0);
            int radio = sharedPref.getInt("radio", 1);
            String country = sharedPref.getString("country", "ISR");
            String countryCode = sharedPref.getString("countryCode", "972");
            int countryPos = sharedPref.getInt("countryPos", 100);
            String text = sharedPref.getString("text", "");

            switchStatus = (TextView) findViewById(R.id.mySwitch);
            mySwitch = (Switch) findViewById(R.id.mySwitch);
            All = (RadioButton) findViewById(R.id.radioButton);
            ONE = (RadioButton) findViewById(R.id.radioButton2);
            contact = (Button) findViewById(R.id.button);
            phone = (EditText) findViewById(R.id.editText);
            radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if (Locale.getDefault().getDisplayLanguage().equals("עברית")){
            radioGroup.setGravity();
        }

            cntry = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                    this, R.array.item_array, R.layout.spinner_layout);
            countryAdapter.setDropDownViewResource(R.layout.spinner_layout);
            cntry.setAdapter(countryAdapter);

            cntry.setSelection(countryPos);

            CountryList = Arrays.asList(getResources().getStringArray(R.array.item_array));
            CountryCodeList = Arrays.asList(getResources().getStringArray(R.array.code_array));

            cntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                    String selecteditem = adapter.getItemAtPosition(i).toString();

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("country", selecteditem);
                    editor.putInt("countryPos", i);
                    editor.putString("countryCode", CountryCodeList.get(i));
                    editor.commit();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });

            if (power == 1) {
                //set the switch to ON
                mySwitch.setText("Power ON");
                mySwitch.setChecked(true);
            } else {
                //set the switch to ON
                mySwitch.setChecked(false);
            }

            if (radio == 1) {
                All.setChecked(true);
                ONE.setChecked(false);
            } else if (radio == 0) {
                All.setChecked(false);
                ONE.setChecked(true);
            } else {
                All.setChecked(false);
                ONE.setChecked(false);
            }

            All.setEnabled(mySwitch.isChecked());
            ONE.setEnabled(mySwitch.isChecked());

            phone.setText(text);

            contact.setEnabled(ONE.isChecked() && mySwitch.isChecked());
            phone.setEnabled(ONE.isChecked() && mySwitch.isChecked());
            cntry.setEnabled(ONE.isChecked() && mySwitch.isChecked());

            //attach a listener to check for changes in state
            mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();

                    if (isChecked) {
                        mySwitch.setText(getResources().getText(R.string.Power_ON));
                        editor.putInt("power", 1);

                    } else {
                        mySwitch.setText(getResources().getText(R.string.Power_OFF));
                        editor.putInt("power", 0);
                    }
                    editor.commit();

                    All.setEnabled(mySwitch.isChecked());
                    ONE.setEnabled(mySwitch.isChecked());
                    contact.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                    phone.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                    cntry.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // checkedId is the RadioButton selected
                    RadioButton rb = (RadioButton) findViewById(checkedId);

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();

                    if (checkedId == R.id.radioButton) {
                        editor.putInt("radio", 1);
                    } else {
                        //RadioButton r = (RadioButton)findViewById(R.id.radioButton) ;
                        //r.setChecked(true);
                        //r = (RadioButton)findViewById(R.id.radioButton2) ;
                        //r.setChecked(false);
                        editor.putInt("radio", 0);
                    }

                    editor.commit();

                    contact.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                    phone.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                    cntry.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                }
            });

            phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("text", s.toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }

    public void getContact (View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String name1 = "";
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        name1 = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }
                }
                break;
        }

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();
        do {
            String name   = people.getString(indexName);
            String number = people.getString(indexNumber);
            if (name.equals(name1)) {
                String FinalNumber = putOutChars(number);
                FinalNumber = putOutChars2(FinalNumber);
                phone.setText(FinalNumber);
                break;
            }
        } while (people.moveToNext());
    }

    private String putOutChars(String number) {
        String res = number;
        while(res.indexOf("-") != -1) {
            int x = res.indexOf("-");
            res = res.substring(0,x) + res.substring(x+1);
        }
        return res;
    }

    private String putOutChars2(String number) {
        String res = number;
        while(res.indexOf(" ") != -1) {
            int x = res.indexOf(" ");
            res = res.substring(0,x) + res.substring(x+1);
        }
        return res;
    }
}


