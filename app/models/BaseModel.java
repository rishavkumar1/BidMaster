package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@MappedSuperclass
public class BaseModel extends Model {

    public static final String FIELD_CREATED_AT = "created_at";
    @JsonIgnore
    @CreatedTimestamp
    @Column(name=FIELD_CREATED_AT)
    protected Date createdAt;

    public static final String FIELD_UPDATED_AT = "updated_at";
    @JsonIgnore
    @UpdatedTimestamp
    @Column(name=FIELD_UPDATED_AT)
    protected Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String toString(){
        return Json.toJson(this).toString();
    }

    @Transient
    private boolean isForceUpdateEnabled = false;

    /**
     * Does a force update, persist controller is disabled and the values are persisted
     */
    @Deprecated
    public void forceUpdate() {
        isForceUpdateEnabled = true;
        this.update();
        isForceUpdateEnabled = false;
    }

    @Deprecated
    public void forceInsert() {
        isForceUpdateEnabled = true;
        this.insert();
        isForceUpdateEnabled = false;
    }

    public boolean isForceUpdateEnabled() {
        return isForceUpdateEnabled;
    }

    private static final String ARRAY_SEPARATOR = ",";

    protected <T extends Enum> Set<T> deserializeSet(String data, Class<T> enumClass, Set<T> into) {
        if(into == null) {
            into = new HashSet<>();
        }
        if(!into.isEmpty()) return into;
        if(data == null || data.trim().isEmpty()) return into;
        String[] splits = data.split(ARRAY_SEPARATOR);
        T[] all = enumClass.getEnumConstants();
        for(String split : splits) {
            for(T obj : all) {
                if(obj.name().equals(split)) {
                    into.add(obj);
                }
            }
        }

        return into;
    }

    protected <T extends Enum> String serializeSet(T...newData) {
        if(newData == null) return null;

        Set<T> set = new HashSet<>();
        for(T obj : newData) {
            set.add(obj);
        }
        return set.stream().map(v -> v.name()).collect(Collectors.joining(ARRAY_SEPARATOR));
    }

}