package org.example.anibuddy.Reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_reservation",
                        columnNames = {"phone_number", "reservation_year", "reservation_month", "reservation_day", "reservation_hour", "reservation_minute"}
                )
        }
)
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약 ID

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber; // 전화번호

    @Column(name = "reservation_year", nullable = false)
    private int year; // 예약 연도

    @Column(name = "reservation_month", nullable = false)
    private int month; // 예약 월

    @Column(name = "reservation_day", nullable = false)
    private int day; // 예약 일

    @Column(name = "reservation_hour", nullable = false)
    private int hour; // 예약 시

    @Column(name = "reservation_minute", nullable = false)
    private int minute; // 예약 분
    }


