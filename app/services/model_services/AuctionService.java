package services.model_services;

import com.google.inject.ImplementedBy;
import models.Auction;
@ImplementedBy(AuctionServiceImpl.class)
public interface AuctionService extends ModelService<Long, Auction> {
}
