package com.cesarandres.ps2link.module;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cesarandres.ps2link.module.twitter.PS2Tweet;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.World;
import com.cesarandres.ps2link.soe.content.backlog.Times;
import com.cesarandres.ps2link.soe.content.character.BattleRank;
import com.cesarandres.ps2link.soe.content.character.Certs;
import com.cesarandres.ps2link.soe.content.character.Name;
import com.cesarandres.ps2link.soe.content.character.Server;
import com.cesarandres.ps2link.soe.content.world.Name_Multi;
import com.cesarandres.ps2link.soe.util.Logger;

/**
 * Class that retrieves information from the SQLiteManager and convert it into
 * objects that can be used by other classes.
 * 
 * @author Cesar
 * 
 */
public class ObjectDataSource {

    private SQLiteDatabase database;
    private SQLiteManager dbHelper;
    private String[] allColumnsWorlds = { SQLiteManager.WORLDS_COLUMN_ID, SQLiteManager.WORLDS_COLUMN_NAME, SQLiteManager.WORLDS_COLUMN_STATE };

    private String[] allColumnsFactions = { SQLiteManager.FACTIONS_COLUMN_ID, SQLiteManager.FACTIONS_COLUMN_NAME, SQLiteManager.FACTIONS_COLUMN_CODE,
	    SQLiteManager.FACTIONS_COLUMN_ICON };

    private String[] allColumnsCharacters = { SQLiteManager.CHARACTERS_COLUMN_ID, SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST,
	    SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID,
	    SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS, SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
	    SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK, SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN,
	    SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, SQLiteManager.CHARACTERS_COLUMN_FACTION_ID, SQLiteManager.CHARACTERS_COLUMN_WORLD_ID,
	    SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, SQLiteManager.CACHE_COLUMN_SAVES, SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME };

    private String[] allColumnsMembers = { SQLiteManager.MEMBERS_COLUMN_ID, SQLiteManager.MEMBERS_COLUMN_RANK, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID,
	    SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS, SQLiteManager.MEMBERS_COLUMN_NAME, SQLiteManager.CACHE_COLUMN_SAVES };

    private String[] allColumnsOutfit = { SQLiteManager.OUTFIT_COLUMN_ID, SQLiteManager.OUTFIT_COLUMN_NAME, SQLiteManager.OUTFIT_COLUMN_ALIAS,
	    SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID, SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT, SQLiteManager.OUTFIT_COLUMN_TIME_CREATED,
	    SQLiteManager.OUTFIT_COLUMN_WORDL_ID, SQLiteManager.OUTFIT_COLUMN_FACTION_ID, SQLiteManager.CACHE_COLUMN_SAVES };

    private String[] allColumnsTweet = { SQLiteManager.TWEETS_COLUMN_ID, SQLiteManager.TWEETS_COLUMN_DATE, SQLiteManager.TWEETS_COLUMN_USER,
	    SQLiteManager.TWEETS_COLUMN_TAG, SQLiteManager.TWEETS_COLUMN_CONTENT, SQLiteManager.TWEETS_COLUMN_PICTURE, SQLiteManager.TWEETS_COLUMN_OWNER };

    /**
     * Constructor that requires a reference to the current context.
     * 
     * @param context
     *            reference to the calling activity.
     */
    public ObjectDataSource(Context context) {
	dbHelper = new SQLiteManager(context);
    }

    /**
     * Open the database and get it ready to retrieve information.
     * 
     * @throws SQLException
     *             if there is an error while opening the database.
     */
    public void open() throws SQLException {
	database = dbHelper.getWritableDatabase();

    }

    /**
     * Close the database.
     */
    public void close() {

	dbHelper.close();

    }

    /**
     * Drops all the tables and creates them again.
     */
    public void reset() {
	dbHelper.onUpgrade(database, SQLiteManager.DATABASE_VERSION, SQLiteManager.DATABASE_VERSION);
    }

    public static Cursor cursorToPosition(Cursor cursor, int index) {
	cursor.moveToPosition(index);
	return cursor;
    }

