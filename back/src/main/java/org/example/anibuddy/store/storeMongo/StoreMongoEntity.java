package org.example.anibuddy.store.storeMongo;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@Document
public class StoreMongoEntity {
    @Id
    private int _id;
    private List<String> storeImageList;
    private String category;
    private List<String> facilities;
    private String info;
    private List<String> keywords;
    private Integer mapx;
    private Integer mapy;
    private List<String> menuList;
    private String name;
    private List<String> openDay;
    private String parking;
    private String phone_number;
    private String rating;
//    private List<ReviewMongoDto> reviewList;
    private String address;
    private String roadAddress;


}
