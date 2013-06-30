package com.cesarandres.ps2link.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class will handle accessing and retrieving information from the local
 * database.
 * 
 * @author Cesar
 * 
 */
public class SQLiteManager extends SQLiteOpenHelper {

	public static final String TABLE_WORLDS_NAME = "worlds";
	public static final String TABLE_FACTIONS_NAME = "factions";
	public static final String TABLE_CHARACTERS_NAME = "characters";
	public static final String TABLE_MEMBERS_NAME = "members";
	public static final String TABLE_OUTFITS_NAME = "outfits";

	public static final String CACHE_COLUMN_SAVES = "cached";
	
	public static final String WORLDS_COLUMN_ID = "world_id";
	public static final String WORLDS_COLUMN_STATE = "state";
	public static final String WORLDS_COLUMN_NAME = "name";

	public static final String FACTIONS_COLUMN_ID = "id";
	public static final String FACTIONS_COLUMN_NAME = "name";
	public static final String FACTIONS_COLUMN_CODE = "code";
	public static final String FACTIONS_COLUMN_ICON = "icon";

	public static final String CHARACTERS_COLUMN_ID = "id";
	public static final String CHARACTERS_COLUMN_NAME_FIRST = "name_first";
	public static final String CHARACTERS_COLUMN_NAME_FIRST_LOWER = "name_first_lower";
	public static final String CHARACTERS_COLUMN_ACTIVE_PROFILE_ID = "active_profile_id";
	public static final String CHARACTERS_COLUMN_CURRENT_POINTS = "current_points";
	public static final String CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT = "percentage_to_next_cert";
	public static final String CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK = "percentage_to_next_rank";
	public static final String CHARACTERS_COLUMN_RANK_VALUE = "rank_value";
	public static final String CHARACTERS_COLUMN_LAST_LOGIN = "last_login";
	public static final String CHARACTERS_COLUMN_MINUTES_PLAYED = "minutes_played";
	public static final String CHARACTERS_COLUMN_FACTION_ID = "faction_id";
	public static final String CHARACTERS_COLUMN_WORLD_ID = "world_id";

	public static final String MEMBERS_COLUMN_ID = "character_id";
	public static final String MEMBERS_COLUMN_RANK = "rank";
	public static final String MEMBERS_COLUMN_OUTFIT_ID = "outfit";
	public static final String MEMBERS_COLUMN_ONLINE_STATUS = "online_status";
	public static final String MEMBERS_COLUMN_NAME = "name";

	public static final String OUTFIT_COLUMN_ID = "id";
	public static final String OUTFIT_COLUMN_NAME = "name";
	public static final String OUTFIT_COLUMN_ALIAS = "alias";
	public static final String OUTFIT_COLUMN_LEADER_CHARACTER_ID = "leader_character_id";
	public static final String OUTFIT_COLUMN_MEMBER_COUNT = "member_count";
	public static final String OUTFIT_COLUMN_TIME_CREATED = "time_created";
	public static final String OUTFIT_COLUMN_WORDL_ID = "world_id";
	public static final String OUTFIT_COLUMN_FACTION_ID = "faction_id";

	public static final String DATABASE_NAME = "ps2link.db";
	public static final int DATABASE_VERSION = 19;

	private static final String CREATE_WORLDS_TABLE = "create table "
			+ TABLE_WORLDS_NAME + " ( " + WORLDS_COLUMN_ID + " Int, "
			+ WORLDS_COLUMN_STATE + " Int, " + WORLDS_COLUMN_NAME
			+ " varchar(-1), " + "PRIMARY KEY (" + WORLDS_COLUMN_ID + "));";

	private static final String CREATE_FACTIONS_TABLE = "create table "
			+ TABLE_FACTIONS_NAME + " ( " + FACTIONS_COLUMN_ID + " Int, "
			+ FACTIONS_COLUMN_NAME + " varchar(-1), " + FACTIONS_COLUMN_CODE
			+ " varchar(-1), " + FACTIONS_COLUMN_ICON + " Int, "
			+ "PRIMARY KEY (" + FACTIONS_COLUMN_ID + "));";

