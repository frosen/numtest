package justfun.theonepiano.com.numtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luleyan on 2016/11/9.
 */
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("历史");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_history);

        ListView listView = new ListView(this);
        setContentView(listView);

        Intent intent = getIntent();
        List<Map<String, Object>> data = (List<Map<String, Object>>) intent.getSerializableExtra("justfun.history.data");

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.vlist,
                new String[]{"title", "time", "correct", "img"},
                new int[]{R.id.title, R.id.timeT, R.id.correct, R.id.img}
        );
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("history", "返回");

        if(id == android.R.id.home) {//back key
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
