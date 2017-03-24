package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2017/3/23.
 */
public class BuyCoinsMethodBean {

    private List<String> bank;
    private List<String> mol;
    private List<String> cash;

    public List<String> getBank() {
        return bank;
    }

    public void setBank(List<String> bank) {
        this.bank = bank;
    }

    public List<String> getMol() {
        return mol;
    }

    public void setMol(List<String> mol) {
        this.mol = mol;
    }

    public List<String> getCash() {
        return cash;
    }

    public void setCash(List<String> cash) {
        this.cash = cash;
    }
}
