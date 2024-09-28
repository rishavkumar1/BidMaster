package pojo.response;

import enums.BidStatus;
import java.util.Date;

public class BidHistory {
    Long bidId;
    Date bidTimestamp;
    Double bidPrice;
    String productName;
    String productDescription;
    BidStatus bidStatus;
    String auctionName;

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }

    public Date getBidTimestamp() {
        return bidTimestamp;
    }

    public void setBidTimestamp(Date bidTimestamp) {
        this.bidTimestamp = bidTimestamp;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BidStatus getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
