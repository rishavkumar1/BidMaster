package controllers;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import enums.AuctionStatus;
import enums.ProductCategory;
import helper.HelperTest;
import models.*;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import services.model_services.*;
import utils.DateUtil;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.*;

public class BidderControllerTest extends HelperTest {

    @Inject
    AuctionServiceImpl auctionService;

    @Inject
    AuctionProductServiceImpl auctionProductService;

    @Inject
    ProductServiceImpl productService;

    @Inject
    BidService bidService;

    String requestTemplate = "{\n" +
            "    \"auctionProductId\" : %AUCTION_PRODUCT_ID%,\n" +
            "    \"price\" : %PRICE%\n" +
            "}";

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
    public void searchProductTest() {
        createAuctionAndProducts();
        Http.RequestBuilder searchRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/searchProduct/1/bike");
        Result searchResult = route(app, searchRequest);
        assertEquals(OK, searchResult.status());
        searchRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/searchProduct/1/clothing");
        searchResult = route(app, searchRequest);
        assertEquals(NO_CONTENT, searchResult.status());
    }

    @Test
    public void bidProductTest(){
        Long auctionProductId = createAuctionAndProducts();
        String auctionProductIdText = String.valueOf(auctionProductId);
        String requestBody = requestTemplate.replace("%AUCTION_PRODUCT_ID%", auctionProductIdText).replace("%PRICE%", "300020.00");
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/placeBid/2").bodyJson(Json.parse(requestBody));
        Result result = route(app, request);
        Bid bid = bidService.getFinder().findList().get(0);
        assertNotNull(bid);
        assertEquals(CREATED, result.status());
        Http.RequestBuilder bidHistoryRequest = new Http.RequestBuilder().method(GET).uri("/getBidHistory/2");
        Result bidHistoryResult = route(app, bidHistoryRequest);
        assertEquals(OK, bidHistoryResult.status());
        request = new Http.RequestBuilder().method(POST).uri("/placeBid/2").bodyText("abc");
        result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
        requestBody = requestTemplate.replace("%AUCTION_PRODUCT_ID%", auctionProductIdText).replace("%PRICE%", "100020.00");
        request = new Http.RequestBuilder().method(POST).uri("/placeBid/2").bodyJson(Json.parse(requestBody));
        result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
        requestBody = requestTemplate.replace("%AUCTION_PRODUCT_ID%", auctionProductIdText).replace("%PRICE%", "290020.00");
        request = new Http.RequestBuilder().method(POST).uri("/placeBid/2").bodyJson(Json.parse(requestBody));
        result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
        request = new Http.RequestBuilder().method(POST).uri("/placeBid/-1").bodyJson(Json.parse(requestBody));
        result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
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
