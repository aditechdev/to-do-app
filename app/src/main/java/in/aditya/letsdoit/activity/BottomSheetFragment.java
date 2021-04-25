package in.aditya.letsdoit.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        sYear = t.year;
        sMonth = t.month+1;
        sDay = t.monthDay;
        sHour = t.hour;
        sMin = t.minute;

       // String date1 = t.format("%d/%m/%Y");
        //dd_mm_yy.setText(date1);



        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        String var = timeFormat.format(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String date1 = dateFormat.format(date);
        dd_mm_yy.setText(date1);
        hh_mm_am_pm.setText(var);

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

//            if (task.length() > 0) {
//                mSaveButton.setEnabled(false);
//            }

        }



        // *****************************************************************************************
        // **********                         TO CHECK TEXT CHANGE                ******************
        // *****************************************************************************************


//        mEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @SuppressLint("UseCompatLoadingForDrawables")
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().equals("")) {
//                    mSaveButton.setEnabled(false);
//                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.background));
//                    mSaveButton.setBackground(getResources().getDrawable(R.drawable.btn_inactive));
//                } else {
//                    mSaveButton.setEnabled(true);
//                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                    mSaveButton.setBackground(getResources().getDrawable(R.drawable.btn_active));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(v -> {

            if(mEditText.getText() == null || mEditText.getText().length() < 1){
                Toast.makeText(v.getContext(), "Please give task Name", Toast.LENGTH_SHORT).show();
            }
            else {
                String text = Objects.requireNonNull(mEditText.getText()).toString();
                String date = (dd_mm_yy.getText()).toString();
                String time = (hh_mm_am_pm.getText()).toString();
                int alarm = alarmStatus;

                //   TO UPDATE THE TASK

                LocalDateTime updateDateTime = LocalDateTime.of(sYear, sMonth, sDay, sHour, sMin);
                String dateTime = String.valueOf(updateDateTime);
//
                if (finalIsUpdate) {

//                    if (alarm == 1) {
//                        Intent intent = new Intent(getContext(), ReminderBroadcast.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//
//                        String timeAtCheck = String.valueOf(updateDateTime);
//                        Log.i(TAG, "Time at check update:" +timeAtCheck);
////                        String timeAtCheck = String.valueOf(updateDateTime);
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//
//                        try {
//                            Date mDate = simpleDateFormat.parse(timeAtCheck);
//                            long timeInMilliSeconds = mDate.getTime();
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSeconds, pendingIntent);
//
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    myDb.updateTask(bundle.getInt("id"), text);
                    myDb.updateDate(bundle.getInt("id"), date);
                    myDb.updateTime(bundle.getInt("id"), time);
                    myDb.updateDateTime(bundle.getInt("id"), dateTime);
                    myDb.updateAlarm(bundle.getInt("id"), alarm);
                } else {
//
//                    From the save button I am doing two things create and save
//                     I want notification from both the way
//                    And I am getting notification only from activity
//                     not
//                    if (alarm == 1) {
//                        Intent intent = new Intent(getContext(), ReminderBroadcast.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//
//                        String timeAtCheck = String.valueOf(updateDateTime);
////                        String timeAtCheck = dd_mm_yy.getText().toString() +"T"+ hh_mm_am_pm.getText().toString();
//                        Log.i(TAG, "Time at check create: " +timeAtCheck);
////                        String timeAtCheck = String.valueOf(updateDateTime);
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//                        try {
//                            Date mDate = simpleDateFormat.parse(timeAtCheck);
//                            long timeInMilliSeconds = mDate.getTime();
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSeconds, pendingIntent);
//
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    // To create new row in columns

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
            }
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

            final Calendar mCalender = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mCalender.set(Calendar.MONTH, month);
            mCalender.set(Calendar.YEAR, year);
            dd_mm_yy.setText(simpleDateFormat.format(mCalender.getTime()));



//            dd_mm_yy.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

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
            Log.i("TAG", "HOURS: "+ hour_of_day + " SECOND: "+ minute);
             final Calendar mCalendar = Calendar.getInstance();
             final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            mCalendar.set(Calendar.HOUR_OF_DAY, hour_of_day);
            mCalendar.set(Calendar.MINUTE, minute);

            hh_mm_am_pm.setText(mFormat.format(mCalendar.getTime()));

            sHour = hour_of_day;
            sMin = minute;

        }
    }

}
