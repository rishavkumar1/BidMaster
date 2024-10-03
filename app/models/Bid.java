package models;

import enums.BidStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class Bid extends BaseIdModel{

    private Long auctionProductId;

    private Long bidderId;

    private Date bidTimestamp;

    private Double price;

    @Enumerated(EnumType.STRING)
    private BidStatus status;

    public Long getAuctionProductId() {
        return auctionProductId;
    }

    public void setAuctionProductId(Long auctionProductId) {
        this.auctionProductId = auctionProductId;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public Date getBidTimestamp() {
        return bidTimestamp;
    }

    public void setBidTimestamp(Date bidTimestamp) {
        this.bidTimestamp = bidTimestamp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }
}
