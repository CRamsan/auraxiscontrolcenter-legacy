package com.cesarandres.ps2link.fragments;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.content.CharacterEvent;
import com.cesarandres.ps2link.soe.util.Logger;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This fragment will retrieve the current list of alerts as well as give the
 * user the option to register for push notifications.
 * 
 */
public class FragmentAlertSettings extends BaseFragment {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";

	String SENDER_ID = "656146981691";

	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;

	String regid;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
	 * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_kill_list, container, false);
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

		Activity context = getActivity();		
		if (ApplicationPS2Link.checkPlayServices(context)) {
			gcm = GoogleCloudMessaging.getInstance(context);
			regid = ApplicationPS2Link.getRegistrationId(context);

			if (regid.isEmpty()) {
				APIRegistrationTask currentTask = new APIRegistrationTask();
				setCurrentTask(currentTask);
				currentTask.execute();
			}
		} else {
			Logger.log(Log.INFO, this, "No valid Google Play Services APK found.");
		}

		
		ListView listRoot = (ListView) getActivity().findViewById(
				R.id.listViewKillList);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				mCallbacks.onItemSelected(
						ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE
								.toString(),
						new String[] { ((CharacterEvent) myAdapter
								.getItemAtPosition(myItemInt))
								.getImportant_character_id() });
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
	
	private class APIRegistrationTask extends AsyncTask<Integer, Integer, Integer> {
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
        protected Integer doInBackground(Integer... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                //TODO Implement this method
                sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                ApplicationPS2Link.storeRegistrationId(getActivity(), regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            Logger.log(Log.INFO, this, msg);
            return 1;
        }

		private void sendRegistrationIdToBackend() throws IOException {			
			String url = "http://10.0.0.15:8080/register";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			String urlParameters = "regId=" + regid;
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			Logger.log(Log.INFO, this, "\nSending 'POST' request to URL : " + url);
			Logger.log(Log.INFO, this, "Post parameters : " + urlParameters);
			Logger.log(Log.INFO, this, "Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			Logger.log(Log.INFO, this, response.toString());
		}
	}
}