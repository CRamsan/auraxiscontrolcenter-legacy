package com.cesarandres.ps2link.soe.util;

/**
 * Created by cesar on 6/16/13.
 *
 * Some of the collections(especially from PS2) are very big. This file will contaim them
 * to prevent other files to be too big with data that is read-only.
 */
public class Collections {
    public static enum EQ2Collection {
        ACHIEVEMENT("achievement"),
        ALTERNATEADVANCEMENT("alternateadvancement"),
        CHARACTER("character"),
        CHARACTERSTAT("character.stat"),
        CHARACTERSTATWORLD("character.stat.world"),
        CONSTANTS("constants"),
        FACTION("faction"),
        GUILD("guild"),
        ITEM("item"),
        SPELL("spell"),
        WORLD("world");

        private final String collection;

        private EQ2Collection(final String collection) {
            this.collection = collection;
        }

        @Override
        public String toString() {
            return this.collection;
        }
    }
    
    public static enum PS2Collection {
        AMERISHMAP("amerishmap"),
        CHARACTER("character"),
        CHARACTERS_CURRENCY("characters_currency"),
        CHARACTERS_EVENT("characters_event"),
        CHARACTERS_EVENT_GROUPED("characters_event_grouped"),
        CHARACTERS_FRIEND("characters_friend"),
        CHARACTERS_ITEM("characters_item"),
        CHARACTERS_LEADERBOARD("characters_leaderboard"),
        CHARACTERS_ONLINE_STATUS("characters_online_status"),
        CHARACTERS_STAT("characters_stat"),
        CHARACTERS_STAT_BY_FACTION("characters_stat_by_faction"),
        CHARACTERS_STAT_HISTORY("characters_stat_history"),
        CHARACTERS_WEAPON_STAT("characters_weapon_stat"),
        CHARACTERS_WEAPON_STAT_BY_FACTION("characters_weapon_stat_by_faction"),
        CHARACTERS_WORLD("characters_world"),
        CHARACTER_NAME("character_name"),
        CURRENCY("currency"),
        ESAMIRMAP("esamirmap"),
        EVENT("event"),
        FACTION("faction"),
        ICONATTACHMENT("icon.attachment"),
        INDARMAP("indarmap"),
        ITEM("item"),
        LEADERBOARD("leaderboard"),
        MAP("map"),
        OUTFIT("outfit"),
        OUTFIT_MEMBER("outfit_member"),
        OUTFIT_MEMBER_EXTENDED("outfit_member_extended"),
        OUTFIT_RANK("outfit_rank"),
        PROFILE("profile"),
        PROFILETYPE("profile.type"),
        RANK("rank"),
        RESOURCE("resource"),
        SINGLE_CHARACTER_BY_ID("single_character_by_id"),
        STAT_INFO("stat_info"),
        VEHICLE("vehicle"),
        WORLD("world"),
        WORLD_EVENT("world_event"),
        ZONE("zone");

        private final String collection;

        private PS2Collection (final String collection) {
            this.collection = collection;
        }

        @Override
        public String toString() {
            return this.collection;
        }
    }
}
