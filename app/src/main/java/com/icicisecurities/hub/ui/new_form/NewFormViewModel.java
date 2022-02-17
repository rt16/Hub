package com.icicisecurities.hub.ui.new_form;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewFormViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewFormViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}