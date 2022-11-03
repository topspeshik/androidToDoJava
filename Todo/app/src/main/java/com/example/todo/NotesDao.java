package com.example.todo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")
//    Single<List<Note>> getNotes(); // Отличие от Completable, в том, что он возвращает данные
    LiveData<List<Note>> getNotes();

    @Insert // (onConflict = OnConflictStrategy.REPLACE) // Если вставляем обьект который есть в бд, то старый удалится, новый добавится
    Completable add(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
    Completable remove(int id);
}
