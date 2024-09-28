package services.model_services;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by vinay on 01/04/16.
 */
public interface ModelService<I, T extends Model> {

    Model.Finder<I, T> getFinder();

    List<T> findByIdIn(List<Long> ids);

    int countByField(String field, Object value);

    List<T> findByField(String field, Object value);

    T findUniqueByField(String field, Object value);

    List<T> findByFieldList(String field, Collection value);

    List<T> findByFieldList(String field, List value);

    List<T> findAllUpdatedSince(Date updatedSince);

}
