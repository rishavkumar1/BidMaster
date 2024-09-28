package services;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import enums.AuctionStatus;
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
import play.test.WithApplication;
import services.model_services.AuctionProductServiceImpl;
import services.model_services.AuctionServiceImpl;
import services.model_services.BidService;
import services.model_services.ProductServiceImpl;

import java.util.Date;

public class AuctionManagerTest extends HelperTest {
    @Inject
    AuctionServiceImpl auctionService;

    @Inject
    AuctionProductServiceImpl auctionProductService;

    @Inject
    ProductServiceImpl productService;

    @Inject
    BidService bidService;

    @Inject
    AuctionManager auctionManager;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
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
    public void PlaceBidTest(){
        Long auctionProductId = createAuctionAndProducts();
        Bid bid = new Bid();
        bid.setAuctionProductId(auctionProductId);
        bid.setPrice(300035.2);
        String message = auctionManager.placeBid(1L, bid);
        Assert.assertNull(message);
        bid.setPrice(32321.2);
        message = auctionManager.placeBid(2L, bid);
        String errorMessage = "Bid price " + bid.getPrice() + " is less than base price " + 232433.20 + " of auction.";
        Assert.assertEquals(message, errorMessage);
        bid.setPrice(222321.2);
        message = auctionManager.placeBid(2L, bid);
        String lowerBasePriceError = "Bid price " + bid.getPrice() + " is less than base price " + 232433.20 + " of auction.";
        Assert.assertEquals(message, lowerBasePriceError);
    }


    private Long createAuctionAndProducts(){
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
        auctionProduct.save();
        return auctionProduct.getId();
    }
}
