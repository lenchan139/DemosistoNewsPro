package news.demosisto.demosistonewspro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import news.demosisto.demosistonewspro.Class.PostToSQLite_DbIO;
import news.demosisto.demosistonewspro.Objects.PostItem;

public class ListViewActivty extends AppCompatActivity {
    PostToSQLite_DbIO dbio;
    ArrayList<PostItem> pi = new ArrayList<PostItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         ListView listView;
        ArrayAdapter<String> listAdapter;
        dbio = new PostToSQLite_DbIO(ListViewActivty.this);
            listView = (ListView)findViewById(R.id.listView);
       pi =  dbio.getAllPosts();
            ArrayList<String> listString = new ArrayList<String>();
            for(int i=0; i<pi.size();i++){
                listString.add(pi.get(i).getTitle());
                Log.v("1111",pi.get(i).getTitle());
            }
            listAdapter = new ArrayAdapter(this,R.layout.list_v,listString);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Snackbar.make(view,String.valueOf(pi.get(position)) , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intent = new Intent(ListViewActivty.this,ViewContentActivity.class);
                    intent.putExtra(getString(R.string.postIdKey), String.valueOf(pi.get(position).getPostId()));
                    startActivity(intent);
                }
                                            });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
