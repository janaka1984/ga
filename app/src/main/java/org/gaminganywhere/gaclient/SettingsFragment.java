/*
 * Copyright (c) 2013 Chun-Ying Huang
 *
 * This file is part of GamingAnywhere (GA).
 *
 * GA is free software; you can redistribute it and/or modify it
 * under the terms of the 3-clause BSD License as published by the
 * Free Software Foundation: http://directory.fsf.org/wiki/License:BSD_3Clause
 *
 * GA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the 3-clause BSD License along with GA;
 * if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.gaminganywhere.gaclient;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	private String current_profile = null;
	private Context context = null;
	private boolean modified = false;
	private boolean isUpdate = false;
	
	private void showToast(String s) {
		Toast t = Toast.makeText(context, s, Toast.LENGTH_SHORT);
		t.show();
	}
	
	public void setProfile(String s) {
		current_profile = s;
	}
	
	public void setContext(Context ctx) {
		context = ctx;
	}

	public boolean isModified() {
		return modified;
	}

	private void profileConfigListPreference(String name, String summary, Object value) {
		ListPreference p = null;
		if((p = (ListPreference) findPreference(name)) != null) {
			p.setSummary(summary);
			p.setValue(value.toString());
		}
	}
	
	private void profileConfigEditTextPreference(String name, String summary, Object value, int type) {
		EditTextPreference p = null;
		if((p = (EditTextPreference) findPreference(name)) != null) {
			p.setSummary(summary);
			p.setText(value.toString());
			if(type != 0)
				p.getEditText().setInputType(type);
		}
	}
	
	private void profileConfigCheckBoxPreference(String name, boolean checked) {
		CheckBoxPreference p = null;
		if((p = (CheckBoxPreference) findPreference(name)) != null) {
			p.setChecked(checked);
		}
	}
	
	public void profileLoadDefault() {
		//
		profileConfigEditTextPreference("pref_title", "", "", InputType.TYPE_CLASS_TEXT);
		//
		profileConfigListPreference("pref_protocol", "rtsp", "rtsp");
		profileConfigEditTextPreference("pref_host", "", "",
				InputType.TYPE_TEXT_VARIATION_URI);
		profileConfigEditTextPreference("pref_port", "8554", "8554",
				InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		profileConfigEditTextPreference("pref_object", "/desktop", "/desktop",
				InputType.TYPE_TEXT_VARIATION_URI);
		profileConfigCheckBoxPreference("pref_rtpovertcp", false);
		//
		profileConfigCheckBoxPreference("pref_ctrlenable", true);
		profileConfigListPreference("pref_ctrlprotocol", "udp", "udp");
		profileConfigEditTextPreference("pref_ctrlport", "8555", "8555",
				InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		profileConfigCheckBoxPreference("pref_ctrlrelative", false);
		//
		profileConfigListPreference("pref_audio_channels", "2", "2");
		profileConfigListPreference("pref_audio_samplerate", "44100", "44100");
		//
		isUpdate = false;
		//showToast("Default value loaded");
	}

	public boolean profileLoad(String key) {
		HashMap<String,String> cfg = null;
		//
		EditTextPreference p = null;
		if((p = (EditTextPreference) findPreference("pref_title")) != null) {
			p.getEditText().setEnabled(false);
		}
		//
		if((cfg = GAConfigHelper.profileLoad(context, key)) == null) {
			showToast("Load profile '" + key + "' failed");
			return false;
		}
		//
		profileConfigEditTextPreference("pref_title", key, key, InputType.TYPE_CLASS_TEXT);
		//
		profileConfigListPreference("pref_protocol", cfg.get("protocol"), cfg.get("protocol"));
		profileConfigEditTextPreference("pref_host", cfg.get("host"), cfg.get("host"),
				InputType.TYPE_TEXT_VARIATION_URI);
		profileConfigEditTextPreference("pref_port", cfg.get("port"), cfg.get("port"),
				InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		profileConfigEditTextPreference("pref_object", cfg.get("object"), cfg.get("object"),
				InputType.TYPE_TEXT_VARIATION_URI);
		profileConfigCheckBoxPreference("pref_rtpovertcp", Integer.parseInt(cfg.get("rtpovertcp")) != 0);
		//
		profileConfigCheckBoxPreference("pref_ctrlenable", Integer.parseInt(cfg.get("ctrlenable")) != 0);
		profileConfigListPreference("pref_ctrlprotocol", cfg.get("ctrlprotocol"), cfg.get("ctrlprotocol"));
		profileConfigEditTextPreference("pref_ctrlport", cfg.get("ctrlport"), cfg.get("ctrlport"),
				InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		profileConfigCheckBoxPreference("pref_ctrlrelative", Integer.parseInt(cfg.get("ctrlrelative")) != 0);
		//
		profileConfigListPreference("pref_audio_channels", cfg.get("audio_channels"), cfg.get("audio_channels"));
		profileConfigListPreference("pref_audio_samplerate", cfg.get("audio_samplerate"), cfg.get("audio_samplerate"));
		//
		isUpdate = true;
		return true;
	}

	public boolean profileSave(JSONObject settings) throws JSONException {
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);

		JSONArray settingObject = settings.getJSONArray("settings");
//		JSONObject name = (JSONObject) settingObject.get(7);
		JSONObject protocol = (JSONObject) settingObject.get(1);
		JSONObject host = (JSONObject) settingObject.get(2);
		JSONObject port = (JSONObject) settingObject.get(3);
		JSONObject object = (JSONObject) settingObject.get(4);
		JSONObject rtpovertcp = (JSONObject) settingObject.get(5);
		JSONObject ctrlenable = (JSONObject) settingObject.get(8);
		JSONObject ctrlprotocol = (JSONObject) settingObject.get(9);
		JSONObject ctrlport = (JSONObject) settingObject.get(10);
		JSONObject ctrlrelative = (JSONObject) settingObject.get(11);
		JSONObject audio_channels = (JSONObject) settingObject.get(13);
		JSONObject audio_samplerate = (JSONObject) settingObject.get(14);

		HashMap<String,String> config = new HashMap<String,String>();

		String key = "janakatest"; //settings.getString("profileName");
//		JSONObject key = (JSONObject) profileObject.get(0);
		//
		if(key.equals("")) {
			showToast("Profile name cannot be null");
			return false;
		}
		if(host.equals("")) {
			showToast("Server host cannot be null");
			return false;
		}
		//
		config.clear();
		config.put("name", key);
		config.put("protocol", protocol.optString("value"));
		config.put("host", host.optString("value"));
		config.put("port", port.optString("value"));
		config.put("object", object.optString("value"));
		config.put("rtpovertcp", rtpovertcp.optBoolean("value") ? "1": "0");
		config.put("ctrlenable", ctrlenable.optBoolean("value") ? "1": "0");
		config.put("ctrlprotocol", ctrlprotocol.optString("value"));
		config.put("ctrlport", ctrlport.optString("value"));
		config.put("ctrlrelative", ctrlrelative.optBoolean("value") ? "1": "0");
		config.put("audio_channels", audio_channels.optString("value"));
		config.put("audio_samplerate", audio_samplerate.optString("value"));
		//
		if(GAConfigHelper.profileSave(context, key, config, isUpdate) == false)
			return false;
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context); 
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_profile);
		//
		if(current_profile == null)
			profileLoadDefault();
		else
			profileLoad(current_profile);
		//
		spref.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences spref,
			String key) {
		Preference p = null;
		if((p = findPreference(key)) == null)
			return;
		// trim?
		if(p.getClass() == EditTextPreference.class) {
			String olds = spref.getString(key, "");
			String news = olds.trim();
			if(olds.equals(news) == false) {
					profileConfigEditTextPreference(key, news, news, 0);
			}
		}
		//
		if(key.equals("pref_object")) {
			String s = spref.getString(key, "").trim();
			if(s.charAt(0) != '/')
				profileConfigEditTextPreference("pref_object", "/" + s, "/" + s,
						InputType.TYPE_TEXT_VARIATION_URI);
		}
		if(p.getClass() != CheckBoxPreference.class) {
			p.setSummary(spref.getString(key, "<error>"));
		}
		modified = true;
	}
	
}
