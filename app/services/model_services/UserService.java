package services.model_services;

import com.google.inject.ImplementedBy;
import models.User;
@ImplementedBy(UserServiceImpl.class)
public interface UserService extends ModelService<Long, User>{

}
