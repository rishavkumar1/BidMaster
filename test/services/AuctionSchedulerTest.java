package services;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import enums.AuctionStatus;
import enums.BidStatus;
import enums.ProductCategory;
import helper.HelperTest;
import models.Auction;
import models.AuctionProduct;
import models.Bid;
import models.Product;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import services.model_services.AuctionProductServiceImpl;
import services.model_services.AuctionServiceImpl;
import services.model_services.BidService;
import services.model_services.ProductServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionSchedulerTest extends HelperTest {
    @Inject
    AuctionServiceImpl auctionService;

    @Inject
    AuctionProductServiceImpl auctionProductService;

    @Inject
    ProductServiceImpl productService;

    @Inject
    BidService bidService;

    @Inject
    AuctionScheduler auctionScheduler;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void setUp(){
        truncateTables();
    }

    @After
    public void truncateTables(){
        Ebean.deleteAllPermanent(auctionProductService.getFinder().all());
        Ebean.deleteAllPermanent(productService.getFinder().all());
        Ebean.deleteAllPermanent(auctionService.getFinder().all());
        Ebean.deleteAllPermanent(bidService.getFinder().all());
    }

    @Test
    public void startAndCloseAuctionTest(){
        createAuctionAndBids();
        auctionScheduler.startAuctions(new Date());
        List<AuctionProduct> auctionProducts = auctionProductService.getFinder().all();
        Assert.assertEquals(1L, auctionProducts.size());
        AuctionProduct auctionProduct = auctionProducts.get(0);
        Assert.assertEquals(AuctionStatus.RUNNING, auctionProduct.getStatus());
        Date now = new Date();
        Date past = DateUtils.addMinutes(now, -1);
        auctionProduct.setBidEndTime(past);
        auctionProduct.save();
        auctionScheduler.closeAuctions(new Date());
        auctionProducts = auctionProductService.getFinder().all();
        Assert.assertEquals(1L, auctionProducts.size());
        auctionProduct = auctionProducts.get(0);
        Assert.assertEquals(AuctionStatus.CLOSED, auctionProduct.getStatus());
    }

    private void createAuctionAndBids(){
        Auction auction = new Auction("September Grand Auction", 1L);
        auction.save();
        Long auctionId = auction.getId();
        Product product = new Product("royal_enfield_bike", "royal enfield classic 350 bike", ProductCategory.bike);
        product.save();
        Long productId = product.getId();
        Date now = new Date();
        Date startTime = DateUtils.addDays(now, -1);
        Date endTime = DateUtils.addDays(now, 1);
        AuctionProduct auctionProduct = new AuctionProduct(auctionId, productId, 232433.20, AuctionStatus.RUNNING, startTime, endTime);
        Long auctionProductId = auctionProduct.getId();
        Bid bid = new Bid();
        bid.setBidderId(2L);
        bid.setStatus(BidStatus.RUNNING);
        bid.setAuctionProductId(auctionProductId);
        bid.setPrice(300000.00);
        bid.setBidTimestamp(new Date());
        bid.save();
        auctionProduct.setHighestBidId(bid.getId());
        auctionProduct.setStatus(AuctionStatus.PENDING);
        auctionProduct.save();
    }
}
