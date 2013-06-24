package com.cesarandres.ps2link;

import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfile extends BaseFragment {

	private CharacterProfile profile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.profile = new Gson().fromJson(getActivity().getIntent()
				.getExtras().getString("profile"), CharacterProfile.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View root = inflater.inflate(R.layout.fragment_profile, container,
				false);

		((TextView) root.findViewById(R.id.textViewProfileCharacterName))
				.setText(this.profile.getName().getFirst());
		// TODO
		// ((TextView)
		// findViewById(R.id.textViewProfileServer)).setText(this.profile.get());
		((TextView) root.findViewById(R.id.textViewProfileBattleRankValue))
				.setText(Integer.toString(this.profile.getBattle_rank()
						.getValue()));
		((TextView) root.findViewById(R.id.textViewProfileBattleRankPercent))
				.setText(Integer.toString(this.profile.getBattle_rank()
						.getPercent_to_next()));
		((TextView) root.findViewById(R.id.textViewProfileCertsValue))
				.setText(this.profile.getCerts().getCurrentpoints());
		((TextView) root.findViewById(R.id.textViewProfileCertsPercent))
				.setText(this.profile.getCerts().getPercentagetonext());
		((TextView) root.findViewById(R.id.textViewProfileMinutesPlayed))
				.setText(this.profile.getTimes().getMinutes_played());
		((TextView) root.findViewById(R.id.textViewProfileLastLogin))
				.setText(this.profile.getTimes().getLast_login());
		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("Profile");
		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
