package news.demosisto.demosistonewspro.Class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by len on 9/13/16.
 */
public class PostToSQLite_DBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "demo.db";
    private static int DATABASE_VERSION = 1;
    String TABLE_NAME = "POST_SAVING";
    SQLiteDatabase db;

    public PostToSQLite_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db1) {
        db=db1;
        //private int postId;
        //String date,URL,short_URL,content,title;

        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "postId INTEGER," +
                "date VARCHAR," +
                "url VARCHAR," +
                "short_url VARCHAR," +
                "content VARCHAR," +
                "title VARCHAR);";
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
