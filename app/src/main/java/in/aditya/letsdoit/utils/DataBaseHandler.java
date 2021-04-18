package in.aditya.letsdoit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.aditya.letsdoit.model.RVModel;

public class DataBaseHandler extends SQLiteOpenHelper {

    //      TODO 6: REMOVE WARNING

    //    ------------------------------------------------ DATA BASE -------------------------------------------------------------------- //
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "toDoDataBase";
    public static final String TABLE_NAME = "TODO_TABLE";

    //    ------------------------------------------- DATA BASE COLUMN ----------------------------------------------------------------- //

    public static final String KEY_ID = "ID";
    public static final String KEY_TASK_NAME = "TASK_NAME";
    public static final String KEY_TASK_STATUS = "TASK_STATUS";

    //    ------------------------------------------- Other Declaration ----------------------------------------------------------------- //

    private Context context;
    private SQLiteDatabase db;

    //    ------------------------------------------- DATA BASE HANDLER ----------------------------------------------------------------- //

    public DataBaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    //    ------------------------------------------- ON CREATE METHOD START HERE -------------------------------------------------------- //

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASK_NAME + " TEXT, " + KEY_TASK_STATUS + " INTEGER)";
        db.execSQL(createTableStatement);
    }

    //    ------------------------------------------- ON UPGRADE METHOD START HERE -------------------------------------------------------- //

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //    ------------------------------------------------- TO ADD TASK ------------------------------------------------------------------ //

    public void insertTask(RVModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, model.getTask());
        values.put(KEY_TASK_STATUS, 0);
        long insert = db.insert(TABLE_NAME, null, values);
        if (insert == -1) {
            Toast.makeText(context, "Failed to Save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "TODO List SAVE", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //    ------------------------------------------------- UPDATE TASK ------------------------------------------------------------------ //

    public void updateTask(int id, String task) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, task);
        int update = db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
        if (update == -1) {
            Toast.makeText(context, "Your File not Updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Your TODO Updated ", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //    ------------------------------------------------- CHECK BOX STATUS CHECK ---------------------------------------------------- //

    public void updateStatus(int id, int status) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_STATUS, status);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //    ---------------------------------------------- DELETE THE TASK --------------------------------------------------------------- //

    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //    -------------------------------------------    TO GET ALL THE SAVED TASK   ---------------------------------------------------- //


    public List<RVModel> getAllTasks() {
        List<RVModel> modelList = new ArrayList<>();
        db = this.getWritableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        RVModel task = new RVModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_TASK_STATUS)));
                        modelList.add(task);

                    } while (cursor.moveToNext());

                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
            db.close();

        }
        return modelList;
    }

//    -------------------------------------------------   Get Count     ------------------------------------------------------------------ //

//    public int getCount(){
//        String query = "SELECT  * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        return cursor.getCount();
//
//    }

}

//    -------------------------------------------------   END OF CODE     ------------------------------------------------------------------ //

