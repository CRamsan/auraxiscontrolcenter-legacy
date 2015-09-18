package com.cesarandres.ps2link.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.dbg.DBGCensus.Verb;
import com.cesarandres.ps2link.dbg.content.World;
import com.cesarandres.ps2link.dbg.content.WorldEvent;
import com.cesarandres.ps2link.dbg.content.response.Server_Status_response;
import com.cesarandres.ps2link.dbg.content.response.Server_response;
import com.cesarandres.ps2link.dbg.content.response.World_event_list_response;
import com.cesarandres.ps2link.dbg.content.response.server.PS2;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.view.ServerItemAdapter;
import com.cesarandres.ps2link.module.ButtonSelectSource;
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener;

import java.util.ArrayList;

/**
 * This fragment will display the servers and theirs status
 */
public class FragmentServerList extends BaseFragment implements SourceSelectionChangedListener {

    private ButtonSelectSource selectionButton;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_list, container, false);
        selectionButton = new ButtonSelectSource(getActivity(), (ViewGroup) getActivity().findViewById(R.id.linearLayoutTitle));
        selectionButton.setListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTitle);
        selectionButton.removeButtons(getActivity(), titleLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.fragmentTitle.setText(getString(R.string.title_servers));
        this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                downloadServers();
            }
        });

        ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
        listRoot.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListView listParent = (ListView) getActivity().findViewById(R.id.listViewServers);
                ((ServerItemAdapter) listParent.getAdapter()).onItemSelected(myItemInt, getContext());
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
        this.fragmentUpdate.setVisibility(View.VISIBLE);
        getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_SERVER_LIST);
        downloadServers();
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    public void downloadServers() {
        setProgressButton(true);
        String url = DBGCensus.generateGameDataRequest(Verb.GET, PS2Collection.WORLD, "",
                QueryString.generateQeuryString().AddCommand(QueryCommand.LIMIT, "10")).toString();

        final Listener<Server_response> success = new Response.Listener<Server_response>() {
            @Override
            public void onResponse(Server_response response) {
                try {
                    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
                    listRoot.setAdapter(new ServerItemAdapter(getActivity(), response.getWorld_list()));

                    for(World world : response.getWorld_list()){
                        downloadServerAlert(world.getWorld_id());
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
                }
                setProgressButton(false);
                downloadServerPopulation();
            }
        };

        ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProgressButton(false);
                Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        };
        DBGCensus.sendGsonRequest(url, Server_response.class, success, error, this);
    }

    /**
     * Query the API and retrieve the latest population info for all the servers
     * and update the UI. This method uses a non-standard API call. This API
     * call has been unreliable in the past.
     */
    public void downloadServerPopulation() {
        setProgressButton(true);
        // This is not an standard API call
        String url = "http://census.daybreakgames.com/s:PS2Link/json/status/ps2";

        Listener<Server_Status_response> success = new Response.Listener<Server_Status_response>() {
            @Override
            public void onResponse(Server_Status_response response) {
                setProgressButton(false);
                try {
                    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
                    PS2 servers = response.getPs2();
                    ((ServerItemAdapter) listRoot.getAdapter()).setServerPopulation(servers);
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

        DBGCensus.sendGsonRequest(url, Server_Status_response.class, success, error, this);
    }

    /**
     * This call will request the last METAGEME event less than 15 minutes old for this given server
     */
    private void downloadServerAlert(String serverId) {
        setProgressButton(true);
        // The URL looks like this:
        // http://census.daybreakgames.com/get/ps2:v2/world_event?
        // world_id=17&c:limit=1&type=METAGAME&c:join=metagame_event&c:lang=en
        String url = DBGCensus.generateGameDataRequest(Verb.GET, PS2Collection.WORLD_EVENT, "",
                QueryString.generateQeuryString().
                        AddCommand(QueryCommand.LIMIT, "1").
                        AddComparison("type", QueryString.SearchModifier.EQUALS, "METAGAME").
                        AddComparison("world_id", QueryString.SearchModifier.EQUALS, serverId).
                        AddComparison("after", QueryString.SearchModifier.EQUALS,
                                //Get metagame events that are newer than  minutes
                                Long.toString((System.currentTimeMillis() / 1000L) - 7200)).
                        AddCommand(QueryCommand.JOIN, "metagame_event")).toString();

        Listener<World_event_list_response> success = new Response.Listener<World_event_list_response>() {
            @Override
            public void onResponse(World_event_list_response response) {
                setProgressButton(false);
                try {
                    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
                    ArrayList<WorldEvent> events = response.getWorld_event_list();
                    if(events != null && events.size() > 0) {
                        ((ServerItemAdapter) listRoot.getAdapter()).setServerAlert(events.get(0));
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
                }
            }
        };

        ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProgressButton(false);
            }
        };

        DBGCensus.sendGsonRequest(url, World_event_list_response.class, success, error, this);
    }

    @Override
    public void onSourceSelectionChanged(Namespace selectedNamespace) {
        ApplicationPS2Link.volley.cancelAll(this);
        downloadServers();
    }

    public class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(inflater.inflate(R.layout.layout_server_register, null));
            builder.setMessage(R.string.text_about_thanks)
                    .setPositiveButton(R.string.text_add, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.text_about_thanks, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}