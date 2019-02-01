package tv.newtv.screening;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import newtv.tv.serverlibrary.httpServer;
import newtv.tv.serverlibrary.utils.wifiUtil;
import tv.newtv.screening.zxing.encoding.EncodingHandler;

/**
 * create by zhaikn on 2019/1/23
 */
public class tvShow extends Activity {
    private static String mLocalIP;
    private static Intent mServerService;
    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        onReceivePhoneCommand callBack = new onReceivePhoneCommand();
        httpServer.getInstance().sdkInit(callBack,getCacheDir().getAbsolutePath());
        mServerService = new Intent(getApplicationContext(), myServer.class);
        startService(mServerService);

        mContext = this.getApplicationContext();

        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            mLocalIP = wifiUtil.getIpAddress(this);
            Log.d(Constant.TAG,"========ip:" + mLocalIP);

            final Button btn = findViewById(R.id.wifi_name);
            btn.setText(wifiInfo.getSSID().replace("\"",""));

            Button deviceBtn = findViewById(R.id.device_name);
            deviceBtn.setText(android.os.Build.MODEL);

            createQR();
            mContext = this;

        }catch (Exception e){
            Log.e(Constant.TAG,"========" + e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            httpServer.getInstance().sdkExit();
            if(mServerService != null) {
                stopService(mServerService);
                Log.d(Constant.TAG,"========停止Service");
            }

            System.exit(0);
        }catch (Exception e) {
            Log.e(Constant.TAG,"========" + e.toString());
        }
    }

    /**
     * 生成二维码
     */
    private void createQR(){
        try {
            JSONObject QRInfo = new JSONObject();
            QRInfo.put("templateid",Constant.TEMPLATE_ID);
            QRInfo.put("deviceGroupId",Constant.DEVICE_GROUP_ID);
            QRInfo.put("districtCode",Constant.DISTRICT_CODE);
            QRInfo.put("lvpurl",Constant.LVP_URL);
            if(mLocalIP != null && !mLocalIP.isEmpty()) {
                QRInfo.put("TVdeviceid",mLocalIP + ":8080");
            } else {
                Log.e(Constant.TAG,"======Can't get local ip");
                Toast.makeText(this,"获取本地IP失败！",Toast.LENGTH_SHORT).show();
            }

            ImageView QRView = findViewById(R.id.QR_position);//new ImageView(this);
            Bitmap bitmap = EncodingHandler.createQRCode(QRInfo.toString(), 360);
            QRView.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class onReceivePhoneCommand implements httpServer.severCallBack{

        @Override
        public void onReceiveImg(String s) {

        }

        @Override
        public void onReceiveDocument(String s) {

        }

        @Override
        public void onReceiveVideo(String s) {

        }

        @Override
        public void onReceiveMusic(String s) {

        }

        @Override
        public void onReceiveCommand(String s) {
            try {
                Log.d(Constant.TAG,"========onReceiveCommand:" + s);
                JSONObject command = new JSONObject(s);
//                String type = command.getString("type");
//                if (type != null && type.equalsIgnoreCase("play_url")) {
//                    String playurl = command.getString("info");
//                    Toast.makeText(mContext,"手机传过来的地址\n" + playurl,Toast.LENGTH_LONG).show();
//                } else {
                    Log.d(Constant.TAG,"========onReceiveCommand else");
                    Intent intent = new Intent(mContext, PlayActivity.class);
                    startActivity(intent);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}