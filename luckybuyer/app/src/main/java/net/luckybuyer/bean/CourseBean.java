package net.luckybuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/27.
 * yangshuyu
 */
public class CourseBean {

    /**
     * MI_szKPID : 121,122,123,124,F77,F76,F75,F74,F73,F78,F79,F80,241,F81,F82,F83,E27,F84,F85,F86,F87,F88,F89,F90,F91,F92,F93,F94
     * MI_szVCPID : 1000
     * MI_szVCPName : 一年级上册
     * children : [{"MI_szKPID":"121,122,123,124,F77,F76,F75,F74,F73","MI_szVCPID":1100,"MI_szVCPName":"第一单元","children":[{"MI_szKPID":121,"MI_szVCPID":1110,"MI_szVCPName":"汉语拼音（一）","count":0},{"MI_szKPID":122,"MI_szVCPID":1120,"MI_szVCPName":"汉语拼音（二）","count":0},{"MI_szKPID":123,"MI_szVCPID":1130,"MI_szVCPName":"汉语拼音（三）","count":0},{"MI_szKPID":124,"MI_szVCPID":1140,"MI_szVCPName":"汉语拼音（四）","count":0},{"MI_szKPID":"F77,F76,F75,F74,F73","MI_szVCPID":1150,"MI_szVCPName":"认一认","count":0}],"count":0},{"MI_szKPID":"F78,F79,F80","MI_szVCPID":1200,"MI_szVCPName":"第二单元","children":[{"MI_szKPID":"F78","MI_szVCPID":1210,"MI_szVCPName":"识字1","count":0},{"MI_szKPID":"F79","MI_szVCPID":1220,"MI_szVCPName":"识字2","count":0},{"MI_szKPID":"F80","MI_szVCPID":1230,"MI_szVCPName":"识字3","count":0}],"count":0},{"MI_szKPID":"241,F81,F82,F83","MI_szVCPID":1300,"MI_szVCPName":"第三单元","children":[{"MI_szKPID":241,"MI_szVCPID":1310,"MI_szVCPName":"人有两个宝","count":0},{"MI_szKPID":"F81","MI_szVCPID":1320,"MI_szVCPName":"升国旗","count":0},{"MI_szKPID":"F82","MI_szVCPID":1330,"MI_szVCPName":"江南","count":0},{"MI_szKPID":"F83","MI_szVCPID":1340,"MI_szVCPName":"我叫\u201c神舟号\u201d","count":0}],"count":0},{"MI_szKPID":"E27,F84,F85,F86","MI_szVCPID":1400,"MI_szVCPName":"第四单元","children":[{"MI_szKPID":"E27","MI_szVCPID":1410,"MI_szVCPName":"家","count":0},{"MI_szKPID":"F84","MI_szVCPID":1420,"MI_szVCPName":"东方明珠","count":0},{"MI_szKPID":"F85","MI_szVCPID":1430,"MI_szVCPName":"秋姑娘的信","count":0},{"MI_szKPID":"F86","MI_szVCPID":1440,"MI_szVCPName":"看菊花","count":0}],"count":0},{"MI_szKPID":"F87,F88,F89","MI_szVCPID":1500,"MI_szVCPName":"第五单元","children":[{"MI_szKPID":"F87","MI_szVCPID":1510,"MI_szVCPName":"识字4","count":0},{"MI_szKPID":"F88","MI_szVCPID":1520,"MI_szVCPName":"识字5","count":0},{"MI_szKPID":"F89","MI_szVCPID":1530,"MI_szVCPName":"识字6","count":0}],"count":0},{"MI_szKPID":"F90,F91,F92","MI_szVCPID":1600,"MI_szVCPName":"第六单元","children":[{"MI_szKPID":"F90","MI_szVCPID":1610,"MI_szVCPName":"大海睡了","count":0},{"MI_szKPID":"F91","MI_szVCPID":1620,"MI_szVCPName":"冰花","count":0},{"MI_szKPID":"F92","MI_szVCPID":1630,"MI_szVCPName":"北风和小鱼","count":0}],"count":0},{"MI_szKPID":"F93,F94","MI_szVCPID":1700,"MI_szVCPName":"第七单元","children":[{"MI_szKPID":"F93","MI_szVCPID":1710,"MI_szVCPName":"怀素写字","count":0},{"MI_szKPID":"F94","MI_szVCPID":1720,"MI_szVCPName":"小河与青草","count":0}],"count":0}]
     * count : 0
     */

