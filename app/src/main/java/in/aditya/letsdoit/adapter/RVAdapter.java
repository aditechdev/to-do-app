package in.aditya.letsdoit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aditya.letsdoit.activity.BottomSheetFragment;
import in.aditya.letsdoit.activity.MainActivity;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {


    // TODO 4: REMOVE WARNING

    //    --------------------------------------------------------  VARIABLE DECLARATION   ------------------------------------------------ //

    private List<RVModel> mList;
    private MainActivity activity;
    private DataBaseHandler myDb;

    //    --------------------------------------------------  CONSTRUCTOR    ------------------------------------------------------------ //

    public RVAdapter(DataBaseHandler myDb , MainActivity activity) {
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
//        holder.recyclerTextYear.setText(item.getyyyy());
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setOnCheckedChangeListener(null); // this is update
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));

//        COMPOUND BUTTON ADDED TO CHECKBOX

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDb.updateStatus(item.getId() , 1);
                }else
                    myDb.updateStatus(item.getId() , 0);
            }
        });
    }

//    TO CHECK BOOLEAN VALUE
    public  boolean toBoolean(int num){
        return num != 0;

    }

    public Context getContext(){
        return activity;
    }

    //    -------------------------------------------------   SET TASK METHOD TO ADD DATA  --------------------------------------------- //

    public void setTasks(List<RVModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    //    --------------------------------------------------------  DELETE TASK METHOD   ----------------------------------------------- //

    public void deleteTask(int position){
        RVModel item = mList.get(position);
        myDb.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    //    -------------------------------------------------UPDATE THE ITEM    ------------------------------------------------------------ //

    public void editItem(int position){
        RVModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        BottomSheetFragment task = new BottomSheetFragment();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());

    }

    //    ---------------------------------------------------GET INT COUNT    ------------------------------------------------------------ //

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //    ---------------------------------------------------- VIEW HOLDER SUPER CLASS  ------------------------------------------------- //

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox mCheckBox;
//        TextView recyclerTextYear;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mCheckBox);
//            recyclerTextYear = itemView.findViewById(R.id.recyclerTextYear);

        }
    }
}
