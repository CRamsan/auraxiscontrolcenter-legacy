package com.cesarandres.ps2link.module;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Region;

import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.World;
import com.cesarandres.ps2link.soe.content.backlog.Times;
import com.cesarandres.ps2link.soe.content.character.BattleRank;
import com.cesarandres.ps2link.soe.content.character.Certs;
import com.cesarandres.ps2link.soe.content.character.Name;
import com.cesarandres.ps2link.soe.content.world.Name_Multi;

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
	private String[] allColumnsWorlds = { SQLiteManager.WORLDS_COLUMN_ID,
			SQLiteManager.WORLDS_COLUMN_NAME, SQLiteManager.WORLDS_COLUMN_STATE };

	private String[] allColumnsFactions = { SQLiteManager.FACTIONS_COLUMN_ID,
			SQLiteManager.FACTIONS_COLUMN_NAME,
			SQLiteManager.FACTIONS_COLUMN_CODE,
			SQLiteManager.FACTIONS_COLUMN_ICON };

	private String[] allColumnsCharacters = {
			SQLiteManager.CHARACTERS_COLUMN_ID,
			SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST,
			SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER,
			SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID,
			SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS,
			SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
			SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK,
			SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE,
			SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN,
			SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED,
			SQLiteManager.CHARACTERS_COLUMN_FACTION_ID,
			SQLiteManager.CHARACTERS_COLUMN_WORLD_ID };

	private String[] allColumnsMembers = { SQLiteManager.MEMBERS_COLUMN_ID,
			SQLiteManager.MEMBERS_COLUMN_MEMBER_SINCE,
			SQLiteManager.MEMBERS_COLUMN_RANK,
			SQLiteManager.MEMBERS_COLUMN_ORDINAL,
			SQLiteManager.MEMBERS_COLUMN_OUTFIT_ID,
			SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS };

	private String[] allColumnsOutfit = { SQLiteManager.OUTFIT_COLUMN_ID,
			SQLiteManager.OUTFIT_COLUMN_NAME,
			SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID,
			SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT,
			SQLiteManager.OUTFIT_COLUMN_TIME_CREATED,
			SQLiteManager.OUTFIT_COLUMN_WORDL_ID,
			SQLiteManager.OUTFIT_COLUMN_FACTION_ID };

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
		dbHelper.onUpgrade(database, SQLiteManager.DATABASE_VERSION,
				SQLiteManager.DATABASE_VERSION);
	}

	public boolean insertCharacter(CharacterProfile character, boolean temp) {
		ContentValues values = new ContentValues();
		values.put(SQLiteManager.CHARACTERS_COLUMN_ID, character.getId());
		values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST, character
				.getName().getFirst());
		values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, character
				.getName().getFirst_lower());
		values.put(SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID,
				character.getActive_profile_id());
		values.put(SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS, character
				.getCerts().getAvailable_points());
		values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
				character.getCerts().getPercent_to_next());
		values.put(SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, character
				.getBattle_rank().getValue());
		values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK,
				character.getBattle_rank().getPercent_to_next());
		values.put(SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN, character
				.getTimes().getLast_login());
		values.put(SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, character
				.getTimes().getMinutes_played());
		values.put(SQLiteManager.CHARACTERS_COLUMN_FACTION_ID,
				character.getFaction_id());
		values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_ID,
				character.getWorld_id());

		String target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		if (temp) {
			target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		}
		long insertId = database.insert(target, null, values);
		return (insertId != -1);
	}

	public int insertAllCharacters(ArrayList<CharacterProfile> characterList,
			boolean temp) {
		int count = 0;
		for (CharacterProfile character : characterList) {
			if (insertCharacter(character, temp)) {
				count++;
			}
		}
		return count;
	}

	public void deleteCharacter(CharacterProfile character, boolean temp) {
		String id = character.getId();
		String target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		if (temp) {
			target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		}
		database.delete(target,
				SQLiteManager.CHARACTERS_COLUMN_ID + " = " + id, null);
	}

	public CharacterProfile getCharacter(String characterId, boolean temp) {
		String target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		if (temp) {
			target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		}
		Cursor cursor = database.query(target, allColumnsCharacters,
				SQLiteManager.CHARACTERS_COLUMN_ID + " = " + characterId, null,
				null, null, null);
		cursor.moveToFirst();
		CharacterProfile character = null;
		while (!cursor.isAfterLast()) {
			character = cursorToCharacterProfile(cursor);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return character;
	}

	public int updateCharacter(CharacterProfile character, boolean temp) {
		String target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		if (temp) {
			target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		}
		ContentValues values = new ContentValues();
		values.put(SQLiteManager.CHARACTERS_COLUMN_ID, character.getId());
		values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST, character
				.getName().getFirst());
		values.put(SQLiteManager.CHARACTERS_COLUMN_NAME_FIRST_LOWER, character
				.getName().getFirst_lower());
		values.put(SQLiteManager.CHARACTERS_COLUMN_ACTIVE_PROFILE_ID,
				character.getActive_profile_id());
		values.put(SQLiteManager.CHARACTERS_COLUMN_CURRENT_POINTS, character
				.getCerts().getAvailable_points());
		values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT,
				character.getCerts().getPercent_to_next());
		values.put(SQLiteManager.CHARACTERS_COLUMN_RANK_VALUE, character
				.getBattle_rank().getValue());
		values.put(SQLiteManager.CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK,
				character.getBattle_rank().getPercent_to_next());
		values.put(SQLiteManager.CHARACTERS_COLUMN_LAST_LOGIN, character
				.getTimes().getLast_login());
		values.put(SQLiteManager.CHARACTERS_COLUMN_MINUTES_PLAYED, character
				.getTimes().getMinutes_played());
		values.put(SQLiteManager.CHARACTERS_COLUMN_FACTION_ID,
				character.getFaction_id());
		values.put(SQLiteManager.CHARACTERS_COLUMN_WORLD_ID,
				character.getWorld_id());

		return database.update(target, values,
				SQLiteManager.CHARACTERS_COLUMN_ID + " = " + character.getId(),
				null);
	}

	private CharacterProfile cursorToCharacterProfile(Cursor cursor) {
		CharacterProfile character = new CharacterProfile();
		character.setId(cursor.getString(0));
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
		br.setValue(cursor.getInt(6));
		br.setPercent_to_next(cursor.getInt(7));
		character.setBattle_rank(br);

		Times times = new Times();
		times.setLast_login(cursor.getString(8));
		times.setMinutes_played(cursor.getString(9));
		character.setTimes(times);

		character.setFaction_id(cursor.getString(10));
		character.setWorld_id(cursor.getString(11));

		return character;
	}

	public ArrayList<CharacterProfile> daleteAllCharacterProfiles(boolean temp) {
		ArrayList<CharacterProfile> profiles = new ArrayList<CharacterProfile>(
				0);

		String target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		if (temp) {
			target = SQLiteManager.TABLE_CHARACTERS_TMP_NAME;
		}

		Cursor cursor = database.query(target, allColumnsCharacters, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CharacterProfile character = cursorToCharacterProfile(cursor);
			profiles.add(character);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return profiles;
	}

	public ArrayList<CharacterProfile> getAllCharacterProfiles() {
		ArrayList<CharacterProfile> profiles = new ArrayList<CharacterProfile>(
				0);

		Cursor cursor = database.query(SQLiteManager.TABLE_CHARACTERS_NAME,
				allColumnsCharacters, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CharacterProfile character = cursorToCharacterProfile(cursor);
			profiles.add(character);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return profiles;
	}

	public boolean insertFaction(Faction faction) {
		ContentValues values = new ContentValues();
		values.put(SQLiteManager.FACTIONS_COLUMN_ID, faction.getId());
		values.put(SQLiteManager.FACTIONS_COLUMN_NAME, faction.getName()
				.getEn());
		values.put(SQLiteManager.FACTIONS_COLUMN_CODE, faction.getCode());
		values.put(SQLiteManager.FACTIONS_COLUMN_ICON, faction.getIcon());
		long insertId = database.insert(SQLiteManager.TABLE_CHARACTERS_NAME,
				null, values);
		return (insertId != -1);
	}

	public void deleteFaction(Faction faction) {
		String id = faction.getId();
		database.delete(SQLiteManager.TABLE_FACTIONS_NAME,
				SQLiteManager.FACTIONS_COLUMN_ID + " = " + id, null);
	}

	private Faction cursorToFaction(Cursor cursor) {
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

		Cursor cursor = database.query(SQLiteManager.TABLE_FACTIONS_NAME,
				allColumnsFactions, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Faction faction = cursorToFaction(cursor);
			factions.add(faction);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return factions;
	}

	public Faction getFaction(int factionId) {
		Cursor cursor = database.query(SQLiteManager.TABLE_FACTIONS_NAME,
				allColumnsFactions, SQLiteManager.FACTIONS_COLUMN_ID + " = "
						+ factionId, null, null, null, null);
		cursor.moveToFirst();
		Faction faction = null;
		while (!cursor.isAfterLast()) {
			faction = cursorToFaction(cursor);
		}
		// Make sure to close the cursor
		cursor.close();
		return faction;
	}

	public int updateFaction(Faction faction) {

		ContentValues values = new ContentValues();
		values.put(SQLiteManager.FACTIONS_COLUMN_ID, faction.getId());
		values.put(SQLiteManager.FACTIONS_COLUMN_NAME, faction.getName()
				.getEn());
		values.put(SQLiteManager.FACTIONS_COLUMN_CODE, faction.getCode());
		values.put(SQLiteManager.FACTIONS_COLUMN_ICON, faction.getIcon());

		return database.update(SQLiteManager.TABLE_FACTIONS_NAME, values,
				SQLiteManager.FACTIONS_COLUMN_ID + " = " + faction.getId(),
				null);
	}

	public boolean insertMember(Member member) {
		ContentValues values = new ContentValues();
		values.put(SQLiteManager.MEMBERS_COLUMN_ID, member.getCharacter_id());
		values.put(SQLiteManager.MEMBERS_COLUMN_MEMBER_SINCE,
				member.getMember_since());
		values.put(SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS,
				member.getOnline_status());
		values.put(SQLiteManager.MEMBERS_COLUMN_RANK, member.getRank());
		values.put(SQLiteManager.MEMBERS_COLUMN_ORDINAL,
				member.getRank_ordinal());
		long insertId = database.insert(SQLiteManager.TABLE_MEMBERS_NAME, null,
				values);
		return (insertId != -1);
	}

	public void deleteMember(Member member) {
		String id = member.getCharacter_id();
		database.delete(SQLiteManager.TABLE_MEMBERS_NAME,
				SQLiteManager.MEMBERS_COLUMN_ID + " = " + id, null);
	}

	private Member cursorToMember(Cursor cursor) {
		Member member = new Member();
		member.setCharacter_id(cursor.getString(0));
		member.setMember_since(cursor.getString(1));
		member.setOnline_status(cursor.getString(2));
		member.setRank(cursor.getString(3));
		member.setRank_ordinal(cursor.getString(4));
		return member;
	}

	public ArrayList<Member> getAllMembers() {
		ArrayList<Member> members = new ArrayList<Member>(0);
		Cursor cursor = database.query(SQLiteManager.TABLE_MEMBERS_NAME,
				allColumnsMembers, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Member member = cursorToMember(cursor);
			members.add(member);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return members;
	}

	public int insertAllMembers(ArrayList<Member> memberList) {
		int count = 0;
		for (Member member : memberList) {
			if (insertMember(member)) {
				count++;
			}
		}
		return count;
	}

	public Member getMember(int memberId) {
		Cursor cursor = database.query(SQLiteManager.TABLE_MEMBERS_NAME,
				allColumnsMembers, SQLiteManager.MEMBERS_COLUMN_ID + " = "
						+ memberId, null, null, null, null);
		cursor.moveToFirst();
		Member member = null;
		while (!cursor.isAfterLast()) {
			member = cursorToMember(cursor);
		}
		// Make sure to close the cursor
		cursor.close();
		return member;
	}

	public int updateMember(Member member) {

		ContentValues values = new ContentValues();
		values.put(SQLiteManager.MEMBERS_COLUMN_ID, member.getCharacter_id());
		values.put(SQLiteManager.MEMBERS_COLUMN_MEMBER_SINCE,
				member.getMember_since());
		values.put(SQLiteManager.MEMBERS_COLUMN_ONLINE_STATUS,
				member.getOnline_status());
		values.put(SQLiteManager.MEMBERS_COLUMN_RANK, member.getRank());
		values.put(SQLiteManager.MEMBERS_COLUMN_ORDINAL,
				member.getRank_ordinal());

		return database.update(
				SQLiteManager.TABLE_MEMBERS_NAME,
				values,
				SQLiteManager.MEMBERS_COLUMN_ID + " = "
						+ member.getCharacter_id(), null);
	}

	public boolean insertOutfit(Outfit outfit) {
		ContentValues values = new ContentValues();
		values.put(SQLiteManager.OUTFIT_COLUMN_ID, outfit.getId());
		values.put(SQLiteManager.OUTFIT_COLUMN_NAME, outfit.getName());
		values.put(SQLiteManager.OUTFIT_COLUMN_ALIAS, outfit.getAlias());
		values.put(SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID,
				outfit.getLeader_character_id());
		values.put(SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT,
				outfit.getMember_count());
		values.put(SQLiteManager.OUTFIT_COLUMN_TIME_CREATED,
				outfit.getLeader_character_id());
		long insertId = database.insert(SQLiteManager.TABLE_MEMBERS_NAME, null,
				values);
		return (insertId != -1);
	}

	public void deleteOutfit(Outfit outfit) {
		String id = outfit.getId();
		database.delete(SQLiteManager.TABLE_OUTFITS_NAME,
				SQLiteManager.OUTFIT_COLUMN_ID + " = " + id, null);
	}

	private Outfit cursorToOutfit(Cursor cursor) {
		Outfit outfit = new Outfit();
		outfit.setId(cursor.getString(0));
		outfit.setName(cursor.getString(1));
		outfit.setAlias(cursor.getString(2));
		outfit.setLeader_character_id(cursor.getString(3));
		outfit.setTime_created(cursor.getString(4));
		outfit.setWorld_id(cursor.getString(5));
		outfit.setFaction_id(cursor.getString(6));
		return outfit;
	}

	public ArrayList<Outfit> getAllOutfits() {
		ArrayList<Outfit> outfits = new ArrayList<Outfit>(0);
		Cursor cursor = database.query(SQLiteManager.TABLE_OUTFITS_NAME,
				allColumnsOutfit, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Outfit outfit = cursorToOutfit(cursor);
			outfits.add(outfit);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return outfits;
	}

	public int insertAllOutfits(ArrayList<Outfit> outfitList) {
		int count = 0;
		for (Outfit outfit : outfitList) {
			if (insertOutfit(outfit)) {
				count++;
			}
		}
		return count;
	}

	public Outfit getOutfit(int outfitId) {
		Cursor cursor = database.query(SQLiteManager.TABLE_OUTFITS_NAME,
				allColumnsOutfit, SQLiteManager.OUTFIT_COLUMN_ID + " = "
						+ outfitId, null, null, null, null);
		cursor.moveToFirst();
		Outfit outfit = null;
		while (!cursor.isAfterLast()) {
			outfit = cursorToOutfit(cursor);
		}
		// Make sure to close the cursor
		cursor.close();
		return outfit;
	}

	public int updateOutfit(Outfit outfit) {

		ContentValues values = new ContentValues();
		values.put(SQLiteManager.OUTFIT_COLUMN_ID, outfit.getId());
		values.put(SQLiteManager.OUTFIT_COLUMN_NAME, outfit.getName());
		values.put(SQLiteManager.OUTFIT_COLUMN_ALIAS, outfit.getAlias());
		values.put(SQLiteManager.OUTFIT_COLUMN_LEADER_CHARACTER_ID,
				outfit.getLeader_character_id());
		values.put(SQLiteManager.OUTFIT_COLUMN_MEMBER_COUNT,
				outfit.getMember_count());
		values.put(SQLiteManager.OUTFIT_COLUMN_TIME_CREATED,
				outfit.getLeader_character_id());

		return database.update(SQLiteManager.TABLE_OUTFITS_NAME, values,
				SQLiteManager.OUTFIT_COLUMN_ID + " = " + outfit.getId(), null);
	}

	public boolean insertWorld(World world) {
		ContentValues values = new ContentValues();
		values.put(SQLiteManager.WORLDS_COLUMN_NAME, world.getName().getEn());
		values.put(SQLiteManager.WORLDS_COLUMN_ID, world.getWorld_id());
		values.put(SQLiteManager.WORLDS_COLUMN_STATE, world.getState());
		long insertId = database.insert(SQLiteManager.TABLE_WORLDS_NAME, null,
				values);
		return (insertId != -1);
	}

	public void deleteWorld(World world) {
		String id = world.getWorld_id();
		database.delete(SQLiteManager.TABLE_WORLDS_NAME,
				SQLiteManager.OUTFIT_COLUMN_ID + " = " + id, null);
	}

	private World cursorToWorld(Cursor cursor) {
		World world = new World();

		world.setWorld_id(cursor.getString(0));
		Name_Multi name = new Name_Multi();
		name.setEn(cursor.getString(1));
		world.setName(name);
		world.setState(cursor.getString(2));

		return world;
	}

	public ArrayList<World> getAllWorlds() {
		ArrayList<World> worlds = new ArrayList<World>(0);
		Cursor cursor = database.query(SQLiteManager.TABLE_WORLDS_NAME,
				allColumnsWorlds, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			World world = cursorToWorld(cursor);
			worlds.add(world);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return worlds;
	}

	public Cursor getAllWorldsInCursor() {
		return database.query(SQLiteManager.TABLE_WORLDS_NAME,
				allColumnsWorlds, null, null, null, null, null);
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
		Cursor cursor = database.query(SQLiteManager.TABLE_WORLDS_NAME,
				allColumnsWorlds, SQLiteManager.WORLDS_COLUMN_ID + " = "
						+ worldId, null, null, null, null);
		cursor.moveToFirst();
		World world = null;
		while (!cursor.isAfterLast()) {
			world = cursorToWorld(cursor);
		}
		// Make sure to close the cursor
		cursor.close();
		return world;
	}

	public int updateWorld(World world) {

		ContentValues values = new ContentValues();
		values.put(SQLiteManager.WORLDS_COLUMN_NAME, world.getName().getEn());
		values.put(SQLiteManager.WORLDS_COLUMN_ID, world.getWorld_id());
		values.put(SQLiteManager.WORLDS_COLUMN_STATE, world.getState());

		return database.update(SQLiteManager.TABLE_WORLDS_NAME, values,
				SQLiteManager.WORLDS_COLUMN_ID + " = " + world.getWorld_id(),
				null);
	}

}