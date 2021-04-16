package in.aditya.letsdoit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.params.Params;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String CREATE_TODO_TABLE = " CREATE TABLE " + Params.TABLE_NAME + "(" + Params.KEY_ID + " INTEGER PRIMARY KEY, " + Params.KEY_TASK_NAME + " TEXT, " + Params.KEY_STATUS + " INTEGER " + ")";

    private SQLiteDatabase db;


    public DataBaseHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + Params.TABLE_NAME);

        onCreate(db);

    }

    public void openDataBase(){
        db = this.getWritableDatabase();
    }

    public void createTask(RVModel task){

        ContentValues cv = new ContentValues();
        cv.put(Params.KEY_TASK_NAME, task.getTask());
        cv.put(Params.KEY_STATUS, 0);
        db.insert(Params.TABLE_NAME, null, cv);

    }

    public List<RVModel> getTask(){
        List<RVModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
            try{
                cursor = db.query(Params.TABLE_NAME, null, null, null, null, null, null, null);
                if (cursor != null){
                    if (cursor.moveToFirst()){
                        do {
                            RVModel task = new RVModel();
                            task.setId(cursor.getInt(cursor.getColumnIndex(Params.KEY_ID)));
                            task.setTask(cursor.getString(cursor.getColumnIndex(Params.KEY_TASK_NAME)));
                            task.setStatus(cursor.getInt(cursor.getColumnIndex(Params.KEY_STATUS)));
                            taskList.add(task);
                        }while (cursor.moveToNext());
                    }
                }
            }finally {
                db.endTransaction();
                cursor.close();

            }
            return taskList;

    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(Params.KEY_STATUS, status);
        db.update(Params.TABLE_NAME, cv, Params.KEY_ID + "=?", new String[] {String.valueOf(id)});

    }
    public void updateTask (int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(Params.KEY_TASK_NAME, task);
        db.update(Params.TABLE_NAME, cv, Params.KEY_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(Params.TABLE_NAME, Params.KEY_ID + "?=", new String[] {String.valueOf(id)});
    }
}
