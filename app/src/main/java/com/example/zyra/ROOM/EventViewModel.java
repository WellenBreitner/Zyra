package com.example.zyra.ROOM;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class EventViewModel extends AndroidViewModel {
        private LiveData<List<EventRoom>> eventList;

        public EventViewModel(Application application) {
            super(application);
            eventList = Database.getInstance(application).getEventDatabase().eventDao().getAllEvents();
        }

        public LiveData<List<EventRoom>> getEventList() {
            return eventList;
        }
}
