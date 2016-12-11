package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by אילון on 29/11/2016.
 */

public class DBOperator extends SQLiteOpenHelper {
   public static final int DB_VERSION = 1;
   public static final String DB_NAME = "DBOperator_minesweeper";

    public DBOperator (Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(FeedReaderContract.DELETE_SCORE_TABLE);
        onCreate(db);
    }

    public void addRow(DataRow dr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_PLAYER_NAME, dr.getmName());
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_LEVEL, dr.getLevel());
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_SCORE, dr.getScore());

        // Inserting Row
        db.insert(FeedReaderContract.FeedScore.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public DataRow getHighScoreFromDB(int level){
        SQLiteDatabase db = getReadableDatabase();
        DataRow row = null;
        String query =
                "SELECT *  FROM "+FeedReaderContract.FeedScore.TABLE_NAME +
                        " WHERE " +FeedReaderContract.FeedScore.COLUMN_NAME_LEVEL +"="+level +
                        " ORDER BY "+FeedReaderContract.FeedScore.COLUMN_NAME_SCORE+
                        " ASC LIMIT 1";
        Cursor c = db.rawQuery(query,null);
        if (c != null &&  c.moveToFirst() ){
            row = new DataRow(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3));
        }
        c.close();
        return row;
    }

    public ArrayList<DataRow> getTopThree(int level){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DataRow> rows = new ArrayList<>();
        String query =
                "SELECT *  FROM "+FeedReaderContract.FeedScore.TABLE_NAME +
                        " WHERE " +FeedReaderContract.FeedScore.COLUMN_NAME_LEVEL +"="+level +
                        " ORDER BY "+FeedReaderContract.FeedScore.COLUMN_NAME_SCORE+
                        " ASC LIMIT 3";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.moveToFirst()){
            rows.add (new DataRow(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3)));
            if (c.moveToNext())
                rows.add (new DataRow(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3)));
            if (c.moveToNext())
                rows.add (new DataRow(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3)));
        }
        c.close();
        return rows;
    }

    public DataRow getLastGameFromDB(){
        SQLiteDatabase db=null;
        try {
            db = getReadableDatabase();
            DataRow row = null;
            String query =
                    "SELECT * FROM " + FeedReaderContract.FeedScore.TABLE_NAME +
                            " ORDER BY " + FeedReaderContract.FeedScore.COLUMN_NAME_ID + " DESC LIMIT 1";

            Cursor c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                row = new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3));
            }
            c.close();
            return row;
        }catch(Exception E){
            onCreate(db);
        }
        return null;
    }
}
