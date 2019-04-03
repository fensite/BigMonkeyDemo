package com.fensite.scanning.client;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, String url) {
                if(url == null) return false;

                try {
                    if(url.startsWith("weixin://") //微信
                            || url.startsWith("alipays://") //支付宝
                            || url.startsWith("mailto://") //邮件
                            || url.startsWith("tel://")//电话
                            || url.startsWith("dianping://")//大众点评
                            || url.startsWith("js2cmd://") //影音投屏
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                //处理http和https开头的url
                wv.loadUrl(url);
                return true;
            }
        };
        wView.setWebViewClient(webViewClient);
        wView.loadUrl(sData);
    }
}
