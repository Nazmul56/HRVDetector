package com.example.android.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.android.utils.Lg;

import java.util.List;

/**
 * Created by Nazmul Haque on 4/18/18.
 */

public class DatabaseManager {
    public static DatabaseManager databaseManager;
    private static AppDatabase mDb;
    private static String TAG = DatabaseManager.class.getSimpleName();

    public DatabaseManager(Context context) {
        mDb = AppDatabase.getDatabase(context);
    }

    public static void init(Context context) {
        databaseManager = new DatabaseManager(context);
    }

    public static DatabaseManager getInstance(Context context) {
        //throws Exception
        // throw new Exception();
        if (databaseManager == null) {
            init(context);
        }
        return databaseManager;
    }

    public void clearAllTables() {
        mDb.clearAllTables();
    }

    /**
     * CallHistories Table
     */
    public long addBgTestData(BgTestData bgTestData) {

        long flag = mDb.bgTestDataDao().insertBgTest(bgTestData);
        Lg.d(TAG, "CallHistory Insert in Database " + flag);
        return flag;
    }

    public LiveData<List<BgTestData>> getBgTestLiveList() {
        return mDb.bgTestDataDao().loadAllMessage();
    }

}