package services.model_services;

import enums.AuctionStatus;
import models.AuctionProduct;
import utils.Constants;

import java.util.Date;
import java.util.List;

public class AuctionProductServiceImpl extends ModelServiceImpl<Long, AuctionProduct> implements AuctionProductService{
    public List<AuctionProduct> getCurrentAuctionProducts(){
        return getFinder().where().eq(Constants.auctionStatusField, AuctionStatus.RUNNING).findList();
    }

    public AuctionProduct findRunningAuction(Long id){
        return getFinder().where().idEq(id).eq(Constants.auctionStatusField, AuctionStatus.RUNNING).findUnique();
    }

    public List<AuctionProduct> getAuctionsToStart(Date currentDate){
        return getFinder().where().le(Constants.bidStartTimeField, currentDate).ge(Constants.bidEndTimeField, currentDate)
                .eq(Constants.auctionStatusField, AuctionStatus.PENDING).findList();
    }

    public List<AuctionProduct> getAuctionsToClose(Date currentDate){
        return getFinder().where().lt(Constants.bidEndTimeField, currentDate)
                .eq(Constants.auctionStatusField, AuctionStatus.RUNNING).findList();
    }
}
