package com.divakrishnam.quizroom2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.divakrishnam.quizroom2.entity.Mahasiswa;

import java.util.List;

@Dao
public interface MahasiswaDao {

    @Query("SELECT * FROM mahasiswa WHERE username = :username AND password = :password LIMIT 1")
    Mahasiswa login(String username, String password);

    @Query("SELECT * FROM mahasiswa")
    List<Mahasiswa> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Mahasiswa mahasiswa);

    @Update
    int update(Mahasiswa mahasiswa);

    @Delete
    int delete(Mahasiswa mahasiswa);
}
