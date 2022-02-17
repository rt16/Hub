package com.icicisecurities.hub.custom;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

public class PANCardEditText extends AppCompatEditText implements TextWatcher {

    private String blockCharacterSet = "~#^|$%&*!.,?/;:\"[]{}-_=+@%()<>abcdefghijklmnopqrstuvwxyz";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }


    };


    public PANCardEditText(Context context) {
        super(context);
        setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});

    }

    public PANCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});
    }

    public PANCardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("PancardEditText :" , "beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Log.d("PancardEditText :" , "onTextChanged" +s.length());

        if (s.length() >= 5 && s.length() < 9){
            setInputType(InputType.TYPE_CLASS_NUMBER);

        }else if (s.length() <= 0){

            setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }else {
            setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }

    }


    @Override
    public void afterTextChanged(Editable s) {

        Log.d("PancardEditText :" , "afterTextChanged");
    }
}

