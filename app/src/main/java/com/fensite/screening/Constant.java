package com.fensite.screening;

/**
 * create by zhaikn on 2019/1/24
 */
public class Constant {
    public static final String TAG = "scanning";

    /**
     * templateid	模板id
     * deviceGroupId	设备分组
     * districtCode	区域code
     * lvpurl	看点服务接口地址
     * TVdeviceid	TV端终端ID
     * http://203.195.197.20:8080/ysten-lvoms-epg/epg/getAssortCatgs.shtml?
     * deviceGroupId=230100&districtCode=230100&templateId=0453&random=0.04342376652950297
     * http://203.195.197.20:8080/ysten-lvoms-epg/epg/getNewPrograms.shtml?catgId=1214
     * &actionType=cctv&deviceGroupId=230100&districtCode=230100&templateId=0453
     * &random=0.5874712889735525
     */
    public static final String TEMPLATE_ID = "0453";
    public static final String DEVICE_GROUP_ID = "230100";
    public static final String DISTRICT_CODE = "230100";
    public static final String LVP_URL = " http://203.195.197.20:8080/ysten-lvoms-epg/epg/getNewPrograms.shtml";
    public static final String TV_DEVICE_ID = "ip+port";

}
