package com.example.shilo90.ringandfind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shilo90 on 08/06/15.
 */
public class fragmentHome extends Fragment{

    public static fragmentHome ins;

    public static final int PICK_CONTACT = 1;

    public View layout;
    public TextView switchStatus;
    public Switch mySwitch;
    public RadioButton All;
    public RadioButton ONE;
    public Button contact;
    public EditText phone;
    public RadioGroup radioGroup;
    public Spinner cntry;
    public TextView tagMyCountry;

    private List<String> CountryList;
    private List<String> CountryCodeList;
    private ArrayAdapter<CharSequence> countryAdapter;



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int power = sharedPref.getInt("power", 0);
        int radio = sharedPref.getInt("radio", 1);
        int countryPos = sharedPref.getInt("countryPos", 100);
        String text = sharedPref.getString("text", "");

        layout = inflater.inflate(R.layout.activity_main, container, false);
        switchStatus = ((TextView) layout.findViewById(R.id.mySwitch)); //(TextView) findViewById(R.id.mySwitch);
        mySwitch = (Switch) layout.findViewById(R.id.mySwitch);
        All = (RadioButton) layout.findViewById(R.id.radioButton);
        ONE = (RadioButton) layout.findViewById(R.id.radioButton2);
        contact = (Button) layout.findViewById(R.id.button);
        phone = (EditText) layout.findViewById(R.id.editText);
        radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
        cntry = (Spinner) layout.findViewById(R.id.spinner);
        tagMyCountry = ((TextView) layout.findViewById(R.id.my_country_tag));

        countryAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.item_array, R.layout.spinner_layout);
        countryAdapter.setDropDownViewResource(R.layout.spinner_layout);
        cntry.setAdapter(countryAdapter);
        cntry.setSelection(countryPos);

        CountryList = Arrays.asList(getResources().getStringArray(R.array.item_array));
        CountryCodeList = Arrays.asList(getResources().getStringArray(R.array.code_array));

        if (power == 1) {
            //set the switch to ON
            mySwitch.setText("Power ON");
            mySwitch.setChecked(true);
            mySwitch.setTextColor(Color.rgb(169, 208, 221));
        } else {
            mySwitch.setText("Power OFF");
            mySwitch.setChecked(false);
            mySwitch.setTextColor(Color.RED);
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
        if (cntry.isEnabled()) {
            cntry.setAlpha(1.0f);
            tagMyCountry.setTextColor(Color.BLACK);
        }
        else {
            cntry.setAlpha(0.4f);
            tagMyCountry.setTextColor(Color.GRAY);
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();

                if (isChecked) {
                    mySwitch.setText(getResources().getText(R.string.Power_ON));
                    mySwitch.setTextColor(Color.rgb(169, 208, 221));
                    editor.putInt("power", 1);

                } else {
                    mySwitch.setText(getResources().getText(R.string.Power_OFF));
                    mySwitch.setTextColor(Color.RED);
                    editor.putInt("power", 0);
                }
                editor.commit();

                All.setEnabled(mySwitch.isChecked());
                ONE.setEnabled(mySwitch.isChecked());
                contact.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                cntry.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                if (cntry.isEnabled()) {
                    cntry.setAlpha(1.0f);
                    tagMyCountry.setTextColor(Color.BLACK);
                }
                else {
                    cntry.setAlpha(0.4f);
                    tagMyCountry.setTextColor(Color.GRAY);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                if (cntry.isEnabled()) {
                    cntry.setAlpha(1.0f);
                    tagMyCountry.setTextColor(Color.BLACK);
                }
                else {
                    cntry.setAlpha(0.4f);
                    tagMyCountry.setTextColor(Color.GRAY);
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("text", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                getActivity().startActivityForResult(intent, PICK_CONTACT);
            }
        });


        ins = this;
        return layout;
    }

    public void setPhone (String p) {
        phone.setText(p);
    }

}
