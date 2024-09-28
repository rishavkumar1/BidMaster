package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vinay on 02/11/16.
 */
@MappedSuperclass
public class BaseIdModel extends BaseModel {
    public static final String FIELD_ID = "id";
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    @JsonProperty("remoteId")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static String getIdList(List<? extends BaseIdModel> modelList) {
        if (modelList == null) {
            return "[]";
        }
        return modelList.stream().map(BaseIdModel::getId).collect(Collectors.toList()).toString();
    }

    public static List<Long> getModelIdList(List<? extends BaseIdModel> modelList) {
        return modelList.stream().map(BaseIdModel::getId).collect(Collectors.toList());
    }

}
