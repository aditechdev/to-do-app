package in.aditya.letsdoit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import in.aditya.letsdoit.activity.AddNewTask;
import in.aditya.letsdoit.activity.MainActivity;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<RVModel> todList;
    private MainActivity activity;
    private DataBaseHandler dataBaseHandler;


    public RVAdapter(DataBaseHandler dataBaseHandler, MainActivity activity) {
        this.dataBaseHandler =dataBaseHandler;
        this.activity = activity;
    }


    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_added_task, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
        dataBaseHandler.openDataBase();
        RVModel item = todList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dataBaseHandler.updateStatus(item.getId(),1);
                }
                else {
                    dataBaseHandler.updateStatus(item.getId(),0);
                }
            }
        });

    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todList.size();
    }

    public void setTask(List<RVModel> todList) {
        this.todList = todList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        RVModel item = todList.get(position);
        dataBaseHandler.deleteTask(item.getId());
        todList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        RVModel item = todList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.chkBx_todo);
        }
    }
}
