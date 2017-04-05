package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/3/27.
 */
public class TopUpOptionBean {

    /**
     * _resource : TopupOption
     * amount : 1
     * android_product_id : staging_1
     * category : {fpx_my, rhb_my, maybank2u_my, cimb_my, hlb_my, affinepg_my, mol_1, mol_1, mol_804, mol_806, mol_805, mol_815, , webcash_my, , 7eleven_my, , esapay_my, , epay_my, android-inapp}
     * currency : MYR
     * description : 这是测试数据
     * gifts_first_topup : 0
     * id : 8
     * name : 123
     * price : 1
     */

    private String _resource;
    private int amount;
    private String android_product_id;
    private String category;
    private String currency;
    private String description;
    private int gifts_first_topup;
    private int id;
    private String name;
    private int price;

    public String get_resource() {
        return _resource;
    }

    public void set_resource(String _resource) {
        this._resource = _resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAndroid_product_id() {
        return android_product_id;
    }

    public void setAndroid_product_id(String android_product_id) {
        this.android_product_id = android_product_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGifts_first_topup() {
        return gifts_first_topup;
    }

    public void setGifts_first_topup(int gifts_first_topup) {
        this.gifts_first_topup = gifts_first_topup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
