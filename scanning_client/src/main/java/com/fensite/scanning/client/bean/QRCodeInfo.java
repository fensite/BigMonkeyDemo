package com.fensite.scanning.client.bean;

import com.google.gson.annotations.Expose;

/**
 * create by zhaikn on 2019/2/27
 */
public class QRCodeInfo {
/**
 * {"templateid":"0453",
 * "deviceGroupId":"230100",
 * "districtCode":"230100",
 * "lvpurl":"http://203.195.197.20:8080",
 * "TVdeviceid":"192.168.199.152:8090"}
 */
    @Expose
    private String templateid;

    @Expose
    private String deviceGroupId;

    @Expose
    private String districtCode;

    @Expose
    private String lvpurl;

    @Expose
    private String TVdeviceid;

    public QRCodeInfo(){

    }

    public String getTemplateid(){return templateid;}
    public void setTemplateid(String templateid){this.templateid = templateid;}

    public String getDeviceGroupId(){return deviceGroupId;}
    public void setDeviceGroupId(String deviceGroupId){this.deviceGroupId = deviceGroupId;}

    public String getDistrictCode(){return districtCode;}
    public void setDistrictCode(String districtCode){this.districtCode = districtCode;}

    public String getLvpurl(){return lvpurl;}
    public void setLvpurl(String lvpurl){this.lvpurl = lvpurl;}

    public String getTVdeviceid(){return TVdeviceid;}
    public void setTVdeviceid(String TVdeviceid){this.TVdeviceid = TVdeviceid;}
}
