package com.example.shilo90.ringandfind;

import android.app.Activity;
import android.app.NotificationManager;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity2Activity extends ActionBarActivity implements ActionBar.TabListener {


    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    private fragmentHome f;

    public static final int PICK_CONTACT = 1;
    private boolean stopM;
    private boolean stopToast;
    private int intValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(SMSBroadcastReceiver.NOTIFICATION_ID);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ColorDrawable colorDrawable = new ColorDrawable(Color.rgb(169, 208, 221));
        actionBar.setBackgroundDrawable(colorDrawable);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("screenOn", false);
        editor.commit();

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return PlaceholderFragment.newInstance(position + 1);
            f = new fragmentHome();
            fragmentIntro f2 = new fragmentIntro();
            if (position == 0) {
                return f;
            }
            return f2;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String name1 = "";
        switch (reqCode) {
            case (fragmentHome.PICK_CONTACT) :
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
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("text", FinalNumber);
                editor.commit();

                fragmentHome.ins.setPhone(FinalNumber);
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

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        try {
            SMSBroadcastReceiver.notificationManager.cancel(SMSBroadcastReceiver.NOTIFICATION_ID);
            stopM = false;
        }
        catch (Exception e) {

        }
        boolean i = super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(intValue == 5) {
            try {
                SMSBroadcastReceiver.notificationManager.cancel(SMSBroadcastReceiver.NOTIFICATION_ID);
                stopM = false;
            } catch (Exception e) {

            }
        }

    }

    @Override
    protected void onStart() {

        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        stopToast = sharedPref.getBoolean("stopToast", true);
        Intent mIntent = getIntent();
        intValue = mIntent.getIntExtra("ring", 0);

        if(intValue == 5 && stopToast) {
            stopToast = false;
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity2Activity.this).create();
            //alertDialog.setTitle("ringring");
            //alertDialog.setMessage(getResources().getText(R.string.toast));
            //alertDialog.show();
            //Toast t = Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast), Toast.LENGTH_LONG);
            //t.setDuration();
            //t.show();
            stopM = true;
            editor.putBoolean("stopToast", false);
        }
        else {
            stopM = false;
            editor.putBoolean("stopToast", true);
        }
        editor.commit();
    }

}
