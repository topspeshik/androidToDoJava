package com.example.todo;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDataBase extends RoomDatabase {
    private static NoteDataBase instance = null;
    private static final String DB_NAME = "notes.db";

    public static NoteDataBase getInstance(Application application){
        if (instance == null){
            instance = Room.databaseBuilder( // создает наследника и возвращает его экземпляр
                    application,
                    NoteDataBase.class,
                    DB_NAME

            ).build();
        }
        return instance;
    }

    public abstract NotesDao notesDao(); // Room сгенерирует реализацию интерфейса
}
