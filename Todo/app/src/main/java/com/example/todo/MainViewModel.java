package com.example.todo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private NoteDataBase noteDataBase;
    private int count =0;
    private MutableLiveData<Integer> countLD = new MutableLiveData<>(); // Можем сами устанавливать значение
//    private MutableLiveData<List<Note>> notes = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
        noteDataBase = NoteDataBase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes() {
        return noteDataBase.notesDao().getNotes();
    }

//    public void refreshList(){
//        Disposable disposable = noteDataBase.notesDao().getNotes()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Note>>() {
//                    @Override
//                    public void accept(List<Note> notesFromDb) throws Throwable {
//                        notes.setValue(notesFromDb);//Когда данные загрузятся, то мы сможем установить их в LiveDate
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Throwable {
//                        Log.d("MainViewModel", "ErrorRefreshList")
//                    }
//                });
//        compositeDisposable.add(disposable);
//
//    }

    public void showCount(){
        count++;
        countLD.setValue(count);
    }

    public LiveData<Integer> getCount(){
        return countLD;
    }

    public void remove(Note note) {
        Disposable disposable= noteDataBase.notesDao().remove(note.getId())
                .subscribeOn(Schedulers.io()) // io - фоновый поток
                .observeOn(AndroidSchedulers.mainThread())// все операторы указанные ниже, будут исполняться в основном потоке
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable { // Будет выполнено, когда завершится удаление заметки
//                        refreshList();
                    }
                });
        compositeDisposable.add(disposable);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                noteDataBase.notesDao().remove(note.getId());
//            }
//        });
//        thread.start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();

    }
}
