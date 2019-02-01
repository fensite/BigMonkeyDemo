package tv.newtv.screening;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import tv.newtv.screening.utils.CommonUtil;
import tv.newtv.screening.utils.httpClient;
import tv.newtv.screening.zxing.activity.CaptureActivity;

import static tv.newtv.screening.utils.httpClient.httpPostCommand;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initPermission();

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

        findViewById(R.id.start_screening_assistant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, tvShow.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.start_screening_assistant).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    view.setBackgroundColor(Color.parseColor("#031343"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF2E364E"));
                }
            }
        });

        findViewById(R.id.start_phone_scanning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtil.isCameraCanUse()){
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    Toast.makeText(mContext,"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.start_phone_scanning).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    view.setBackgroundColor(Color.parseColor("#031343"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF2E364E"));
                }
            }
        });

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            TextView show = findViewById(R.id.show);
            show.setText(scanResult);
            try {
                JSONObject jsonObject = new JSONObject(scanResult);
                String url = jsonObject.optString("TVdeviceid");
                Log.d(Constant.TAG,"========TVdeviceid:" + url);

                LinearLayout createInfo = (LinearLayout)findViewById(R.id.create_info);
                Button button = createFilesInfoButton("发送给TV", url , "info");
                createInfo.addView(button);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    /**
     * 创建搜索到的文件按钮
     * @param fileInfo
     * @return
     */
    private Button createFilesInfoButton(final String fileInfo, final  String url, final String info){
        Button child = new Button(this);
        child.setText(fileInfo);
        child.setTextSize(15);
        child.setEnabled(true);
        child.setTextColor(Color.RED);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        httpClient.httpPostCommand(url, info);
                        JSONObject json = new JSONObject();
                        try {
                            json.put("name","value");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        postCommand(json.toString(), "http://" + url);
                    }
                });
                thread.start();
                //当按钮点击时调用

                LinearLayout createInfo = (LinearLayout)findViewById(R.id.create_info);
                createInfo.removeAllViews();
            }
        });
        return child;
    }

    /**
     * post json数据
     * @param jsonStr
     * @param path
     * @return
     */
    public synchronized int postCommand(String jsonStr, String path){
        httpPostCommand(path + File.separator + "postCommand", jsonStr);
        return 0;
    }

}
