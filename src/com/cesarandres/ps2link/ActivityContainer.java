package com.cesarandres.ps2link;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

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
import com.cesarandres.ps2link.fragments.FragmentReddit;
import com.cesarandres.ps2link.fragments.FragmentServerList;
import com.cesarandres.ps2link.fragments.FragmentTwitter;
import com.cesarandres.ps2link.fragments.holders.FragmentOutfitPager;
import com.cesarandres.ps2link.fragments.holders.FragmentProfilePager;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * 
 * 
 * Class that will hold the current fragments. It behaves differently if it is
 * run on a table or a phone. On a phone, every time a new fragment needs to be
 * created, a new instance of this activity will be created. If this activity is
 * running on a table, this tablet will keep a main menu on the left side while
 * new fragments will be swapped on the right side.
 * 
 * This activity will also use the @activityMode variable to keep track of the
 * current fragment on top of the stack. This works correctly in phone mode, it
 * has not been tested in tablets yet.
 * 
 */
public class ActivityContainer extends BaseActivity implements FragmentCallbacks {

    private ActivityMode activityMode;
    private ObjectDataSource data;
    private boolean isTablet = false;

    protected Button fragmentTitle;
    protected ProgressBar fragmentProgress;
    protected ImageButton fragmentUpdate;
    protected ToggleButton fragmentShowOffline;
    protected ImageButton fragmentAdd;
    protected ToggleButton fragmentStar;
    protected ToggleButton fragmentAppend;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Check if any activity mode has been set, set it to Main Menu
	// otherwise
	Bundle extras = getIntent().getExtras();
	if (savedInstanceState != null) {
	    setActivityMode(ActivityMode.valueOf(savedInstanceState.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY)));
	} else if (extras != null) {
		String mode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY);
		if(mode == null){
		    setActivityMode(ActivityMode.ACTIVITY_MAIN_MENU);
		}else{
		    setActivityMode(ActivityMode.valueOf(mode));
		}
	} else {
	    setActivityMode(ActivityMode.ACTIVITY_MAIN_MENU);
	}

	// Load the UI
	setContentView(R.layout.activity_panel);

	// Check if the second panel exists
	if (findViewById(R.id.activityMainMenuFragment) != null) {
	    isTablet = true;
	}

	if (savedInstanceState == null) {
	    // This will prevent to populate the second panel when starting in
	    // main menu mode on a tablet
	    if (!isTablet || getActivityMode() != ActivityMode.ACTIVITY_MAIN_MENU) {
		BaseFragment fragment = getFragmentByMode(getActivityMode());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.activityFrameLayout, fragment);
		transaction.commit();
	    }
	}

	// Set references to all the buttons in the action bar
	this.fragmentTitle = (Button) this.findViewById(R.id.buttonFragmentTitle);
	this.fragmentProgress = (ProgressBar) this.findViewById(R.id.progressBarFragmentTitleLoading);
	this.fragmentUpdate = (ImageButton) this.findViewById(R.id.buttonFragmentUpdate);
	this.fragmentShowOffline = (ToggleButton) this.findViewById(R.id.toggleButtonShowOffline);
	this.fragmentAdd = (ImageButton) this.findViewById(R.id.buttonFragmentAdd);
	this.fragmentStar = (ToggleButton) this.findViewById(R.id.toggleButtonFragmentStar);
	this.fragmentAppend = (ToggleButton) this.findViewById(R.id.toggleButtonFragmentAppend);

	this.fragmentTitle.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			upNavigation();
		}
	});
	
	this.getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
	    public void onBackStackChanged() {
		if (isTablet && getSupportFragmentManager().getBackStackEntryCount() == 0) {
		    // If the second panel is empty after popping a profile or
		    // outfit pager, finish the activity
		    if (getActivityMode() == ActivityMode.ACTIVITY_PROFILE || getActivityMode() == ActivityMode.ACTIVITY_MEMBER_LIST) {
			finish();
		    } else {// In any other case, we are back at the first
			    // activity, so just empty the second panel
			setActivityMode(ActivityMode.ACTIVITY_MAIN_MENU);
			fragmentTitle.setText(R.string.app_name_capital);
			clearActionBar();
		    }
		}
	    }
	});

	// Open the database for all other fragments to use
	setData(new ObjectDataSource(this));
	data.open();
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
	// Reset the database, this will also force some tasks to end
	data.close();
	data.open();

	ActivityMode mode = ActivityMode.valueOf(id);
	// Seen as we can't have embedded fragments, we will create a new
	// activity if we want to open a profile or outfit pager. With all other
	// not nested fragments, we can just push a new fragment on top of the
	// stack
	if (isTablet && !(mode == ActivityMode.ACTIVITY_PROFILE) && !(mode == ActivityMode.ACTIVITY_MEMBER_LIST)) {
	    // A main menu fragment never goes in the second panel
	    if (mode == ActivityMode.ACTIVITY_MAIN_MENU) {
		return;
	    }

	    FragmentTransaction transaction;
	    transaction = this.getSupportFragmentManager().beginTransaction();

	    BaseFragment newFragment = getFragmentByMode(mode);

	    // Build the bundle from the argument list
	    Bundle bundle = new Bundle();
	    if (args != null && args.length > 0) {
		for (int i = 0; i < args.length; i++) {
		    bundle.putString("PARAM_" + i, args[i]);
		}
	    }

	    newFragment.setArguments(bundle);
	    transaction.replace(R.id.activityFrameLayout, newFragment);
	    transaction.addToBackStack(null);
	    clearActionBar();
	    transaction.commit();
	    setActivityMode(mode);
	} else {
	    // On a non-tablet, a new activity gets created every time we need a
	    // new fragment
	    Class<ActivityContainer> newActivityClass = ActivityContainer.class;
	    Intent intent = new Intent(this, newActivityClass);
	    if (args != null && args.length > 0) {
		for (int i = 0; i < args.length; i++) {
		    intent.putExtra("PARAM_" + i, args[i]);
		}
	    }
	    intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, mode.toString());
	    clearActionBar();
	    startActivity(intent);
	}
    }
	
    /**
     * This method will enable all the title bar buttons as well as hide them.
     * This method should be called before showing a new fragment. Each new
     * fragment is in charge of showing the buttons they need.
     */
    private void clearActionBar() {
	this.fragmentUpdate.setEnabled(true);
	this.fragmentProgress.setEnabled(true);
	this.fragmentShowOffline.setEnabled(true);
	this.fragmentAdd.setEnabled(true);
	this.fragmentStar.setEnabled(true);
	this.fragmentAppend.setEnabled(true);
	this.fragmentUpdate.setVisibility(View.GONE);
	this.fragmentProgress.setVisibility(View.GONE);
	this.fragmentShowOffline.setVisibility(View.GONE);
	this.fragmentAdd.setVisibility(View.GONE);
	this.fragmentStar.setVisibility(View.GONE);
	this.fragmentAppend.setVisibility(View.GONE);
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
     *            Activity mode
     * @return the fragment corresponding to the activity mode requested. If the
     *         activity mode does not belong to any class, this method will
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
	case ACTIVITY_REDDIT:
	    newFragment = new FragmentReddit();
	    break;
	default:
	    break;
	}
	return newFragment;
    }

    /**
     * @return if the activity is running on tablet mode
     */
    public boolean isTablet() {
	return isTablet;
    }

    /**
     * If a main menu fragment can be located. It will refresh the state of the
     * preferred buttons for both profile and outfit
     */
    public void checkPreferedButtons() {
	Fragment mainMenu = getSupportFragmentManager().findFragmentById(R.id.activityMainMenuFragment);
	if (mainMenu != null) {
	    ((FragmentMainMenu) mainMenu).checkPreferedButtons();
	}
    }
    
    /**
     * This should simulate the back button behavior. If we are on tablet or phone 
     * mode, we will go back on different ways. On normal phone mode we end the activity, 
     * if we are on tablet mode we will pop the top of the stack.
     */
    public void upNavigation(){
    	if(this.getActivityMode() != ActivityMode.ACTIVITY_MAIN_MENU){
	    	if (isTablet) {
	    		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
	    	        getSupportFragmentManager().popBackStack();
	    	        
	    	        Fragment currentFrag = (Fragment) getSupportFragmentManager().findFragmentById(R.id.activityFrameLayout);
	    	        if (currentFrag != null){
		    	        FragmentTransaction removeTransaction =	getSupportFragmentManager().beginTransaction();
		    	        removeTransaction.remove(currentFrag);
	    	        	removeTransaction.commit();
	    	        }
	    	    }else{
	    	    	finish();
	    	    }
	    	}else{
	    		finish();
	    	}
    	}
    }
}
