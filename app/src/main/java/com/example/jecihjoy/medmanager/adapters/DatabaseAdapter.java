package com.example.jecihjoy.medmanager.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jecihjoy.medmanager.model.Medicine;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jecihjoy on 4/2/2018.
 */

public class DatabaseAdapter {
    private static final String DATABASE_NAME = "meds.db";
    private static final int DATABASE_VERSION = 7;

    public static final String MED_TABLE = "medicine";
    public static final String USERS_TABLE = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_STARTDATE = "startdate";
    public static final String COLUMN_ENDDATE = "enddate";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_TIME = "starttime";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_ID2 = "id";

    public static long id;

    String [] allcolumns = {"id", "name", "description", "frequency", "startdate", "days","enddate", "starttime", "duration", "month"};

    String [] columnUsers = {"id", "pName", "gName", "pgName", "email"};

    private SQLiteDatabase sqlDB;
    private MedicineDbHelper medicineDbHelper;
    private Context context;

    public DatabaseAdapter(Context ctx){
        context = ctx;
    }

    public DatabaseAdapter open() throws android.database.SQLException{
        medicineDbHelper = new MedicineDbHelper(context);
        sqlDB = medicineDbHelper.getWritableDatabase();
        return  this;
    }

    public void close(){
        medicineDbHelper.close();
    }

    public Medicine createMeds(String name, String desc, String f, String days, String date, String starttime, String endDate, int duration, String month){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_DESC,desc);
        values.put(COLUMN_FREQUENCY,f);
        values.put(COLUMN_DAYS,days);
        values.put(COLUMN_STARTDATE, date);
        values.put(COLUMN_ENDDATE, endDate);
        values.put(COLUMN_TIME, starttime);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_MONTH, month);
        long insertId = sqlDB.insert(MED_TABLE, null, values);
        Cursor cursor = sqlDB.query(MED_TABLE, allcolumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Medicine newMed = cursorToMedicine(cursor);
        cursor.close();
        return  newMed;
    }

    public long updateMedicine(int idToUpdate, String newName, String newDesc, String newf, int newDays){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,newName);
        values.put(COLUMN_DESC,newDesc);
        values.put(COLUMN_FREQUENCY,newf);
        values.put(COLUMN_DAYS,newDays);
        values.put(COLUMN_STARTDATE, Calendar.getInstance().getTime() + "");
        values.put(COLUMN_ENDDATE, Calendar.getInstance().getTime() + "");
        return  sqlDB.update(MED_TABLE, values, COLUMN_ID+ " = "+idToUpdate, null );
    }

    public long deleteMedicine(long idToDelete){
        return sqlDB.delete(MED_TABLE, COLUMN_ID+ " = " +idToDelete,null);
    }
    public void deleteAllMeds(){
        sqlDB.execSQL(" DELETE FROM " + MED_TABLE);
    }

    //getAll meds
    public ArrayList<Medicine>getAllMeds(){
        ArrayList<Medicine> meds = new ArrayList<Medicine>();
        Cursor cursor = sqlDB.query(MED_TABLE, allcolumns,null,null,null,null,null);
        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
            Medicine myMed = cursorToMedicine(cursor);
            meds.add(myMed);
        }
        cursor.close();
        return meds;
    }

    public ArrayList<Medicine>getMonthlyMeds(String mymonth){
        ArrayList<Medicine> meds = new ArrayList<Medicine>();
        Cursor cursor = sqlDB.query(MED_TABLE, allcolumns,"month = ?", new String[] {mymonth},null,null,null);
        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
            Medicine myMed = cursorToMedicine(cursor);
            meds.add(myMed);

        }
        cursor.close();
        return meds;
    }

    private Medicine cursorToMedicine(Cursor cursor){
        Medicine newMed = new Medicine(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4),cursor.getInt(5),cursor.getString(6),
                cursor.getString(7), cursor.getInt(8), cursor.getString(9));
        return newMed;

    }

    //insert current user
    public void insertUers(String id, String pname, String gname, String pgname, String email){
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("pName",pname);
        values.put("gName",gname);
        values.put("pgName",pgname);
        values.put("email",email);
        long insertId = sqlDB.insert("users", null, values);
    }

    public Cursor getUserInfo() {
        return sqlDB.query("users", columnUsers, null, null, null, null, String.valueOf(1));
    }

    public long updateProfile(String id, String pname, String gname, String pgname, String email){
        ContentValues values = new ContentValues();
        values.put("pName",pname);
        values.put("gName",gname);
        values.put("pgName",pgname);
        values.put("email",email);
        return  sqlDB.update("users", values, COLUMN_ID2+ " = "+id, null );

    }
    //sql helper class
    private static class MedicineDbHelper extends SQLiteOpenHelper{
        MedicineDbHelper(Context ctx){
            super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "  CREATE TABLE  " + MED_TABLE + "( id integer not null PRIMARY KEY AUTOINCREMENT , name TEXT, description TEXT, " +
                    "frequency TEXT, startdate TEXT, days integer, enddate TEXT, starttime TEXT, duration integer, month TEXT  )";
            db.execSQL(sql);

            String sqlUsers = "  CREATE TABLE  users ( id TEXT, pName TEXT, gName TEXT, pgName TEXT, email TEXT )";
            db.execSQL(sqlUsers);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.d(MedicineDbHelper.class.getName(),
                "Upgrading database from version"+oldVersion+"to" +newVersion+ ", which will destroy all the old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MED_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
            onCreate(sqLiteDatabase);
        }
    }


}
