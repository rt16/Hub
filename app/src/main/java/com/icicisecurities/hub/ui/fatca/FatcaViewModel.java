package com.icicisecurities.hub.ui.fatca;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FatcaViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public FatcaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fatca fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
