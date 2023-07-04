package com.runecost;

public class ShopStorage {

    private String shopName;
    public String[][] shopItems;

    public float getShopCostPer() {
        return shopCostPer;
    }

    private float shopCostPer;

    ShopStorage(String shopName, float shopCostPer, String[][] shopItems)
    {
        this.shopName = shopName;
        this.shopItems = shopItems;
        this.shopCostPer = shopCostPer;
    }

    public String getShopName() {
        return shopName;
    }
}
