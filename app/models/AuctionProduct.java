package models;

import enums.AuctionStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class AuctionProduct extends BaseIdModel{
    private Long auctionId;
    private Long productId;
    private Double basePrice;
    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    private Date bidStartTime;

    private Date bidEndTime;

    private Double currentHighestBid;

    private Long highestBidId;

    public AuctionProduct() {
    }

    public AuctionProduct(Long auctionId, Long productId, Double basePrice, AuctionStatus status, Date bidStartTime, Date bidEndTime) {
        this.auctionId = auctionId;
        this.productId = productId;
        this.basePrice = basePrice;
        this.status = status;
        this.bidStartTime = bidStartTime;
        this.bidEndTime = bidEndTime;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Date getBidStartTime() {
        return bidStartTime;
    }

    public void setBidStartTime(Date bidStartTime) {
        this.bidStartTime = bidStartTime;
    }

    public Date getBidEndTime() {
        return bidEndTime;
    }

    public void setBidEndTime(Date bidEndTime) {
        this.bidEndTime = bidEndTime;
    }

    public Double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(Double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public Long getHighestBidId() {
        return highestBidId;
    }

    public void setHighestBidId(Long highestBidId) {
        this.highestBidId = highestBidId;
    }
}