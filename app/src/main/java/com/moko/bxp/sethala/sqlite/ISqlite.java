package com.moko.bxp.sethala.sqlite;

import android.annotation.SuppressLint;

import java.util.ArrayList;

public interface ISqlite {
    void CreateDatabaseConnection();

    void CloseDatabaseConnection();

    public <T> T GetById(String id, Class<T> type);

    @SuppressLint("Range")
    <T> T GetById(int id, Class<T> type);

    public <T> ArrayList<T> GetAll();

    public <T> long Add(T model);

    public <T> void Update(T model);

    public <T> void Delete(String model);

    public int GetCount();
}
