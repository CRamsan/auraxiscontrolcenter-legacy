package com.cesarandres.ps2link;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

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
import com.cesarandres.ps2link.fragments.FragmentServerList;
import com.cesarandres.ps2link.fragments.FragmentTwitter;
import com.cesarandres.ps2link.fragments.holders.FragmentOutfitPager;
import com.cesarandres.ps2link.fragments.holders.FragmentProfilePager;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * @author Cesar Ramirez
 * 
 *         Class that will hold the current fragments. It behaves differently if
 *         it is run on a table or a phone. On a phone, every time a new
 *         fragment needs to be created, a new instance of this activity will be
 *         created. If this activity is running on a table, this tablet will
 *         keep a main menu on the left side while new fragments will be swapped
 *         on the right side.
 * 
 *         This activity will also use the @activityMode variable to keep track
 *         of the current fragment on top of the stack. This works correctly in
 *         phone mode, it has not been tested in tablets yet. //TODO Check the
 *         function of activityMode in tablets
 * 
 */
public class ActivityContainer extends BaseActivity implements FragmentCallbacks {

    private ActivityMode activityMode;
    private ObjectDataSource data;
    private boolean tablet = false;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	Bundle extras = getIntent().getExtras();
	if (savedInstanceState != null) {
	    setActivityMode(ActivityMode.valueOf(savedInstanceState.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY)));
	} else if (extras != null) {
	    setActivityMode(ActivityMode.valueOf(extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY)));
	} else {
	    setActivityMode(ActivityMode.ACTIVITY_MAIN_MENU);
	}

	setContentView(R.layout.activity_panel);

	if (findViewById(R.id.activityMainMenuFragment) != null) {
	    tablet = true;
	}

	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	BaseFragment newFragment = getFragmentByMode(getActivityMode());
	transaction.add(R.id.activityFrameLayout, newFragment);
	transaction.commit();

	setData(new ObjectDataSource(this));
	data.open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseActivity#onStart()
     */
    @Override
    protected void onStart() {
	super.onStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onRestart()
     */
    @Override
    protected void onRestart() {
	super.onRestart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseActivity#onResume()
     */
    @Override
    protected void onResume() {
	super.onResume();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
	super.onPause();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
	super.onStop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
	super.onDestroy();
	data.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
     * .Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	savedInstanceState.putString(ApplicationPS2Link.ACTIVITY_MODE_KEY, getActivityMode().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cesarandres.ps2link.base.BaseFragment.FragmentCallbacks#onItemSelected
     * (java.lang.String, java.lang.String[])
     */
    @Override
    public void onItemSelected(String id, String args[]) {
	data.close();
	data.open();
	ActivityMode mode = ActivityMode.valueOf(id);
	if (tablet) {
	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    BaseFragment newFragment = getFragmentByMode(mode);
	    Bundle bundle = new Bundle();
	    if (args != null && args.length > 0) {
		for (int i = 0; i < args.length; i++) {
		    bundle.putString("PARAM_" + i, args[i]);
		}
	    }
	    newFragment.setArguments(bundle);
	    transaction.replace(R.id.activityFrameLayout, newFragment);
	    transaction.addToBackStack(null);
	    transaction.commit();
	    setActivityMode(mode);
	} else {
	    Class<ActivityContainer> newActivityClass = ActivityContainer.class;
	    Intent intent = new Intent(this, newActivityClass);
	    if (args != null && args.length > 0) {
		for (int i = 0; i < args.length; i++) {
		    intent.putExtra("PARAM_" + i, args[i]);
		}
	    }
	    intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, mode);
	    startActivity(intent);
	}
    }

    /**
     * @return current activity mode
     */
    public ActivityMode getActivityMode() {
	return activityMode;
    }

    /**
     * @param activityMode
     *            set the new mode for this activity
     */
    public void setActivityMode(ActivityMode activityMode) {
	this.activityMode = activityMode;
    }

    /**
     * @return the ObjectDataSource that gives access to the local sqlite db.
     *         You should not open or close this, those methods are tied to the
     *         activity lyfecycle.
     */
    public ObjectDataSource getData() {
	return data;
    }

    /**
     * @param data
     *            a new ObjectDataSource to be handled by this activity.
     */
    public void setData(ObjectDataSource data) {
	this.data = data;
    }

    /**
     * @param activityMode
     *            string that represents one of the activity modes
     * @return the corresponding Fragment for the mode. If the parameter is null
     *         or invalid, this method returns null
     */
    private BaseFragment getFragmentByMode(String activityMode) {
	if (activityMode == null) {
	    Log.e(this.getClass().getName(), "Activity Mode could not be created because parameter was null");
	} else {
	    try {
		ActivityMode mode = ActivityMode.valueOf(activityMode);
		return getFragmentByMode(mode);
	    } catch (IllegalArgumentException e) {
		Log.e(this.getClass().getName(), "Activity Mode could not be created from parameter: " + activityMode);
	    }
	}
	return null;
    }

    /**
     * @param activityMode
     *            Activity mode
     * @return the fragment corresponding to the activity mode requested. If the
     *         activity mode is does not belong to any class, this method will
     *         return null.
     */
    private BaseFragment getFragmentByMode(ActivityMode activityMode) {
	BaseFragment newFragment = null;
	switch (activityMode) {
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
	case ACTIVITY_SERVER_LIST:
	    newFragment = new FragmentServerList();
	    break;
	case ACTIVITY_TWITTER:
	    newFragment = new FragmentTwitter();
	    break;
	case ACTIVITY_MAIN_MENU:
	    newFragment = new FragmentMainMenu();
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
}
