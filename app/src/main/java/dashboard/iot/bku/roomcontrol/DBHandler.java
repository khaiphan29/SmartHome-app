package dashboard.iot.bku.roomcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jjoe64.graphview.series.DataPoint;

import java.time.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ADMIN on 3/28/2022.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, "MyDB", null, 1);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String createTable = "create table tempTable(xValues REAL, yValues REAL);";
        db.execSQL("create table tempTable(xValues REAL, yValues REAL);");
        db.execSQL("create table humidTable(xValues REAL, yValues REAL);");
        db.execSQL("create table airTable(xValues REAL, yValues REAL);");
        db.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void resetData () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table tempTable;");
        db.execSQL("drop table humidTable;");
        db.execSQL("drop table airTable;");
        db.execSQL("create table tempTable(xValues REAL, yValues REAL);");
        db.execSQL("create table humidTable(xValues REAL, yValues REAL);");
        db.execSQL("create table airTable(xValues REAL, yValues REAL);");
    }

    public Boolean insertData(String tableName, long valueX, float valueY ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("xValues", valueX);
        contentValues.put("yValues", valueY);
        sqLiteDatabase.insert(tableName, null, contentValues);
        return true;
    }

    public DataPoint[] getDataPoint(String tableName)
    {
        String[] columns = {"xValues", "yValues"};
        Cursor cursor = this.getWritableDatabase().query(tableName ,columns, null,null,null,null,null );
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i ++){
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getLong(0), cursor.getFloat(1));
        }
        return dp;
    }
    //Register
    public Boolean insertUserData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String cmd = "Select * from users where username = ?";
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

}