    public boolean insertCharacter(CharacterProfile character, boolean temp) {
	ContentValues values = new ContentValues();
	values.put(SQLiteManager.CHARACTERS_COLUMN_ID, character.getCharacterId());
	values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST, character.getName().getFirst());
	values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, character.getName().getFirst_lower());
	values.put(SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID, character.getActive_profile_id());
	values.put(SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS, character.getCerts().getAvailable_points());
	values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT, character.getCerts().getPercent_to_next());
	values.put(SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, character.getBattle_rank().getValue());
	values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK, character.getBattle_rank().getPercent_to_next());
	values.put(SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN, character.getTimes().getLast_login());
	values.put(SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, character.getTimes().getMinutes_played());
	values.put(SQLiteManager.CHARACTERS_COLUMN_FACTION_ID, character.getFaction_id());
	values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_ID, character.getWorld_id());
	values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME, character.getServer().getName().getEn());
	if (character.getOutfit() == null) {
	    values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, "NONE");
	} else {
	    values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, character.getOutfit().getName());
	}

	String target = SQLiteManager.TABLE_CHARACTERS_NAME;
	if (temp) {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, false);
	} else {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, true);
	}

	long insertId = -1;
	try {
	    insertId = database.insert(target, null, values);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while inserting a single character");
	}

	return (insertId != -1);
    }

    public int insertAllCharacters(ArrayList<CharacterProfile> characterList, boolean temp) {
	int count = 0;
	for (CharacterProfile character : characterList) {
	    if (insertCharacter(character, temp)) {
		count++;
	    }
	}
	return count;
    }

    public void deleteCharacter(CharacterProfile character) {
	String id = character.getCharacterId();
	String target = SQLiteManager.TABLE_CHARACTERS_NAME;
	try {
	    database.delete(target, SQLiteManager.CHARACTERS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting a single character");
	}
    }

    public CharacterProfile getCharacter(String characterId) {
	String target = SQLiteManager.TABLE_CHARACTERS_NAME;
	CharacterProfile character = null;
	try {
	    Cursor cursor = database.query(target, allColumnsCharacters, SQLiteManager.CHARACTERS_COLUMN_ID + " = " + characterId, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		character = cursorToCharacterProfile(cursor);
		cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while retrieving a single character");
	}
	return character;
    }

    public int updateCharacter(CharacterProfile character, boolean temp) {
	String target = SQLiteManager.TABLE_CHARACTERS_NAME;

	ContentValues values = new ContentValues();
	values.put(SQLiteManager.CHARACTERS_COLUMN_ID, character.getCharacterId());
	values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST, character.getName().getFirst());
	values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, character.getName().getFirst_lower());
	values.put(SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID, character.getActive_profile_id());
	values.put(SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS, character.getCerts().getAvailable_points());
	values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT, character.getCerts().getPercent_to_next());
	values.put(SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, character.getBattle_rank().getValue());
	values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK, character.getBattle_rank().getPercent_to_next());
	values.put(SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN, character.getTimes().getLast_login());
	values.put(SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, character.getTimes().getMinutes_played());
	values.put(SQLiteManager.CHARACTERS_COLUMN_FACTION_ID, character.getFaction_id());
	values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_ID, character.getWorld_id());
	if (character.getOutfitName() != null) {
	    values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, character.getOutfitName());
	} else if (character.getOutfit() != null) {
	    values.put(SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, character.getOutfit().getName());
	}
	values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME, character.getServer().getName().getEn());

	if (temp) {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, false);
	} else {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, true);
	}

	int rowsChanged = 0;
	try {
	    rowsChanged = database.update(target, values, SQLiteManager.CHARACTERS_COLUMN_ID + " = " + character.getCharacterId(), null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while retrieving a single character");
	}
	return rowsChanged;
    }

    public CharacterProfile cursorToCharacterProfile(final Cursor cursor) {
	CharacterProfile character = new CharacterProfile();
	character.setCharacterId(cursor.getString(0));
	Name name = new Name();
	name.setFirst(cursor.getString(1));
	name.setFirst_lower(cursor.getString(2));
	character.setName(name);

	character.setActive_profile_id(cursor.getString(3));
	Certs certs = new Certs();
	certs.setAvailable_points(cursor.getString(4));
	certs.setPercent_to_next(cursor.getString(5));
	character.setCerts(certs);

	BattleRank br = new BattleRank();
	br.setPercent_to_next(cursor.getInt(6));
	br.setValue(cursor.getInt(7));
	character.setBattle_rank(br);

	Times times = new Times();
	times.setLast_login(cursor.getString(8));
	times.setMinutes_played(cursor.getString(9));
	character.setTimes(times);

	character.setFaction_id(cursor.getString(10));
	character.setWorld_id(cursor.getString(11));

	character.setOutfitName(cursor.getString(12));

	if (cursor.getInt(13) == 1) {
	    character.setCached(true);
	} else {
	    character.setCached(false);
	}

	character.setServer(new Server() {
	    {
		setName(new Name_Multi() {
		    {
			setEn(cursor.getString(14));
		    }
		});
	    }
	});

	return character;
    }

    public ArrayList<CharacterProfile> deleteAllCharacterProfiles(boolean deleteAll) {
	ArrayList<CharacterProfile> profiles = new ArrayList<CharacterProfile>(0);

	String target = SQLiteManager.TABLE_CHARACTERS_NAME;

	try {
	    Cursor cursor = null;
	    if (deleteAll) {
		cursor = database.query(target, allColumnsCharacters, null, null, null, null, null);
	    } else {
		cursor = database.query(target, allColumnsCharacters, SQLiteManager.CACHE_COLUMN_SAVES + " = 0", null, null, null, null);
	    }
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		CharacterProfile character = cursorToCharacterProfile(cursor);
		profiles.add(character);
		cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting all profiles");
	}

	return profiles;
    }

    public ArrayList<CharacterProfile> getAllCharacterProfiles(boolean temp) {
	ArrayList<CharacterProfile> profiles = new ArrayList<CharacterProfile>(0);

	try {
	    Cursor cursor = null;
	    if (temp) {
		cursor = database.query(SQLiteManager.TABLE_CHARACTERS_NAME, allColumnsCharacters, null, null, null, null, null);
	    } else {
		cursor = database.query(SQLiteManager.TABLE_CHARACTERS_NAME, allColumnsCharacters, SQLiteManager.CACHE_COLUMN_SAVES + " = 1", null, null, null,
			null);
	    }
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		CharacterProfile character = cursorToCharacterProfile(cursor);
		profiles.add(character);
		cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all profiles");
	}

	return profiles;
    }

    public boolean insertFaction(Faction faction) {
	ContentValues values = new ContentValues();
	values.put(SQLiteManager.FACTIONS_COLUMN_ID, faction.getId());
	values.put(SQLiteManager.FACTIONS_COLUMN_NAME, faction.getName().getEn());
	values.put(SQLiteManager.FACTIONS_COLUMN_CODE, faction.getCode());
	values.put(SQLiteManager.FACTIONS_COLUMN_ICON, faction.getIcon());
	long insertId = -1;
	try {
	    insertId = database.insert(SQLiteManager.TABLE_CHARACTERS_NAME, null, values);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while inserting faction");
	}
	return (insertId != -1);
    }

    public void deleteFaction(Faction faction) {
	String id = faction.getId();
	try {
	    database.delete(SQLiteManager.TABLE_FACTIONS_NAME, SQLiteManager.FACTIONS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting faction");
	}
    }

    public Faction cursorToFaction(Cursor cursor) {
	Faction faction = new Faction();
	faction.setId(cursor.getString(0));
	Name_Multi name = new Name_Multi();
	name.setEn(cursor.getString(1));
	faction.setName(name);

	faction.setCode(cursor.getString(2));
	faction.setIcon(cursor.getString(3));
	return faction;
    }

    public int insertAllFactions(ArrayList<Faction> factionList) {
	int count = 0;
	for (Faction faction : factionList) {
	    if (insertFaction(faction)) {
		count++;
	    }
	}
	return count;
    }

    public ArrayList<Faction> getAllFactions() {
	ArrayList<Faction> factions = new ArrayList<Faction>(0);

	try {
	    Cursor cursor = database.query(SQLiteManager.TABLE_FACTIONS_NAME, allColumnsFactions, null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		Faction faction = cursorToFaction(cursor);
		factions.add(faction);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all factions");
	}

	return factions;
    }

    public Faction getFaction(int factionId) {
	Faction faction = null;
	try {
	    Cursor cursor = database.query(SQLiteManager.TABLE_FACTIONS_NAME, allColumnsFactions, SQLiteManager.FACTIONS_COLUMN_ID + " = " + factionId, null,
		    null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		faction = cursorToFaction(cursor);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting faction");
	}
	return faction;
    }

    public int updateFaction(Faction faction) {
	ContentValues values = new ContentValues();
	values.put(SQLiteManager.FACTIONS_COLUMN_ID, faction.getId());
	values.put(SQLiteManager.FACTIONS_COLUMN_NAME, faction.getName().getEn());
	values.put(SQLiteManager.FACTIONS_COLUMN_CODE, faction.getCode());
	values.put(SQLiteManager.FACTIONS_COLUMN_ICON, faction.getIcon());
	int rowsChanged = 0;
	try {
	    rowsChanged = database.update(SQLiteManager.TABLE_FACTIONS_NAME, values, SQLiteManager.FACTIONS_COLUMN_ID + " = " + faction.getId(), null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while updating faction");
	}
	return rowsChanged;
    }

    public boolean insertMember(Member member, String outfit_id, boolean temp) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	long insertId = -1;
	ContentValues values = new ContentValues();
	try {
	    values.put(SQLiteManager.MEMBERS_COLUMN_ID, member.getCharacter_id());
	    values.put(SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS, member.getOnline_status());
	    values.put(SQLiteManager.MEMBERS_COLUMN_RANK, member.getRank());
	    values.put(SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID, outfit_id);
	    values.put(SQLiteManager.MEMBERS_COLUMN_NAME, member.getName().getFirst());
	    if (temp) {
		values.put(SQLiteManager.CACHE_COLUMN_SAVES, false);
	    } else {
		values.put(SQLiteManager.CACHE_COLUMN_SAVES, true);
	    }
	    insertId = database.insert(target, null, values);
	} catch (NullPointerException e) {
	    Logger.log(Log.INFO, this, "Error while inserting member: " + member.getCharacter_id() + ", probably character was deleted");
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while updating faction");
	}
	return (insertId != -1);
    }

    public void deleteMember(Member member, boolean temp) {
	String id = member.getCharacter_id();
	String target = SQLiteManager.TABLE_MEMBERS_NAME;

	String[] whereArgs = new String[] { id };
	try {
	    database.delete(target, SQLiteManager.MEMBERS_COLUMN_ID + " = ?", whereArgs);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting member");
	}
    }

    public static Member cursorToMember(Cursor cursor) {
	Member member = new Member();
	member.setCharacter_id(cursor.getString(0));
	member.setRank(cursor.getString(1));
	member.setOutfit_id(cursor.getString(2));
	member.setOnline_status(cursor.getString(3));
	Name name = new Name();
	name.setFirst(cursor.getString(4));
	member.setName(name);
	return member;
    }

    public ArrayList<Member> getAllMembers(String outfit_id, boolean temp) {
	ArrayList<Member> members = new ArrayList<Member>(0);
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	String[] whereArgs = new String[] { outfit_id };
	try {
	    Cursor cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		Member member = cursorToMember(cursor);
		members.add(member);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all members");
	}
	return members;
    }

    public ArrayList<Member> getMembers(String outfit_id, boolean temp, int index, int count) {
	ArrayList<Member> members = new ArrayList<Member>(0);
	String target = SQLiteManager.TABLE_MEMBERS_NAME;

	try {
	    String[] whereArgs = new String[] { outfit_id };
	    Cursor cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs, null, null, null, "LIMIT "
		    + count + " OFFSET " + index);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		Member member = cursorToMember(cursor);
		members.add(member);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting members");
	}

	return members;
    }

    public int countAllMembers(String outfit_id, boolean showOffline) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	int count = 0;

	try {
	    Cursor cursor = null;
	    if (showOffline) {
		String[] whereArgs = new String[] { outfit_id };
		cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs, null, null, null);
	    } else {
		String[] whereArgs = new String[] { outfit_id, "0" };
		cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ? AND "
			+ SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS + " != ?", whereArgs, null, null, null);

	    }

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		count++;
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while counting member");
	}

	return count;
    }

    public int insertAllMembers(ArrayList<Member> memberList, String outfit_id, boolean temp) {
	int count = 0;
	for (Member member : memberList) {
	    if (insertMember(member, outfit_id, temp)) {
		count++;
	    }
	}
	return count;
    }

    public Member getMember(String memberId) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	Member member = null;

	try {
	    String[] whereArgs = new String[] { memberId };
	    Cursor cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_ID + " = ?", whereArgs, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		member = cursorToMember(cursor);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting member");
	}

	return member;
    }

    public Cursor getMembersCursor(String outfit_id, boolean temp, boolean showOffline) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	Cursor cursor = null;
	try {
	    if (showOffline) {
		String[] whereArgs = new String[] { outfit_id, };
		cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs, null, null,
			SQLiteManager.MEMBERS_COLUMN_NAME + " COLLATE NOCASE ASC");
	    } else {
		String[] whereArgs = new String[] { outfit_id, "0" };
		cursor = database.query(target, allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ? AND "
			+ SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS + " != ?", whereArgs, null, null, SQLiteManager.MEMBERS_COLUMN_NAME
			+ " COLLATE NOCASE ASC");
	    }
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting the member cursor");
	}
	return cursor;
    }

    public int updateMember(Member member, boolean temp) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	int rowsChanged = 0;
	try {
	    ContentValues values = new ContentValues();
	    values.put(SQLiteManager.MEMBERS_COLUMN_ID, member.getCharacter_id());
	    values.put(SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS, member.getOnline_status());
	    values.put(SQLiteManager.MEMBERS_COLUMN_RANK, member.getRank());
	    if (temp) {
		values.put(SQLiteManager.CACHE_COLUMN_SAVES, false);
	    } else {
		values.put(SQLiteManager.CACHE_COLUMN_SAVES, true);
	    }
	    String[] whereArgs = new String[] { member.getCharacter_id() };
	    rowsChanged = database.update(target, values, SQLiteManager.MEMBERS_COLUMN_ID + " = ?", whereArgs);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while updating a member");
	}
	return rowsChanged;
    }

    public void deleteAllMembers(String outfit_id) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	try {
	    String[] whereArgs = new String[] { outfit_id };
	    database.delete(target, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while removing members");
	}
    }

    public boolean insertOutfit(Outfit outfit, boolean temp) {
	String target = SQLiteManager.TABLE_OUTFITS_NAME;
	ContentValues values = new ContentValues();
	values.put(SQLiteManager.OUTFIT_COLUMN_ID, outfit.getOutfit_Id());
	values.put(SQLiteManager.OUTFIT_COLUMN_NAME, outfit.getName());
	values.put(SQLiteManager.OUTFIT_COLUMN_ALIAS, outfit.getAlias());
	values.put(SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID, outfit.getLeader_character_id());
	values.put(SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT, outfit.getMember_count());
	values.put(SQLiteManager.OUTFIT_COLUMN_TIME_CREATED, outfit.getLeader_character_id());
	values.put(SQLiteManager.OUTFIT_COLUMN_WORDL_ID, outfit.getWorld_id());
	values.put(SQLiteManager.OUTFIT_COLUMN_FACTION_ID, outfit.getFaction_id());
	if (temp) {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, false);
	} else {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, true);
	}
	long insertId = -1;
	try {
	    insertId = database.insert(target, null, values);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while updating a member");
	}
	return (insertId != -1);
    }

    public void deleteOutfit(Outfit outfit, boolean temp) {
	String id = outfit.getOutfit_Id();
	String target = SQLiteManager.TABLE_OUTFITS_NAME;
	try {
	    database.delete(target, SQLiteManager.OUTFIT_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting an outfit");
	}
    }

    public Outfit cursorToOutfit(Cursor cursor) {
	Outfit outfit = new Outfit();
	outfit.setOutfit_Id(cursor.getString(0));
	outfit.setName(cursor.getString(1));
	outfit.setAlias(cursor.getString(2));
	outfit.setLeader_character_id(cursor.getString(3));
	outfit.setMember_count(cursor.getInt(4));
	outfit.setTime_created(cursor.getString(5));
	outfit.setWorld_id(cursor.getString(6));
	outfit.setFaction_id(cursor.getString(7));
	if (cursor.getInt(8) == 1) {
	    outfit.setCached(true);
	} else {
	    outfit.setCached(false);
	}

	return outfit;
    }

    public ArrayList<Outfit> getAllOutfits(boolean temp) {
	ArrayList<Outfit> outfits = new ArrayList<Outfit>(0);
	String target = SQLiteManager.TABLE_OUTFITS_NAME;

	try {
	    Cursor cursor = null;
	    if (temp) {
		cursor = database.query(target, allColumnsOutfit, null, null, null, null, null);
	    } else {
		cursor = database.query(target, allColumnsOutfit, SQLiteManager.CACHE_COLUMN_SAVES + " = 1", null, null, null, null);
	    }

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		Outfit outfit = cursorToOutfit(cursor);
		outfits.add(outfit);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all outfits");
	}
	return outfits;
    }

    public int insertAllOutfits(ArrayList<Outfit> outfitList, boolean temp) {
	int count = 0;
	for (Outfit outfit : outfitList) {
	    if (insertOutfit(outfit, temp)) {
		count++;
	    }
	}
	return count;
    }

    public Outfit getOutfit(String outfitId) {
	String target = SQLiteManager.TABLE_OUTFITS_NAME;
	Outfit outfit = null;

	try {
	    Cursor cursor = database.query(target, allColumnsOutfit, SQLiteManager.OUTFIT_COLUMN_ID + " = " + outfitId, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		outfit = cursorToOutfit(cursor);
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting an outfits");
	}

	return outfit;
    }

    public int updateOutfit(Outfit outfit, boolean temp) {
	String target = SQLiteManager.TABLE_OUTFITS_NAME;

	ContentValues values = new ContentValues();
	values.put(SQLiteManager.OUTFIT_COLUMN_ID, outfit.getOutfit_Id());
	values.put(SQLiteManager.OUTFIT_COLUMN_NAME, outfit.getName());
	values.put(SQLiteManager.OUTFIT_COLUMN_ALIAS, outfit.getAlias());
	values.put(SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID, outfit.getLeader_character_id());
	values.put(SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT, outfit.getMember_count());
	values.put(SQLiteManager.OUTFIT_COLUMN_TIME_CREATED, outfit.getLeader_character_id());
	if (temp) {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, false);
	} else {
	    values.put(SQLiteManager.CACHE_COLUMN_SAVES, true);
	}

	int rowsChanged = 0;
	try {
	    rowsChanged = database.update(target, values, SQLiteManager.OUTFIT_COLUMN_ID + " = " + outfit.getOutfit_Id(), null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while updating an outfits");
	}

	return rowsChanged;
    }

    public boolean insertWorld(World world) {
	ContentValues values = new ContentValues();
	values.put(SQLiteManager.WORLDS_COLUMN_NAME, world.getName().getEn());
	values.put(SQLiteManager.WORLDS_COLUMN_ID, world.getWorld_id());
	values.put(SQLiteManager.WORLDS_COLUMN_STATE, world.getState());
	long insertId = -1;
	try {
	    insertId = database.insert(SQLiteManager.TABLE_WORLDS_NAME, null, values);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while inserting a world");
	}
	return (insertId != -1);
    }

    public void deleteWorld(World world) {
	String id = world.getWorld_id();
	try {
	    database.delete(SQLiteManager.TABLE_WORLDS_NAME, SQLiteManager.WORLDS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting a world");
	}
    }

    public ArrayList<World> getAllWorlds() {
	ArrayList<World> worlds = new ArrayList<World>(0);
	try {
	    Cursor cursor = database.query(SQLiteManager.TABLE_WORLDS_NAME, allColumnsWorlds, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		World world = cursorToWorld(cursor);
		worlds.add(world);
		cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed");
	}

	return worlds;
    }

    public Cursor getAllWorldsInCursor() {
	Cursor cursor = null;
	try {
	    cursor = database.query(SQLiteManager.TABLE_WORLDS_NAME, allColumnsWorlds, null, null, null, null, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting a cursor for all worlds");
	}
	return cursor;
    }

    public int insertAllWorlds(ArrayList<World> WorldList) {
	int count = 0;
	for (World World : WorldList) {
	    if (insertWorld(World)) {
		count++;
	    }
	}
	return count;
    }

    public World getWorld(String worldId) {
	World world = null;
	try {
	    Cursor cursor = database.query(SQLiteManager.TABLE_WORLDS_NAME, allColumnsWorlds, SQLiteManager.WORLDS_COLUMN_ID + " = " + worldId, null, null,
		    null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		world = cursorToWorld(cursor);
		cursor.moveToNext();

	    }
	    // Make sure to close the cursor
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting a world");
	}
	return world;
    }

    public int updateWorld(World world) {

	ContentValues values = new ContentValues();
	values.put(SQLiteManager.WORLDS_COLUMN_NAME, world.getName().getEn());
	values.put(SQLiteManager.WORLDS_COLUMN_ID, world.getWorld_id());
	values.put(SQLiteManager.WORLDS_COLUMN_STATE, world.getState());
	int rowsChanged = 0;
	try {
	    rowsChanged = database.update(SQLiteManager.TABLE_WORLDS_NAME, values, SQLiteManager.WORLDS_COLUMN_ID + " = " + world.getWorld_id(), null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while updating a world");
	}
	return rowsChanged;
    }

    public World cursorToWorld(Cursor cursor) {
	World world = new World();

	world.setWorld_id(cursor.getString(0));
	Name_Multi name = new Name_Multi();
	name.setEn(cursor.getString(1));
	world.setName(name);
	world.setState(cursor.getString(2));

	return world;
    }

    public static PS2Tweet cursorToTweet(Cursor cursor) {
	PS2Tweet tweet = new PS2Tweet();
	tweet.setId(cursor.getString(0));
	tweet.setDate(cursor.getInt(1));
	tweet.setUser(cursor.getString(2));
	tweet.setTag(cursor.getString(3));
	tweet.setContent(cursor.getString(4));
	tweet.setImgUrl(cursor.getString(5));
	return tweet;
    }

    public boolean insertTweet(PS2Tweet tweet, String owner) {
	ContentValues values = new ContentValues();
	values.put(SQLiteManager.TWEETS_COLUMN_ID, tweet.getId());
	values.put(SQLiteManager.TWEETS_COLUMN_USER, tweet.getUser().toString());
	values.put(SQLiteManager.TWEETS_COLUMN_DATE, tweet.getDate());
	values.put(SQLiteManager.TWEETS_COLUMN_CONTENT, tweet.getContent());
	values.put(SQLiteManager.TWEETS_COLUMN_TAG, tweet.getTag());
	values.put(SQLiteManager.TWEETS_COLUMN_PICTURE, tweet.getImgUrl());
	values.put(SQLiteManager.TWEETS_COLUMN_OWNER, owner);

	String target = SQLiteManager.TABLE_TWEETS_NAME;
	long insertId = -1;
	try {
	    insertId = database.insert(target, null, values);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while inserting a tweet");
	}
	return (insertId != -1);
    }

    public int insertAllTweets(ArrayList<PS2Tweet> tweetList, String owner) {
	int count = 0;
	for (PS2Tweet tweet : tweetList) {
	    if (insertTweet(tweet, owner)) {
		count++;
	    } else {
		return count;
	    }
	}
	return count;
    }

    public void deleteTweet(PS2Tweet tweet) {
	String id = tweet.getId();
	String target = SQLiteManager.TABLE_TWEETS_NAME;
	try {
	    database.delete(target, SQLiteManager.TWEETS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while removing a tweet");
	}

    }

    public ArrayList<PS2Tweet> getAllTweets(String[] users, String startDate, String endDate) {
	ArrayList<PS2Tweet> tweets = new ArrayList<PS2Tweet>(0);

	String[] whereArgs = users;
	String[] betweenArgs = new String[] { startDate, endDate };
	Cursor cursor = null;
	try {
	    for (int i = 0; i < whereArgs.length; i++) {
		cursor = database.query(SQLiteManager.TABLE_TWEETS_NAME, allColumnsTweet, SQLiteManager.TWEETS_COLUMN_OWNER + " = " + users[i]
			+ SQLiteManager.TWEETS_COLUMN_DATE + " BETWEEN ? AND ?", betweenArgs, null, null, SQLiteManager.TWEETS_COLUMN_DATE + " DESC ");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
		    PS2Tweet tweet = cursorToTweet(cursor);
		    tweets.add(tweet);
		    cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
	    }
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all tweets for a specific date");
	}
	return tweets;
    }

    public ArrayList<PS2Tweet> getAllTweets(String[] users) {
	ArrayList<PS2Tweet> tweets = new ArrayList<PS2Tweet>(0);

	String[] whereArgs = users;
	Cursor cursor = null;

	try {
	    for (int i = 0; i < whereArgs.length; i++) {
		cursor = database.query(SQLiteManager.TABLE_TWEETS_NAME, allColumnsTweet, SQLiteManager.TWEETS_COLUMN_OWNER + " = ?",
			new String[] { users[i] }, null, null, SQLiteManager.TWEETS_COLUMN_DATE + " DESC ");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
		    PS2Tweet tweet = cursorToTweet(cursor);
		    tweets.add(tweet);
		    cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
	    }
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all tweets");
	}

	return tweets;
    }

    public int countAllTweets(String[] users) {
	String target = SQLiteManager.TABLE_TWEETS_NAME;
	int count = 0;
	Cursor cursor = null;
	try {
	    for (int i = 0; i < users.length; i++) {
		cursor = database.query(target, allColumnsTweet, SQLiteManager.TWEETS_COLUMN_OWNER + " = '" + users[i] + "'", null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
		    count++;
		    cursor.moveToNext();
		}
		cursor.close();
	    }
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while counting all tweets");
	}
	return count;
    }

    public ArrayList<PS2Tweet> getAllTweets(String[] users, int pageSize, int pageNumber) {
	ArrayList<PS2Tweet> tweets = new ArrayList<PS2Tweet>(0);

	String[] whereArgs = users;
	Cursor cursor = null;
	try {
	    for (int i = 0; i < whereArgs.length; i++) {
		cursor = database.query(SQLiteManager.TABLE_TWEETS_NAME, allColumnsTweet, SQLiteManager.TWEETS_COLUMN_OWNER + " = " + users[i], null, null,
			null, SQLiteManager.TWEETS_COLUMN_DATE + " DESC", " limit " + pageSize + " offset " + pageSize * pageNumber);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
		    PS2Tweet tweet = cursorToTweet(cursor);
		    tweets.add(tweet);
		    cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
	    }
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all tweets by pages");
	}
	return tweets;
    }

    public PS2Tweet getTweet(String tweetId) {
	String target = SQLiteManager.TABLE_TWEETS_NAME;
	Cursor cursor = database.query(target, allColumnsTweet, SQLiteManager.TWEETS_COLUMN_ID + " = " + tweetId, null, null, null, null);
	cursor.moveToFirst();
	PS2Tweet tweet = null;
	try {
	    while (!cursor.isAfterLast()) {
		tweet = cursorToTweet(cursor);
		cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting a tweet");
	}
	return tweet;
    }

    public Cursor getTweetCursor(String[] users) {
	String target = SQLiteManager.TABLE_TWEETS_NAME;
	Cursor cursor = null;
	try {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < users.length; i++) {
		builder.append(SQLiteManager.TWEETS_COLUMN_OWNER + " = '" + users[i] + "'");
		if (i < users.length - 1) {
		    builder.append(" OR ");
		}
	    }
	    cursor = database.query(target, allColumnsTweet, builder.toString(), null, null, null, SQLiteManager.TWEETS_COLUMN_DATE + " DESC");
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while a tweet cursor");
	}
	return cursor;
    }

}