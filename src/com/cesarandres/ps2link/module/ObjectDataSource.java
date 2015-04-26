package com.cesarandres.ps2link.module;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.dbg.content.CharacterProfile;
import com.cesarandres.ps2link.dbg.content.Faction;
import com.cesarandres.ps2link.dbg.content.Member;
import com.cesarandres.ps2link.dbg.content.Outfit;
import com.cesarandres.ps2link.dbg.content.World;
import com.cesarandres.ps2link.dbg.content.character.BattleRank;
import com.cesarandres.ps2link.dbg.content.character.Certs;
import com.cesarandres.ps2link.dbg.content.character.Name;
import com.cesarandres.ps2link.dbg.content.character.Server;
import com.cesarandres.ps2link.dbg.content.character.Times;
import com.cesarandres.ps2link.dbg.content.world.Name_Multi;
import com.cesarandres.ps2link.dbg.util.Logger;
import com.cesarandres.ps2link.module.twitter.PS2Tweet;



//TODO This class needs to be cleaned up, methods have inconsistent parameters and return values can be misleading
/**
 * Class that retrieves information from the SQLiteManager and convert it into
 * objects that can be used by other classes.
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
	    SQLiteManager.CHARACTERS_COLUMN_OUTFIT_NAME, SQLiteManager.CACHE_COLUMN_SAVES, SQLiteManager.CHARACTERS_COLUMN_WORLD_NAME, SQLiteManager.CHARACTERS_COLUMN_NAMESPACE };

    private String[] allColumnsMembers = { SQLiteManager.MEMBERS_COLUMN_ID, SQLiteManager.MEMBERS_COLUMN_RANK, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID,
	    SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS, SQLiteManager.MEMBERS_COLUMN_NAME, SQLiteManager.CACHE_COLUMN_SAVES };

    private String[] allColumnsOutfit = { SQLiteManager.OUTFIT_COLUMN_ID, SQLiteManager.OUTFIT_COLUMN_NAME, SQLiteManager.OUTFIT_COLUMN_ALIAS,
	    SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID, SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT, SQLiteManager.OUTFIT_COLUMN_TIME_CREATED,
	    SQLiteManager.OUTFIT_COLUMN_WORDL_ID, SQLiteManager.OUTFIT_COLUMN_FACTION_ID, SQLiteManager.CACHE_COLUMN_SAVES, SQLiteManager.OUTFIT_COLUMN_NAMESPACE };

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
    public void open(){
    try{
    	database = dbHelper.getWritableDatabase();
    }catch(Exception e){
    	Logger.log(Log.ERROR, this, "Could not open database, database is already locked. Trying again");
    	dbHelper.close();
    	database = dbHelper.getWritableDatabase();
    }
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

    /**
     * @param cursor
     *            Cursor pointing to the somewhere in the database
     * @param index
     *            position to move the cursor to
     * @return the cursor on the requested position
     */
    public static Cursor cursorToPosition(Cursor cursor, int index) {
	cursor.moveToPosition(index);
	return cursor;
    }

    /**
     * @param character
     *            Character to be stored in the database
     * @param temp
     *            Boolean to set the character as a temporary or permanent entry
     * @return True if the operation was successful, false otherwise
     */
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

    values.put(SQLiteManager.CHARACTERS_COLUMN_NAMESPACE, character.getNamespace().name());
    
	long insertId = -1;
	try {
	    insertId = database.insert(target, null, values);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while inserting a single character");
	}

	return (insertId != -1);
    }

    /**
     * @param characterList
     *            Arraylist with all the characters that need to be stored in
     *            the database
     * @param temp
     *            Boolean to set the character as a temporary or permanent entry
     * @return the number of characters successfully stored
     */
    public int insertAllCharacters(ArrayList<CharacterProfile> characterList, boolean temp) {
	int count = 0;
	for (CharacterProfile character : characterList) {
	    if (insertCharacter(character, temp)) {
		count++;
	    }
	}
	return count;
    }

    /**
     * @param character
     *            Character to remove from the databse
     */
    public void deleteCharacter(CharacterProfile character) {
	String id = character.getCharacterId();
	String target = SQLiteManager.TABLE_CHARACTERS_NAME;
	try {
	    database.delete(target, SQLiteManager.CHARACTERS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting a single character");
	}
    }

    /**
     * @param characterId
     *            Chraracter ID of the character to retrieve
     * @return The character with the given id, null if none is found
     */
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

    /**
     * @param character
     *            Character with more current information
     * @param temp
     *            true if the character is just a temporary one, false otherwise
     * @return the number of rows updated
     */
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

    /**
     * @param cursor
     *            cursor pointing at a character
     * @return the Character from the database
     */
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

	character.setNamespace(Namespace.valueOf(cursor.getString(11)));
	
	return character;
    }

    /**
     * @param all
     *            true to retrieve all characters in database, false will
     *            retrieve only the non-temporary ones
     * @return Arraylist containing all the characters found
     */
    public ArrayList<CharacterProfile> getAllCharacterProfiles(boolean all) {
	ArrayList<CharacterProfile> profiles = new ArrayList<CharacterProfile>(0);

	try {
	    Cursor cursor = null;
	    if (all) {
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

    /**
     * @param all
     *            true to remove all characters in database, false will
     *            delete only the non-temporary ones
     * @return int number of charaters deleted
     */
    public int deleteAllCharacterProfiles(boolean all) {
	int removed = 0;
	try {
	    if (all) {
	    	removed = database.delete(SQLiteManager.TABLE_CHARACTERS_NAME, "*", null);
	    } else {
			removed = database.delete(SQLiteManager.TABLE_CHARACTERS_NAME, SQLiteManager.CACHE_COLUMN_SAVES + " = 0", null);
	    }
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting profiles");
	}

	return removed;
    }
    
    /**
     * @param faction
     *            Faction to be inserted to the database
     * @return true if the process was succesful, false otherwise
     */
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

    /**
     * @param faction
     *            Faction to be deleted from the database
     */
    public void deleteFaction(Faction faction) {
	String id = faction.getId();
	try {
	    database.delete(SQLiteManager.TABLE_FACTIONS_NAME, SQLiteManager.FACTIONS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting faction");
	}
    }

    /**
     * @param cursor
     *            Cursor pointing to a faction in the database
     * @return The faction in the position of the cursor
     */
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

    /**
     * @param factionList
     *            Arraylist containing the factions to be inserted
     * @return the number of entries added
     */
    public int insertAllFactions(ArrayList<Faction> factionList) {
	int count = 0;
	for (Faction faction : factionList) {
	    if (insertFaction(faction)) {
		count++;
	    }
	}
	return count;
    }

    /**
     * @return An arraylist with all the factions in the database
     */
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

    /**
     * @param factionId
     *            faction id of the faction to retrieve
     * @return the Faction object in the database
     */
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

    /**
     * @param faction
     *            faction already existing in the database but with more current
     *            information
     * @return the number of rows changed
     */
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

    /**
     * @param member
     *            the member to be inserted
     * @param outfit_id
     *            id of the outfit the member belongs to
     * @param temp
     *            true for a temporary member, false for a permanent one
     * @return true if the operation was succesful, false otherwise
     */
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

    /**
     * @param member
     *            member to remove from the database
     */
    public void deleteMember(Member member) {
	String id = member.getCharacter_id();
	String target = SQLiteManager.TABLE_MEMBERS_NAME;

	String[] whereArgs = new String[] { id };
	try {
	    database.delete(target, SQLiteManager.MEMBERS_COLUMN_ID + " = ?", whereArgs);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting member");
	}
    }

    /**
     * @param cursor
     *            cursor pointing to a member in the database
     * @return member that the cursor is pointing at
     */
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

    /**
     * @param outfit_id
     *            outfit id for which all members will be read
     * @return an arraylist with all the members for the requested outfit
     */
    public ArrayList<Member> getAllMembers(String outfit_id) {
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

    /**
     * @param outfit_id
     *            outfit id to read members from
     * @param index
     *            position to start reading from
     * @param count
     *            number of members to read
     * @return an arraylist with all the members from the requested outfit
     *         inside the given range
     */
    public ArrayList<Member> getMembers(String outfit_id, int index, int count) {
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

    /**
     * @param outfit_id
     *            outfit id of the outfit to read from
     * @param showOffline
     *            true will read all members, false will read only online
     *            members
     * @return an arraylist with all the members of the outfit that match the
     *         criteria
     */
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

    /**
     * @param memberList
     *            arraylist containing all the members to add to the database
     * @param outfit_id
     *            outfit id of the outfit this members belong to
     * @param temp
     *            true will set the member as temporary, false will set it as
     *            permanent
     * @return number of entries added
     */
    public int insertAllMembers(ArrayList<Member> memberList, String outfit_id, boolean temp) {
	int count = 0;
	for (Member member : memberList) {
	    if (insertMember(member, outfit_id, temp)) {
		count++;
	    }
	}
	return count;
    }

    /**
     * @param memberId
     *            id of the member to retrieve
     * @return the member with the given id
     */
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

    /**
     * @param outfit_id
     *            id of the outfit to read from
     * @param showOffline
     *            true for reading all members, false will only read online
     *            members
     * @return a cursor that points to the members that match the given
     *         parameters
     */
    public Cursor getMembersCursor(String outfit_id, boolean showOffline) {
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

    /**
     * @param member
     *            member with more current information than the one on the
     *            database
     * @param temp
     *            true will set the member as temporary, false will set it as
     *            permanent
     * @return the number of rows changed
     */
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

    /**
     * @param outfit_id
     *            id of the outfit that will have all of it's members removed
     */
    public void deleteAllMembers(String outfit_id) {
	String target = SQLiteManager.TABLE_MEMBERS_NAME;
	try {
	    String[] whereArgs = new String[] { outfit_id };
	    database.delete(target, SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID + " = ?", whereArgs);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while removing members");
	}
    }

    /**
     * @param outfit
     *            outfit to be inserted into the database
     * @param temp
     *            true will set this outfit as temporary, false will set it as
     *            permanent
     * @return true if the operation was succesful, false otherwise
     */
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
	values.put(SQLiteManager.OUTFIT_COLUMN_NAMESPACE, outfit.getNamespace().name());
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

    /**
     * @param outfit
     *            outfit to be removed from the database
     */
    public void deleteOutfit(Outfit outfit) {
	String id = outfit.getOutfit_Id();
	String target = SQLiteManager.TABLE_OUTFITS_NAME;
	try {
	    database.delete(target, SQLiteManager.OUTFIT_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting an outfit");
	}
    }

    /**
     * @param all
     *            true will remove all outfits, false will only remove the ones set
     *            as temporary entries.
     */
    public int deleteAllOutfit(boolean all) {	
	String target = SQLiteManager.TABLE_OUTFITS_NAME;

	try {
	    Cursor cursor = null;
	    if (all) {
		cursor = database.query(target, allColumnsOutfit, null, null, null, null, null);
	    } else {
		cursor = database.query(target, allColumnsOutfit, SQLiteManager.CACHE_COLUMN_SAVES + " = 0", null, null, null, null);
	    }

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		Outfit outfit = cursorToOutfit(cursor);
		deleteAllMembers(outfit.getOutfit_Id());
		cursor.moveToNext();
	    }
	    cursor.close();
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting all outfits");
	}
	
	int removed = 0;
	try {
		if(all){
			removed = database.delete(target, "*", null);
		}else{
			removed = database.delete(target, SQLiteManager.CACHE_COLUMN_SAVES + " = 0", null);
		}
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting an outfit");
	}
	return removed;
    }
    
    /**
     * @param cursor
     *            cursor pointing at an outfit in the database
     * @return the outfit the cursor is pointing at
     */
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
	outfit.setNamespace(Namespace.valueOf(cursor.getString(9)));

	return outfit;
    }

    /**
     * @param all
     *            true will read all outfits, false will return only the non
     *            temporary ones
     * @return an arraylist with all the outfits found
     */
    public ArrayList<Outfit> getAllOutfits(boolean all) {
	ArrayList<Outfit> outfits = new ArrayList<Outfit>(0);
	String target = SQLiteManager.TABLE_OUTFITS_NAME;

	try {
	    Cursor cursor = null;
	    if (all) {
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

    /**
     * @param outfitList
     *            arraylist with outfits to be inserted to the database
     * @param temp
     *            true will set the outfits as temporary, false will set them as
     *            permanent
     * @return the number of rows changed
     */
    public int insertAllOutfits(ArrayList<Outfit> outfitList, boolean temp) {
	int count = 0;
	for (Outfit outfit : outfitList) {
	    if (insertOutfit(outfit, temp)) {
		count++;
	    }
	}
	return count;
    }

    /**
     * @param outfitId
     *            id of the outfit to retrieve
     * @return the requested Outfit
     */
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

    /**
     * @param outfit
     *            outfit with more current information
     * @param temp
     *            true to set it as temporary, false to set it as permanent
     * @return the number of rows changed
     */
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

    /**
     * @param world
     *            world or server to be inserted to the database
     * @return true if the operation was succesful
     */
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

    /**
     * @param world
     *            world to be removed
     */
    public void deleteWorld(World world) {
	String id = world.getWorld_id();
	try {
	    database.delete(SQLiteManager.TABLE_WORLDS_NAME, SQLiteManager.WORLDS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while deleting a world");
	}
    }

    /**
     * @return an arraylist with all the worlds in the database
     */
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

    /**
     * @return returns a cursor that points to all the worlds
     */
    public Cursor getAllWorldsInCursor() {
	Cursor cursor = null;
	try {
	    cursor = database.query(SQLiteManager.TABLE_WORLDS_NAME, allColumnsWorlds, null, null, null, null, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while getting a cursor for all worlds");
	}
	return cursor;
    }

    /**
     * @param WorldList
     *            arraylist with all worlds to be inserted
     * @return the number of entries added
     */
    public int insertAllWorlds(ArrayList<World> WorldList) {
	int count = 0;
	for (World World : WorldList) {
	    if (insertWorld(World)) {
		count++;
	    }
	}
	return count;
    }

    /**
     * @param worldId
     *            id of the world to retrieve
     * @return world found
     */
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

    /**
     * @param world
     *            world with the most current information
     * @return the number of rows changed
     */
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

    /**
     * @param cursor
     *            cursor pointing to a world
     * @return the world at the cursor's position
     */
    public World cursorToWorld(Cursor cursor) {
	World world = new World();

	world.setWorld_id(cursor.getString(0));
	Name_Multi name = new Name_Multi();
	name.setEn(cursor.getString(1));
	world.setName(name);
	world.setState(cursor.getString(2));

	return world;
    }

    /**
     * @param cursor
     *            cursor pointing at a tweet
     * @return the tweet in the cursor's position
     */
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

    /**
     * @param tweet
     *            the tweet to be inserted in the database
     * @param owner
     *            the person who wrote o RT the tweet
     * @return true if the operation was sucessful, false otherwise
     */
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

    /**
     * @param tweetList
     *            an arraylist with all the tweets to be stored
     * @param owner
     *            the person who wrote or RTed the tweet
     * @return the number of entries added
     */
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

    /**
     * @param tweet
     *            tweet to be removed
     */
    public void deleteTweet(PS2Tweet tweet) {
	String id = tweet.getId();
	String target = SQLiteManager.TABLE_TWEETS_NAME;
	try {
	    database.delete(target, SQLiteManager.TWEETS_COLUMN_ID + " = " + id, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while removing a tweet");
	}

    }

    /**
     * @param older
     *            delete all the tweets older than the specified ammount.
     * @return return the number of tweets removed
     */
    public int deleteAllTweet(int older) {
	String target = SQLiteManager.TABLE_TWEETS_NAME;
	int removed = 0;
	try {
	    removed = database.delete(target, SQLiteManager.TWEETS_COLUMN_DATE + " < " + older, null);
	} catch (IllegalStateException e) {
	    Logger.log(Log.INFO, this, "Connection closed while removing all tweets");
	}
	return removed;
    }
    
    /**
     * @param users
     *            array with users to retrieve tweets from
     * @param startDate
     *            string with the start date in unix time
     * @param endDate
     *            string with the end date in unix time
     * @return the arraylist with all the tweets that match the criteria
     */
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

    /**
     * @param users
     *            array with all users to retrieve tweets from
     * @return an arraylist with all the tweets for the requested users
     */
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

    /**
     * @param users
     *            users to retrieve tweets from
     * @return the number of tweets for the requested users
     */
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

    /**
     * @param users
     *            array with the users to retrieve users from
     * @param pageSize
     *            number of tweets to retrieve
     * @param pageNumber
     *            tweets will be read on sections of length pageSize. This is
     *            the nth section to retrieve
     * @return arraylist with the tweets found
     */
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

    /**
     * @param tweetId
     *            id of the tweet to retrieve
     * @return the tweet with the given id
     */
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

    /**
     * @param users
     *            the array of users to read tweets from
     * @return a cursor pointing at a list of tweets for the given users
     */
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