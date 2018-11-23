package netposa.pem.sdk.decode;

import android.annotation.TargetApi;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import netposa.pem.sdk.PemSdkListener;

public class HardDecoder {

    private byte[] mCurrentData;
    //处理音视频的编解码的类MediaCodec
    private MediaCodec mDecoder;
    //视频数据
    private BlockingQueue<byte[]> video_data_Queue = new ArrayBlockingQueue<>(10000);
    private boolean isRuning = false;
    private ByteBuffer[] inputBuffers;
    private ByteBuffer[] outputBuffers;
    private int frameCount = 0;
    private long deltaTime = 0;
    private long counterTime = System.currentTimeMillis();
    private boolean isReady = false;
    private int fps = 0;
    private MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
    private PemSdkListener mDecodeCallBack;
    //        private int mWidth = 1920;
//    private int mHeight = 1088;
    private int mWidth = 704;
    private int mHeight = 576;

    private static final int COLOR_FormatI420 = 1;
    private static final int COLOR_FormatNV21 = 2;

    public HardDecoder(PemSdkListener decodeCallBack, int width, int height) {
        this.mDecodeCallBack = decodeCallBack;
        this.mWidth = width;
        this.mHeight = height;
    }

    /**
     * 获取 sps pps
     *
     * @param data
     * @param len
     */
    private List<byte[]> getSpsPpS(byte[] data, int len) {
        byte temp[];
        List<byte[]> spspps = new ArrayList<>();

        boolean isStart = true;
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < len; i++) {
            if (data[i] == 0 && data[i + 1] == 0 && data[i + 2] == 0 && data[i + 3] == 1) {
                if (isStart) {
                    startIndex = i;
                    isStart = false;
                } else {
                    endIndex = i;
                    int lens = endIndex - startIndex;
                    temp = new byte[lens];
                    System.arraycopy(data, startIndex + 4, temp, 0, lens);
                    spspps.add(temp);
                    //重新开始
                    startIndex = i;
                }
            }
        }
        return spspps;
    }


    //添加视频数据
    public void setVideoData(byte[] data) {
        try {
            video_data_Queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initial(byte[] bytes, int len) throws IOException {

        MediaFormat format = null;

        format = MediaFormat.createVideoFormat("video/avc", mWidth, mHeight);

        if (mDecoder != null) {
            mDecoder.stop();
            mDecoder.release();
            mDecoder = null;
        }
        mDecoder = MediaCodec.createDecoderByType("video/avc");
        if (mDecoder == null) {
            return;
        }
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        mDecoder.configure(format, null, null, 0);
        mDecoder.start();
        inputBuffers = mDecoder.getInputBuffers();
        outputBuffers = mDecoder.getOutputBuffers();
        frameCount = 0;
        deltaTime = 0;
        isRuning = true;
        runDecodeVideoThread();
    }

    private void runDecodeVideoThread() {

        Thread t = new Thread() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void run() {

                while (isRuning) {

                    int inIndex = -1;
                    try {
                        inIndex = mDecoder.dequeueInputBuffer(0);
                        if (inIndex >= 0) {
                            ByteBuffer buffer = inputBuffers[inIndex];
                            buffer.clear();

                            if (!video_data_Queue.isEmpty()) {
                                byte[] data;
                                data = video_data_Queue.take();
                                buffer.put(data);
                                mDecoder.queueInputBuffer(inIndex, 0, data.length, 66, 0);
                            } else {
                                mDecoder.queueInputBuffer(inIndex, 0, 0, 66, 0);
                            }
                        }

                        int outIndex = mDecoder.dequeueOutputBuffer(info, 0);

                        if (outIndex >= 0) {
                            ByteBuffer outputbuffer = outputBuffers[outIndex];
                            mCurrentData = new byte[info.size];
                            outputbuffer.get(mCurrentData);
                            Image img = mDecoder.getOutputImage(outIndex);
                            mCurrentData = getDataFromImage(img, COLOR_FormatI420);
                            mDecodeCallBack.getYUVI420(mCurrentData, mWidth, mHeight);
                        }

                        switch (outIndex) {
                            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                                outputBuffers = mDecoder.getOutputBuffers();
                                break;
                            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                                isReady = true;
                                break;
                            case MediaCodec.INFO_TRY_AGAIN_LATER:
                                break;
                            default:
                                mDecoder.releaseOutputBuffer(outIndex, true);
                                frameCount++;
                                deltaTime = System.currentTimeMillis() - counterTime;
                                if (deltaTime > 1000) {
                                    fps = (int) (((float) frameCount / (float) deltaTime) * 1000);
                                    counterTime = System.currentTimeMillis();
                                    frameCount = 0;
                                }
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                mDecoder.flush();
                mDecoder.stop();
                mDecoder.release();
                mDecoder = null;
            }
        };

        t.start();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean isImageFormatSupported(Image image) {
        int format = image.getFormat();
        switch (format) {
            case ImageFormat.YUV_420_888:
            case ImageFormat.NV21:
                return true;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static byte[] getDataFromImage(Image image, int colorFormat) {
        if (colorFormat != COLOR_FormatI420 && colorFormat != COLOR_FormatNV21) {
            throw new IllegalArgumentException("only support COLOR_FormatI420 " + "and COLOR_FormatNV21");
        }
        if (!isImageFormatSupported(image)) {
            throw new RuntimeException("can't convert Image to byte array, format " + image.getFormat());
        }
        Rect crop = image.getCropRect();
        int format = image.getFormat();
        int width = crop.width();
        int height = crop.height();
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];
        byte[] rowData = new byte[planes[0].getRowStride()];
        int channelOffset = 0;
        int outputStride = 1;
        for (int i = 0; i < planes.length; i++) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = width * height;
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height + 1;
                        outputStride = 2;
                    }
                    break;
                case 2:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = (int) (width * height * 1.25);
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height;
                        outputStride = 2;
                    }
                    break;
            }
            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();
            int shift = (i == 0) ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < h; row++) {
                int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (w - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
        }
        return data;
    }

    public void stopDecode() {
        isRuning = false;
    }

    public byte[] getCurrentData() {
        return mCurrentData;
    }

}
