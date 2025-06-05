package com.example.zyra.ROOM;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class Database {

    private Context context;
    private  static Database instance;

    private  EventDatabase eventDatabase;

    private Database(Context context){
        this.context = context;

        eventDatabase = Room.databaseBuilder(context,EventDatabase.class,"ZyraEventsDatabase")
                .fallbackToDestructiveMigration().build();
    }

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = new Database(context);
        }
        return instance;
    }

    public EventDatabase getEventDatabase(){
        return eventDatabase;
    }
}
