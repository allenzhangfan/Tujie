package com.netposa.component.ytst.mvp.model.entity;

    public  class PonitEntity {
        /**
         * dataKey : 1ae1ad597554483c9a835caf2bbeddd5
         * dataType : FACE
         * position : 77,104,138,168
         * imgPath : PFSB:/viasbimg/bimg/data/20181215/11_0/6d6299aa3ea03f2966ad6fb7428bf467
         * sessionKey : 53d406efa3604e59a1836d060b86e6f0
         */

        private String dataKey;
        private String dataType;
        private String position;
        private String imgPath;
        private String sessionKey;
        private boolean isSelected;

        public String getDataKey() {
            return dataKey;
        }

        public void setDataKey(String dataKey) {
            this.dataKey = dataKey;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getSessionKey() {
            return sessionKey;
        }

        public void setSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

