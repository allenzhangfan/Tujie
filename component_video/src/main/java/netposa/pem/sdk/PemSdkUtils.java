package netposa.pem.sdk;

import android.os.Build;
import com.netposa.common.log.Log;

import com.netposa.ffmpeg.ffmpegkit;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by dell on 2017/1/18.
 */

public class PemSdkUtils {

    private static final String TAG = PemSdkUtils.class.getSimpleName();

    public static boolean isSupportMediaCodecHardDecoder() {
        boolean isHardcode = false;
        //读取系统配置文件/system/etc/media_codecs.xml
        File file = new File("/system/etc/media_codecs.xml");
        InputStream inFile = null;
        try {
            inFile = new FileInputStream(file);
        } catch (Exception e) {
            Log.e(TAG, "Can't fine /system/etc/media_codecs.xml !");
            return false;
        }
        if (inFile != null) {
            XmlPullParserFactory pullFactory;
            try {
                pullFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = pullFactory.newPullParser();
                xmlPullParser.setInput(inFile, "UTF-8");
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("MediaCodec".equals(tagName)) {
                                String componentName = xmlPullParser.getAttributeValue(0);
                                if (componentName.startsWith("OMX.")) {
                                    Log.i(TAG, "MediaCodec:" + componentName);
                                    if (!componentName.startsWith("OMX.google.")) {
                                        isHardcode = true;
                                    }
                                }
                            }
                    }
                    eventType = xmlPullParser.next();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        return isHardcode;
    }


    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean yuv4202jpeg(String in, byte[] yuvdata, int width, int height) {
        try {
            ffmpegkit kit = new ffmpegkit();
            if (kit._yuv2jpeg == null) {
                kit.ffmpegkit_encoder_yuv2jpeg();
            }
            return kit._yuv2jpeg.encoder(in.getBytes("GB2312"), yuvdata, width, height);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }
}
