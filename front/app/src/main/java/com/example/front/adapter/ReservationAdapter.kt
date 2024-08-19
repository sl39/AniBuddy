package com.example.front.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.front.R
import com.example.front.retrofit.Reservation

class ReservationAdapter(
    private val reservations: MutableList<Reservation>,
    private val onReservationClick: (Reservation) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationTextView: TextView = itemView.findViewById(R.id.textViewReservation)
        val storeNameTextView: TextView = itemView.findViewById(R.id.storeNameTextView) // 매장 이름 TextView 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = reservations[position]

        // 예약 정보 텍스트 설정
        holder.reservationTextView.text = "${reservation.year}-${reservation.month}-${reservation.day} ${reservation.hour}:${reservation.minute} - ${reservation.phoneNumber}"

        // 매장 이름 텍스트 설정
        holder.storeNameTextView.text = reservation.storeName // 매장 이름 표시

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            onReservationClick(reservation) // 클릭 시 예약 정보 전달
        }
    }

    override fun getItemCount(): Int = reservations.size

    fun updateReservations(newReservations: List<Reservation>) {
        reservations.clear()
        reservations.addAll(newReservations)
        notifyDataSetChanged()
    }
}
