package in.aditya.letsdoit.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import in.aditya.letsdoit.OnDialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.RecyclerItemTouchHelper;
import in.aditya.letsdoit.adapter.RVAdapter;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    //    ------------------------------------------------VARIABLE DECLARATION ------------------------------------------------------ //

    private RecyclerView mRecyclerView;
    private DataBaseHandler dataBaseHandler;
    private List<RVModel> mList;
    private RVAdapter adapter;
    private ImageView imageBlank;

    public MainActivity() {
    }

    //    -------------------------------------------------- ON CREATE METHOD   ------------------------------------------------------------ //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        imageBlank = findViewById(R.id.image_main);
        dataBaseHandler = new DataBaseHandler(this);
        mList = new ArrayList<>();
        adapter = new RVAdapter(dataBaseHandler, MainActivity.this);
        createNotificationChannel();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        //       TO GET ALL THE TASK SAVED
        mList = dataBaseHandler.getAllTasks();
        adapter.setTasks(mList);

        // To load Blank image
        loadBlank();


        fab.setOnClickListener(v -> {

            //      GET ADD TASK BOTTOM SUPPORTED  FRAGMENT

            BottomSheetFragment.newInstance().show(getSupportFragmentManager(), BottomSheetFragment.TAG);

        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void loadBlank() {
        if (adapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            imageBlank.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            imageBlank.setVisibility(View.GONE);

        }
    }

    //    ---------------------------------------------------ON DIALOGUE BOX CLOSE METHOD   ----------------------------------------- //

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = dataBaseHandler.getAllTasks();
        adapter.setTasks(mList);
        loadBlank();
        adapter.notifyDataSetChanged();
    }

    //----------------------------------------------------      Notification Channel      ------------------------------------------- //

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TODOReminderChannel";
            String description = "Channel For TODO";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("todoList", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

}

//----------------------------------------------------            End Of CODE           ------------------------------------------- //
