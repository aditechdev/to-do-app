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

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "TODO_TABLE";

    public static final String KEY_ID = "ID";
    public static final String KEY_TASK_NAME = "TASK_NAME";
    public static final String KEY_TASK_STATUS = "TASK_STATUS";


//    ------------------------------------------------ DATA BASE -------------------------------------------------------------------- //
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "toDoDataBase";
    private SQLiteDatabase db;
//    public static final String TABLE_NAME = "todo";

    //    keys of our table in db
//    public static final String KEY_ID = "ID";
//    public static final String KEY_TASK_NAME = "TASK";
//    public static final String KEY_STATUS = "STATUS";


    public DataBaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_TODO_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASK_NAME + " TEXT, " + KEY_STATUS + " INTEGER)";
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)");

        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASK_NAME + " TEXT, " + KEY_TASK_STATUS + " INTEGER)";
        db.execSQL(createTableStatement);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void insertTask(RVModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, model.getTask());
        values.put(KEY_TASK_STATUS, model.getStatus());
        db.insert(TABLE_NAME, null, values);
        db.close(); // added tonight
    }

//    public int updateTask(RVModel task) {
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_TASK_NAME, task.getTask());
//        //Cursor cursor = db.rawQuery("Select * from TABLE_NAME where ")
//        return db.update(TABLE_NAME, values, KEY_ID + "+?", new String[]{String.valueOf(task.getId())});
//    }
    public void updateTask(int id, String task) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, task);
        //Cursor cursor = db.rawQuery("Select * from TABLE_NAME where ")
        db.update(TABLE_NAME, values, KEY_ID + "+?", new String[]{String.valueOf(id)});
}

    public void updateStatus(int id, int status) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_STATUS, status);
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(id)});


    }

    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    // TODO : Use db = this.getReadableDatabase(); to see if it fix error.

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
//            TODO: db.close(); is the added line.
            // TODO : CLose cursor and db when done.

        }
        return modelList;
    }



}

//    private SQLiteDatabase db;
//
//
//    public DataBaseHandler(Context context) {
//        super(context, Params.DB_NAME, null, Params.DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_TODO_TABLE = " CREATE TABLE " + Params.TABLE_NAME + "(" + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Params.KEY_TASK_NAME + " TEXT, " + Params.KEY_STATUS + " INTEGER)";
//
//        db.execSQL(CREATE_TODO_TABLE);
//
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        db.execSQL(" DROP TABLE IF EXISTS " + Params.TABLE_NAME);
//
//        onCreate(db);
//
//    }
//
//    public void openDataBase(){
//        db = this.getWritableDatabase();
//    }
//
//    public void insertTask(RVModel task){
//
//        ContentValues cv = new ContentValues();
//        cv.put(Params.KEY_TASK_NAME, task.getTask());
//        cv.put(Params.KEY_STATUS, 0);
//        db.insert(Params.TABLE_NAME, null, cv);
//
//    }
//
//    public List<RVModel> getTask(){
//        List<RVModel> taskList = new ArrayList<>();
//        Cursor cursor = null;
//        db.beginTransaction();
//            try{
//                cursor = db.query(Params.TABLE_NAME, null, null, null, null, null, null, null);
//                if (cursor != null){
//                    if (cursor.moveToFirst()){
//                        do {
//                            RVModel task = new RVModel();
//                            task.setId(cursor.getInt(cursor.getColumnIndex(Params.KEY_ID)));
//                            task.setTask(cursor.getString(cursor.getColumnIndex(Params.KEY_TASK_NAME)));
//                            task.setStatus(cursor.getInt(cursor.getColumnIndex(Params.KEY_STATUS)));
//                            taskList.add(task);
//                        }while (cursor.moveToNext());
//                    }
//                }
//            }finally {
//                db.endTransaction();
//                cursor.close();
//
//            }
//            return taskList;
//
//    }
//
//    public void updateStatus(int id, int status){
//        ContentValues cv = new ContentValues();
//        cv.put(Params.KEY_STATUS, status);
//        db.update(Params.TABLE_NAME, cv, Params.KEY_ID + "=?", new String[] {String.valueOf(id)});
//
//    }
//    public void updateTask (int id, String task){
//        ContentValues cv = new ContentValues();
//        cv.put(Params.KEY_TASK_NAME, task);
//        db.update(Params.TABLE_NAME, cv, Params.KEY_ID + "=?", new String[] {String.valueOf(id)});
//    }
//
//    public void deleteTask(int id){
//        db.delete(Params.TABLE_NAME, Params.KEY_ID + "?=", new String[] {String.valueOf(id)});
//    }

