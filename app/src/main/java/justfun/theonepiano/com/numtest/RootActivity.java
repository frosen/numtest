package justfun.theonepiano.com.numtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

/**
 * Created by luleyan on 2016/12/9.
 */

public class RootActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_layout);

        RadioGroup rg = (RadioGroup)findViewById(R.id.tabbar);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bottom_tabbar_rb_1:
                        Log.d("tt", "1");
                        break;
                    case R.id.bottom_tabbar_rb_2:
                        Log.d("tt", "2");
                        break;
                    case R.id.bottom_tabbar_rb_3:
                        Log.d("tt", "3");
                        break;
                }
            }
        });

    }
}
