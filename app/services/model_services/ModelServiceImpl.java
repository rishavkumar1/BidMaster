package services.model_services;

import com.avaje.ebean.Model;
import models.BaseIdModel;
import models.BaseModel;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by vinay on 01/04/16.
 */
public class ModelServiceImpl<I, T extends Model> implements ModelService<I, T> {

    protected Model.Finder<I, T> mFinder;
    protected  Class entityClass;

    public ModelServiceImpl(){
        //getActualTypeArguments()[1] will get parameterizedType T
        Class clazz = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
        entityClass = clazz;
        mFinder = new Model.Finder<>( clazz );
    }

    public ModelServiceImpl(Class<T> clazz) {
        mFinder = new Model.Finder<I, T>(clazz);
    }

    protected synchronized  void init(){}

    @Override
    public Model.Finder<I, T> getFinder() {
        return mFinder;
    }

    @Override
    public int countByField(String field, Object value){
        return mFinder.where().eq(field, value).findRowCount();
    }

    public List<T> findByField(String field, Object value){
        return mFinder.where().eq(field, value).findList();
    }

    @Override
    public T findUniqueByField(String field, Object value) {
        return mFinder.where().eq(field, value).findUnique();
    }

    public List<T> findByFieldList(String field, List list){
        return mFinder.where().in(field, list).findList();
    }

    @Override
    public List<T> findByFieldList(String field, Collection list){
        return mFinder.where().in(field, list).findList();
    }

    @Override
    public List<T> findByIdIn(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Collections.emptyList();
        return mFinder.where().in(BaseIdModel.FIELD_ID, ids).findList();
    }

    public List<T> findAllUpdatedSince(Date updatedSince){
        return mFinder.where().gt(BaseModel.FIELD_UPDATED_AT, updatedSince).findList();
    }

}
