package com.cesarandres.ps2link.module.parse;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.fragments.FragmentSettings;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/**
 * Created by cramsan on 9/18/15.
 */
public class PS2BroadcastReceiver extends ParsePushBroadcastReceiver{

    @Override
    protected Notification getNotification(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int fromTimeHours = prefs.getInt(FragmentSettings.PREF_KEY_START_HOUR, 0);
        int fromTimeMinutes = prefs.getInt(FragmentSettings.PREF_KEY_START_MINUTE, 0);
        int toTimeHours = prefs.getInt(FragmentSettings.PREF_KEY_END_HOUR, 23);
        int toTimeMinutes = prefs.getInt(FragmentSettings.PREF_KEY_END_MINUTE, 59);
        boolean checkEnabled = prefs.getBoolean(FragmentSettings.PREF_KEY_NOTIFICATION_ENABLE, false);

        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);

        if(!checkEnabled || (currentHour < fromTimeHours) || (currentHour > toTimeHours) ||
            (currentHour == fromTimeHours && currentMinute < fromTimeMinutes) ||
            (currentHour == toTimeHours && currentMinute > toTimeMinutes) ){
            return null;
        }

        JSONObject pushData = getPushData(intent);
        if(pushData != null && (pushData.has("alert") || pushData.has("title"))) {
            String title = pushData.optString("title", context.getResources().getString(R.string.app_name));
            String worldId = pushData.optString("world_id", "");
            String eventId = pushData.optString("event_id", "");
            String alert = generateMessage(eventId, worldId);
            String tickerText = String.format(Locale.getDefault(), "%s: %s", new Object[]{title, alert});
            Bundle extras = intent.getExtras();
            Random random = new Random();
            int contentIntentRequestCode = random.nextInt();
            int deleteIntentRequestCode = random.nextInt();
            String packageName = context.getPackageName();
            Intent contentIntent = new Intent("com.parse.push.intent.OPEN");
            contentIntent.putExtras(extras);
            contentIntent.setPackage(packageName);
            Intent deleteIntent = new Intent("com.parse.push.intent.DELETE");
            deleteIntent.putExtras(extras);
            deleteIntent.setPackage(packageName);
            PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode, contentIntent, 134217728);
            PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode, deleteIntent, 134217728);
            NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context);
            parseBuilder.setContentTitle(title).setContentText(alert).setTicker(tickerText).setSmallIcon(this.getSmallIconId(context, intent)).setLargeIcon(this.getContinentIcon(context, intent, eventId)).setContentIntent(pContentIntent).setDeleteIntent(pDeleteIntent).setAutoCancel(true).setDefaults(-1);
            if(alert != null && alert.length() > 38) {
                parseBuilder.setStyle((new NotificationCompat.BigTextStyle()).bigText(alert));
            }

            return parseBuilder.build();
        } else {
            return null;
        }
    }

    private Bitmap getContinentIcon(Context context, Intent intent, String eventId){
        if (eventId == null) {
            return this.getLargeIcon(context, intent);
        } else {
            try{
                int resourceId;
                switch (eventId) {
                    case "1":
                        resourceId = R.drawable.indar;
                        break;
                    case "2":
                        resourceId = R.drawable.esamir;
                        break;
                    case "3":
                        resourceId = R.drawable.amerish;
                        break;
                    case "4":
                        resourceId = R.drawable.hossin;
                        break;
                    default:
                        resourceId = R.drawable.icon_launcher;
                        break;
                }
                return BitmapFactory.decodeResource(context.getResources(), resourceId);
            }catch (Exception e){
                return this.getLargeIcon(context, intent);
            }
        }
    }

    private JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra("com.parse.Data"));
        } catch (JSONException var3) {
            return null;
        }
    }

    private String getServerName(String worldId){
        switch (worldId) {
            case "19":
                return "Jaeger";
            case "25":
                return "Briggs";
            case "1":
                return "Connery";
            case "10":
                return "Miller";
            case "13":
                return "Cobalt";
            case "17":
                return "Emerald";
            case "2000":
                return "Ceres";
            case "2001":
                return "Lithcorp";
            case "1001":
                return "Palos";
            case "1000":
                return "Genudine";
            default:
                return "Auraxis";
        }
    }
    
    private String getAlert(String eventId){
        switch (eventId) {
            case "1":
                return "Capture Indar";
            case "2":
                return "Capture Esamir";
            case "3":
                return "Capture Amerish";
            case "4":
                return "Capture Hossin";
            case "106":
                return "Conquer Auraxis";
            default:
                return "Alert";
        }
    }

    private String generateMessage(String eventId, String worldId){
        return this.getServerName(worldId) + ": " + this.getAlert(eventId);
    }
}
