package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/2/24.
 */
public class FCMBean {

    /**
     * lang : string
     * platform : web
     * token : string
     */

    private String lang;
    private String platform;
    private String token;

    public FCMBean(String lang, String platform, String token) {
        this.lang = lang;
        this.platform = platform;
        this.token = token;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "\"lang\"" + ":\"" + lang  + "\""  +
                ",\"platform\"" + ":\"" + platform  + "\""  +
                ",\"token\"" + ":\"" + token  + "\""  +
                '}';
    }
}
