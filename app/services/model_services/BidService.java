package services.model_services;

import com.google.inject.ImplementedBy;
import models.Bid;

import java.util.List;

@ImplementedBy(BidServiceImpl.class)
public interface BidService extends ModelService<Long, Bid>{

    List<Bid> getBids(List<Long> auctionProductIds);
}
