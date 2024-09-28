package pojo.response;

import java.util.List;

public class AuctionHistory {
    String auctionName;

    List<FoundProduct> products;

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public List<FoundProduct> getProducts() {
        return products;
    }

    public void setProducts(List<FoundProduct> products) {
        this.products = products;
    }
}
