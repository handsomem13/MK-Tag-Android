package com.moko.bxp.sethala.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SethalaBleTool.db";
    private static final int DATABASE_VERSION = 5;

    public static final String COLUMN_ID = "Id";

    // --------------- Settings Table Setup ----------------
    public static final String TABLE_BEACONINFORMATION = "BeaconInformation";
    public static final String BEACONINFO_COLUMN_ASSETID= "AssetId";
    public static final String BEACONINFO_COLUMN_ASSETNAME= "AssetName";
    public static final String BEACONINFO_COLUMN_MACADDRESS= "MacAddress";
    public static final String BEACONINFO_COLUMN_RSSI= "Rssi";
    public static final String BEACONINFO_COLUMN_THINGID= "ThingId";
    public static final String BEACONINFO_COLUMN_THINGNAME= "ThingName";
    public static final String BEACONINFO_COLUMN_SOFTWAREVERSION= "SoftwareVersion";
    public static final String BEACONINFO_COLUMN_SETTINGSVERSION= "SettingsVersion";
    public static final String BEACONINFO_COLUMN_DATETIMESTAMP= "DateTimeStamp";
    public static final String BEACONINFO_COLUMN_LASTUPDATEDTIMESTAMP= "LastUpdatedDateTimeStamp";
    public static final String BEACONINFO_COLUMN_ISUPTODATE= "IsUpToDate";
    public static final String BEACONINFO_COLUMN_PASSWORD= "Password";
    public static final String BEACONINFO_COLUMN_FIRMWAREURL= "FirmwareUrl";
    public static final String BEACONINFO_COLUMN_REQUESTFIRMWARE= "RequestFirmware";
    public static final String BEACONINFO_COLUMN_RESET= "Reset";
    private String BeaconInformationTable = "Create table " + TABLE_BEACONINFORMATION + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + BEACONINFO_COLUMN_ASSETID + " text, "
            + BEACONINFO_COLUMN_THINGNAME + " text, "
            + BEACONINFO_COLUMN_ASSETNAME + " text, "
            + BEACONINFO_COLUMN_MACADDRESS + " text, "
            + BEACONINFO_COLUMN_RSSI + " text, "
            + BEACONINFO_COLUMN_THINGID + " text, "
            + BEACONINFO_COLUMN_SOFTWAREVERSION + " text, "
            + BEACONINFO_COLUMN_SETTINGSVERSION + " text, "
            + BEACONINFO_COLUMN_DATETIMESTAMP + " text, "
            + BEACONINFO_COLUMN_ISUPTODATE + " text, "
            + BEACONINFO_COLUMN_PASSWORD + " text, "
            + BEACONINFO_COLUMN_FIRMWAREURL + " text, "
            + BEACONINFO_COLUMN_REQUESTFIRMWARE + " text, "
            + BEACONINFO_COLUMN_RESET + " text, "
            + BEACONINFO_COLUMN_LASTUPDATEDTIMESTAMP + " text "
            + ")";

    //-----------------------------------------------------


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(BeaconInformationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqliteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONINFORMATION);

        onCreate(db);
    }

}
