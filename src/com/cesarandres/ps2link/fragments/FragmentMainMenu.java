package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.module.BitmapWorkerTask;

/**
 * 
 * 
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 * 
 */
public class FragmentMainMenu extends BaseFragment {

	private IInAppBillingService mService;
	private ServiceConnection mServiceConn;
	
    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_main_menu, container, false);
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

	this.fragmentTitle.setText(getString(R.string.app_name_capital));

	Button buttonCharacters = (Button) getActivity().findViewById(R.id.buttonCharacters);
	Button buttonServers = (Button) getActivity().findViewById(R.id.buttonServers);
	Button buttonOutfit = (Button) getActivity().findViewById(R.id.buttonOutfit);
	Button buttonNews = (Button) getActivity().findViewById(R.id.buttonNews);
	Button buttonTwitter = (Button) getActivity().findViewById(R.id.buttonTwitter);
	Button buttonReddit = (Button) getActivity().findViewById(R.id.buttonRedditFragment);
	Button buttonDonate = (Button) getActivity().findViewById(R.id.buttonDonate);
	Button buttonAbout = (Button) getActivity().findViewById(R.id.buttonAbout);

	buttonCharacters.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_PROFILE_LIST.toString(), null);
	    }
	});
	buttonServers.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_SERVER_LIST.toString(), null);
	    }
	});
	buttonOutfit.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_OUTFIT_LIST.toString(), null);
	    }
	});
	buttonNews.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_LINK_MENU.toString(), null);
	    }
	});
	buttonTwitter.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_TWITTER.toString(), null);
	    }
	});
	buttonReddit.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_REDDIT.toString(), null);
	    }
	});
	buttonAbout.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    mCallbacks.onItemSelected(ActivityMode.ACTIVITY_ABOUT.toString(), null);		
	    }
	});
	buttonDonate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(mService != null){
	    		DownloadDonationsTask task = new DownloadDonationsTask();
	    		setCurrentTask(task);
	    		task.execute();
	    	}
	    }
	});	
	
	final ImageButton buttonPS2Background = (ImageButton) getActivity().findViewById(R.id.buttonPS2);
	buttonPS2Background.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		ImageView background = ((ImageView) getActivity().findViewById(R.id.imageViewBackground));
		background.setImageResource(R.drawable.ps2_activity_background);
		background.setScaleType(ScaleType.FIT_START);

		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.PS2.toString());
		editor.commit();
	    }
	});

	final ImageButton buttonNCBackground = (ImageButton) getActivity().findViewById(R.id.buttonNC);
	buttonNCBackground.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		BitmapWorkerTask task = new BitmapWorkerTask((ImageView) (getActivity().findViewById(R.id.imageViewBackground)), getActivity());
		task.execute("nc_wallpaper.jpg");
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.NC.toString());
		editor.commit();
	    }
	});

	final ImageButton buttonTRBackground = (ImageButton) getActivity().findViewById(R.id.buttonTR);
	buttonTRBackground.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		BitmapWorkerTask task = new BitmapWorkerTask((ImageView) (getActivity().findViewById(R.id.imageViewBackground)), getActivity());
		task.execute("tr_wallpaper.jpg");
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.TR.toString());
		editor.commit();

	    }
	});

	final ImageButton buttonVSBackground = (ImageButton) getActivity().findViewById(R.id.buttonVS);
	buttonVSBackground.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		BitmapWorkerTask task = new BitmapWorkerTask((ImageView) (getActivity().findViewById(R.id.imageViewBackground)), getActivity());
		task.execute("vs_wallpaper.jpg");
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.VS.toString());
		editor.commit();
	    }
	});
	
	mServiceConn = new ServiceConnection() {
		   @Override
		   public void onServiceDisconnected(ComponentName name) {
		       mService = null;
		   }

		   @Override
		   public void onServiceConnected(ComponentName name, 
		      IBinder service) {
		       mService = IInAppBillingService.Stub.asInterface(service);
		   }
		};
		
	  Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
	  serviceIntent.setPackage("com.android.vending");
	  getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	checkPreferedButtons();
    }

    /**
     * This function will check the preferences to see if any profile or outfit
     * has been set as preferred. If any has been set then the respective button
     * will be displayed, they will be hidden otherwise.
     */
    public void checkPreferedButtons() {
	SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);

	String preferedProfileId = settings.getString("preferedProfile", "");
	String preferedProfileName = settings.getString("preferedProfileName", "");
	final Button buttonPreferedProfile = (Button) getActivity().findViewById(R.id.buttonPreferedProfile);
	if (!preferedProfileId.equals("")) {
	    buttonPreferedProfile.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		    mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			    new String[] { 	settings.getString("preferedProfile", ""),
		    					settings.getString("preferedProfileNamespace", Namespace.PS2PC.name())});
		}
	    });
	    buttonPreferedProfile.setText(preferedProfileName);
	    buttonPreferedProfile.setVisibility(View.VISIBLE);
	} else {
	    buttonPreferedProfile.setVisibility(View.GONE);
	}

	String preferedOutfitId = settings.getString("preferedOutfit", "");
	String preferedOutfitName = settings.getString("preferedOutfitName", "");
	final Button buttonPreferedOutfit = (Button) getActivity().findViewById(R.id.buttonPreferedOutfit);
	if (!preferedOutfitId.equals("")) {

	    buttonPreferedOutfit.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		    mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
			    new String[] { 	settings.getString("preferedOutfit", ""),
		    					settings.getString("preferedOutfitNamespace", Namespace.PS2PC.name())});
		}
	    });
	    buttonPreferedOutfit.setVisibility(View.VISIBLE);
	    buttonPreferedOutfit.setText(preferedOutfitName);
	} else {
	    buttonPreferedOutfit.setVisibility(View.GONE);
	}

    }
    
    /* (non-Javadoc)
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroy()
     */
    @Override
	public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }
    
    /**
     * @author cramsan
     *
     */
    private class DownloadDonationsTask extends AsyncTask<Void, Void, ArrayList<String>> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			int response = -1;
			Bundle ownedItems;

			try {
				ownedItems = mService.getPurchases(3, getActivity().getPackageName(), "inapp", null);
				response = ownedItems.getInt("RESPONSE_CODE");
			} catch (RemoteException e1) {
				e1.printStackTrace();
				return null;
			}

			if (response == 0) {
				ArrayList<String>  purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
			   
				for (int i = 0; i < purchaseDataList.size(); ++i) {
					String purchaseData = purchaseDataList.get(i);
					try {
						JSONObject ownedObject;
						ownedObject = new JSONObject(purchaseData);
						String token = ownedObject.getString("purchaseToken");
						response = mService.consumePurchase(3, getActivity().getPackageName(), token);
					} catch (JSONException e) {
						e.printStackTrace();
						return null;
					} catch (RemoteException e) {
						e.printStackTrace();
					return null;
				}
			   }
			}
						
			ArrayList<String> skuList = new ArrayList<String> ();
			skuList.add("item_donation_1");
			skuList.add("item_donation_2");
			skuList.add("item_donation_3");
			skuList.add("item_donation_4");
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			Bundle skuDetails;
			try {
				skuDetails = mService.getSkuDetails(3, getActivity().getPackageName(), "inapp", querySkus);
				response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {
				   return skuDetails.getStringArrayList("DETAILS_LIST");			   
				}else{
					return null;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}		
		}
		
        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        protected void onPostExecute(ArrayList<String> result) {
        	if(result != null){
        		if(result.size() > 0){
        		DonationsDialogFragment newFragment = new DonationsDialogFragment();
                if(!newFragment.setResponseList(result)){
            		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_error_server_error), Toast.LENGTH_LONG).show();
            		return;
                }
                FragmentManager manager = getActivity().getSupportFragmentManager();
                if(manager != null){
                	newFragment.show(manager, "donations");	
                }else{
            		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_error_empty_response), Toast.LENGTH_LONG).show();
                }
        		}else{
            		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_error_empty_response), Toast.LENGTH_LONG).show();
        		}
        	}else{
        		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_error_response_error), Toast.LENGTH_LONG).show();
        	}
        }

    }
    
    public class DonationsDialogFragment extends DialogFragment {
    	
    	private ArrayList<String> responseList;
    	private CharSequence[] displayData;
    	
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder	.setTitle(R.string.text_choose_donation)
            		.setItems(displayData, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int index) {
        			String thisResponse = responseList.get(index);
  					JSONObject object;
					try {
						object = new JSONObject(thisResponse);
	   				    String sku = object.getString("productId");
	                	Bundle buyIntentBundle = mService.getBuyIntent(3, getActivity().getPackageName(), sku, "inapp", "");
	                	PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
	                	getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
	                			   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
	                			   Integer.valueOf(0));
	                	return;
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (SendIntentException e) {
						e.printStackTrace();
					}
	        		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_error_error_sending), Toast.LENGTH_LONG).show();
	        		return;
                }});
            setRetainInstance(true);
            return builder.create();
        }

		public boolean setResponseList(ArrayList<String> responseList) {
			this.displayData = new String[responseList.size()];
			for (int i=0; i<responseList.size(); i++) {
				String thisResponse = responseList.get(i);
				try {
					JSONObject object = new JSONObject(thisResponse);
				    String sku = object.getString("title");
				    displayData[i] = sku;
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}			      
			}
			this.responseList = responseList;
			return true;
		}
    }
}
