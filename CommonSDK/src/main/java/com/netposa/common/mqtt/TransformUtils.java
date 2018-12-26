package com.netposa.common.mqtt;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.entity.push.AlarmCarDetailResponseForExt;
import com.netposa.common.entity.push.AlarmPeopleDetailResponseForExt;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.common.entity.push.AlarmMessage;
import com.netposa.common.entity.push.AlarmMessageContent;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.room.entity.AlarmMessageEntity;

import java.util.ArrayList;

import static com.netposa.common.constant.GlobalConstants.TYPE_CAR_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_FACE_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_FAMALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_MALE;

public final class TransformUtils {

    private static final String TAG = TransformUtils.class.getSimpleName();

    public static AlarmMessageEntity copyToAlarmMessage(AlarmMessage message,Gson gson) {
        AlarmMessageContent alarmContent = gson.fromJson(message.getContent(), AlarmMessageContent.class);
        AlarmMessageEntity entity = new AlarmMessageEntity();   // 构造函数中初始化数据库主键uuid
        entity.setTitle(message.getTitle());
        entity.setType(message.getType());

        entity.setAbsTime(alarmContent.getAbsTime());
        entity.setAlarmAddress(alarmContent.getAlarmAddress());
        entity.setAlarmLevel(alarmContent.getAlarmLevel());
        entity.setAlarmStatus(alarmContent.getAlarmStatus());
        entity.setAlarmTime(alarmContent.getAlarmTime());
        entity.setAlarmType(alarmContent.getAlarmType());
        entity.setDeviceId(alarmContent.getDeviceId());
        entity.setEndTime(alarmContent.getEndTime());
        entity.setExt(alarmContent.getExt());
        entity.setId(alarmContent.getId());
        entity.setIdCard(alarmContent.getIdCard());
        entity.setImageUrl(alarmContent.getImageUrl());
        entity.setLatitude(alarmContent.getLatitude());
        entity.setLibId(alarmContent.getLibId());
        entity.setLibName(alarmContent.getLibName());
        entity.setLongitude(alarmContent.getLongitude());
        entity.setName(alarmContent.getName());
        entity.setNote(alarmContent.getNote());
        entity.setReceiveUser(alarmContent.getReceiveUser());
        entity.setRecordId(alarmContent.getRecordId());
        entity.setScore(alarmContent.getScore());
        entity.setSex(alarmContent.getSex());
        entity.setStartTime(alarmContent.getStartTime());
        entity.setTarget(alarmContent.getTarget());
        entity.setTargetImage(alarmContent.getTargetImage());
        entity.setTaskId(alarmContent.getTaskId());
        entity.setTaskName(alarmContent.getTaskName());
        entity.setTaskReason(alarmContent.getTaskReason());
        entity.setUserName(alarmContent.getUserName());
        return entity;
    }

    public static JqItemEntity transform(AlarmMessage message,Gson gson) {
        AlarmMessageContent alarmContent = gson.fromJson(message.getContent(), AlarmMessageContent.class);
        Log.d(TAG, "alarmMessageContent:" + gson.toJson(alarmContent));
        Log.d(TAG, "alarmMessageContent ext:" + String.valueOf(alarmContent.getExt()));
        JqItemEntity entity = new JqItemEntity();
        String alarmType = alarmContent.getAlarmType();
        int type = Integer.valueOf(alarmType);
        if (type == TYPE_FACE_DEPLOY) {
            entity = transformFaceJqItemEntity(entity, alarmContent, gson);
        } else {
            entity = transformCarJqItemEntity(entity, alarmContent, gson);
        }
        return entity;
    }

