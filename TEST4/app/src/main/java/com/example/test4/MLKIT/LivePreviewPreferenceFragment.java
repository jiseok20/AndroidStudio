package com.example.test4.MLKIT;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import com.example.test4.R;

public class LivePreviewPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_live_preview_quickstart);
        setUpCameraPreferences();
    }

    void setUpCameraPreferences() {
        PreferenceCategory cameraPreference =
                (PreferenceCategory) findPreference(getString(R.string.pref_category_key_camera));
        cameraPreference.removePreference(
                findPreference(getString(R.string.pref_key_camerax_rear_camera_target_resolution)));
        cameraPreference.removePreference(
                findPreference(getString(R.string.pref_key_camerax_front_camera_target_resolution)));
    }



}