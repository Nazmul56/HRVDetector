package com.example.android.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.MenuItem
import androidx.multidex.BuildConfig
import com.example.android.camera2basic.R
import com.example.android.utils.Lg.d

class SettingsActivity : AppCompatPreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        //load settings fragment
        fragmentManager.beginTransaction().replace(android.R.id.content, MainPreferenceFragment())
            .commit()
    }

    class MainPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings_pref)
            notificationSettingsPref
            ringtoneSettingsPref
            settingsOptionListener()
            val versionName = findPreference(getString(R.string.app_version_key))
            versionName.summary = BuildConfig.VERSION_NAME
        }

        private val notificationSettingsPref: Unit
            private get() {
                val notificationPref = findPreference(getString(R.string.key_notifications))
                val notificationPrefBoolean = PreferenceManager
                    .getDefaultSharedPreferences(notificationPref.context)
                    .getBoolean(notificationPref.key, true)
                d(TAG, " notification pref status: $notificationPrefBoolean")
            }
        private val ringtoneSettingsPref: Unit
            private get() {
                val notificationPref = findPreference(getString(R.string.key_ringtone))
                val notificationPrefBoolean = PreferenceManager
                    .getDefaultSharedPreferences(notificationPref.context)
                    .getString(notificationPref.key, "No")
                d(TAG, "ringtone pref status: $notificationPrefBoolean")
            }

        private fun settingsOptionListener() {
            val activity = activity
            //feedback preference click listener
            val feedBackPref = findPreference(getString(R.string.key_send_feedback))
            feedBackPref.onPreferenceClickListener =
                Preference.OnPreferenceClickListener { preference: Preference? ->
                    sendFeedback(activity)
                    true
                }
            //FAQ Activity
            val faqPref = findPreference(getString(R.string.key_faq))
            faqPref.onPreferenceClickListener =
                Preference.OnPreferenceClickListener { preference: Preference? -> true }
            //Privacy and Policy
            val privacyPref = findPreference(getString(R.string.key_privacy))
            privacyPref.onPreferenceClickListener =
                Preference.OnPreferenceClickListener { preference: Preference? -> true }
            val notificationPref = findPreference(getString(R.string.key_notifications))
            notificationPref.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference: Preference?, value: Any? -> true }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        d(TAG, "onBackPressed Called")
    }

    companion object {
        private val TAG = SettingsActivity::class.java.simpleName
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener =
                sBindPreferenceSummaryToValueListener
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getString(preference.key, "")
            )
        }

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener =
            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
                val stringValue = newValue.toString()
                if (preference is ListPreference) {
                    // For list preferences, look up the correct display value in
                    // the preference's 'entries' list.
                    val listPreference = preference
                    val index = listPreference.findIndexOfValue(stringValue)
                    // Set the summary to reflect the new value.
                    preference.setSummary(
                        if (index >= 0) listPreference.entries[index] else null
                    )
                } else if (preference is RingtonePreference) {
                    // For ringtone preferences, look up the correct display value
                    // using RingtoneManager.
                    if (TextUtils.isEmpty(stringValue)) {
                        // Empty values correspond to 'silent' (no ringtone).
                        preference.setSummary(R.string.pref_ringtone_silent)
                    } else {
                        val ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue)
                        )
                        if (ringtone == null) {
                            // Clear the summary if there was a lookup error.
                            preference.setSummary(R.string.summary_choose_ringtone)
                        } else {
                            // Set the summary to reflect the new ringtone display
                            // name.
                            val name = ringtone.getTitle(preference.getContext())
                            preference.setSummary(name)
                        }
                    }
                } else if (preference is EditTextPreference) {
                    if (preference.getKey() == "key_gallery_name") {
                        // update the changed gallery name to summary filed
                        preference.setSummary(stringValue)
                    }
                } else {
                    preference.summary = stringValue
                }
                true
            }

        /**
         * Email client intent to send support mail
         * Appends the necessary device information to email body
         * useful when providing support
         */
        fun sendFeedback(context: Context) {
            var body: String? = null
            try {
                body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
                body = """
                -----------------------------
                Please don't remove this information
                Device OS: Android 
                Device OS version: ${Build.VERSION.RELEASE}
                App Version: $body
                Device Brand: ${Build.BRAND}
                Device Model: ${Build.MODEL}
                Device Manufacturer: ${Build.MANUFACTURER}"""
            } catch (e: PackageManager.NameNotFoundException) {
                d(TAG, " Send FeedBack Bug $e")
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@durbinlabs.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.choose_email_client)
                )
            )
        }
    }
}