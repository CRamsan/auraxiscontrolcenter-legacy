package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.cesarandres.ps2link.dbg.content.CharacterProfile;
import com.cesarandres.ps2link.dbg.content.response.Character_list_response;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.dbg.view.LoadingItemAdapter;
import com.cesarandres.ps2link.dbg.view.ProfileItemAdapter;
import com.cesarandres.ps2link.module.ButtonSelectSource;
import com.cesarandres.ps2link.module.ButtonSelectSource.SourceSelectionChangedListener;

import java.util.Locale;

/**
 * This fragment will show the user with a field and a button to search for
 * profiles. The only requirement is that the name needs to be at least three
 * characters long.
 */
public class FragmentAddProfile extends BaseFragment implements SourceSelectionChangedListener {

    private Namespace lastUsedNamespace;
    private ButtonSelectSource selectionButton;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_profile, container, false);
        selectionButton = new ButtonSelectSource(getActivity(), (ViewGroup) getActivity().findViewById(R.id.linearLayoutTitle));
        selectionButton.setListener(this);
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
        this.fragmentTitle.setText(getString(R.string.title_profiles));
        final ImageButton buttonCharacters = (ImageButton) getActivity().findViewById(R.id.imageButtonSearchProfile);
        buttonCharacters.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                downloadProfiles();
            }
        });
        this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                downloadProfiles();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_ADD_PROFILE);
        this.fragmentUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTitle);
        selectionButton.removeButtons(getActivity(), titleLayout);
    }

    /**
     * This method will retrieve profiles based on the criteria given by the
     * user in the text fields. The user needs to provide a name to start a
     * search. If a value is provided but it is less than three characters long,
     * then the user will see a toast asking to provide more information.
     */
    private void downloadProfiles() {
        EditText searchField = (EditText) getView().findViewById(R.id.fieldSearchProfile);
        this.lastUsedNamespace = DBGCensus.currentNamespace;
        if (searchField.getText().toString().length() < 3) {
            Toast.makeText(getActivity(), R.string.text_profile_name_too_short, Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the loading adapter
        ListView listRoot = (ListView) getView().findViewById(R.id.listFoundProfiles);
        listRoot.setOnItemClickListener(null);
        listRoot.setAdapter(new LoadingItemAdapter(getActivity()));

        String url = DBGCensus.generateGameDataRequest(
                Verb.GET,
                PS2Collection.CHARACTER_NAME,
                "",
                QueryString.generateQeuryString()
                        .AddComparison("name.first_lower", SearchModifier.STARTSWITH, searchField.getText().toString().toLowerCase(Locale.getDefault()))
                        .AddCommand(QueryCommand.LIMIT, "25")
                        .AddCommand(QueryCommand.JOIN, "character")).toString();

        Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
            @Override
            public void onResponse(Character_list_response response) {
                try {
                    ListView listRoot = (ListView) getView().findViewById(R.id.listFoundProfiles);
                    listRoot.setAdapter(new ProfileItemAdapter(getActivity(), response.getCharacter_name_list(), false));
                    listRoot.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                            mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                                    new String[]{((CharacterProfile) myAdapter.getItemAtPosition(myItemInt)).getCharacterId(), lastUsedNamespace.name()});
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
                }
            }
        };

        ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ListView listRoot = (ListView) getView().findViewById(R.id.listFoundProfiles);
                if (listRoot != null) {
                    listRoot.setAdapter(null);
                }
                Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        };

        DBGCensus.sendGsonRequest(url, Character_list_response.class, success, error, this);
    }

    @Override
    public void onSourceSelectionChanged(Namespace selectedNamespace) {
        downloadProfiles();
    }
}
