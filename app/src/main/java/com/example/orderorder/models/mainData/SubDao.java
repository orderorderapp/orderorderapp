package com.example.orderorder.models.mainData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubDao {

    @Insert
    void insert(Sub sub);

    @Update
    void update(Sub sub);

    @Delete
    void delete(Sub sub);

    @Query("DELETE FROM sub_table")
    void deleteAllSubs();

    @Query("SELECT * FROM sub_table ORDER BY price DESC")
    List<Sub> getAllSubs();
}
