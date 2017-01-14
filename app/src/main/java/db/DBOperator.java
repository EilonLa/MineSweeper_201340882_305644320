package db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by eilon & dvir on 29/11/2016.
 */

public class DBOperator extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "MinesweeperDB";

    public Activity activity;

    public DBOperator(Activity context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.activity = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(FeedReaderContract.CREATE_TABLE);
        } catch (Exception e) {
            onUpgrade(db, DB_VERSION, 0);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(FeedReaderContract.DELETE_SCORE_TABLE);
        onCreate(db);
    }

    public void addRow(DataRow dr) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dbCursor = db.query(FeedReaderContract.FeedScore.TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_PLAYER_NAME, dr.getmName());
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_LEVEL, dr.getLevel());
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_SCORE, dr.getScore());
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_LONG, dr.getLongtitude());
        values.put(FeedReaderContract.FeedScore.COLUMN_NAME_LAT, dr.getLatidude());
        // Inserting Row
        db.insert(FeedReaderContract.FeedScore.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public DataRow getHighScoreFromDB(int level) {
        SQLiteDatabase db = getReadableDatabase();
        DataRow row = null;
        String query =
                "SELECT *  FROM " + FeedReaderContract.FeedScore.TABLE_NAME +
                        " WHERE " + FeedReaderContract.FeedScore.COLUMN_NAME_LEVEL + "=" + level +
                        " ORDER BY " + FeedReaderContract.FeedScore.COLUMN_NAME_SCORE +
                        " ASC LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            if (c.moveToFirst())
                row = new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5)));
        }
        c.close();
        return row;
    }

    public ArrayList<DataRow> getAllGames() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DataRow> rows = new ArrayList<>();
        String query =
                "SELECT *  FROM " + FeedReaderContract.FeedScore.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            rows.add(new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))));
            while (c.moveToNext())
                rows.add(new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))));
        }
        c.close();
        for (DataRow row : rows)
            Log.i("Row:", "name: "+row.getmName()+ " score: "+row.getScore() +" level: "+ row.getLevel());
        return rows;
    }


    public ArrayList<DataRow> getTopTen(int level) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DataRow> rows = new ArrayList<>();
        String query =
                "SELECT *  FROM " + FeedReaderContract.FeedScore.TABLE_NAME +
                        " WHERE " + FeedReaderContract.FeedScore.COLUMN_NAME_LEVEL + "=" + level +
                        " ORDER BY " + FeedReaderContract.FeedScore.COLUMN_NAME_SCORE +
                        " ASC LIMIT 10";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            rows.add(new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))));
            while (c.moveToNext())
                rows.add(new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))));
        }
        c.close();
        return rows;
    }

    public DataRow getLastGameFromDB() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            DataRow row = null;
            String query =
                    "SELECT * FROM " + FeedReaderContract.FeedScore.TABLE_NAME +
                            " ORDER BY " + FeedReaderContract.FeedScore.COLUMN_NAME_ID + " DESC LIMIT 1";
            Cursor c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                row = new DataRow(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5)));
            }
            c.close();
            return row;
        } catch (Exception E) {
            Log.i("", "creating data base");
            onCreate(db);
        }
        return null;
    }
}
