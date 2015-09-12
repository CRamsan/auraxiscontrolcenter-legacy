package com.cesarandres.ps2link.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.util.Logger;
import com.cesarandres.ps2link.dbg.view.TwitterItemAdapter;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.module.twitter.PS2Tweet;
import com.cesarandres.ps2link.module.twitter.TwitterUtil;

import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.TwitterException;

/**
 * Fragment that retrieves the Twitter feed for several users planetside 2
 * related. It also has UI to show and hide some users.
 */
public class FragmentTwitter extends BaseFragment {

    private final String USER_PREFIX = "cb_";
    private boolean loaded = false;
    private String[] users;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        LinearLayout holder = (LinearLayout) view.findViewById(R.id.linearLayoutTwitterHolder);
        this.users = getActivity().getResources().getStringArray(R.array.twitter_users);
        for (String user : this.users) {
            CheckBox cb = new CheckBox(getActivity());
            cb.setText("@" + user);
            cb.setTag(user);
            holder.addView(cb);
        }
        return view;
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
        this.fragmentTitle.setText(getString(R.string.title_twitter));

        SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);

        OnCheckedChangeListener updateTweetLitener = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTweets();
            }
        };

        for (String user : this.users) {
            CheckBox userCheckbox = ((CheckBox) getView().findViewWithTag(user));
            userCheckbox.setChecked(settings.getBoolean(USER_PREFIX + user, false));
            userCheckbox.setOnCheckedChangeListener(updateTweetLitener);
        }

        this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> usersnames = new ArrayList<String>();

                for (String user : FragmentTwitter.this.users) {
                    CheckBox userCheckbox = ((CheckBox) getView().findViewWithTag(user));
                    if (userCheckbox.isChecked()) {
                        usersnames.add(user);
                    }
                }

                String[] stringArray = Arrays.copyOf(usersnames.toArray(), usersnames.size(), String[].class);

                UpdateTweetsTask task = new UpdateTweetsTask();
                setCurrentTask(task);
                task.execute(stringArray);
            }
        });

        ArrayList<String> usersnames = new ArrayList<String>();
        for (String user : FragmentTwitter.this.users) {
            usersnames.add(user);
        }
        String[] stringArray = Arrays.copyOf(usersnames.toArray(), usersnames.size(), String[].class);

        if (savedInstanceState == null) {
            UpdateTweetsTask task = new UpdateTweetsTask();
            setCurrentTask(task);
            task.execute(stringArray);
        } else {
            this.loaded = savedInstanceState.getBoolean("twitterLoader", false);
            if (!this.loaded) {
                UpdateTweetsTask task = new UpdateTweetsTask();
                setCurrentTask(task);
                task.execute(stringArray);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_TWITTER);
        this.fragmentUpdate.setVisibility(View.VISIBLE);
        updateTweets();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("twitterLoader", loaded);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
        SharedPreferences.Editor editor = settings.edit();
        for (String user : FragmentTwitter.this.users) {
            CheckBox userCheckbox = ((CheckBox) getView().findViewWithTag(user));
            editor.putBoolean(USER_PREFIX + user, userCheckbox.isChecked());
        }
        editor.commit();
    }

    /**
     * Check the UI and update the content based on the selected users
     */
    private void updateTweets() {
        ArrayList<String> usersnames = new ArrayList<String>();

        for (String user : FragmentTwitter.this.users) {
            CheckBox userCheckbox = ((CheckBox) getView().findViewWithTag(user));
            if (userCheckbox.isChecked()) {
                usersnames.add(user);
            }
        }

        String[] stringArray = Arrays.copyOf(usersnames.toArray(), usersnames.size(), String[].class);
        updateContent(stringArray);
    }

    /**
     * @param users list of users to read from the database
     */
    private void updateContent(String[] users) {
        ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewTweetList);
        listRoot.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String url = "https://twitter.com/#!/" + ((PS2Tweet) myAdapter.getItemAtPosition(myItemInt)).getTag() + "/status/"
                        + ((PS2Tweet) myAdapter.getItemAtPosition(myItemInt)).getId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        ObjectDataSource data = getActivityContainer().getData();
        listRoot.setAdapter(new TwitterItemAdapter(getActivity(), users, data));
    }

    /**
     * Task that will update the tweets for the given users. The tweets will be
     * cached into the database
     */
    private class UpdateTweetsTask extends AsyncTask<String, Integer, String[]> {

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
        protected String[] doInBackground(String... users) {
            ArrayList<PS2Tweet> tweetList = new ArrayList<PS2Tweet>(0);
            ObjectDataSource data = getActivityContainer().getData();
            int currentTime = (int) (System.currentTimeMillis() / 1000);
            int weekInSeconds = 60 * 60 * 24 * 7;
            data.deleteAllTweet(currentTime - weekInSeconds);
            for (String user : users) {
                if (this.isCancelled()) {
                    break;
                }
                try {
                    tweetList = TwitterUtil.getTweets(user);
                    data.insertAllTweets(tweetList, user);
                } catch (TwitterException e) {
                    Logger.log(Log.WARN, FragmentTwitter.this, "Error trying retrieve tweets");
                } catch (IllegalStateException e) {
                    Logger.log(Log.INFO, FragmentTwitter.this, "DB was closed. This is normal.");
                    break;
                }

            }
            return users;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                updateTweets();
            }
            setProgressButton(false);
            loaded = true;
        }
    }
}
