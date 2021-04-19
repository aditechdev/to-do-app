package in.aditya.letsdoit.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

import in.aditya.letsdoit.OnDialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class BottomSheetFragment extends BottomSheetDialogFragment {



    // TODO 1: REMOVE WARNING


    //    -----------------------------------------------------TAG DECLARATION    ------------------------------------------------------------ //

    public static final String TAG = "BottomSheetFragment";

    //    ---------------------------------------------------VARIABLE DECLARATION   ------------------------------------------------------------ //


    private TextInputEditText mEditText;
    private Button mSaveButton;
    private DataBaseHandler myDb;
    private ImageButton ibSelectCalender;
    private ImageButton ibSelectClock;

    // Calender and time

    private static TextView dd_mm_yy;
    private static TextView hh_mm_am_pm;
//    private static String yyyy_mm_dd_hh_mm;
//    private static String yyyy;
//    private static int mm;
//    private static int dd;
//    private static int hh;
//    private static int min;
//    private static String day;
//    private static String day_mm_date_year_hh_mm;
//    private static String am_pm;







    //      CONSTRUCTOR


    public static BottomSheetFragment newInstance() {
        return new BottomSheetFragment();
    }



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






        return v;
    }

    //    ------------------------------------------------- ON VIEW CREATED METHOD     ------------------------------------------------------- //

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);


            //      DISABLE BUTTON

            if (task.length() > 0) {
                mSaveButton.setEnabled(false);
            }
        }

        ibSelectCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new DatePickerFragment();
            assert getFragmentManager() != null;
            newFragment.show(getFragmentManager(), "datePicker");

            }
        });


//        ibSelectCalender.setOnClickListener(v -> {
//            DialogFragment newFragment = new DatePickerFragment();
//            assert getFragmentManager() != null;
//            newFragment.show(getFragmentManager(), "datePicker");
//        });


        ibSelectClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new TimePickerFragment();
                timeFragment.show(getFragmentManager(),"timePicker");
            }
        });


        //      TO CHECK TEXT CHANGE

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(v -> {
            String text = Objects.requireNonNull(mEditText.getText()).toString();
//            String yyyyText = yyyy.toString();

            //   TO UPDATE THE TASK

            if (finalIsUpdate) {
                myDb.updateTask(bundle.getInt("id"), text);
            }

            //      TO CREATE NEW TASK
            else {

                RVModel item = new RVModel();
                item.setTask(text);
                item.setStatus(0);
//                item.setyyyy(yyyyText);
//
                myDb.insertTask(item);
            }
            dismiss();

//            Log.v("This is year ", String.valueOf(+ yyyy));
        });

    }

    //    ------------------------------------------------ ON DISMISS ACTIVITY   ------------------------------------------ //


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();

        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        super.onCreate(savedInstanceState);
    }

    // ---------------------------------- Date Here-----------------------------------------------------------//

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{



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


            dd_mm_yy.setText(dayOfMonth + " / " + (month + 1) + " / "
                    + year);

//            mm = month;
//            yyyy = String.valueOf(year);






        }


    }

//    public static String getYyyy() {
//        return yyyy;
//    }
//
//    public static void setYyyy(String yyyy) {
//        BottomSheetFragment.yyyy = yyyy;
//    }

    //    =============================================== Time ======================================================//

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
                int hour_of_day = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);


            return new TimePickerDialog(getActivity(), this, hour_of_day, min, DateFormat.is24HourFormat(getActivity()));

        }

        @Override
        public void onTimeSet(TimePicker view, int hour_of_day , int minute) {

            String AM_PM ;
            if (hour_of_day < 12){
                AM_PM = "AM";
            }else {
                AM_PM = "PM";
            }

            hh_mm_am_pm.setText(hour_of_day + " : " + minute + " " + AM_PM);

//            hh = hour_of_day;
//            min = minute;
//            am_pm = AM_PM;

        }
    }




}
