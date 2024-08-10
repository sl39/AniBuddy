package org.example.anibuddy.user;

import java.util.List;
import java.util.Optional;

import org.example.anibuddy.following.FollowingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}