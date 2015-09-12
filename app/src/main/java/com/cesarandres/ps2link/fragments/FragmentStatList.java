package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Verb;
import com.cesarandres.ps2link.dbg.content.CharacterProfile;
import com.cesarandres.ps2link.dbg.content.character.Stats;
import com.cesarandres.ps2link.dbg.content.response.Character_list_response;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.view.StatItemAdapter;

/**
 * Retrieve the stats for the given character
 */
public class FragmentStatList extends BaseFragment {

    private String profileId;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stat_list, container, false);
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
        this.profileId = getArguments().getString("PARAM_0");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        downloadStatList(this.profileId);
    }

    /**
     * @param character_id Character id of the character whose stats will be downloaded
     */
    public void downloadStatList(String character_id) {
        setProgressButton(true);
        String url = DBGCensus.generateGameDataRequest(
                Verb.GET,
                PS2Collection.CHARACTER,
                character_id,
                QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "stat_history")
                        .AddCommand(QueryCommand.HIDE, "name,battle_rank,certs,times,daily_ribbon")).toString();

        Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
            @Override
            public void onResponse(Character_list_response response) {
                setProgressButton(false);
                try {
                    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewStatList);
                    CharacterProfile profile = response.getCharacter_list().get(0);
                    Stats stats = profile.getStats();
                    listRoot.setAdapter(new StatItemAdapter(getActivity(), stats.getStat_history(), profileId));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
                }
            }
        };

        ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProgressButton(false);
                Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        };

        DBGCensus.sendGsonRequest(url, Character_list_response.class, success, error, this);
    }
}