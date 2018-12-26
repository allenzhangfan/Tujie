package com.netposa.common.entity.push;

import com.google.gson.annotations.SerializedName;

public class AlarmMessage {

    /**
     * content : {"alarmId":"f725c3f2-d024-4ff2-90dd-79f776369d12","gisMessage":"0","msg":{"absTime":1544522794000,"adminList":"[]","alarmAddress":"baitian","alarmLevel":"1","alarmStatus":"0","alarmTime":1544522794119,"alarmType":"1","deviceId":"fxSJ000139MWIx1V","ext":"{\"vehicleColor\":\"J\",\"monitorId\":\"fxSJ000139MWIx1V\",\"libId\":\"610b325e-addd-e02b-a648-9f2fcfcf25b0\",\"plateType\":\"2\",\"targetImages\":[\"PFSB:/viasbimg/bimg/data/20181211/15_0/ee8b45381243394b98b7bdcd4709a985\"],\"monitorName\":\"baitian\",\"passTime\":\"2018-12-11 18:04:56\",\"libName\":\"车辆库03\",\"alarmObjName\":\"桂A6P603\",\"speed\":\"0.0\",\"plateColor\":\"2\",\"vehicleBrand\":\"125\",\"imageURLs\":\"http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181211/18_0/38ccddee629b4cd57788928ffe754129\",\"x\":\"114.4045808831109\",\"y\":\"30.49291132658303\",\"vehicleType\":\"K33\"}","faceId":null,"id":"f725c3f2-d024-4ff2-90dd-79f776369d12","idCard":null,"imageUrl":"http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181211/18_0/38ccddee629b4cd57788928ffe754129","latitude":30.49291132658303,"libId":"610b325e-addd-e02b-a648-9f2fcfcf25b0","libName":"车辆库03","loginId":"11230d93-e406-49a8-82be-039a6c516ff2,af2005b8-59df-4a71-a231-e0e6d232b60c,c05fc8c8-8cb3-418b-b130-994df4a0a103","longitude":114.4045808831109,"name":null,"number":0,"realAlarm":-1,"recordId":"c8e66958-b7d0-4e56-9054-c08daf72dd8b","score":0,"sex":null,"source":"pvd","status":"0","targetImage":null,"taskId":"9d403df7-3d59-4cfa-b767-5773bd798a94"},"systemMessage":"0","type":"VEHICLE_ALARM","userId":"c05fc8c8-8cb3-418b-b130-994df4a0a103"}
     * title : 您收到一条报警，请及时查看！
     * type : VEHICLE_ALARM
     */
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
