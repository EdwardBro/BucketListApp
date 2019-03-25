package com.example.bucketlistapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Dream.class}, version = 1, exportSchema = false)
public abstract class DreamRoomDatabase extends RoomDatabase {
	private final static String NAME_DATABASE = "dream_database";

	public abstract DreamDao dreamDao();

	private static volatile DreamRoomDatabase INSTANCE;

	public static DreamRoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (DreamRoomDatabase.class) {
				if (INSTANCE == null) {
					// Create database here
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							DreamRoomDatabase.class, NAME_DATABASE)
							.build();
				}
			}
		}
		return INSTANCE;
	}

}
