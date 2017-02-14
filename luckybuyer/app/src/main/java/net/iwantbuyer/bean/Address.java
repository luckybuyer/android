package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/2/9.
 */
public class Address {

    /**
     * _resource : type of the resource
     * address : string
     * created_at : 2017-02-09T07:11:28.043Z
     * id : 0
     * is_default : true
     * name : string
     * phone : string
     * url : string
     * zipcode : string
     */

    private String _resource;
    private String address;
    private String created_at;
    private int id;
    private boolean is_default;
    private String name;
    private String phone;
    private String url;
    private String zipcode;

    public String get_resource() {
        return _resource;
    }

    public void set_resource(String _resource) {
        this._resource = _resource;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{" +
                "\"_resource\"" + ":\"" + _resource  + "\"" +
                ", \"address\"" + ":\""+ address + "\"" +
                ", \"created_at\"" + ":\"" + created_at + "\"" +
                ", \"id\"" + ":" + id +
                ", \"is_default\"" + ":" +is_default +
                ", \"name\"" + ":\"" + name + "\"" +
                ", \"phone\"" + ":\"" + phone + "\"" +
                ", \"url\"" + ":\"" + url + "\"" +
                ", \"zipcode\"" + ":\"" + zipcode + "\"" +
                '}';
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
