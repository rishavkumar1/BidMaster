package services;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import enums.AuctionStatus;
import enums.BidStatus;
import models.AuctionProduct;
import models.BaseIdModel;
import models.Bid;
import play.Logger;
import services.model_services.AuctionProductService;
import services.model_services.BidService;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Singleton
public class AuctionScheduler {

    AuctionProductService auctionProductService;

    BidService bidService;

    NotificationService notificationService;

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    ExecutorService notificationExecutorService = Executors.newFixedThreadPool(10);

    private final String className = AuctionScheduler.class.getSimpleName();

    @Inject
    public AuctionScheduler(AuctionProductService auctionProductService, BidService bidService, NotificationService notificationService) {
        this.auctionProductService = auctionProductService;
        this.bidService = bidService;
        this.notificationService = notificationService;
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                    scheduleAuctions();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void scheduleAuctions(){
        Date currentDate = new Date();
        startAuctions(currentDate);
        closeAuctions(currentDate);
    }

    public void startAuctions(Date currentDate){
        List<AuctionProduct> auctionProducts = auctionProductService.getAuctionsToStart(currentDate);
        for(AuctionProduct auctionProduct : auctionProducts){
            auctionProduct.setStatus(AuctionStatus.RUNNING);
            Logger.info(className + " : startAuctions : started auction for auction product id {}", auctionProduct.getId());
        }
        Ebean.saveAll(auctionProducts);
    }

    public void closeAuctions(Date currentDate){
        List<AuctionProduct> auctionProducts = auctionProductService.getAuctionsToClose(currentDate);
        for(AuctionProduct auctionProduct : auctionProducts){
            auctionProduct.setStatus(AuctionStatus.CLOSED);
            Logger.info(className + " : closeAuctions : closed auction for auction product id {}", auctionProduct.getId());
        }
        List<Long> auctionProductIds = auctionProducts.stream().map(BaseIdModel::getId).collect(Collectors.toList());
        List<Bid> bids = bidService.getBids(auctionProductIds);
        Set<Long> winningBidIds = auctionProducts.stream().map(AuctionProduct::getHighestBidId).collect(Collectors.toSet());
        for(Bid bid : bids){
            Long bidId = bid.getId();
            if(winningBidIds.contains(bidId)){
                bid.setStatus(BidStatus.CONFIRMED);
                Logger.info(className + " : closeAuctions : confirmed bid for bid id {}", bid.getId());
                CompletableFuture.runAsync(() -> {
                    notificationService.sendNotification(bid);
                }, notificationExecutorService);
            }else {
                Logger.info(className + " : closeAuctions : expired bid for bid id {}", bid.getId());
                bid.setStatus(BidStatus.EXPIRED);
            }
        }
        Ebean.saveAll(auctionProducts);
        Ebean.saveAll(bids);
    }
}
