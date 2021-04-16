package in.aditya.letsdoit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import in.aditya.letsdoit.DialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.RecyclerItemTouchHelper;
import in.aditya.letsdoit.adapter.RVAdapter;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {


    private List<RVModel> taskList;
    private RecyclerView rvTask;
    private RVAdapter rVAdapter;
    private DataBaseHandler dataBaseHandler;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<>();
        dataBaseHandler =new DataBaseHandler(this);
        dataBaseHandler.openDataBase();
        floatingActionButton = findViewById(R.id.fab_add_task);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(rVAdapter));
        itemTouchHelper.attachToRecyclerView(rvTask);

        rvTask = findViewById(R.id.rv_todo_task);
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        rVAdapter = new RVAdapter(dataBaseHandler, this);
        rvTask.setAdapter(rVAdapter);
        taskList = dataBaseHandler.getTask();
        Collections.reverse(taskList);
        rVAdapter.setTask(taskList);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }


    @Override
    public void handleDialogClose(DialogInterface dialogInterface){
        taskList = dataBaseHandler.getTask();
        Collections.reverse(taskList);
        rVAdapter.setTask(taskList);
        rVAdapter.notifyDataSetChanged();

    }


}