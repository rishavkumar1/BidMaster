package services;

import com.google.inject.Inject;
import models.*;
import pojo.response.NotificationDetail;
import services.model_services.AuctionProductService;
import services.model_services.AuctionService;
import services.model_services.ProductService;
import services.model_services.UserService;

public class NotificationService {

    @Inject
    AuctionService auctionService;

    @Inject
    AuctionProductService auctionProductService;

    @Inject
    UserService userService;

    @Inject
    ProductService productService;

    public void sendNotification(Bid bid){
        NotificationDetail notificationDetail = prepareNotificationDetails(bid);
        // After here we will call notification service by an API or send a request through kafka to notification service
    }

    private NotificationDetail prepareNotificationDetails(Bid bid) {
        NotificationDetail notificationDetail = new NotificationDetail();
        User bidUser = userService.getFinder().byId(bid.getBidderId());
        AuctionProduct auctionProduct = auctionProductService.getFinder().byId(bid.getAuctionProductId());
        assert auctionProduct != null;
        Double confirmedPrice = auctionProduct.getCurrentHighestBid();
        Product product = productService.getFinder().byId(auctionProduct.getProductId());
        Auction auction = auctionService.getFinder().byId(auctionProduct.getAuctionId());
        notificationDetail.setAuction(auction);
        notificationDetail.setProduct(product);
        notificationDetail.setUser(bidUser);
        notificationDetail.setConfirmedPrice(confirmedPrice);
        return notificationDetail;
    }
}
