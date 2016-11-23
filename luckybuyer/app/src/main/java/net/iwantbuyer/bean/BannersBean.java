package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/23.
 */
public class BannersBean {

    /**
     * _resource : Banner
     * batch_id : 1
     * game_id : 11
     * id : 1
     * image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/banner.png
     * url :
     */

    private List<BannerBean> banner;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean {
        private String _resource;
        private int batch_id;
        private int game_id;
        private int id;
        private String image;
        private String url;

        public String get_resource() {
            return _resource;
        }

        public void set_resource(String _resource) {
            this._resource = _resource;
        }

        public int getBatch_id() {
            return batch_id;
        }

        public void setBatch_id(int batch_id) {
            this.batch_id = batch_id;
        }

        public int getGame_id() {
            return game_id;
        }

        public void setGame_id(int game_id) {
            this.game_id = game_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
