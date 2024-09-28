package services.model_services;

import com.google.inject.ImplementedBy;
import models.Product;

@ImplementedBy(ProductServiceImpl.class)
public interface ProductService extends ModelService<Long, Product>{
    Product getProduct(Long id, String category);
}
