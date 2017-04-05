package net.iwantbuyer.bean;

/**
 * Created by admin on 2016/9/23.
 */
public class BuyStateBean {

    /**
     * errors : null
     * message : InsufficientBalance
     * status_code : 409
     */

    private Object errors;
    private String message;
    private int status_code;

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
}
