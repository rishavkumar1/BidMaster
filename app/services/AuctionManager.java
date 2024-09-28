package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import enums.AuctionStatus;
import enums.BidStatus;
import models.Auction;
import models.AuctionProduct;
import models.Bid;
import models.Product;
import play.Logger;
import pojo.request.NewAuction;
import pojo.request.NewProduct;
import pojo.response.AuctionHistory;
import pojo.response.BidHistory;
import pojo.response.FoundProduct;
import services.model_services.AuctionProductService;
import services.model_services.AuctionService;
import services.model_services.BidService;
import services.model_services.ProductService;
import utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class AuctionManager {

    @Inject
    ProductService productService;

    @Inject
    AuctionProductService auctionProductService;

    @Inject
    AuctionService auctionService;

    @Inject
    BidService bidService;

    private final String className = AuctionManager.class.getSimpleName();

    public void createAuction(Long userId, NewAuction newAuction){
        Auction auction = new Auction();
        auction.setName(newAuction.getAuctionName());
        auction.setVendorId(userId);
        auction.save();
        List<NewProduct> newProducts = newAuction.getNewProducts();
        for(NewProduct newProduct : newProducts){
            saveProduct(newProduct, auction);
        }
        Logger.info(className + ": createAuction : auction with name {} is created by user {}", auction.getName(), userId);
    }

    private void saveProduct(NewProduct newProduct, Auction auction) {
        Product product = new Product();
        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setCategory(newProduct.getCategory());
        product.save();
        AuctionProduct auctionProduct = new AuctionProduct();
        auctionProduct.setAuctionId(auction.getId());
        auctionProduct.setProductId(product.getId());
        auctionProduct.setBasePrice(newProduct.getBasePrice());
        auctionProduct.setBidStartTime(newProduct.getBidStartTime());
        auctionProduct.setBidEndTime(newProduct.getBidEndTime());
        auctionProduct.setStatus(AuctionStatus.PENDING);
        auctionProduct.save();
        Logger.info(className + ": saveProduct : auction product is created with id {} and product with id {}", auctionProduct.getId(), product.getId());
    }

    public List<FoundProduct> searchProducts(String category){
        List<AuctionProduct> auctionProducts = auctionProductService.getCurrentAuctionProducts();
        if(auctionProducts == null || auctionProducts.isEmpty())
            return new ArrayList<>();
        List<FoundProduct> foundProducts = new ArrayList<>();
        for(AuctionProduct auctionProduct : auctionProducts){
            Long productId = auctionProduct.getProductId();
            Product product = productService.getProduct(productId, category);
            if(product != null){
                FoundProduct foundProduct = getFoundProduct(auctionProduct, product);
                foundProducts.add(foundProduct);
                Logger.debug(className + " : searchProducts : product with id {} is found for category {}", product.getId(), category);
            }
        }
        return foundProducts;
    }

    private FoundProduct getFoundProduct(AuctionProduct auctionProduct, Product product) {
        FoundProduct foundProduct = new FoundProduct();
        foundProduct.setName(product.getName());
        foundProduct.setAuctionProductId(auctionProduct.getId());
        foundProduct.setDescription(product.getDescription());
        foundProduct.setBidStartTime(auctionProduct.getBidStartTime());
        foundProduct.setBidEndTime(auctionProduct.getBidEndTime());
        foundProduct.setBasePrice(auctionProduct.getBasePrice());
        foundProduct.setCurrentHighestBid(auctionProduct.getCurrentHighestBid());
        foundProduct.setAuctionStatus(auctionProduct.getStatus());
        Logger.debug(className + " : getFoundProduct : created product details for auction product with id {}", auctionProduct.getId());
        return foundProduct;
    }

    public String placeBid(Long userId, Bid bid){
        Long auctionProductId = bid.getAuctionProductId();
        AuctionProduct auctionProduct = auctionProductService.findRunningAuction(auctionProductId);
        if(auctionProduct != null){
            synchronized (auctionProduct){
                Double bidPrice = bid.getPrice();
                Double auctionBasePrice = auctionProduct.getBasePrice();
                if(bidPrice < auctionBasePrice){
                    return "Bid price " + bidPrice + " is less than base price " + auctionBasePrice + " of auction.";
                }
                Double currentHighestBid = auctionProduct.getCurrentHighestBid();
                if(currentHighestBid != null && bidPrice < currentHighestBid){
                    return "Bid price " + bidPrice + " is less than highest bid price " + currentHighestBid + " of auction.";
                }
                auctionProduct.setCurrentHighestBid(bidPrice);
                bid.setBidTimestamp(new Date());
                bid.setStatus(BidStatus.RUNNING);
                bid.setBidderId(userId);
                bid.save();
                if(currentHighestBid == null || bidPrice > currentHighestBid)
                    auctionProduct.setHighestBidId(bid.getId());
                auctionProduct.save();
                return null;
            }
        }else {
            return "Auction for this product is not running currently.";
        }
    }

    public List<BidHistory> getBidHistory(Long userId){
        List<Bid> bids = bidService.findByField(Constants.bidderIdField, userId);
        List<BidHistory> bidHistories = new ArrayList<>();
        for(Bid bid : bids){
            BidHistory history = buildBidHistory(bid);
            bidHistories.add(history);
        }
        Logger.info(className + " : getBuildHistory : successfully created bid history for user {}", userId);
        return bidHistories;
    }

    private BidHistory buildBidHistory(Bid bid) {
        BidHistory history = new BidHistory();
        AuctionProduct auctionProduct = auctionProductService.getFinder().byId(bid.getAuctionProductId());
        assert auctionProduct != null;
        Auction auction = auctionService.getFinder().byId(auctionProduct.getAuctionId());
        Product product = productService.getFinder().byId(auctionProduct.getProductId());
        history.setBidId(bid.getId());
        assert auction != null;
        history.setAuctionName(auction.getName());
        history.setBidPrice(bid.getPrice());
        history.setBidStatus(bid.getStatus());
        history.setBidTimestamp(bid.getBidTimestamp());
        assert product != null;
        history.setProductName(product.getName());
        history.setProductDescription(product.getDescription());
        Logger.debug(className + " : buildBidHistory : successfully build bid history for bid id {}", bid.getId());
        return history;
    }

    public List<AuctionHistory> getAuctionHistory(Long userId){
        List<Auction> auctions = auctionService.findByField(Constants.vendorUserIdField, userId);
        List<AuctionHistory> histories = new ArrayList<>();
        for(Auction auction : auctions){
            AuctionHistory history = buildAuctionHistory(auction);
            histories.add(history);
        }
        Logger.info(className + " : getAuctionHistory : successfully created auction history for user {}", userId);
        return histories;
    }

    private AuctionHistory buildAuctionHistory(Auction auction){
        List<AuctionProduct> auctionProducts = auctionProductService.findByField(Constants.auctionIdField, auction.getId());
        AuctionHistory history = new AuctionHistory();
        history.setAuctionName(auction.getName());
        List<FoundProduct> foundProducts = new ArrayList<>();
        for(AuctionProduct auctionProduct : auctionProducts){
            Product product = productService.getFinder().byId(auctionProduct.getProductId());
            assert product != null;
            FoundProduct foundProduct = getFoundProduct(auctionProduct, product);
            foundProducts.add(foundProduct);
            Logger.debug(className + " : buildAuctionHistory : successfully build auction history for auction product id {}", auctionProduct.getId());
        }
        history.setProducts(foundProducts);
        return history;
    }
}
