package com.foobear.carfax.ui.carlist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.ui.cardetails.CarDetailsViewModel
import com.foobear.carfax.ui.cardetails.CarDetailsViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
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

    private lateinit var navController: NavController

    private val compositeDisposable = CompositeDisposable()


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

    private fun populateList(carList: RecyclerView) {

       val disposable = viewModel.getCarList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                carList.adapter = CarListDetailsRecyclerViewAdapter(it,
                    { selectedCar: CarDetailsData -> goToCarDetails(selectedCar) })
                { selectedDealer: CarDetailsData -> callDealer(selectedDealer)
                }
            }

        compositeDisposable.add(disposable)
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
        navController.navigate(R.id.action_carListDetailsFragment_to_carDetailsFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}