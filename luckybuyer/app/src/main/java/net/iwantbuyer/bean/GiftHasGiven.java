package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/3/10.
 */
public class GiftHasGiven {

    /**
     * display_message : null
     * errors : null
     * message : GiftGivenToUser
     * status_code : 409
     * type : GiftGivenToUser
     */

    private Object display_message;
    private Object errors;
    private String message;
    private int status_code;
    private String type;

    public Object getDisplay_message() {
        return display_message;
    }

    public void setDisplay_message(Object display_message) {
        this.display_message = display_message;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
