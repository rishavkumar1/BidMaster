package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import pojo.request.NewAuction;
import pojo.response.AuctionHistory;
import services.AuctionManager;
import services.ValidationService;
import utils.JsonUtil;
import java.util.List;


public class VendorController extends Controller {
    @Inject
    AuctionManager auctionManager;

    @Inject
    ValidationService validationService;

    private final String className = AuctionManager.class.getSimpleName();

    public Result createAuction(Long userId){
        JsonNode auctionData = request().body().asJson();
        NewAuction newAuction = null;
        try {
            newAuction = Json.fromJson(auctionData, NewAuction.class);
        }catch (Exception e){
            Logger.error(className + " : createAuction: Some error occurred while parsing new auction data for userId {} and auctionData {}", userId, auctionData, e);
            return badRequest("New auction data is not in correct format.");
        }
        String invalidReason = validationService.isNewAuctionValid(newAuction);
        if(StringUtils.isNotBlank(invalidReason)){
            Logger.info(className + " : createAuction : validation failed for new auction creation for userId {} and auctionData {} because {}", userId, auctionData, invalidReason);
            return badRequest(invalidReason);
        }
        try {
            auctionManager.createAuction(userId, newAuction);
            Logger.info(className + " : createAuction : auction is successfully created for userId {} and auctionData {}", userId, auctionData);
            return created("Auction is successfully created");
        }catch (Exception e){
            Logger.error(className + " : createAuction: Some error occurred while creation auction for userId {} and auctionData {}", userId, auctionData, e);
            return internalServerError("Some error occurred while creating auction");
        }
    }

    public Result getAuctionHistory(Long userId){
        List<AuctionHistory> histories = auctionManager.getAuctionHistory(userId);
        if(histories.isEmpty()){
            return noContent();
        }
        String historyJson = JsonUtil.toJson(histories);
        Logger.info(className + " : getAuctionHistory : For userId {}, found these auction history {}", historyJson);
        return ok(historyJson);
    }

}
