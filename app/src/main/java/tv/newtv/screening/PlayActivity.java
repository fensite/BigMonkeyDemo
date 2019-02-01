package tv.newtv.screening;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

/**
 * create by zhaikn on 2019/1/25
 */
public class PlayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_ui);
        VideoView videoView = (VideoView)findViewById(R.id.play_view);
        videoView.setVideoURI(Uri.parse("http://live1.cloud.ottcn.com/live/3db08add54284dbd871475c9c8b6ff51/a28ae072f63b416a975798647563a2be.m3u8"));
        videoView.start();
    }
}
