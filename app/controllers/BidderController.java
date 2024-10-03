package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Bid;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import pojo.response.BidHistory;
import pojo.response.FoundProduct;
import services.AuctionManager;
import utils.JsonUtil;
import java.util.List;

public class BidderController extends Controller {

    @Inject
    AuctionManager auctionManager;

    private final String className = AuctionManager.class.getSimpleName();

    public Result searchProduct(Long userId, String category){
        List<FoundProduct> foundProducts = auctionManager.searchProducts(category);
        if(foundProducts.isEmpty()){
            Logger.info(className + " : searchProduct : For userId {}, no product is found for category {}", userId, category);
            return noContent();
        }
        String foundProductsJson = JsonUtil.toJson(foundProducts);
        Logger.info(className + " : searchProduct : For userId {}, found these products {}", userId, category);
        return ok(foundProductsJson);
    }

    public Result placeBid(Long userId){
        JsonNode bidData = null;
        Bid bid = null;
        try {
            bidData = request().body().asJson();
            bid = Json.fromJson(bidData, Bid.class);
        }catch (Exception e){
            Logger.error(className + " : placeBid: Some error occurred while parsing new bid data for userId {} and bidData {}", userId, bidData, e);
            return badRequest("New bid data is not in correct format.");
        }
        try {
            String failureReason = auctionManager.placeBid(userId, bid);
            if(StringUtils.isBlank(failureReason)){
                Logger.info(className + " : placeBid : auction is successfully created for userId {} and bidData {}", userId, bidData);
                return created("Bid is successfully placed.");
            }else{
                Logger.info(className + " : placeBid : bid can't be placed for userId {} because {}", userId, failureReason);
                return badRequest(failureReason);
            }
        }catch (Exception e){
            Logger.error(className + " : placeBid: Some error occurred while creating auction for userId {} and bidData {}", userId, bidData, e);
            return internalServerError("Some error occurred while placing bid");
        }
    }

    public Result getBidHistory(Long userId){
        List<BidHistory> histories = auctionManager.getBidHistory(userId);
        if(histories.isEmpty()){
            return noContent();
        }
        String historyJson = JsonUtil.toJson(histories);
        Logger.info(className + " : getBidHistory : For userId {}, found these bid history {}", historyJson);
        return ok(historyJson);
    }

}
