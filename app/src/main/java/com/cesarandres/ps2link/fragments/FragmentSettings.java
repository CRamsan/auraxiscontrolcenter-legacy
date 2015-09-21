package com.cesarandres.ps2link.fragments;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.parse.ParseInstallation;

/**
 * Fragment that will provide settings for the user
 */
public class FragmentSettings extends BaseFragment {

    private int fromTimeHours;
    private int fromTimeMinutes;
    private int toTimeHours;
    private int toTimeMinutes;

    public static final String PREF_KEY_NOTIFICATION_ENABLE = "push_enabled";
    public static final String PREF_KEY_START_HOUR = "push_start_hours";
    public static final String PREF_KEY_START_MINUTE = "push_start_minutes";
    public static final String PREF_KEY_END_HOUR = "push_end_hours";
    public static final String PREF_KEY_END_MINUTE = "push_end_minutes";


    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.fragmentTitle.setText(getString(R.string.title_settings));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        fromTimeHours = prefs.getInt(PREF_KEY_START_HOUR, 0);
        fromTimeMinutes = prefs.getInt(PREF_KEY_START_MINUTE, 0);
        toTimeHours = prefs.getInt(PREF_KEY_END_HOUR, 23);
        toTimeMinutes = prefs.getInt(PREF_KEY_END_MINUTE, 59);

        boolean checkEnabled = prefs.getBoolean(PREF_KEY_NOTIFICATION_ENABLE, false);

        CheckBox enabledCheckbox = (CheckBox)getView().findViewById(R.id.checkBoxSettingsNotificationsEnabled);
        final EditText fromTime = (EditText)getView().findViewById(R.id.editTextSettingsFrom);
        final EditText toTime = (EditText)getView().findViewById(R.id.editTextSettingsTo);
        fromTime.setKeyListener(null);
        toTime.setKeyListener(null);

        enabledCheckbox.setChecked(checkEnabled);
        fromTime.setEnabled(checkEnabled);
        toTime.setEnabled(checkEnabled);

        this.setTimeText(fromTime, fromTimeHours, fromTimeMinutes);
        this.setTimeText(toTime, toTimeHours, toTimeMinutes);

        enabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fromTime.setEnabled(true);
                    toTime.setEnabled(true);
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    Object savedChannels = installation.get("channels_saved");
                    if(savedChannels != null) {
                        installation.put("channels", savedChannels);
                    }
                    installation.remove("channels_saved");
                    installation.saveInBackground();
                } else {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                    Object channelsToSave = installation.get("channels");
                    if(channelsToSave != null) {
                        installation.put("channels_saved", channelsToSave);
                    }
                    installation.remove("channels");
                    installation.saveInBackground();
                    fromTime.setEnabled(false);
                    toTime.setEnabled(false);
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PREF_KEY_NOTIFICATION_ENABLE, isChecked);
                editor.commit();
            }
        });
        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tp1 = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay > toTimeHours ||
                                (hourOfDay == toTimeHours && minute > toTimeMinutes)){
                            Toast.makeText(getActivity(), R.string.toast_time_error, Toast.LENGTH_LONG).show();
                            return;
                        }
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREF_KEY_START_HOUR, hourOfDay);
                        editor.putInt(PREF_KEY_START_MINUTE, minute);
                        editor.commit();
                        fromTimeHours = hourOfDay;
                        fromTimeMinutes = minute;
                        setTimeText(fromTime, hourOfDay, minute);
                    }
                },  fromTimeHours, fromTimeMinutes, false);
                tp1.show();
            }
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tp1 = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if(hourOfDay < fromTimeHours ||
                                (hourOfDay == fromTimeHours && minute < fromTimeMinutes)){
                            Toast.makeText(getActivity(), R.string.toast_time_error, Toast.LENGTH_LONG).show();
                            return;
                        }

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREF_KEY_END_HOUR, hourOfDay);
                        editor.putInt(PREF_KEY_END_MINUTE, minute);
                        editor.commit();
                        toTimeHours = hourOfDay;
                        toTimeMinutes = minute;
                        setTimeText(toTime, hourOfDay, minute);
                    }
                }, toTimeHours, toTimeMinutes, false);
                tp1.show();
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //TODO This needs to be localized
    private void setTimeText(EditText editView, int hours, int minutes){
        int hourOfDay = hours;
        String partOfDay = "AM";
        if(hourOfDay >= 12){
            partOfDay = "PM";
            if(hourOfDay > 12){
                hourOfDay-=12;
            }
        }
        editView.setText(hourOfDay + ":" + minutes + " " + partOfDay);
    }
}
