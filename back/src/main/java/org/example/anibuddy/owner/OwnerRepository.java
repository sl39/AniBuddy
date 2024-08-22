package org.example.anibuddy.owner;

import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Integer> {

    Optional<OwnerEntity> findById(int id);

    Optional<OwnerEntity> findByUserEntity(UserEntity userEntity);


}
