package justfun.theonepiano.com.numtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<String> textList;
    private TextView numText;
    private TextView timeText;
    private int level = 1;
    private int inputState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // nav
        setTitleForLevel();

        // content
        setContentView(R.layout.activity_main);

        textList = new ArrayList<>();

        numText = (TextView) findViewById(R.id.numtext);

        final Button resetBtn = (Button) findViewById(R.id.reset);
        if (resetBtn != null) {
            resetBtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    resetNum();
                }
            });
        }

        Button history = (Button) findViewById(R.id.history);
        if (history != null) {
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, HistoryActivity.class);

                    intent.setAction("justfun.history");
                    List<Map<String, Object>> data = getData();
                    intent.putExtra("justfun.history.data", (Serializable) data);

                    startActivity(intent);
                }
            });
        }

        Button levelBtn = (Button) findViewById(R.id.level);
        if (levelBtn != null) {
            levelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLevelChangeDialog();

                }
            });
        }

        Button inputBtn = (Button) findViewById(R.id.input);
        if (inputBtn != null) {
            inputBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inputState == 0) {
                        numText.setAlpha(0);
                        inputState = 1;
                    } else {
                        numText.setAlpha(1);
                        inputState = 0;
                    }

                }
            });
        }

        timeText = (TextView) findViewById(R.id.timetext);

    }

    void setTitleForLevel() {
        String title = "记数字  ( " + Integer.toString(level * 8) + "个 )";
        setTitle(title);
    }

    void resetNum() {
        String zero = "00000000";
        String finalNumStr = "";

        for (int i = 0; i < level; ++i) {
            Random r = new Random();
            int num = r.nextInt(99999999);
            String str = Integer.toString(num);
            String strWith0 = zero.substring(0, 8 - str.length()) + str;
            finalNumStr += strWith0;
        }

        numText.setText(finalNumStr);
        textList.add(finalNumStr);

        beginTiming();
        resetTime();
    }

    void showLevelChangeDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("选择等级")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(new String[] {
                                "等级1 8个",
                                "等级2 16个",
                                "等级3 24个",
                                "等级4 48个",
                                "等级5 72个"

                        }, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int lv = 1;
                                if (which == 0) lv = 1;
                                else if (which == 1) lv = 2;
                                else if (which == 2) lv = 3;
                                else if (which == 3) lv = 6;
                                else if (which == 4) lv = 9;
                                if (level != lv) {
                                    level = lv;
                                    setTitleForLevel();
                                    resetNum();
                                }
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton("取消", null).show();
    }

    private static final int msgKey1 = 1;
    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

    Boolean hasBeginTiming = false;
    void beginTiming() {
        if (!hasBeginTiming) {
            new TimeThread().start();
        }
    }

    long beginTime = 0l;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis();
                    long dis = sysTime - beginTime;
                    CharSequence sysTimeStr = DateFormat.format("mm:ss", dis);
                    timeText.setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };

    void resetTime() {
        beginTime = System.currentTimeMillis();
        timeText.setText("00:00");
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("title", "G2");
        map.put("time", "google 1");
        map.put("correct", "google 1");
        map.put("img", R.mipmap.i2);
        list.add(map);

        map = new HashMap<>();
        map.put("title", "G3");
        map.put("time", "google 2");
        map.put("correct", "google 1");
        map.put("img", R.mipmap.i2);
        list.add(map);

        map = new HashMap<>();
        map.put("title", "G4");
        map.put("time", "google 3");
        map.put("correct", "google 1");
        map.put("img", R.mipmap.i2);
        list.add(map);

        return list;
    }
}
