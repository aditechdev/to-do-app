package in.aditya.letsdoit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.aditya.letsdoit.OnDialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.RecyclerItemTouchHelper;
import in.aditya.letsdoit.adapter.RVAdapter;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

//public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {
public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {



    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DataBaseHandler dataBaseHandler;
    private List<RVModel> mList;
    private RVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab_add_task);
        dataBaseHandler =new DataBaseHandler(this);
        mList = new ArrayList<>();
        adapter = new RVAdapter(dataBaseHandler, MainActivity.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mList = dataBaseHandler.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {

        mList = dataBaseHandler.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();

    }
}

//
//
//        taskList = new ArrayList<>();
//
//        dataBaseHandler.openDataBase();
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(rVAdapter));
//        itemTouchHelper.attachToRecyclerView(rvTask);
//
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        rVAdapter = new RVAdapter(dataBaseHandler, this);
//        rvTask.setAdapter(rVAdapter);
//        taskList = dataBaseHandler.getTask();
//        Collections.reverse(taskList);
//        rVAdapter.setTask(taskList);
//
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
//            }
//        });
//    }
//
//
//    @Override
//    public void handleDialogClose(DialogInterface dialogInterface){
//        taskList = dataBaseHandler.getTask();
//        Collections.reverse(taskList);
//        rVAdapter.setTask(taskList);
//        rVAdapter.notifyDataSetChanged();
//
//    }
//

