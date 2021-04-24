package in.aditya.letsdoit.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.aditya.letsdoit.R;
import in.aditya.letsdoit.activity.BottomSheetFragment;
import in.aditya.letsdoit.activity.MainActivity;
import in.aditya.letsdoit.broadcast.ReminderBroadcast;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

import static android.content.Context.ALARM_SERVICE;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {


    //    --------------------------------------------------------  VARIABLE DECLARATION   ------------------------------------------------ //

    private final MainActivity activity;
    private final DataBaseHandler myDb;
    private List<RVModel> mList;

    //    --------------------------------------------------  CONSTRUCTOR    ------------------------------------------------------------ //

    public RVAdapter(DataBaseHandler myDb, MainActivity activity) {
        this.activity = activity;
        this.myDb = myDb;
    }

    //    --------------------------------------------------   VIEW HOLDER   ------------------------------------------------------------ //

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_added_task, parent, false);
        return new MyViewHolder(view);
    }

    //    ----------------------------------------------------BINDING VIEW HOLDER------------------------------------------------------------ //

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RVModel item = mList.get(position);
        holder.timeTextView.setText(item.getTime());
        holder.dateTextView.setText(item.getDate());
        holder.alarmCheck.setChecked(item.getAlarm() == 0 ? false : true);
        holder.alarmCheck.setOnCheckedChangeListener(null);
        holder.alarmCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            //    ----------------------------------------------------ALARM AND NOTIFICATION------------------------------------------------------------ //
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myDb.updateAlarm(item.getId(), 1);
                    Intent intent = new Intent(buttonView.getContext(), ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(buttonView.getContext(), 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);

                    //String to get date time in "2021-05-21T07:08" this format, and convert to mills
                    // String timeAtCheckmy = item.getDatetime(); //2021-05-21T07:08
                    //long millis = localDateTime
//                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                    ##############################################################

//                    long timeAtCheck = System.currentTimeMillis();
//                    long tenSecMills = 1000 * 10;

                    String timeAtCheck = item.getDatetime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    try {
                        Date mDate = simpleDateFormat.parse(timeAtCheck);
                        long timeInMilliSeconds = mDate.getTime();
                        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSeconds , pendingIntent);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } else
                    myDb.updateAlarm(item.getId(), 0);
            }
        });

        holder.mCheckBox.setText(item.getTask());

        holder.mCheckBox.setOnCheckedChangeListener(null); // this is update
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));

//        COMPOUND BUTTON ADDED TO CHECKBOX

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myDb.updateStatus(item.getId(), 1);
                } else
                    myDb.updateStatus(item.getId(), 0);
            }
        });
    }

    //    TO CHECK BOOLEAN VALUE
    public boolean toBoolean(int num) {
        return num != 0;

    }

    public Context getContext() {
        return activity;
    }

    //    -------------------------------------------------   SET TASK METHOD TO ADD DATA  --------------------------------------------- //

    public void setTasks(List<RVModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    //    --------------------------------------------------------  DELETE TASK METHOD   ----------------------------------------------- //

    public void deleteTask(int position) {
        RVModel item = mList.get(position);
        myDb.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    //    -------------------------------------------------UPDATE THE ITEM    ------------------------------------------------------------ //

    public void editItem(int position) {
        RVModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        bundle.putInt("alarm", item.getAlarm());

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setArguments(bundle);

        bottomSheetFragment.show(activity.getSupportFragmentManager(), bottomSheetFragment.getTag());


    }

    //    ---------------------------------------------------GET INT COUNT    ------------------------------------------------------------ //

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //    ---------------------------------------------------- VIEW HOLDER SUPER CLASS  ------------------------------------------------- //

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        CheckBox alarmCheck;
        TextView dateTextView;
        TextView timeTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mCheckBox);
            alarmCheck = itemView.findViewById(R.id.alarmCheckBox);
            dateTextView = itemView.findViewById(R.id.date_main_layout);
            timeTextView = itemView.findViewById(R.id.time_main_layout);

        }
    }


}
