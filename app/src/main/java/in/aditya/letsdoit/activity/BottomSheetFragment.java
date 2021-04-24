package in.aditya.letsdoit.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

import in.aditya.letsdoit.OnDialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class BottomSheetFragment extends BottomSheetDialogFragment {


    //    -----------------------------------------------------TAG DECLARATION    ------------------------------------------------------------ //

    public static final String TAG = "BottomSheetFragment";

    //    ---------------------------------------------------VARIABLE DECLARATION   ------------------------------------------------------------ //
    @SuppressLint("StaticFieldLeak")
    private static TextView dd_mm_yy;
    @SuppressLint("StaticFieldLeak")
    private static TextView hh_mm_am_pm;

    // variable to sort date and time
    private static int sYear, sMonth, sDay, sHour, sMin;
    int alarmStatus;
    private TextInputEditText mEditText;
    private Button mSaveButton;
    private DataBaseHandler myDb;
    private ImageButton ibSelectCalender;
    private ImageButton ibSelectClock;
    private CheckBox checkBox;

    //      CONSTRUCTOR
    public static BottomSheetFragment newInstance() {
        return new BottomSheetFragment();
    }

    //    --------------------------------------------------- On Create View   ------------------------------------------------------------ //


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        ibSelectCalender = v.findViewById(R.id.imageButtonCalender);
        ibSelectClock = v.findViewById(R.id.imageButtonTime);
        dd_mm_yy = v.findViewById(R.id.tvCalender);
        hh_mm_am_pm = v.findViewById(R.id.tvTime);
        mEditText = v.findViewById(R.id.et_new_task);
        mSaveButton = v.findViewById(R.id.btn_new_task);
        myDb = new DataBaseHandler(getActivity());
        checkBox = v.findViewById(R.id.notification_checkbox);
        return v;
    }

    //    ------------------------------------------------- ON VIEW CREATED METHOD     ------------------------------------------------------- //

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);

        // For Date Picker
        ibSelectCalender.setOnClickListener(view1 -> {
            DialogFragment newFragment = new DatePickerFragment();
            assert getFragmentManager() != null;
            newFragment.show(getFragmentManager(), "datePicker");
        });

        // FOr time Picker

        ibSelectClock.setOnClickListener(v -> {
            DialogFragment timeFragment = new TimePickerFragment();
            assert getFragmentManager() != null;
            timeFragment.show(getFragmentManager(), "timePicker");
        });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                alarmStatus = 1;
            } else {
                alarmStatus = 0;
            }
        });

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            String date = bundle.getString("date");
            String time = bundle.getString("time");
            int alarm = bundle.getInt("alarm");


            mEditText.setText(task);
            dd_mm_yy.setText(date);
            hh_mm_am_pm.setText(time);
            checkBox.setChecked(alarm != 0);


            //      DISABLE BUTTON

            if (task.length() > 0) {
                mSaveButton.setEnabled(false);
            }

        }



        // *****************************************************************************************
        // **********                         TO CHECK TEXT CHANGE                ******************
        // *****************************************************************************************


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.background));
                    mSaveButton.setBackground(getResources().getDrawable(R.drawable.btn_inactive));
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    mSaveButton.setBackground(getResources().getDrawable(R.drawable.btn_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(v -> {

            String text = Objects.requireNonNull(mEditText.getText()).toString();
            String date = (dd_mm_yy.getText()).toString();
            String time = (hh_mm_am_pm.getText()).toString();
            int alarm = alarmStatus;

            //   TO UPDATE THE TASK

            LocalDateTime updateDateTime = LocalDateTime.of(sYear, sMonth, sDay, sHour, sMin);
            String dateTime = String.valueOf(updateDateTime);
            if (finalIsUpdate) {

                myDb.updateTask(bundle.getInt("id"), text);
                myDb.updateDate(bundle.getInt("id"), date);
                myDb.updateTime(bundle.getInt("id"), time);
                myDb.updateDateTime(bundle.getInt("id"), dateTime);
                myDb.updateAlarm(bundle.getInt("id"), alarm);
            } else {

//                To create new row in colums

                RVModel item = new RVModel();
                item.setTask(text);
                item.setStatus(0);
                item.setDate(date);
                item.setTime(time);
                item.setAlarm(alarm);
                item.setDatetime(dateTime);

                myDb.insertTask(item);
            }
            dismiss();
        });
    }

    //    ------------------------------------------------ ON DISMISS ACTIVITY   ------------------------------------------ //


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();

        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        super.onCreate(savedInstanceState);
    }


    // ---------------------------------- Date Here-----------------------------------------------------------//

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


            dd_mm_yy.setText(dayOfMonth + " / " + (month + 1) + " / " + year);

            sYear = year;
            sMonth = month + 1;
            sDay = dayOfMonth;


        }
    }

    //    =============================================== Time ======================================================//

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour_of_day = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);


            return new TimePickerDialog(getActivity(), this, hour_of_day, min, DateFormat.is24HourFormat(getActivity()));

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTimeSet(TimePicker view, int hour_of_day, int minute) {
            hh_mm_am_pm.setText(hour_of_day + ":" + minute);
            sHour = hour_of_day;
            sMin = minute;

        }
    }

}
