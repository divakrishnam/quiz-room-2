package com.divakrishnam.quizroom2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.divakrishnam.quizroom2.dao.MahasiswaDao;
import com.divakrishnam.quizroom2.entity.Mahasiswa;

@Database(entities = {Mahasiswa.class}, version = 1)
public abstract class MahasiswaDatabase extends RoomDatabase {
    public abstract MahasiswaDao mahasiswaDao();
}
