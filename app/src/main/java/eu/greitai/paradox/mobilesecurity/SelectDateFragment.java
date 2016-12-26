package eu.greitai.paradox.mobilesecurity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

import eu.greitai.paradox.mobilesecurity.helpers.DateUtils;

public class SelectDateFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnDateSelectedListener callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnDateSelectedListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = DateUtils.getStarOfDayInUtc();
        cal.setTimeInMillis(getArguments().getLong("date"));

        return new DatePickerDialog(getActivity(), this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar cal = DateUtils.getStarOfDayInUtc();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        callback.onDateSelected(cal.getTimeInMillis());
    }
}
