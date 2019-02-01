package tv.newtv.screening;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import newtv.tv.serverlibrary.httpServer;

import static tv.newtv.screening.Constant.TAG;

/**
 * create by zhaikn on 2019/1/24
 */
public class myServer extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启HTTP服务
        httpServer.getInstance().sdkStart();
        Log.d(TAG,"Server start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        httpServer.getInstance().sdkExit();
    }

}
