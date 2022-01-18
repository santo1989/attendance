package com.santosystemsandsolutions.attendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class MyCalender extends DialogFragment {
    Calendar calendar = Calendar.getInstance();
//    private int year = Calendar.YEAR;
//    private int month = Calendar.MONTH;
//    private int day = Calendar.DAY_OF_MONTH;

    public interface OnCalenderOkClickLisener{
        void onClick(int year,int month,int day);
    }

    public OnCalenderOkClickLisener onCalenderOkClickLisener;

    public void setOnCalenderOkClickLisener(OnCalenderOkClickLisener onCalenderOkClickLisener){
        this.onCalenderOkClickLisener = onCalenderOkClickLisener;
    }
    @NonNull

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return new DatePickerDialog(getActivity().((view, year, month, dayOfMonth)-> {},year,month,day));
        return new DatePickerDialog(getActivity(),((view, year, month, dayOfMonth) ->{
            onCalenderOkClickLisener.onClick(year,month,dayOfMonth);
        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));//year,month,day
    }

    void setDate(int year, int month, int day){
calendar.set(Calendar.YEAR,year);
calendar.set(Calendar.MONTH,month);
calendar.set(Calendar.DAY_OF_MONTH,day);
    }

    String getDate(){
        return DateFormat.format("dd.MM.yyyy",calendar).toString();

    }
}
