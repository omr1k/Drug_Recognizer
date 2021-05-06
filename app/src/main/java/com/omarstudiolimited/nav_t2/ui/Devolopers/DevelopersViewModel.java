package com.omarstudiolimited.nav_t2.ui.Devolopers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DevelopersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DevelopersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Devolopers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}