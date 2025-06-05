package com.example.zyra.ROOM;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EventRoom.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {

    public abstract EventDao eventDao() ;

    private static volatile EventDatabase INSTANCE;

    public static synchronized EventDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventDatabase.class, "event_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}


