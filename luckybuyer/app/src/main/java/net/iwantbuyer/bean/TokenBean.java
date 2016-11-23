package net.iwantbuyer.bean;

/**
 * Created by admin on 2016/9/20.
 */
public class TokenBean {


    /**
     * iss : https://staging-luckybuyer.auth0.com/
     * sub : facebook|118093991983735
     * aud : HmF3R6dz0qbzGQoYtTuorgSmzgu6Aua1
     * exp : 1474963843
     * iat : 1474359043
     */

    private  String iss;
    private  String sub;
    private  String aud;
    private  int exp;
    private  int iat;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }
}
