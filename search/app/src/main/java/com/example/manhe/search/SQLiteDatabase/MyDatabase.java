package com.example.manhe.search.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.manhe.search.model.MarkerDetails;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";

    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Marker_Manager";


    //  bảng Marker
    private static final String TABLE_MARKER = "marker";
    private static final String COLUMN_ID ="_id";
    private static final String COLUMN_CATE = "_cate";
    private static final String COLUMN_LAT = "_lat";
    private static final String COLUMN_LNG = "_lng";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_NAME_VI = "_name_vi";
    private static final String COLUMN_NAME_EN = "_name_en";
    private static final String COLUMN_NAME_ES = "_name_es";
    private static final String COLUMN_NAME_CN = "_name_cn";
    private static final String COLUMN_NAME_KO = "_name_ko";
    private static final String COLUMN_ADD = "_add";
    private static final String COLUMN_ADD_VI = "_add_vi";
    private static final String COLUMN_ADD_EN = "_add_en";
    private static final String COLUMN_ADD_ES = "_add_es";
    private static final String COLUMN_ADD_CN = "_add_cn";
    private static final String COLUMN_ADD_KO = "_add_ko";
    private static final String COLUMN_ID_D = "_id_d";

    //bảng Search History
    private static final String TABLE_SH = "SearchHistory";
    private static final String ID ="_id";
    private static final String SH = "search_history";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "MyDatabaseHelper.onCreate ... ");
        String script =  "CREATE TABLE "+ TABLE_MARKER + "(" +COLUMN_ID+ " INTEGER PRIMARY KEY," +
                COLUMN_CATE+ " TEXT ,"+ COLUMN_LAT +" TEXT ," +  COLUMN_LNG +" TEXT ," +
                COLUMN_NAME +" TEXT ,"+ COLUMN_NAME_VI + " TEXT ," + COLUMN_NAME_EN + " TEXT ," + COLUMN_NAME_ES + " TEXT ," + COLUMN_NAME_CN + " TEXT ," + COLUMN_NAME_KO + " TEXT ," +
                COLUMN_ADD + " TEXT ," + COLUMN_ADD_VI + " TEXT ," + COLUMN_ADD_EN + " TEXT, " + COLUMN_ADD_ES + " TEXT ," + COLUMN_ADD_CN + " TEXT ," + COLUMN_ADD_KO + " TEXT ," +
                COLUMN_ID_D + " TEXT " +");";
        db.execSQL(script);

        String script_1 = "CREATE TABLE "+ TABLE_SH + "(" + ID + " INTEGER PRIMARY KEY," + SH + " TEXT" + ");";
        db.execSQL(script_1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_MARKER);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SH);
        //db.execSQL("DROP TABLE IF EXISTS ' " + TABLE_SH +"'");


        // Và tạo lại.
        onCreate(db);
    }

    //add Search History
    public void addSH(String txt) {
        Log.e(TAG, "MyDatabaseHelper.addSH ... " +txt);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SH,txt);


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_SH, null, values);


        // Đóng kết nối database.
        db.close();
    }

    //getAllSearchHistory
    public ArrayList<String> getAllSearchHistory(){
        ArrayList<String> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SH;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
               String txt;
               txt = cursor.getString(1);
               list.add(txt);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public String getSH(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SH, new String[] { ID,
                        SH  }, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        return cursor.getString(1);
    }


    //add marker
        public void addMarker(MarkerDetails marker) {
        Log.e(TAG, "MyDatabaseHelper.addMarker ... " +marker.getId_d());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATE, marker.getCate());
        values.put(COLUMN_LAT, marker.getLat());
        values.put(COLUMN_LNG, marker.getLng());
        values.put(COLUMN_NAME, marker.getName());
        values.put(COLUMN_NAME_VI, marker.getName_vi());
        values.put(COLUMN_NAME_EN, marker.getName_en());
        values.put(COLUMN_NAME_ES, marker.getName_es());
        values.put(COLUMN_NAME_CN, marker.getName_cn());
        values.put(COLUMN_NAME_KO, marker.getname_ko());
        values.put(COLUMN_ADD, marker.getAdd());
        values.put(COLUMN_ADD_VI, marker.getAdd_vi());
        values.put(COLUMN_ADD_EN, marker.getAdd_en());
        values.put(COLUMN_ADD_ES, marker.getAdd_es());
        values.put(COLUMN_ADD_CN, marker.getAdd_cn());
        values.put(COLUMN_ADD_KO, marker.getadd_ko());
        values.put(COLUMN_ID_D, marker.getId_d());





        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_MARKER, null, values);


        // Đóng kết nối database.
        db.close();
    }

    public ArrayList<MarkerDetails> getAllMarker() {
        Log.i(TAG, "MyDatabaseHelper.getAllMarker ... " );

        ArrayList<MarkerDetails> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MARKER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                MarkerDetails marker = new MarkerDetails();
                marker.setId(Integer.parseInt(cursor.getString(0)));
                marker.setCate(cursor.getString(1));
                marker.setLat(cursor.getString(2));
                marker.setLng(cursor.getString(3));
                marker.setName(cursor.getString(4));
                marker.setName_vi(cursor.getString(5));
                marker.setAdd_en(cursor.getString(6));
                marker.setName_es(cursor.getString(7));
                marker.setName_cn(cursor.getString(8));
                marker.setadd_ko(cursor.getString(9));
                marker.setAdd(cursor.getString(10));
                marker.setAdd_vi(cursor.getString(11));
                marker.setAdd_en(cursor.getString(12));
                marker.setAdd_es(cursor.getString(13));
                marker.setAdd_cn(cursor.getString(14));
                marker.setadd_ko(cursor.getString(15));
                marker.setId_d(cursor.getString(16));
                // Thêm vào danh sách.
                List.add(marker);
            } while (cursor.moveToNext());
        }

        // return note list_service
        return List;
    }

    public void deleteCate(int cate){
        Log.e("CMM","da xoa gogogo");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKER,COLUMN_CATE+ "=?", new String[]{String.valueOf(cate)});
        db.close();
    }
    public void deleteAll() {
        Log.e("CMM","da xoa");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKER,null,null);
        db.close();
    }

    public void deleteAllSH() {
        Log.e("CMM","da xoa");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SH,null,null);
        db.close();
    }
}
