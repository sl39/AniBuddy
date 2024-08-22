package org.example.anibuddy.reservation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonBackReference
    @ManyToOne
    private UserEntity userEntity;

    @JsonBackReference
    @ManyToOne
    private StoreEntity storeEntity;

    @Column
    private LocalDateTime reservationDate;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Column
    private Integer state;
}


