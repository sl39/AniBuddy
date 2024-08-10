package com.example.front

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PetAdapter(private val pets: List<PetDTO>, private val onItemClick: (PetDTO) -> Unit) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.profile_pet_recyclerview, parent, false)
            return PetViewHolder(view)
        }

        override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
            val pet = pets[position]
            holder.bind(pet)
            holder.itemView.setOnClickListener {
                onItemClick(pet)
            }
}
            override fun getItemCount(): Int = pets.size

            class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val petNameTextView: TextView =
                    itemView.findViewById(R.id.profile_my_pet_name1)
                private val petKindTextView: TextView =
                    itemView.findViewById(R.id.textView_my_pet_kind1)
                private val petGenderTextView: TextView =
                    itemView.findViewById(R.id.profile_my_pet_gender1)
                private val petAgeTextView: TextView =
                    itemView.findViewById(R.id.textView_my_pet_age1)
                private val base64ImageView: ImageView =
                    itemView.findViewById(R.id.imageView_my_pet_1)

                fun bind(pet: PetDTO) {
                    petNameTextView.text = pet.petName
                    petKindTextView.text = pet.petKind
                    petGenderTextView.text = pet.petGender
                    petAgeTextView.text = pet.petAge.toString()
                    //val trimmedUrl = pet.base64Image?.trim()
                    Log.d("base64Image 불러와지나", "base64Image? = ${pet.base64Image}")
                    Glide.with(itemView.context)
                        .load(pet.base64Image) // 프로필 이미지 URL , 절대경로,상대경로확인?
                        .placeholder(R.drawable.anibuddy_logo) // 로딩 중 표시할 이미지
                        .error(R.drawable.anibuddy_logo) // 로드 실패 시 표시할 이미지
                        .into(base64ImageView) // 이미지를 로드할 ImageView

            }
        }
    }