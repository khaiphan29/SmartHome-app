package dashboard.iot.bku.roomcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ADMIN on 4/14/2022.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, "Database1",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "create table Table1 (xValue REAL, yValue REAL)";
        db.execSQL(CreateTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertToData(long ValX, float ValY)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("xValue", ValX);
        contentValues.put("yValue", ValY);

        database.insert("Table1", null,contentValues);
    }
}
