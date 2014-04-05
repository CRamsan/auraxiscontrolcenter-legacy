package com.cesarandres.ps2link;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.base.BaseActivity;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.base.BaseFragment.FragmentCallbacks;
import com.cesarandres.ps2link.fragments.FragmentAddOutfit;
import com.cesarandres.ps2link.fragments.FragmentAddProfile;
import com.cesarandres.ps2link.fragments.FragmentLinksMenu;
import com.cesarandres.ps2link.fragments.FragmentMainMenu;
import com.cesarandres.ps2link.fragments.FragmentOutfitList;
import com.cesarandres.ps2link.fragments.FragmentProfileList;
import com.cesarandres.ps2link.fragments.FragmentServer;
import com.cesarandres.ps2link.fragments.FragmentServerList;
import com.cesarandres.ps2link.fragments.FragmentTwitter;
import com.cesarandres.ps2link.fragments.FragmentWds;
import com.cesarandres.ps2link.fragments.holders.FragmentOutfitPager;
import com.cesarandres.ps2link.fragments.holders.FragmentProfilePager;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityContainerSingle extends BaseActivity implements FragmentCallbacks {

	private String activityMode;
	private ObjectDataSource data;
    private boolean tablet = false;
    private BaseFragment secondPanel;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (savedInstanceState != null) {
			setActivityMode(savedInstanceState.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY));
		} else if (extras != null) {
			setActivityMode(extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY));
		} else {
			setActivityMode("ACTIVITY_MAIN_MENU");
		}

		setContentView(R.layout.activity_panel);

        if(findViewById(R.id.activityMainMenuFragment) != null){
            tablet = true;
        }

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		BaseFragment newFragment = getFragmentByMode(getActivityMode());
        if(newFragment != null){
        	if(secondPanel != null){
        		transaction.remove(newFragment);
        	}
            transaction.add(R.id.activityFrameLayout, newFragment);
            this.secondPanel = newFragment;
            //transaction.addToBackStack(null);
            transaction.commit();
        }

		setData(new ObjectDataSource(this));
		data.open();

		Button titleBack = (Button) findViewById(R.id.buttonFragmentTitle);
		titleBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				navigateUp();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		data.close();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString(ApplicationPS2Link.ACTIVITY_MODE_KEY, getActivityMode());
	}

	@Override
	public void onItemSelected(String id, String args[]) {

        if(tablet){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BaseFragment newFragment = getFragmentByMode(id);
            if(newFragment != null){
            	Bundle bundle = new Bundle();
            	if (args != null && args.length > 0) {
                    for (int i = 0; i < args.length; i++) {
                        bundle.putString("PARAM_" + i, args[i]);
                    }
                }
    			newFragment.setArguments(bundle);
                transaction.replace(R.id.activityFrameLayout, newFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        }else{
            Class newActivityClass = ActivityContainerSingle.class;
            Intent intent = new Intent(this, newActivityClass);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    intent.putExtra("PARAM_" + i, args[i]);
                }
            }
            intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, id);
            startActivity(intent);
        }
	}

	public String getActivityMode() {
		return activityMode;
	}

	public void setActivityMode(String activityMode) {
		this.activityMode = activityMode;
	}

	public ObjectDataSource getData() {
		return data;
	}

	public void setData(ObjectDataSource data) {
		this.data = data;
	}

	//TODO Apparently this code is never used.
	private BaseFragment getFragmentByMode(String activityMode) {
		BaseFragment newFragment = null;
        switch (ActivityMode.valueOf(activityMode)) {
            case ACTIVITY_ADD_OUTFIT:
                newFragment = new FragmentAddOutfit();
                break;
            case ACTIVITY_ADD_PROFILE:
                newFragment = new FragmentAddProfile();
                break;
            case ACTIVITY_MEMBER_LIST:
                newFragment = new FragmentOutfitPager();
                break;
            case ACTIVITY_OUTFIT_LIST:
                newFragment = new FragmentOutfitList();
                break;
            case ACTIVITY_PROFILE_LIST:
                newFragment = new FragmentProfileList();
                break;
            case ACTIVITY_SERVER:
                newFragment = new FragmentServer();
                break;
            case ACTIVITY_SERVER_LIST:
                newFragment = new FragmentServerList();
                break;
            case ACTIVITY_TWITTER:
                newFragment = new FragmentTwitter();
                break;
            case ACTIVITY_MAIN_MENU:
                if(!tablet){
                    newFragment = new FragmentMainMenu();
                }
                break;
            case ACTIVITY_WDS:
                newFragment = new FragmentWds();
                break;
            case ACTIVITY_PROFILE:
    			newFragment = new FragmentProfilePager();
    			break;
            case ACTIVITY_LINK_MENU:
                newFragment = new FragmentLinksMenu();
                break;
            default:
                break;
        }
		return newFragment;
	}

	public static class WellcomeDialogFragment extends DialogFragment {

		public static WellcomeDialogFragment newInstance() {
			WellcomeDialogFragment d = new WellcomeDialogFragment();
			return d;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.content_new_paid_version).setPositiveButton(R.string.text_sure, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String url = "https://play.google.com/store/apps/details?id=com.cesarandres.ps2link.key";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			}).setNegativeButton(R.string.text_not_right_now, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}
