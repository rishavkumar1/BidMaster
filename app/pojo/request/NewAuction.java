package pojo.request;

import java.util.List;

public class NewAuction {
    String auctionName;
    List<NewProduct> newProducts;

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public List<NewProduct> getNewProducts() {
        return newProducts;
    }

    public void setNewProducts(List<NewProduct> newProducts) {
        this.newProducts = newProducts;
    }
}
