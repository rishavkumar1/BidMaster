package pojo.response;

import enums.AuctionStatus;
import utils.DateUtil;

import java.util.Date;

public class FoundProduct {
    Long auctionProductId;
    String name;
    String description;
    String bidStartTime;
    String bidEndTime;
    Double basePrice;
    Double currentHighestBid;
    AuctionStatus auctionStatus;

    public Long getAuctionProductId() {
        return auctionProductId;
    }

    public void setAuctionProductId(Long auctionProductId) {
        this.auctionProductId = auctionProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBidStartTime() {
        return bidStartTime;
    }

    public void setBidStartTime(Date bidStartTime) {
        this.bidStartTime = DateUtil.format(bidStartTime, DateUtil.standardDateFormat);
    }

    public String getBidEndTime() {
        return bidEndTime;
    }

    public void setBidEndTime(Date bidEndTime) {
        this.bidEndTime = DateUtil.format(bidEndTime, DateUtil.standardDateFormat);
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(Double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public AuctionStatus getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(AuctionStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }
}
