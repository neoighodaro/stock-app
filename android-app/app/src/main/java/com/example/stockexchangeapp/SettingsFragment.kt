package com.example.stockexchangeapp

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    // Load the preferences from an XML resource
    setPreferencesFromResource(R.xml.preference, rootKey)
  }

}