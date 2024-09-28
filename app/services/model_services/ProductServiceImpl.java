package services.model_services;

import models.Product;
import utils.Constants;


public class ProductServiceImpl extends ModelServiceImpl<Long, Product> implements ProductService{
    public Product getProduct(Long id, String category){
        return getFinder().where().idEq(id).eq(Constants.categoryField, category).findUnique();
    }
}
