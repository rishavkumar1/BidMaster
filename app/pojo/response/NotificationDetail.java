package pojo.response;

import models.Auction;
import models.Product;
import models.User;

public class NotificationDetail {
    User user;
    Product product;
    Auction auction;

    Double confirmedPrice;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public Double getConfirmedPrice() {
        return confirmedPrice;
    }

    public void setConfirmedPrice(Double confirmedPrice) {
        this.confirmedPrice = confirmedPrice;
    }
}
