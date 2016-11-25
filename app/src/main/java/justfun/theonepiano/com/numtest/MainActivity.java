package justfun.theonepiano.com.numtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private List<String> textList;
    private TextView numText;
    private TextView timeText;
    private Button btn;
    private EditText inputField;

    private int level = -1;
    private int totalTime = -1; //要用的分钟数
    private long useTime = 0; //所用时间
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
                Uri.parse("android-app://justfun.theonepiano.com.numtest/http/host/path")
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
                Uri.parse("android-app://justfun.theonepiano.com.numtest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public enum State {
        ready,
        begin,
        input,
        result,
    }

    private State playState = State.ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // nav
        setTitleForLevel();

        // content
        setContentView(R.layout.activity_main);

        textList = new ArrayList<>();

        numText = (TextView) findViewById(R.id.numtext);
        timeText = (TextView) findViewById(R.id.timetext);

        btn = (Button) findViewById(R.id.begin);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    onPressBtn();
                }
            });
        }

        inputField = (EditText) findViewById(R.id.editText);
        inputField.setAlpha(0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    void setTitleForLevel() {
        String title;
        if (level <= 0 || totalTime <= 0) {
            title = "数字记忆王";
        } else {
            title = "数字记忆王  ( " + Integer.toString(level * 8) + "个 用时 " + Integer.toString(totalTime) + "分钟 )";

        }
        setTitle(title);
    }

    void onPressBtn() {
        switch (playState) {
            case result:
            case ready:
                showLevelChangeDialog();
                break;
            case begin:
                resetState(State.input);
                break;
            case input:
                showConfirmAnswerDialog();
                break;
            default:
                break;
        }
    }

    void resetState(State st) {
        playState = st;
        switch (st) {
            case ready:
                btn.setText("开始新的一局");
                break;
            case begin:
                btn.setText("直接开始默写");
                break;
            case input:
                btn.setText("提交答案");
                inputField.setAlpha(1);
                inputField.setText("");
                numText.setAlpha(0);
                break;
            case result:
                btn.setText("开始新的一局");
                inputField.setAlpha(0);
                numText.setAlpha(1);

                calculateResultAndShow();
                break;
            default:
                break;
        }
    }

    void showLevelChangeDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("选择等级")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setItems(new String[]{
                        "等级1 8个",
                        "等级2 16个",
                        "等级3 24个",
                        "等级4 48个",
                        "等级5 72个",
                        "等级6 120个",

                }, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int lv = 1;
                        if (which == 0)
                            lv = 1;
                        else if (which == 1)
                            lv = 2;
                        else if (which == 2)
                            lv = 3;
                        else if (which == 3)
                            lv = 6;
                        else if (which == 4)
                            lv = 9;
                        else if (which == 5)
                            lv = 15;

                        level = lv;

                        dialog.dismiss();
                        showTimeChangeDialog();
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    void showTimeChangeDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("选择用时")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setItems(new String[]{
                                "3分钟",
                                "4分钟",
                                "5分钟",
                                "6分钟",
                                "8分钟"

                        },
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int t = 3;
                                if (which == 0)
                                    t = 3;
                                else if (which == 1)
                                    t = 4;
                                else if (which == 2)
                                    t = 5;
                                else if (which == 3)
                                    t = 6;
                                else if (which == 4)
                                    t = 8;

                                totalTime = t;
                                setTitleForLevel();

                                dialog.dismiss();

                                resetNum();
                            }
                        }
                )
                .setNegativeButton("取消", null).show();
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

        numText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (level <= 9 ? 48 : 34));

        resetState(State.begin);
        beginTiming();
        resetTime();
    }

    private boolean stopTimeThread = false;
    private static final int msgKey1 = 1;

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    if (stopTimeThread == true) {
                        break;
                    }
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    Boolean hasBeginTiming = false;

    void beginTiming() {
        if (!hasBeginTiming) {
            stopTimeThread = false;
            new TimeThread().start();
        }
    }

    long beginTime = 0l;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis();
                    long dis = sysTime - beginTime;
                    handleTime(dis);
                    break;

                default:
                    break;
            }
        }
    };

    void handleTime(long time) {
        useTime = time;
        CharSequence sysTimeStr = DateFormat.format("mm:ss", time);
        timeText.setText(sysTimeStr);
        if (time >= totalTime * 60 * 1000) {
            stopTimeThread = true;
            resetState(State.input);
        }
    }

    void resetTime() {
        beginTime = System.currentTimeMillis();
        timeText.setText("00:00");
    }

    void showConfirmAnswerDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("您确定要提交答案吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetState(State.result);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    void calculateResultAndShow() {
        String input = inputField.getText().toString();
        String answer = numText.getText().toString();
        int inputLength = input.length();

        List<Integer> wrongIndex = new ArrayList<>();

        for (int i = 0; i < answer.length(); i++) {
            if (i >= inputLength) {
                for (int j = i; j < answer.length(); j++) {
                    wrongIndex.add(j);
                }
                break;
            }
            char answerCh = answer.charAt(i);
            char inputCh = input.charAt(i);

            if (answerCh != inputCh) {
                wrongIndex.add(i);
            }
        }

        // 显示
        if (wrongIndex.size() > 0) {
            SpannableStringBuilder spannableStr = new SpannableStringBuilder(answer);
            for (int i = 0; i < wrongIndex.size(); i++) {
                int charIndex = wrongIndex.get(i);
                spannableStr.setSpan(new ForegroundColorSpan(Color.RED),
                        charIndex, charIndex + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }

            numText.setText(spannableStr);
        }

        // 计算正确率
        int rate = 100 - wrongIndex.size() * 100 / answer.length();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("显示结果")
                .setMessage("正确率：" + String.valueOf(rate) + "%")
                .setPositiveButton("确定", null)
                .show();
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
