package com.example.shilo90.ringandfind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by shilo90 on 08/06/15.
 */
public class fragmentHome extends Fragment{

    public static fragmentHome ins;

    public static final int PICK_CONTACT1 = 1;
    public static final int PICK_CONTACT2 = 2;
    public static final int PICK_CONTACT3 = 3;

    public View layout;
    public TextView switchStatus;
    public Switch mySwitch;
    public RadioButton All;
    public RadioButton ONE;
    public ImageButton contact1;
    public EditText phone1;
    public ImageButton contact2;
    public EditText phone2;
    public ImageButton contact3;
    public EditText phone3;
    public RadioGroup radioGroup;
    public Spinner soundSpinner;
    public ImageButton playBtn;

    public TextView powerText;
    public RadioButton r1;
    public RadioButton r2;

    private List<String> soundList;
    private SpinnerAdapter soundAdapter;

    private MediaPlayer mPlayer1;
    private Boolean isPlaying = false;
    private int clickPlay = 0;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int power = sharedPref.getInt("power", 0);
        int radio = sharedPref.getInt("radio", 1);
        int soundNumber = sharedPref.getInt("soundNumber", 0);
        final String text1 = sharedPref.getString("text1", "");
        final String text2 = sharedPref.getString("text2", "");
        final String text3 = sharedPref.getString("text3", "");

        layout = inflater.inflate(R.layout.activity_main, container, false);
        switchStatus = ((TextView) layout.findViewById(R.id.mySwitch)); //(TextView) findViewById(R.id.mySwitch);
        mySwitch = (Switch) layout.findViewById(R.id.mySwitch);
        All = (RadioButton) layout.findViewById(R.id.radioButton);
        ONE = (RadioButton) layout.findViewById(R.id.radioButton2);
        contact1 = (ImageButton) layout.findViewById(R.id.contacts1);
        phone1 = (EditText) layout.findViewById(R.id.editTextNumber1);
        contact2 = (ImageButton) layout.findViewById(R.id.contacts2);
        phone2 = (EditText) layout.findViewById(R.id.editTextNumber2);
        contact3 = (ImageButton) layout.findViewById(R.id.contacts3);
        phone3 = (EditText) layout.findViewById(R.id.editTextNumber3);
        radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
        soundSpinner = (Spinner) layout.findViewById((R.id.spinner));
        playBtn = (ImageButton) layout.findViewById(R.id.trySound);
        powerText = (TextView) layout.findViewById(R.id.textPower);

        //soundAdapter = ArrayAdapter.createFromResource(
        //        getActivity(), R.array.soundarr, R.layout.spinner_layout);
        soundAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_layout, getResources().getStringArray(R.array.soundarr));
        soundAdapter.setDropDownViewResource(R.layout.spinner_layout);
        soundSpinner.setAdapter(soundAdapter);
        soundSpinner.setSelection(soundNumber);

        soundList = Arrays.asList(getResources().getStringArray(R.array.soundarr));

        if (android.os.Build.VERSION.SDK_INT < 21) {
            powerText.setVisibility(View.GONE);
        }
        if (power == 1) {
            //set the switch to ON
            powerText.setText(getResources().getText(R.string.Power_ON));
            mySwitch.setChecked(true);
            powerText.setTextColor(Color.rgb(169, 208, 221));
        } else {
            powerText.setText(getResources().getText(R.string.Power_OFF));
            mySwitch.setChecked(false);
            powerText.setTextColor(Color.RED);
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

        if (Locale.getDefault().getLanguage().equals("he") || Locale.getDefault().getLanguage().equals("iw")) {
            r1 = (RadioButton)layout.findViewById(R.id.radioButton);
            r2 = (RadioButton)layout.findViewById(R.id.radioButton2);
            //r1.setGravity(Gravity.RIGHT);
            //r2.setGravity(Gravity.RIGHT);
            //r1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            //r2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL );
        }
        All.setEnabled(mySwitch.isChecked());
        ONE.setEnabled(mySwitch.isChecked());

        phone1.setText(text1);
        phone2.setText(text2);
        phone3.setText(text3);

        contact1.setEnabled(ONE.isChecked() && mySwitch.isChecked());
        phone1.setEnabled(ONE.isChecked() && mySwitch.isChecked());
        contact2.setEnabled(ONE.isChecked() && mySwitch.isChecked());
        phone2.setEnabled(ONE.isChecked() && mySwitch.isChecked());
        contact3.setEnabled(ONE.isChecked() && mySwitch.isChecked());
        phone3.setEnabled(ONE.isChecked() && mySwitch.isChecked());

        soundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                //String selecteditem = adapter.getItemAtPosition(i).toString();

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("soundNumber", i);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();

                if (isChecked) {
                    powerText.setText(getResources().getText(R.string.Power_ON));
                    powerText.setTextColor(Color.rgb(169, 208, 221));
                    editor.putInt("power", 1);

                } else {
                    powerText.setText(getResources().getText(R.string.Power_OFF));
                    powerText.setTextColor(Color.RED);
                    editor.putInt("power", 0);
                }
                editor.commit();

                All.setEnabled(mySwitch.isChecked());
                ONE.setEnabled(mySwitch.isChecked());
                contact1.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone1.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                contact2.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone2.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                contact3.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone3.setEnabled(ONE.isChecked() && mySwitch.isChecked());

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

                contact1.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone1.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                contact2.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone2.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                contact3.setEnabled(ONE.isChecked() && mySwitch.isChecked());
                phone3.setEnabled(ONE.isChecked() && mySwitch.isChecked());

            }
        });

        phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("text1", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("text2", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("text3", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                getActivity().startActivityForResult(intent, PICK_CONTACT1);
            }
        });

        contact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                getActivity().startActivityForResult(intent, PICK_CONTACT2);
            }
        });

        contact3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                getActivity().startActivityForResult(intent, PICK_CONTACT3);
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    playMusic(clickPlay);
                }
                else {
                    stopMusic();
                }
            }
        });



        ins = this;
        return layout;
    }

    public void setPhone1 (String p) {
        phone1.setText(p);
    }
    public void setPhone2 (String p) {
        phone2.setText(p);
    }
    public void setPhone3 (String p) {
        phone3.setText(p);
    }

    public void playMusic(final int currentClickPlay) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int soundNumber = sharedPref.getInt("soundNumber", 0);
        switch(soundNumber) {
            case 0:
                mPlayer1 = MediaPlayer.create(getActivity(), R.raw.ringring1long);
                mPlayer1.start();
                isPlaying = true;
                break;
            case 1:
                mPlayer1 = MediaPlayer.create(getActivity(), R.raw.china_long);
                mPlayer1.start();
                isPlaying = true;
                break;
            case 2:
                mPlayer1 = MediaPlayer.create(getActivity(), R.raw.ringring1long);
                mPlayer1.start();
                isPlaying = true;
                break;
            default:
                break;

        }

        playBtn.setBackgroundResource(R.drawable.btn_select4);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentClickPlay == clickPlay - 1) {
                    stopMusic();
                }
            }
        }, 61000);

        clickPlay++;
    }

    public void stopMusic() {
        mPlayer1.stop();
        isPlaying = false;
        //stopBtn.setVisibility(View.GONE);
        //playBtn.setVisibility(View.VISIBLE);
        playBtn.setBackgroundResource(R.drawable.btn_select3);

    }

    @Override
    public void onPause() {
        if (isPlaying) {
            stopMusic();
        }
        super.onPause();
    }



}
