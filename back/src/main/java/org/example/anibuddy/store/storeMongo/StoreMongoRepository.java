package org.example.anibuddy.store.storeMongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreMongoRepository extends MongoRepository<StoreMongoEntity, String> {

}
