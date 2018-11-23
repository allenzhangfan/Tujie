package com.netposa.component.imageupload;

import com.netposa.common.utils.FileUtils;

import java.io.File;

import androidx.annotation.NonNull;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author：yeguoqiang
 * Created time：2018/9/27 14:01
 */
public class RequestUtils {

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String filePath) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFileByPath(filePath);
        RequestBody requestFile = RequestBody.create(null, file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
