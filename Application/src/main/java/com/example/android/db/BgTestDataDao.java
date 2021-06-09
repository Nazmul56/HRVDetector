/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BgTestDataDao {
    @Query("SELECT * FROM BgTestData ORDER BY time ASC")
    LiveData<List<BgTestData>> loadAllMessage();

    @Insert(onConflict = REPLACE)
    Long insertBgLevelData(BgTestData message);

    @Insert(onConflict = REPLACE)
    Long[] insertChatList(List<BgTestData> chatMessageList);

    @Insert(onConflict = IGNORE)
    long insertBgTest(BgTestData chatMessage);

/*  @Query("UPDATE ChatMessage SET isNotificationClear = 0 WHERE threadId = :threadId")
    void updateUnreadIsClear(int threadId );*/

    //@Query("UPDATE ChatMessage SET unread = :true WHERE threadId = :threadId")
    // void updateUnread(int threadId,);

    /*@Query("DROP TABLE ChatMessage")
    void drop();*/

    @Query("DELETE from BgTestData")
    void deleteAll();
}