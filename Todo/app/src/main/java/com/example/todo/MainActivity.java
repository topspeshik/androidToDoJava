package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;
    private MainViewModel viewModel;
//    private Handler handler = new Handler(Looper.getMainLooper()); // Держит ссылку на главный поток


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainAc", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);// Provider нужен, чтобы модель сохранялась при перезапуске активити(переворот экрана),
                                                                                // если полностью уходим с экрана, то уничтожается
        viewModel.getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                Toast.makeText(
                        MainActivity.this,
                        String.valueOf(count),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
//        noteDataBase =  NoteDataBase.getInstance(getApplication());



        notesAdapter = new NotesAdapter();
        notesAdapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                  viewModel.showCount();
//                dataBase.remove(note.getId());
//                showNotes();
            }
        });
        recyclerViewNotes.setAdapter(notesAdapter);

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);// Реагирует на все изменения в бд, сюда прилетает автоматом это обновление
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notesAdapter.getNotes().get(position);
                viewModel.remove(note);
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        noteDataBase.notesDao().remove(note.getId());
////                        handler.post(new Runnable() {
////                            @Override
////                            public void run() {
////                                showNotes();
////                            }
////                        });
//                    }
//                });
//                thread.start();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);



        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        viewModel.refreshList();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showNotes();
//    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);


    }

//    private void showNotes() {
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Note> notes = noteDataBase.notesDao().getNotes();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        notesAdapter.setNotes(notes);
//                    }
//                });
//            }
//        });
//        thread.start();
//    }
}


