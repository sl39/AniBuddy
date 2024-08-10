package org.example.anibuddy.following;

import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FollowingEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "storeId")
	private StoreEntity storeEntity;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserEntity userEntity;
	
	@Column
	private String storeRoadaddress;
	
	    
}	