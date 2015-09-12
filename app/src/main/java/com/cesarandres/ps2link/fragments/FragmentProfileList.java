package com.cesarandres.ps2link.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.content.CharacterProfile;
import com.cesarandres.ps2link.dbg.view.ProfileItemAdapter;
import com.cesarandres.ps2link.module.ObjectDataSource;

import java.util.ArrayList;

/**
 * Fragment that reads the profiles from the database that have been set as not
 * temporary
 */
public class FragmentProfileList extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_list, container, false);
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
        this.fragmentTitle.setText(getString(R.string.title_profiles));
        this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReadProfilesTable task = new ReadProfilesTable();
                setCurrentTask(task);
                task.execute();
            }
        });
        this.fragmentAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallbacks.onItemSelected(ActivityMode.ACTIVITY_ADD_PROFILE.toString(), null);
            }
        });
        ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewProfileList);
        listRoot.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                        new String[]{((CharacterProfile) myAdapter.getItemAtPosition(myItemInt)).getCharacterId(),
                                (DBGCensus.currentNamespace.name())});
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
        getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_PROFILE_LIST);
        this.fragmentUpdate.setVisibility(View.VISIBLE);
        this.fragmentAdd.setVisibility(View.VISIBLE);
        ReadProfilesTable task = new ReadProfilesTable();
        setCurrentTask(task);
        task.execute();
    }

    /**
     * Reads the profiles in the database that are set as non temporary
     */
    private class ReadProfilesTable extends AsyncTask<Integer, Integer, ArrayList<CharacterProfile>> {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            setProgressButton(true);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected ArrayList<CharacterProfile> doInBackground(Integer... params) {
            ArrayList<CharacterProfile> tmpProfileList = null;
            ObjectDataSource data = getActivityContainer().getData();
            data.deleteAllCharacterProfiles(false);
            tmpProfileList = data.getAllCharacterProfiles(false);
            return tmpProfileList;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(ArrayList<CharacterProfile> result) {
            if (result != null) {
                ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewProfileList);
                listRoot.setAdapter(new ProfileItemAdapter(getActivity(), result, true));
            }
            setProgressButton(false);
        }
    }
}