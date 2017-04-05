package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/12/27.
 */
public class ServerBean {

    /**
     * api_server : https://api-staging.luckybuyer.net
     * countries : ["Singapore1"]
     * h5 : https://m.luckybuyer.net
     * name : Singapore(test1)
     * region : sgp-test
     */

    private List<ServersBean> servers;

    public List<ServersBean> getServers() {
        return servers;
    }

    public void setServers(List<ServersBean> servers) {
        this.servers = servers;
    }

    public static class ServersBean {
        private String api_server;
        private String h5;


        private String name;
        private String region;
        private List<String> countries;

        public String getApi_server() {
            return api_server;
        }

        public void setApi_server(String api_server) {
            this.api_server = api_server;
        }

        public String getH5() {
            return h5;
        }

        public void setH5(String h5) {
            this.h5 = h5;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<String> getCountries() {
            return countries;
        }

        public void setCountries(List<String> countries) {
            this.countries = countries;
        }
    }
}
