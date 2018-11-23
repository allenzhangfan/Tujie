package com.netposa.component.spjk.mvp.model.entity;

import java.util.List;

import androidx.annotation.Keep;

/**
 * Author：yeguoqiang
 * Created time：2018/11/12 21:09
 */
@Keep
public class HistoryVideoResponseEntity {

    private List<VideoListEntity> videoList;

    private PageInfoBean pageInfo;

    public List<VideoListEntity> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoListEntity> videoList) {
        this.videoList = videoList;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }


    public static class VideoListEntity {
        /**
         * beginTime : 20181108170922271
         * endTime : 20181108170922271
         * playUrl : rtsp://192.168.15.82:554/PVG/vod/?PVG=172.16.90.151:2100/admin/admin/av/ONVIF/海康236&begin=20181108170922271&end=20181108170922271
         */

        private String beginTime;
        private String endTime;
        private String playUrl;

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("VideoListEntity{");
            sb.append("beginTime='").append(beginTime).append('\'');
            sb.append(", endTime='").append(endTime).append('\'');
            sb.append(", playUrl='").append(playUrl).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HistoryVideoResponseEntity{");
        sb.append("videoList=").append(videoList);
        sb.append('}');
        return sb.toString();
    }

    public static class PageInfoBean {
        /**
         * currentPage : 1
         * pageSize : 20
         * total : 13
         */

        private int currentPage;
        private int pageSize;
        private int total;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
