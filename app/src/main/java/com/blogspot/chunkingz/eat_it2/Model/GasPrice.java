package com.blogspot.chunkingz.eat_it2.Model;

public class GasPrice {
    private String home;
    private String office;

    public GasPrice() {
        // Default constructor required for calls to DataSnapshot.getValue(GasPrice.class)
    }

    public GasPrice(String home, String office) {
        this.home = home;
        this.office = office;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
