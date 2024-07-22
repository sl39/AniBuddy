package org.example.anibuddy.store.storeMongo;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Document
public class StoreMongoEntity {

    @Id
    private String _id;

    private String category;
    private List<String> facilities;
    private String info;
    private List<String> keywords;
    private String mapx;
    private String mapy;
    private List<String> menuList;
    private String name;
    private List<String> openDay;
    private String parking;
    private String phone_number;
    private String rating;
    private List<ReviewMongoDto> reviewList;


}
