package controllers;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import helper.HelperTest;
import models.Auction;
import models.AuctionProduct;
import models.Product;
import models.User;
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

public class VendorControllerTest extends HelperTest {

    @Inject
    AuctionServiceImpl auctionService;

    @Inject
    AuctionProductServiceImpl auctionProductService;

    @Inject
    ProductServiceImpl productService;

    String requestTemplate = "{\n" +
            "    \"auctionName\" : \"%AUCTION_NAME%\",\n" +
            "    \"newProducts\" : [\n" +
            "        {\n" +
            "            \"name\" : \"%PRODUCT_NAME%\",\n" +
            "            \"description\" : \"%PRODUCT_DESC%\",\n" +
            "            \"bidStartTime\" : \"%START_DATE%\",\n" +
            "            \"bidEndTime\" : \"%END_DATE%\",\n" +
            "            \"basePrice\" : %BASE_PRICE%,\n" +
            "            \"category\" : \"%CATEGORY%\"\n" +
            "        }\n" +
            "    ]\n" +
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
    }


    @Test
    public void createAuctionTest() {
        String startDate = DateUtil.format(DateUtils.addDays(new Date(), -1), DateUtil.istDateFormat);
        String endDate = DateUtil.format(DateUtils.addDays(new Date(), 1), DateUtil.istDateFormat);
        String requestBody = requestTemplate.replace("%AUCTION_NAME%", "September Grand Auction").replace("%PRODUCT_NAME%", "royal_enfield_bike").replace("%PRODUCT_DESC%", "royal enfield classic 350 bike")
                .replace("%START_DATE%", startDate).replace("%END_DATE%", endDate).replace("%BASE_PRICE%", "232433.20").replace("%CATEGORY%", "bike");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/createAuction/1").bodyJson(Json.parse(requestBody));
        Result result = route(app, request);
        AuctionProduct auctionProduct = auctionProductService.getFinder().findList().get(0);
        Auction auction = auctionService.getFinder().findList().get(0);
        Product product = productService.getFinder().findList().get(0);
        assertNotNull(auction);
        assertNotNull(product);
        assertNotNull(auctionProduct);
        assertEquals(CREATED, result.status());

        Http.RequestBuilder getAuctionHistoryRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/getAuctionHistory/1");
        Result auctionHistoryResult = route(app, getAuctionHistoryRequest);
        assertEquals(OK, auctionHistoryResult.status());
    }
}
