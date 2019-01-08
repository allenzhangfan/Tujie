package com.netposa.camera.mvp.model.entity;

/**
 * Created by yidong0418 on 2018/12/4.
 */
public class UploadResponseEntity {
        /**
         * sourceUrl : PFSB:/viasbimg/bimg/data/20181204/14_0/8a5b3d5402b8e863cad75ff58c9120eb
         * extend : {"Pos":{"Left":78,"Top":74,"Right":140,"Bottom":136}}
         * feature : DfX7BfsA+QME/hDn9/D7+Q4RARX++RH5/g4R//AOAvwO+gkOCPH/AP/vBPoA+Qj5CQ7xGQDx+vz/+QTzBAQA6/n//f3/COILBf3/C/EA+xr8+O4M9wERCfcS8+HxBgDvBO8DAPD5+ALqCAjsBAkYAPoE9+4EA/4K+fsMAv71BAMPAPr2APT8DvDv9Aj3/AL07hUFAQADI+38Fw7w7Q4F9g397PQVA+3/BP8N9gAXCe0SCwb+9AUmAuwAAgL3+vYM8e/15gsW+eTrBN8AAAf9CQwA//TzBQTyBAH2D/QD9vECA/8RDvj4Fun5EQD/8vAF/gQRDOkM3/ICA/8D+AIb+gcDAREOAQTuAwgD/AcG+gf1/PYAA/UA9AsJ/wTy9gQKBgj0CQ358f/2+PbjL/wPAewXAPQaBPn1BwED+vMPAvT+AQQJ7wP1HRAK/AALAv/+Bfvs7gT/CP3+4fYK/QAO9+sBEhMJERH0/gggBgX79Av8ARfpDgjzAyntGiD4A+/2Z054Q1D3gzs=
         * photoId : 519527351629905920
         */
        private String sourceUrl;
        private ExtendEntity extend;
        private String feature;
        private String photoId;

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public void setExtend(ExtendEntity extend) {
            this.extend = extend;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public void setPhotoId(String photoId) {
            this.photoId = photoId;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public ExtendEntity getExtend() {
            return extend;
        }

        public String getFeature() {
            return feature;
        }

        public String getPhotoId() {
            return photoId;
        }

        public class ExtendEntity {
            /**
             * Pos : {"Left":78,"Top":74,"Right":140,"Bottom":136}
             */
            private PosEntity Pos;

            public void setPos(PosEntity Pos) {
                this.Pos = Pos;
            }

            public PosEntity getPos() {
                return Pos;
            }

            public class PosEntity {
                /**
                 * Left : 78
                 * Top : 74
                 * Right : 140
                 * Bottom : 136
                 */
                private int Left;
                private int Top;
                private int Right;
                private int Bottom;

                public void setLeft(int Left) {
                    this.Left = Left;
                }

                public void setTop(int Top) {
                    this.Top = Top;
                }

                public void setRight(int Right) {
                    this.Right = Right;
                }

                public void setBottom(int Bottom) {
                    this.Bottom = Bottom;
                }

                public int getLeft() {
                    return Left;
                }

                public int getTop() {
                    return Top;
                }

                public int getRight() {
                    return Right;
                }

                public int getBottom() {
                    return Bottom;
                }
            }
        }

}
