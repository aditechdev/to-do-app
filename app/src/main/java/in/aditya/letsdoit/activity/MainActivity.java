package in.aditya.letsdoit.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    // TODO 2: REMOVE WARNING

    //    ------------------------------------------------VARIABLE DECLARATION ------------------------------------------------------ //
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DataBaseHandler dataBaseHandler;
    private List<RVModel> mList;
    private RVAdapter adapter;
    private ImageView imageBlank;

    // TODO 1C: ADD TIME STAMP HERE

//    //Sort Option
//    String orderByNewest = ADDED_TIME_STAMP + "DESC";
//    String orderByOldest = ADDED_TIME_STAMP + "ASC";


    //    -------------------------------------------------- ON CREATE METHOD   ------------------------------------------------------------ //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab_add_task);
        imageBlank = findViewById(R.id.image_main);
        dataBaseHandler = new DataBaseHandler(this);
        mList = new ArrayList<>();
        adapter = new RVAdapter(dataBaseHandler, MainActivity.this);

        //       RECYCLER VIEW SETTING

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        //       TO GET ALL THE TASK SAVED

        mList = dataBaseHandler.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

       // mList = dataBaseHandler.getAllTasks();
        //        TO GET THE LIST IN REVERSE FORM, UPDATED FIRST WILL BE LAST

       // Collections.reverse(mList);

        //         SET ADAPTER

       // adapter.setTasks(mList);

        //          ON CLICK LISTENER ON FLOATING ACTION BUTTON

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //      GET ADD TASK BOTTOM SUPPORTED  FRAGMENT

                BottomSheetFragment.newInstance().show(getSupportFragmentManager(), BottomSheetFragment.TAG);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }








    //    ---------------------------------------------------ON DIALOGUE BOX CLOSE METHOD   ----------------------------------------- //

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = dataBaseHandler.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();


    }

    //    --------------------------------------------------   OPTION MENU   ----------------------------------------- //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO 1a: Inflate menu to sort

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // TODO 1b: Handle menu items
        int id = item.getItemId();
        if (id==R.id.action_sort){


        }

        return super.onOptionsItemSelected(item);

    }
}
