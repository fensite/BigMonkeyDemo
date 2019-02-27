package com.fensite.scanning.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class H5ScreenActivity extends AppCompatActivity {
    private static String TAG = "screening_h5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_screen);

        String sData = getIntent().getStringExtra("param4h5").replace(" ","");
        if(sData == null || sData.isEmpty()){
            Log.i(TAG,"getStringExtra为空，取默认值");
           sData = "http://192.168.199.163/index.html?lvpurl=http://203.195.197.20:8080&deviceGroupId=230100&templateid=0453&districtCode=230100&TVdeviceid=192.168.199.152:8090&Phonedeviceid=b289ac35f046eb2bf2928d324a3c9c9f&tv_assistant_url=1&version=6640";
        }
        Log.i("screening_client","param:" + sData);
        WebView wView = (WebView)findViewById(R.id.h5_screen_ui);
        wView.loadUrl(sData);
    }
}
