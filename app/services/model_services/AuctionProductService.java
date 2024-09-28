package services.model_services;

import com.google.inject.ImplementedBy;
import models.AuctionProduct;

import java.util.Date;
import java.util.List;

@ImplementedBy(AuctionProductServiceImpl.class)
public interface AuctionProductService extends ModelService<Long, AuctionProduct> {
    List<AuctionProduct> getCurrentAuctionProducts();

    AuctionProduct findRunningAuction(Long id);

    List<AuctionProduct> getAuctionsToStart(Date currentDate);

    List<AuctionProduct> getAuctionsToClose(Date currentDate);
}
