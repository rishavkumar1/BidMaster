package services;

import org.apache.commons.lang3.StringUtils;
import pojo.request.NewAuction;
import pojo.request.NewProduct;

import java.util.Date;
import java.util.List;

public class ValidationService {
    public String isNewAuctionValid(NewAuction newAuction){
        if(StringUtils.isBlank(newAuction.getAuctionName()))
            return "auction name is blank";
        List<NewProduct> newProductList = newAuction.getNewProducts();
        if(newProductList == null || newProductList.isEmpty())
            return "no product is found in auction";
        for(NewProduct newProduct : newProductList){
            if(StringUtils.isBlank(newProduct.getName()))
                return "product name is blank";
            if(StringUtils.isBlank(newProduct.getDescription()))
                return "product description is blank";
            if(newProduct.getCategory() == null)
                return "product category is blank";
//            if(newProduct.getBidStartTime() == null || new Date().compareTo(newProduct.getBidStartTime()) >= 0)
//                return "bid start time is invalid";
//            if(newProduct.getBidEndTime() == null || new Date().compareTo(newProduct.getBidEndTime()) >= 0)
//                return "bid end time is invalid";
        }
        return null;
    }
}
