package com.example.zyra.ROOM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface EventDao {

    @Upsert
    void insertEvent(EventRoom event);

    @Query("SELECT * FROM events")
    LiveData<List<EventRoom>> getAllEvents();
}
