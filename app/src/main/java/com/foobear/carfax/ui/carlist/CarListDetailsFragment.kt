package com.foobear.carfax.ui.carlist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.FragmentCarListBinding
import com.foobear.carfax.ui.cardetails.CarDetailsViewModel
import com.foobear.carfax.ui.cardetails.CarDetailsViewModelFactory
import com.foobear.carfax.ui.dummy.DummyContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A fragment representing a list of Items.
 */
class CarListDetailsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CarDetailsViewModelFactory by instance()

    private lateinit var viewModel: CarDetailsViewModel

    private lateinit var binding: FragmentCarListBinding

    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CarDetailsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initRecyclerView(view)

    }

    private fun initRecyclerView(view: View){
        val carList = view.findViewById<RecyclerView>(R.id.rv_car_list)
        carList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        carList.itemAnimator = DefaultItemAnimator()
        populateList(carList)
    }

    private fun populateList(carList: RecyclerView) = CoroutineScope(Dispatchers.IO).launch {
        val results = viewModel.getCarList()
        withContext(Dispatchers.Main) {
            results?.observe(viewLifecycleOwner, Observer { list ->
                if (list == null) return@Observer
                carList.adapter = CarListDetailsRecyclerViewAdapter(list,
                        {selectedCar: CarDetailsData -> goToCarDetails(selectedCar)}) {
                    selectedDealer: CarDetailsData -> callDealer(selectedDealer)
                     }
            })
        }
    }

    private fun callDealer(carDetailsData: CarDetailsData){
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    return
                }
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:" + carDetailsData.phone)
                startActivity(intent)
    }

    private fun goToCarDetails(carDetailsData: CarDetailsData){
        viewModel.carVin.value = carDetailsData.vin
        navController.navigate(R.id.action_carListDetailsFragment_to_carDetailsFragment)
    }
}