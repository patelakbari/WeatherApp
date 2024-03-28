package com.sample.weatherapp.adapter

import android.app.Activity
import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.sample.weatherapp.R
import com.sample.weatherapp.databinding.RowItemCitiesBinding
import com.sample.weatherapp.remote.model.CityLocationData
import com.sample.weatherapp.utils.CountryNameByCode
import com.sample.weatherapp.utils.SharedPreferenceUtil
import me.tangobee.weathernaut.model.CityLocationDataItem

class SearchCitiesAdapter(private val citiesList: CityLocationData) :
    RecyclerView.Adapter<SearchCitiesAdapter.SearchCitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCitiesViewHolder {
        val binding =
            RowItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCitiesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    override fun onBindViewHolder(holder: SearchCitiesViewHolder, position: Int) {

        holder.bind(citiesList[position])
        holder.itemView.setOnClickListener {
            citiesList[position].alreadyExist = !citiesList[position].alreadyExist
            SharedPreferenceUtil.slectedCityLocation = citiesList[position].lat.toString().plus(" ")
                .plus(citiesList[position].lon.toString())
            (holder.itemView.context as Activity).finish()
        }
    }

    inner class SearchCitiesViewHolder(private val binding: RowItemCitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityLocationDataItem) {

            binding.txtCityName.text = item.name
            val location =
                (if (item.state.isNullOrEmpty()) "" else (item.state + ", ")) + CountryNameByCode.getCountryNameByCode(
                    binding.root.context,
                    item.country
                )
            binding.txtCountryName.text = location

        }
    }

}