package com.example.front.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.UUID

open class AlarmId() : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    var reservationId: Int = 0
    var alarmId: Int = 0

    constructor(reservationId: Int, alarmId: Int) : this() {
        this.reservationId = reservationId
        this.alarmId = alarmId
    }
}