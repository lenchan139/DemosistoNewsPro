package news.demosisto.demosistonewspro.Class;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import news.demosisto.demosistonewspro.Objects.PostItem;

/**
 * Created by len on 9/13/16.
 */
public class PostToSQLite_DbIO {
    PostToSQLite_DBHelper db_O;

    String TABLE_NAME = "POST_SAVING";
    public PostToSQLite_DbIO(Context c){
        db_O = new PostToSQLite_DBHelper(c);
    }

    public void rebuildDB(){
        int version = db_O.getReadableDatabase().getVersion();
        db_O.onUpgrade(db_O.getWritableDatabase(),version,version+1);
    }
    public void insert(PostItem pi){
        SQLiteDatabase db = db_O.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME +
                    " (" + "postId,date,url,short_url,content,title) VALUES( " +
                pi.getPostId() + ",\""+
                pi.getDate() + "\",\"" +
                pi.getURL() + "\",\"" +
                pi.getShort_URL() + "\",\"" +
                pi.getContent() + "\",\"" +
                pi.getTitle() + "\");");
        db.close();
    }
    public ArrayList<PostItem> getAllPosts() {
        SQLiteDatabase db = db_O.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY `postId` DESC";
        Cursor cursor      = db.rawQuery(selectQuery, null);

        PostItem[] data = new PostItem[cursor.getCount()];

        ArrayList<PostItem> result = new ArrayList<PostItem>();
        cursor.moveToFirst();
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                result.add(new PostItem(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)));
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.v("LENGTH",result.get(0).toString());
        return result;
    }


    public PostItem getPostById(int postId ) {
        SQLiteDatabase db = db_O.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY `postId` DESC";
        Cursor cursor      = db.rawQuery(selectQuery, null);

        PostItem[] data = new PostItem[cursor.getCount()];

        ArrayList<PostItem> result = new ArrayList<PostItem>();
        cursor.moveToFirst();
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                if(Integer.parseInt(cursor.getString(1)) == postId) {
                    return new PostItem(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                }
                    count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.v("LENGTH",result.get(0).toString());
        return null;
    }
}