    private List<CourseChildBean> course;

    public List<CourseChildBean> getCourseChildBean() {
        return course;
    }

    public void setCourseChildBean(List<CourseChildBean> course) {
        this.course = course;
    }

    public static class CourseChildBean {
        private String MI_szKPID;
        private int MI_szVCPID;
        private String MI_szVCPName;
        private int count;
        /**
         * MI_szKPID : 121,122,123,124,F77,F76,F75,F74,F73
         * MI_szVCPID : 1100
         * MI_szVCPName : 第一单元
         * children : [{"MI_szKPID":121,"MI_szVCPID":1110,"MI_szVCPName":"汉语拼音（一）","count":0},{"MI_szKPID":122,"MI_szVCPID":1120,"MI_szVCPName":"汉语拼音（二）","count":0},{"MI_szKPID":123,"MI_szVCPID":1130,"MI_szVCPName":"汉语拼音（三）","count":0},{"MI_szKPID":124,"MI_szVCPID":1140,"MI_szVCPName":"汉语拼音（四）","count":0},{"MI_szKPID":"F77,F76,F75,F74,F73","MI_szVCPID":1150,"MI_szVCPName":"认一认","count":0}]
         * count : 0
         */

        private List<ChildrenBean> children;

        public String getMI_szKPID() {
            return MI_szKPID;
        }

        public void setMI_szKPID(String MI_szKPID) {
            this.MI_szKPID = MI_szKPID;
        }

        public int getMI_szVCPID() {
            return MI_szVCPID;
        }

        public void setMI_szVCPID(int MI_szVCPID) {
            this.MI_szVCPID = MI_szVCPID;
        }

        public String getMI_szVCPName() {
            return MI_szVCPName;
        }

        public void setMI_szVCPName(String MI_szVCPName) {
            this.MI_szVCPName = MI_szVCPName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class ChildrenBean {
            private String MI_szKPID;
            private int MI_szVCPID;
            private String MI_szVCPName;
            private int count;
            /**
             * MI_szKPID : 121
             * MI_szVCPID : 1110
             * MI_szVCPName : 汉语拼音（一）
             * count : 0
             */

            private List<Children> children;

            public String getMI_szKPID() {
                return MI_szKPID;
            }

            public void setMI_szKPID(String MI_szKPID) {
                this.MI_szKPID = MI_szKPID;
            }

            public int getMI_szVCPID() {
                return MI_szVCPID;
            }

            public void setMI_szVCPID(int MI_szVCPID) {
                this.MI_szVCPID = MI_szVCPID;
            }

            public String getMI_szVCPName() {
                return MI_szVCPName;
            }

            public void setMI_szVCPName(String MI_szVCPName) {
                this.MI_szVCPName = MI_szVCPName;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public List<Children> getChildren() {
                return children;
            }

            public void setChildren(List<Children> children) {
                this.children = children;
            }

            public static class Children {
                private int MI_szKPID;
                private int MI_szVCPID;
                private String MI_szVCPName;
                private int count;

                public int getMI_szKPID() {
                    return MI_szKPID;
                }

                public void setMI_szKPID(int MI_szKPID) {
                    this.MI_szKPID = MI_szKPID;
                }

                public int getMI_szVCPID() {
                    return MI_szVCPID;
                }

                public void setMI_szVCPID(int MI_szVCPID) {
                    this.MI_szVCPID = MI_szVCPID;
                }

                public String getMI_szVCPName() {
                    return MI_szVCPName;
                }

                public void setMI_szVCPName(String MI_szVCPName) {
                    this.MI_szVCPName = MI_szVCPName;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }
            }
        }
    }
}
