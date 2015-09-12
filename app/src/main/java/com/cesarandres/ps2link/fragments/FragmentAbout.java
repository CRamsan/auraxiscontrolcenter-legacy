package com.cesarandres.ps2link.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;

/**
 *
 */
public class FragmentAbout extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
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
        this.fragmentTitle.setText(getString(R.string.title_about));
        final Button buttonHomepage = (Button) getActivity().findViewById(R.id.buttonAboutHomepage);
        buttonHomepage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = getActivity().getResources().getString(R.string.url_homepage);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
