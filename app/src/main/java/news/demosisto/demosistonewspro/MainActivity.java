package news.demosisto.demosistonewspro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import news.demosisto.demosistonewspro.Class.PostToSQLite_DBHelper;
import news.demosisto.demosistonewspro.Class.PostToSQLite_DbIO;
import news.demosisto.demosistonewspro.Objects.PostItem;
import news.demosisto.demosistonewspro.Utils.StringUtils;

public class MainActivity extends AppCompatActivity {
    PostToSQLite_DbIO dbio = new PostToSQLite_DbIO(MainActivity.this);

    TextView txtVersion,loading;
    String domain;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    int prefId;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String version;
        PackageInfo pInfo = null;
        pref = getSharedPreferences("Preference", 0);
        Log.v("PrefId: " , pref.getString(getString(R.string.latestPostId_Key), "0"));
        prefId = Integer.parseInt(pref.getString(getString(R.string.latestPostId_Key),"0"));
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "ERROR: Get Version Code Failed!";
        }
        txtVersion = (TextView) findViewById(R.id.version);
        loading = (TextView) findViewById(R.id.txtLoading);
        txtVersion.setText("版本：" + version);
        domain = "demosisto.news";
            new SyncData().execute();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://news.demosisto.demosistonewspro/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://news.demosisto.demosistonewspro/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    class SyncData extends AsyncTask<String, Integer, Integer> {
        public void updateoading(String in){
            loading.setText(in);
        }
        int prefboo;
        Boolean a = false;
        String strAllPost[];
        int latestPostId = -1;
        @Override
        protected Integer doInBackground(String... param) {
            // !Get latest post id
            String startKey = "[{\"ID\":";
            String endKey = ",\"site_ID\":";
            Log.v("HTML", "AAAAAAA");
            String html = null;
            //get JSON: latest post
            try {
                html = Jsoup.connect("https://public-api.wordpress.com/rest/v1.1/sites/" + domain + "/posts/").ignoreContentType(true).get().html();
                Log.v("html", String.valueOf(html));
                a = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //find latest post id
            if(html!=null) {
                StringUtils sus = new StringUtils();
                String strPostTrimmed = sus.cutString(html,startKey,endKey);
                latestPostId = Integer.parseInt(strPostTrimmed);
            }else{
                Toast.makeText(MainActivity.this,
                                "網絡錯誤：請檢查你嘅網絡連線！" +
                                "\n" +
                                "Network Error: Please check your network connection!", Toast.LENGTH_SHORT)
                        .show();
            }

            if(prefId == 0){
                prefboo = 1;
            }else if(prefId == latestPostId){
                prefboo = 2;
            }
            else{
                prefboo = 3;
            }
           if(prefboo==1) {
                //! get all posts
                strAllPost = new String[latestPostId + 1];
                int count = 0;
                for (int i = latestPostId; i > 0; i--) {
                    if (i == 1000) {
                        break;
                    }
                    String thisHtml = null;
                    count++;
                    publishProgress(count);
                    try {
                        thisHtml = Jsoup
                                .connect("https://public-api.wordpress.com/rest/v1.1/sites/" + domain + "/posts/" + i)
                                .ignoreContentType(true).get().body().text();
                    } catch (HttpStatusException e) {
                        Log.v("Warning: ", "HttpStatusException Was Skipped." + "PostId: " + i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (thisHtml != null) {
                        StringUtils sus = new StringUtils();
                        strAllPost[i] = thisHtml;
                    } else {
                        strAllPost[i] = thisHtml;
                    }
                }
            }else if(prefId  == 2) {
           }else{
                int count = 0;
               strAllPost = new String[latestPostId+1];
               int count1 = latestPostId-prefId;
                for (int i = latestPostId ; i > prefId; i--) {
                    String thisHtml = null;
                    count++;
                    publishProgress(prefId+count);
                    try {
                        thisHtml = Jsoup
                                .connect("https://public-api.wordpress.com/rest/v1.1/sites/" + domain + "/posts/" + i)
                                .ignoreContentType(true).get().body().text();
                    } catch (HttpStatusException e) {
                        Log.v("Warning: ", "HttpStatusException Was Skipped." + "PostId: " + i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (thisHtml != null) {
                        StringUtils sus = new StringUtils();
                        strAllPost[i] = thisHtml;
                    } else {
                    }
                }
            }
            SharedPreferences pref = getSharedPreferences("Preference", 0);
            pref.edit().putString("name", String.valueOf(latestPostId)).commit();
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            //showData();
            //Convert Jason to PostItem Obj
            int intVailble = 0;
            StringUtils s =new StringUtils();
            ArrayList<PostItem> aryPostList = new ArrayList<PostItem>();

            if(prefboo == 1) {
                getApplicationContext().deleteDatabase(getApplicationContext().getDatabasePath("demo.db").getAbsolutePath());
            }
            dbio = new PostToSQLite_DbIO(MainActivity.this);
            if(strAllPost!=null && strAllPost.length >= 1) {
                for (int i = strAllPost.length - 1; i >= 1; i--) {
                    if (strAllPost[i] != null) {
                        PostItem pi = new PostItem(
                                s.jsonFinder(strAllPost[i], "ID"),
                                s.jsonFinder(strAllPost[i], "date"),
                                s.jsonFinder(strAllPost[i], "URL"),
                                s.jsonFinder(strAllPost[i], "short_URL"),
                                s.jsonFinder(strAllPost[i], "content"),
                                s.jsonFinder(strAllPost[i], "title"));
                        aryPostList.add(pi);
                        Log.v("PostItem:", pi.toString());
                        updateToDatabase(pi);
                    }
                }
            }


                pref.edit().putString(getString(R.string.latestPostId_Key), String.valueOf(latestPostId)).commit();


            if(a){
                Intent intent = new Intent(MainActivity.this,ListViewActivty.class);
                MainActivity.this.startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            updateoading(values[0] + "/" + latestPostId);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }


   public void updateToDatabase(PostItem postItem){
       dbio.insert(postItem);
    }
}
