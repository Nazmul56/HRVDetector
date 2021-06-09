package com.example.android.db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BgTestDataDao_Impl implements BgTestDataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BgTestData> __insertionAdapterOfBgTestData;

  private final EntityInsertionAdapter<BgTestData> __insertionAdapterOfBgTestData_1;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BgTestDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBgTestData = new EntityInsertionAdapter<BgTestData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `BgTestData` (`id`,`bgLevel`,`bgUnit`,`time`,`whenMeasure`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BgTestData value) {
        stmt.bindLong(1, value.getId());
        stmt.bindDouble(2, value.getBgLevel());
        if (value.getBgUnit() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getBgUnit());
        }
        stmt.bindLong(4, value.getTime());
        if (value.getWhenMeasure() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getWhenMeasure());
        }
      }
    };
    this.__insertionAdapterOfBgTestData_1 = new EntityInsertionAdapter<BgTestData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `BgTestData` (`id`,`bgLevel`,`bgUnit`,`time`,`whenMeasure`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BgTestData value) {
        stmt.bindLong(1, value.getId());
        stmt.bindDouble(2, value.getBgLevel());
        if (value.getBgUnit() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getBgUnit());
        }
        stmt.bindLong(4, value.getTime());
        if (value.getWhenMeasure() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getWhenMeasure());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from BgTestData";
        return _query;
      }
    };
  }

  @Override
  public Long insertBgLevelData(final BgTestData message) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfBgTestData.insertAndReturnId(message);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Long[] insertChatList(final List<BgTestData> chatMessageList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      Long[] _result = __insertionAdapterOfBgTestData.insertAndReturnIdsArrayBox(chatMessageList);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertBgTest(final BgTestData chatMessage) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfBgTestData_1.insertAndReturnId(chatMessage);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<BgTestData>> loadAllMessage() {
    final String _sql = "SELECT * FROM BgTestData ORDER BY time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"BgTestData"}, false, new Callable<List<BgTestData>>() {
      @Override
      public List<BgTestData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBgLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "bgLevel");
          final int _cursorIndexOfBgUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "bgUnit");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfWhenMeasure = CursorUtil.getColumnIndexOrThrow(_cursor, "whenMeasure");
          final List<BgTestData> _result = new ArrayList<BgTestData>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BgTestData _item;
            final float _tmpBgLevel;
            _tmpBgLevel = _cursor.getFloat(_cursorIndexOfBgLevel);
            final String _tmpBgUnit;
            _tmpBgUnit = _cursor.getString(_cursorIndexOfBgUnit);
            final long _tmpTime;
            _tmpTime = _cursor.getLong(_cursorIndexOfTime);
            final String _tmpWhenMeasure;
            _tmpWhenMeasure = _cursor.getString(_cursorIndexOfWhenMeasure);
            _item = new BgTestData(_tmpBgLevel,_tmpBgUnit,_tmpTime,_tmpWhenMeasure);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
