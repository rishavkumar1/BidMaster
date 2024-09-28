package services.model_services;

import models.Bid;
import utils.Constants;

import java.util.List;

public class BidServiceImpl extends ModelServiceImpl<Long, Bid> implements BidService{
    public List<Bid> getBids(List<Long> auctionProductIds){
        return getFinder().where().in(Constants.auctionProductIdField, auctionProductIds).findList();
    }
}
