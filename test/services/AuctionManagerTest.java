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
import pojo.request.NewAuction;
import pojo.request.NewProduct;
import pojo.response.AuctionHistory;
import pojo.response.BidHistory;
import pojo.response.FoundProduct;
import services.model_services.AuctionProductServiceImpl;
import services.model_services.AuctionServiceImpl;
import services.model_services.BidService;
import services.model_services.ProductServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        List<BidHistory> bidHistories = auctionManager.getBidHistory(1L);
        Assert.assertEquals(1L, bidHistories.size());
        BidHistory history = bidHistories.get(0);
        Assert.assertEquals("September Grand Auction", history.getAuctionName());
        Assert.assertEquals("royal_enfield_bike", history.getProductName());
        Assert.assertEquals(300035.2, history.getBidPrice(), 0.00001);
    }

    @Test
    public void searchProductTest(){
        Long auctionProductId = createAuctionAndProducts();
        List<FoundProduct> foundProducts = auctionManager.searchProducts("bike");
        Assert.assertEquals(1L, foundProducts.size());
        FoundProduct foundProduct = foundProducts.get(0);
        Assert.assertEquals(auctionProductId, foundProduct.getAuctionProductId());
        Assert.assertEquals("royal_enfield_bike", foundProduct.getName());
        Assert.assertEquals(232433.20, foundProduct.getBasePrice(), 0.00001);
    }

    @Test
    public void createAuctionTest(){
        NewAuction newAuction = new NewAuction();
        NewProduct newProduct = new NewProduct();
        newProduct.setName("royal_enfield_bike");
        newProduct.setDescription("royal enfield classic 350 bike");
        newProduct.setBasePrice(232433.20);
        newProduct.setCategory(ProductCategory.bike);
        Date now = new Date();
        Date startTime = DateUtils.addDays(now, -1);
        Date endTime = DateUtils.addDays(now, 1);
        newProduct.setBidStartTime(startTime);
        newProduct.setBidEndTime(endTime);
        newAuction.setNewProducts(Collections.singletonList(newProduct));
        newAuction.setAuctionName("September Grand Auction");
        auctionManager.createAuction(1L, newAuction);
        List<AuctionHistory> auctionHistories = auctionManager.getAuctionHistory(1L);
        Assert.assertEquals(1L, auctionHistories.size());
        AuctionHistory history = auctionHistories.get(0);
        Assert.assertEquals("September Grand Auction", history.getAuctionName());
        FoundProduct foundProduct = history.getProducts().get(0);
        Assert.assertEquals("royal_enfield_bike", foundProduct.getName());
        Assert.assertEquals(232433.20, foundProduct.getBasePrice(), 0.00001);
    }
    @Test
    public void getAuctionHistoryTest(){
        Long auctionProductId = createAuctionAndProducts();
        List<AuctionHistory> auctionHistories = auctionManager.getAuctionHistory(1L);
        Assert.assertEquals(1L, auctionHistories.size());
        AuctionHistory history = auctionHistories.get(0);
        Assert.assertEquals("September Grand Auction", history.getAuctionName());
        FoundProduct foundProduct = history.getProducts().get(0);
        Assert.assertEquals(auctionProductId, foundProduct.getAuctionProductId());
        Assert.assertEquals("royal_enfield_bike", foundProduct.getName());
        Assert.assertEquals(232433.20, foundProduct.getBasePrice(), 0.00001);
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
