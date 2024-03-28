package com.sample.weatherapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sample.weatherapp.R
import com.sample.weatherapp.adapter.SearchCitiesAdapter
import com.sample.weatherapp.databinding.ActivitySearchBinding
import com.sample.weatherapp.utils.AppConstants
import com.sample.weatherapp.utils.InternetConnection
import com.sample.weatherapp.utils.gone
import com.sample.weatherapp.utils.show
import com.sample.weatherapp.utils.showSnackbar
import com.sample.weatherapp.utils.toast
import com.sample.weatherapp.viewmodel.SearchViewModel
import com.sample.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.Normalizer

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSearchQueryListener()
    }

    private fun initSearchQueryListener() {
        binding.searchCity.setOnQueryTextListener(object : OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvCities.gone()
                query?.let {
                    binding.txtSearchPlaceholder.show()
                    if (it.isEmpty()) {
                        binding.txtSearchPlaceholder.gone()
                    } else if (query.trim().length <= 2) {
                        binding.txtSearchPlaceholder.text = getString(R.string.search_city)
                    } else {
                        binding.txtSearchPlaceholder.text = getString(R.string.searching)
                        if (InternetConnection.isNetworkAvailable(this@SearchActivity)) {
                            fetchCityList(query);
                        } else {
                            binding.root.showSnackbar(R.string.no_internet)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newCity: String?): Boolean {
                binding.rvCities.gone()
                if (newCity != null) {
                    binding.txtSearchPlaceholder.show()

                    if (newCity.trim().isEmpty()) {
                        binding.txtSearchPlaceholder.gone()
                    } else if (newCity.trim().length <= 2) {
                        binding.txtSearchPlaceholder.text = getString(R.string.search_city)
                    } else {
                        binding.txtSearchPlaceholder.text = getString(R.string.click_search)
                    }
                } else {
                    binding.txtSearchPlaceholder.gone()
                }
                return false
            }
        })
    }

    private fun fetchCityList(query: String) {
        searchViewModel.getCitiesList(query, 5, onSuccess = { cityLocationData ->
            if (!cityLocationData.isEmpty()) {
                binding.rvCities.adapter = SearchCitiesAdapter(cityLocationData)
                binding.txtSearchPlaceholder.gone()
                binding.rvCities.show()
            } else {
                binding.txtSearchPlaceholder.text = getString(R.string.no_results)
                binding.txtSearchPlaceholder.show()
                binding.rvCities.gone()
            }
        }, onError = {
            toast(it)
        })
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}