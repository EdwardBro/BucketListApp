package com.example.bucketlistapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DreamDao {


	@Query("SELECT * FROM dream_table")
	List<Dream> getallDreams();

	@Insert
	void insertDream(Dream dream);

	@Delete
	void deleteDream(Dream dream);

	@Update
	void updateDream(Dream dream);


}

