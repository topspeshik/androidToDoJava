package com.example.todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {

    private NoteDataBase noteDataBase;
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//    private Disposable disposable; // Для предотвращения утечки памяти, отменить подписку

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        noteDataBase = NoteDataBase.getInstance(application);
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    public void saveNote(Note note){
        Disposable disposable = noteDataBase.notesDao().add(note)
                .subscribeOn(Schedulers.io()) // io - фоновый поток
                .observeOn(AndroidSchedulers.mainThread())// все операторы указанные ниже, будут исполняться в основном потоке
                .subscribe(new Action() {
            @Override
            public void run() throws Throwable { // Будет выполнено, когда завершится добавление заметки
                shouldCloseScreen.setValue(true);
            }
        });
        compositeDisposable.add(disposable);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                    noteDataBase.notesDao().add(note);
//                    shouldCloseScreen.postValue(true); // setvalue - из главного потока, post - из любого
//                }
//            });
//        thread.start();

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
//        disposable.dispose(); // уничтожаем подписку при уничтожении модели(закрытии активити)
    }
}

