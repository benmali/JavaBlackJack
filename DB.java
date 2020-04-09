package com.example.blackjackv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class DB {
    private static final String KEY_ID = "id";
    private static final String KEY_MONEY = "money";
    private static final String KEY_WINS = "wins";
    private static final String KEY_LOSES= "loses";
    private static final String KEY_TIES= "ties";

    private final String DB_NAME = "BlackJackDB";
    private final String DB_TABLE_STATS = "Statistics";
    private final String DB_TABLE_MONEY = "PlayerMoney";
    private final int DB_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDB;

    public DB(Context context){
        ourContext=context;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE PlayerMoney" +
                    "(id INTEGER PRIMARY KEY, money DOUBLE NOT NULL);";
            db.execSQL(query); //executing SQL query to create Table
            String query2 =  "CREATE TABLE Statistics (id INTEGER PRIMARY KEY,wins INTEGER, " +
                             "loses INTEGER, ties INTEGER);";
            db.execSQL(query2);
            String query3 = "CREATE TABLE Decisions (id INTEGER PRIMARY KEY,correct INTEGER," +
                    "incorrect INTEGER );";
            db.execSQL(query3);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            //dropping the old table without saving info on version change
            db.execSQL("DROP TABLE IF EXISTS PlayerMoney");
            db.execSQL("DROP TABLE IF EXISTS Statistics");
            db.execSQL("DROP TABLE IF EXISTS Decisions");
            onCreate(db);

        }
    }

        public DB open() throws SQLException
        {
            ourHelper = new DBHelper(ourContext);
            ourDB = ourHelper.getWritableDatabase();
            return this;
        }

        public void close() {
            ourHelper.close();

        }

        public long insertToPlayerMoney(int id, double money){
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID,id); // insert to DB (id,money) pair in Players Money table
            cv.put(KEY_MONEY,money);
            return ourDB.insert(DB_TABLE_MONEY,null,cv);
        }



        public ArrayList<int []> getPlayerStats(){
            try {
                this.open();
                String [] columns = new String []{KEY_ID,KEY_WINS,KEY_LOSES,KEY_TIES};
                Cursor cursor = ourDB.query(DB_TABLE_STATS, columns, null,
                        null, null, null, null);
                ArrayList<int []> playerStats = new ArrayList<>();
                // getting columns indexes
                int userIDIndex = cursor.getColumnIndex(KEY_ID);
                int winsIndex = cursor.getColumnIndex(KEY_WINS);
                int losesIndex = cursor.getColumnIndex(KEY_LOSES);
                int tiesIndex = cursor.getColumnIndex(KEY_TIES);

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    int [] row = new int []{cursor.getInt(userIDIndex),
                            cursor.getInt(winsIndex),
                            cursor.getInt(losesIndex),
                            cursor.getInt(tiesIndex)};

                    playerStats.add(row);
                }
                cursor.close();
                this.close();
                return playerStats;
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        public long deleteFromDB(String id){
            //question mark will be replaced by the string array parsed down to method
            //will delete all rows where where the user id is equal to the parsed value
            return 0;
        }

        public long updatePlayerMoney(int id,double money){
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID,id);
            cv.put(KEY_MONEY,money);
            return ourDB.update(DB_TABLE_MONEY,cv,KEY_ID + "=?",new String[]{String.valueOf(id)});
        }

        public void updatePlayerStats(int id, int win,int lose,int tie){
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID,id);
            cv.put(KEY_WINS,win);
            cv.put(KEY_LOSES,lose);
            cv.put(KEY_TIES,tie);
            ourDB.update(DB_TABLE_STATS,cv,KEY_ID + "=?",new String[]{String.valueOf(id)});
        }

        public void resetStats(){
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID,1);
            cv.put(KEY_WINS,0);
            cv.put(KEY_LOSES,0);
            cv.put(KEY_TIES,0);
            ourDB.update(DB_TABLE_STATS,cv,KEY_ID + "=?",new String[]{String.valueOf(1)});

        }
}