    private static JqItemEntity transformFaceJqItemEntity(JqItemEntity itemEntity, AlarmMessageContent alarmContent, Gson gson) {
        String ext = alarmContent.getExt();
        String taskName = alarmContent.getTaskName();
        String alarmAddress = alarmContent.getAlarmAddress();
        String note = alarmContent.getNote();
        String id = alarmContent.getId();
        String imageUrl = alarmContent.getImageUrl();
        String taskReason = alarmContent.getTaskReason();
        String alarmLevel = alarmContent.getAlarmLevel();
        String alarmStatus = alarmContent.getAlarmStatus();
        String target = alarmContent.getTarget();

        if (!TextUtils.isEmpty(ext)) {
            AlarmPeopleDetailResponseForExt peopleTemp = parseFaceExt(gson, ext);
            itemEntity.setDeployImgUrl(UrlConstant.parseImageUrl(peopleTemp.getTargetImage()));
            if (TextUtils.isEmpty(peopleTemp.getSex())) {
                itemEntity.setCaptureGender("其它");
            } else if (TYPE_MALE.equals(peopleTemp.getSex())) {
                itemEntity.setCaptureGender("男性");
            } else if (TYPE_FAMALE.equals(peopleTemp.getSex())) {
                itemEntity.setCaptureGender("女性");
            }
            itemEntity.setCaptureIdCard(peopleTemp.getIdcard());
            itemEntity.setCaptureAdress(peopleTemp.getCensus());//户籍地址
            itemEntity.setCaptureLib(peopleTemp.getLibName());//库名称
            itemEntity.setCaptureDetailInfo(peopleTemp.getRemark()); //详细信息
            itemEntity.setCaptureName(peopleTemp.getName());
        }

        itemEntity.setItemType(TYPE_FACE_DEPLOY);
        itemEntity.setSimilarity(alarmContent.getScore());

        if (!TextUtils.isEmpty(taskName)) {
            itemEntity.setCaptureLibName(taskName); //布控名称
        }

        if (!TextUtils.isEmpty(alarmAddress)) {
            itemEntity.setCameraLocation(alarmAddress);
        }
        if (!TextUtils.isEmpty(note)) {
            itemEntity.setCapturetTipMsg(note);//备注
        }

        if (!TextUtils.isEmpty(id)) {
            itemEntity.setId(id);
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            itemEntity.setCaptureImgUrl(UrlConstant.parseImageUrl(imageUrl));
        }
        if (!TextUtils.isEmpty(taskReason)) {
            itemEntity.setTaskReason(taskReason);
        }

        if (!TextUtils.isEmpty(alarmLevel)) {
            itemEntity.setLevel(alarmLevel);
        }

        if (!TextUtils.isEmpty(alarmStatus)) {
            itemEntity.setItemHandleType(
                    Integer.valueOf(alarmStatus));
        }
        if (!TextUtils.isEmpty(target)) {
            itemEntity.setTarget(target); //目标
        }
        itemEntity.setLatitude(alarmContent.getLatitude());
        itemEntity.setLongitude(alarmContent.getLongitude());
        itemEntity.setStartTime(TimeUtils.millis2String(alarmContent.getStartTime()));
        itemEntity.setEndTime(TimeUtils.millis2String(alarmContent.getEndTime()));
        itemEntity.setAlarmTime(TimeUtils.millis2String(alarmContent.getAlarmTime()));
        return itemEntity;
    }

    private static AlarmPeopleDetailResponseForExt parseFaceExt(Gson gson, String ext) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(ext).getAsJsonArray();//获取JsonArray对象
        ArrayList<AlarmPeopleDetailResponseForExt> beanList = new ArrayList<>(jsonElements.size());
        for (JsonElement bean : jsonElements) {
            AlarmPeopleDetailResponseForExt temp = gson.fromJson(bean, AlarmPeopleDetailResponseForExt.class);//解析
            beanList.add(temp);
        }
        return beanList.get(0);
    }


    private static JqItemEntity transformCarJqItemEntity(JqItemEntity itemEntity, AlarmMessageContent alarmContent, Gson gson) {
        String ext = alarmContent.getExt();
        String userName = alarmContent.getUserName();
        String taskName = alarmContent.getTaskName();
        String id = alarmContent.getId();
        String imageUrl = alarmContent.getImageUrl();
        String taskReason = alarmContent.getTaskReason();
        String alarmLevel = alarmContent.getAlarmLevel();
        String target = alarmContent.getTarget();
        String alarmStatus = alarmContent.getAlarmStatus();
        String alarmAddress = alarmContent.getAlarmAddress();
        String note = alarmContent.getNote();
        if (!TextUtils.isEmpty(ext)) {
            AlarmCarDetailResponseForExt carTemp = parseCarExt(gson, ext);
            itemEntity.setCarNumber(carTemp.getAlarmObjName()); //车牌号
            itemEntity.setDeployImgUrl(UrlConstant.parseImageUrl(carTemp.getImageURLs())); //布控图片
            itemEntity.setCaptureLib(carTemp.getLibName()); //库名称
        }
        if (!TextUtils.isEmpty(userName)) {
            itemEntity.setDealPerson(userName); //处理人
        }
        if (!TextUtils.isEmpty(taskName)) {
            itemEntity.setCaptureLibName(taskName); //布控名称
        }
        if (!TextUtils.isEmpty(id)) {
            itemEntity.setId(id);
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            itemEntity.setCaptureImgUrl(UrlConstant.parseImageUrl(imageUrl));
        }
        if (!TextUtils.isEmpty(taskReason)) {
            itemEntity.setTaskReason(taskReason);
        }
        if (!TextUtils.isEmpty(alarmLevel)) {
            itemEntity.setLevel(alarmLevel);
        }
        if (!TextUtils.isEmpty(target)) {
            itemEntity.setTarget(target); //目标
        }
        if (!TextUtils.isEmpty(alarmStatus)) {
            itemEntity.setItemHandleType(
                    Integer.valueOf(alarmStatus));
        }
        if (!TextUtils.isEmpty(alarmAddress)) {
            itemEntity.setCameraLocation(alarmAddress);//摄像机名称
        }
        if (!TextUtils.isEmpty(note)) {
            itemEntity.setCapturetTipMsg(note);//备注
        }
        itemEntity.setItemType(TYPE_CAR_DEPLOY);
        itemEntity.setLatitude(alarmContent.getLatitude());
        itemEntity.setLongitude(alarmContent.getLongitude());
        itemEntity.setAlarmTime(TimeUtils.millis2String(alarmContent.getAlarmTime()));
        return itemEntity;
    }

    private static AlarmCarDetailResponseForExt parseCarExt(Gson gson, String ext) {
        AlarmCarDetailResponseForExt temp = gson.fromJson(ext, AlarmCarDetailResponseForExt.class);
        return temp;
    }
}