	private static final String CREATE_CHARACTERS_TABLE = "create table "
			+ TABLE_CHARACTERS_NAME + " ( " + CHARACTERS_COLUMN_ID
			+ " varchar(-1), " + CHARACTERS_COLUMN_NAME_FIRST
			+ " varchar(-1), " + CHARACTERS_COLUMN_NAME_FIRST_LOWER
			+ " varchar(-1), " + CHARACTERS_COLUMN_ACTIVE_PROFILE_ID + " Int, "
			+ CHARACTERS_COLUMN_CURRENT_POINTS + " Int, "
			+ CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_CERT + " Int, "
			+ CHARACTERS_COLUMN_PERCENTAGE_TO_NEXT_RANK + " Int, "
			+ CHARACTERS_COLUMN_RANK_VALUE + " Int, "
			+ CHARACTERS_COLUMN_LAST_LOGIN + " Int, "
			+ CHARACTERS_COLUMN_MINUTES_PLAYED + " Int, "
			+ CHARACTERS_COLUMN_FACTION_ID + " varchar(-1), "
			+ CHARACTERS_COLUMN_WORLD_ID + " varchar(-1), " 
			+ CACHE_COLUMN_SAVES + " Int, " 
			+ "PRIMARY KEY (" + CHARACTERS_COLUMN_ID + "), " + "FOREIGN KEY("
			+ CHARACTERS_COLUMN_FACTION_ID + ") REFERENCES "
			+ TABLE_FACTIONS_NAME + "(" + FACTIONS_COLUMN_ID + "));";

	private static final String CREATE_MEMBERS_TABLE = "create table "
			+ TABLE_MEMBERS_NAME + " ( " 
			+ MEMBERS_COLUMN_ID + " varchar(-1), "
			+ MEMBERS_COLUMN_RANK + " varchar(-1), " 
			+ MEMBERS_COLUMN_OUTFIT_ID + " Int, " 
			+ MEMBERS_COLUMN_ONLINE_STATUS + " varchar(-1), "
			+ MEMBERS_COLUMN_NAME + " varchar(-1), " 
			+ CACHE_COLUMN_SAVES + " Int, " 
			+ "PRIMARY KEY ("
			+ MEMBERS_COLUMN_ID + "));";

	private static final String CREATE_OUTFITS_TABLE = "create table "
			+ TABLE_OUTFITS_NAME + " ( " + OUTFIT_COLUMN_ID + " varchar(-1), "
			+ OUTFIT_COLUMN_NAME + " varchar(-1), " + OUTFIT_COLUMN_ALIAS
			+ " varchar(-1), " + OUTFIT_COLUMN_LEADER_CHARACTER_ID
			+ " varchar(-1), " + OUTFIT_COLUMN_MEMBER_COUNT + " Int, "
			+ OUTFIT_COLUMN_TIME_CREATED + " Int, " 
			+ OUTFIT_COLUMN_WORDL_ID + " Int, " 
			+ OUTFIT_COLUMN_FACTION_ID + " varchar(-1), "
			+ CACHE_COLUMN_SAVES + " Int, " 
			+ "FOREIGN KEY(" + OUTFIT_COLUMN_FACTION_ID + ") REFERENCES "
			+ TABLE_FACTIONS_NAME + "(" + FACTIONS_COLUMN_ID + "), "
			+ "FOREIGN KEY(" + OUTFIT_COLUMN_WORDL_ID + ") REFERENCES "
			+ TABLE_WORLDS_NAME + "(" + WORLDS_COLUMN_ID + "), "
			+ "PRIMARY KEY (" + OUTFIT_COLUMN_ID + "));";

	/**
	 * @param context
	 *            reference to the activity that is accesing the database.
	 */
	public SQLiteManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_FACTIONS_TABLE);
		db.execSQL(CREATE_WORLDS_TABLE);
		db.execSQL(CREATE_OUTFITS_TABLE);
		db.execSQL(CREATE_MEMBERS_TABLE);
		db.execSQL(CREATE_CHARACTERS_TABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTFITS_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORLDS_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTIONS_NAME);
		onCreate(db);
	}
}