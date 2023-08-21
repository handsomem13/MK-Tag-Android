package com.moko.bxp.sethala.sqlite;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.moko.bxp.sethala.database.BeaconInformationModel;
import com.moko.bxp.sethala.database.SqliteHelper;

import java.util.ArrayList;

public class BeaconDatabaseHelper implements  ISqlite{
    SQLiteDatabase database;
    SqliteHelper dbHelper;
    public BeaconDatabaseHelper(Context context)
    {
        dbHelper = new SqliteHelper(context);
        dbHelper.getWritableDatabase();
    }
    @Override
    public void CreateDatabaseConnection() {
        database = dbHelper.getWritableDatabase();
    }
    @Override
    public void CloseDatabaseConnection() {

    }

    @Override
    public <T> T GetById(String id, Class<T> type) {
        return null;
    }

    @Override
    @SuppressLint("Range")
    public <T> T GetById(int id, Class<T> type) {
        CreateDatabaseConnection();
        BeaconInformationModel _model =  new BeaconInformationModel();
        String selectQuery = "SELECT * FROM " + SqliteHelper.TABLE_BEACONINFORMATION + " WHERE " + SqliteHelper.COLUMN_ID + " = " + id;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                _model.setAssetId(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ASSETID)));
                _model.setAssetName(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ASSETNAME)));
                _model.setSettingsVersion(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_SETTINGSVERSION)));
                _model.setMacAddress(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_MACADDRESS)));
                _model.setRssi(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_RSSI)));
                _model.setThingId(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_THINGID)));
                _model.setThingName(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_THINGNAME)));
                _model.setSoftwareVersion(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_SOFTWAREVERSION)));
                _model.setDateTimeStamp(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_DATETIMESTAMP)));
                _model.setLastUpdatedDateTimeStamp(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_LASTUPDATEDTIMESTAMP)));
                _model.setIsUpToDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ISUPTODATE)));
                _model.setPassword(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_PASSWORD)));
                _model.setFirmwareUrl(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_FIRMWAREURL)));
            } while (cursor.moveToNext());
        }
        return type.cast(_model);
    }
    @SuppressLint("Range")
    public BeaconInformationModel GetByMacAdrress(String mac) {
        CreateDatabaseConnection();
        BeaconInformationModel _model =  new BeaconInformationModel();
        String selectQuery = "SELECT * FROM " + SqliteHelper.TABLE_BEACONINFORMATION + " WHERE " + SqliteHelper.BEACONINFO_COLUMN_MACADDRESS + " = ? " ;
        Cursor cursor = database.rawQuery(selectQuery, new String[] { mac });
        if (cursor.moveToFirst()) {
            do {
                _model.setAssetId(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ASSETID)));
                _model.setAssetName(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ASSETNAME)));
                _model.setSettingsVersion(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_SETTINGSVERSION)));
                _model.setMacAddress(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_MACADDRESS)));
                _model.setRssi(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_RSSI)));
                _model.setThingId(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_THINGID)));
                _model.setThingName(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_THINGNAME)));
                _model.setSoftwareVersion(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_SOFTWAREVERSION)));
                _model.setDateTimeStamp(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_DATETIMESTAMP)));
                _model.setLastUpdatedDateTimeStamp(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_LASTUPDATEDTIMESTAMP)));
                _model.setIsUpToDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ISUPTODATE)));
                _model.setPassword(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_PASSWORD)));
                _model.setFirmwareUrl(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_FIRMWAREURL)));
            } while (cursor.moveToNext());
        }
        return _model;
    }
    @SuppressLint("Range")
    @Override
    public ArrayList GetAll() {
       try {
           CreateDatabaseConnection();
           String selectQuery = "SELECT * FROM " + SqliteHelper.TABLE_BEACONINFORMATION;
           ArrayList<BeaconInformationModel> data =  new ArrayList<BeaconInformationModel>();
           Cursor cursor = database.rawQuery(selectQuery, new String[]{});
           if (cursor != null && cursor.moveToFirst()) {
               do {
                   BeaconInformationModel _model = new BeaconInformationModel();
                   _model.setAssetId(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ASSETID)));
                   _model.setAssetName(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ASSETNAME)));
                   _model.setSettingsVersion(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_SETTINGSVERSION)));
                   _model.setMacAddress(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_MACADDRESS)));
                   _model.setRssi(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_RSSI)));
                   _model.setThingId(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_THINGID)));
                   _model.setSoftwareVersion(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_SOFTWAREVERSION)));
                   _model.setDateTimeStamp(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_DATETIMESTAMP)));
                   _model.setLastUpdatedDateTimeStamp(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_LASTUPDATEDTIMESTAMP)));
                   _model.setIsUpToDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_ISUPTODATE)));
                   _model.setPassword(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_PASSWORD)));
                   _model.setFirmwareUrl(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_FIRMWAREURL)));
                   _model.setThingName(cursor.getString(cursor.getColumnIndex(SqliteHelper.BEACONINFO_COLUMN_THINGNAME)));
                   data.add(_model);
               } while (cursor.moveToNext());
           }
           return  data;
       }catch (Exception ex){
           Log.e("GetAll", ex.toString());
           return  null;
       }
    }

    @Override
    public <T> long Add(T model) {
        try {
            CreateDatabaseConnection();
            BeaconInformationModel _model = (BeaconInformationModel) model;
            ContentValues values = new ContentValues();
            values.put(SqliteHelper.BEACONINFO_COLUMN_ASSETID,_model.getAssetId());
            values.put(SqliteHelper.BEACONINFO_COLUMN_ASSETNAME,_model.getAssetName());
            values.put(SqliteHelper.BEACONINFO_COLUMN_SETTINGSVERSION,_model.getSettingsVersion());
            values.put(SqliteHelper.BEACONINFO_COLUMN_MACADDRESS,_model.getMacAddress());
            values.put(SqliteHelper.BEACONINFO_COLUMN_RSSI,_model.getRssi());
            values.put(SqliteHelper.BEACONINFO_COLUMN_THINGID,_model.getThingId());
            values.put(SqliteHelper.BEACONINFO_COLUMN_SOFTWAREVERSION,_model.getSoftwareVersion());
            values.put(SqliteHelper.BEACONINFO_COLUMN_DATETIMESTAMP,_model.getDateTimeStamp());
            values.put(SqliteHelper.BEACONINFO_COLUMN_LASTUPDATEDTIMESTAMP,_model.getLastUpdatedDateTimeStamp());
            values.put(SqliteHelper.BEACONINFO_COLUMN_ISUPTODATE,_model.getIsUpToDate());
            values.put(SqliteHelper.BEACONINFO_COLUMN_PASSWORD,_model.getPassword());
            values.put(SqliteHelper.BEACONINFO_COLUMN_FIRMWAREURL,_model.getFirmwareUrl());
            values.put(SqliteHelper.BEACONINFO_COLUMN_THINGNAME,_model.getThingName());
            long rowId = database.insert(SqliteHelper.TABLE_BEACONINFORMATION, null, values);
            CloseDatabaseConnection();
            Log.i("Add", rowId+" Created successfully");
            return rowId;
        }catch (Exception ex){
            Log.e("Add", ex.toString());
            throw ex;
        }
    }
    public void insertMany(List<BeaconInformationModel> informationModelList) {
            try {
                for (BeaconInformationModel beaconInformationModel : informationModelList) {
                  this.Add(beaconInformationModel);
                }
                Log.i("BeaconInformation","Saving data complete ");
        } catch (Exception e) {
            // Handle the exception
                Log.e("BeaconInformation",e.toString());
        }
    }
    @Override
    public <T> void Update(T model) {

    }

    @Override
    public <T> void Delete(String model) {
        try {
            int result = database.delete(SqliteHelper.TABLE_BEACONINFORMATION, SqliteHelper.BEACONINFO_COLUMN_MACADDRESS, new String[]{});
        } catch (Exception e) {
            // Handle the exception
            Log.e("BeaconInformation",e.toString());
        }
    }
    public void deleteAllRecords() {
        try {
            CreateDatabaseConnection();
            int result = database.delete(SqliteHelper.TABLE_BEACONINFORMATION, "", new String[]{});
        } catch (Exception e) {
            // Handle the exception
            Log.e("BeaconInformation",e.toString());
        }
    }
    @Override
    public int GetCount() {
        return 0;
    }
}
