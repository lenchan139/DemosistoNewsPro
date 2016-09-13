package news.demosisto.demosistonewspro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import news.demosisto.demosistonewspro.Class.PostToSQLite_DbIO;
import news.demosisto.demosistonewspro.Objects.PostItem;

public class ViewContentActivity extends AppCompatActivity {
    PostItem pi;
    int postId;
    PostToSQLite_DbIO dbio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbio = new PostToSQLite_DbIO(this);
        Intent intentP = getParentActivityIntent();

        Bundle bundle = getIntent().getExtras();
        String yo;
        if (bundle != null) {
           yo=  bundle.getString(getString(R.string.postIdKey));
        }else{
            yo="ERROR";
        }
        Log.v("",yo);
        postId = Integer.parseInt(yo);
        pi = dbio.getPostById(postId);
        TextView title = (TextView) findViewById(R.id.title);
        TextView date = (TextView) findViewById(R.id.date);
        WebView content = (WebView) findViewById(R.id.content);
        title.setText(pi.getTitle());
        date.setText(pi.getDate());
        content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        content.getSettings().setDefaultTextEncodingName("utf-8");
        content.loadDataWithBaseURL(null,pi.getContent(),"text/html","utf-8",null);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "共享畀朋友仔：");
                share.putExtra(Intent.EXTRA_TEXT, pi.getShort_URL());
                startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
    }

}
