package com.fensite.scanning.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fensite.scanning.client.bean.QRCodeInfo;
import com.fensite.scanning.client.utils.CommonUtil;
import com.fensite.scanning.client.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.fensite.scanning.client.utils.httpClient.httpPostCommand;

public class MainActivity extends AppCompatActivity {
    private ImageView m_welcomeImg = null;
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;
    private static Context mContext;
    private static String TAG = "screening_client";
    private EditText mH5ServerAddressText;
    private String mH5ServerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            Log.i(TAG,"scanResult: " + scanResult);
            startH5(scanResult);
            //将扫描出的信息显示出来
//            TextView show = findViewById(R.id.show);
//            show.setText(scanResult);
//            try {
//                JSONObject jsonObject = new JSONObject(scanResult);
//                String url = jsonObject.optString("TVdeviceid");
//                Log.d("screening_client","========TVdeviceid:" + url);
//
//                LinearLayout createInfo = (LinearLayout)findViewById(R.id.create_info);
//                Button button = createFilesInfoButton("发送给TV", url , "info");
//                createInfo.addView(button);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
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
                            json.put("type","liveUrl");
                            json.put("action","play");
                            json.put("url","http://live1.cloud.ottcn.com/live/3db08add54284dbd871475c9c8b6ff51/a28ae072f63b416a975798647563a2be.m3u8");
                            json.put("path","");
                            json.put("startDuration",0L);
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

    private void startH5(String QRInfo){
        QRCodeInfo qrci = new QRCodeInfo();
        try {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            qrci =  gson.fromJson(QRInfo, QRCodeInfo.class);
        } catch(JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(qrci != null){
            StringBuilder param = new StringBuilder();
            String H5ServerAddress = mH5ServerAddressText.getText().toString();
            if(H5ServerAddress == null || H5ServerAddress.isEmpty()){
                param.append("http://192.168.199.163/index.html?");
            } else {
                Log.i(TAG,"输入的h5服务器地址：" + H5ServerAddress);
                if(H5ServerAddress.contains("http://")){
                    param.append(H5ServerAddress + "/index.html?");
                } else {
                    param.append("http://" + H5ServerAddress + "/index.html?");
                }
            }
            param.append("lvpurl=" + qrci.getLvpurl());
            param.append("&deviceGroupId=" + qrci.getDeviceGroupId());
            param.append("&templateid=" + qrci.getTemplateid());
            param.append("&districtCode=" + qrci.getDistrictCode());
            param.append("&TVdeviceid=" + qrci.getTVdeviceid());
            param.append("&Phonedeviceid=b289ac35f046eb2bf2928d324a3c9c9f&tv_assistant_url=1&version=6640");
            Intent intent = new Intent(this,H5ScreenActivity.class);
            intent.putExtra("param4h5",param.toString());
            startActivity(intent);
        } else {
            Log.e(TAG, "二维码信息解析失败");
        }
    }

    /**
     * 初始化操作：
     * id绑定等
     */
    private void init(){
        findViewById(R.id.start_scanner).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.start_scanner).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    view.setBackgroundColor(Color.parseColor("#031343"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF2E364E"));
                }
            }
        });

        mH5ServerAddressText = findViewById(R.id.h5_server_address);
    }
}
