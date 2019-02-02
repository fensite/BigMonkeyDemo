package tv.newtv.screening.utils;

/**
 * create by zhaikn on 2019/2/2
 */
public class ScanningInfo {
    /**
     * {
     *     "type":"",             //类型:  liveUrl(直播地址)、vodUrl(点播地址)、remoteControl(遥控)、livePath(直播地址参数)、requestConnect(请求连接,手机应用扫码后需向TV端发送请求)
     *     "action":"",           //动作： play(播放)、up(遥控上键--换台)、down(遥控下键--换台)、return(遥控返回--退出播放)、turnUpVolume(增大音量)、turnDownVolume(减小音量)
     *     "url":"",              //如果是节目则为节目或直播流的地址；如果是遥控切换台，则附加对应节目或直播流的地址
     *     "path":"",             //如果直播地址无法给出，则将直播所需的参数赋值到该key对应的value
     *     "startDuration":       //如果是点播则传值起播时长 Long类型
     * }
     */
    public String m_type;
    public String m_action;
    public String m_url;
    public String m_path;
    public Long m_startDuration = 0L;
}
