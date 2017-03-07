package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/3/6.
 */
public class BuyCoinsMethodBean {
    private boolean flag;
    private String method;

    public BuyCoinsMethodBean(boolean flag, String method) {
        this.flag = flag;
        this.method = method;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
