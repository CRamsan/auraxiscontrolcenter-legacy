package com.cesarandres.ps2link.soe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

public class ListItem extends View {

	private final Bitmap factionIcon;
	private final String characterName;
	private final String serverName;
	private final int battleRank;

	public ListItem(Context context, AttributeSet attrs, Bitmap factionIcon,
			String characterName, String serverName, int battleRanks) {
		super(context, attrs);
		this.factionIcon = factionIcon;
		this.characterName = characterName;
		this.serverName = serverName;
		this.battleRank = battleRanks;
	}

	public ListItem(Context context, Bitmap factionIcon, String characterName,
			String serverName, int battleRanks) {
		super(context);
		this.factionIcon = factionIcon;
		this.characterName = characterName;
		this.serverName = serverName;
		this.battleRank = battleRanks;
	}

	public Bitmap getFactionIcon() {
		return factionIcon;
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getServerName() {
		return serverName;
	}

	public int getBattleRank() {
		return battleRank;
	}

}
