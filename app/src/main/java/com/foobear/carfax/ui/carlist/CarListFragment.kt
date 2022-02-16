package com.foobear.carfax.ui.carlist

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.FragmentCarListBinding
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
class CarListFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CarDetailsViewModelFactory by instance()

    private lateinit var viewModel: CarDetailsViewModel

    private lateinit var navController: NavController

    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: FragmentCarListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CarDetailsViewModel::class.java)
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_car_list,
                container,
                false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.rvCarList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCarList.itemAnimator = DefaultItemAnimator()
        val adapter = CarListRecyclerViewAdapter(
                { selectedCar: CarDetailsData -> goToCarDetails(selectedCar) })
                { selectedDealer: CarDetailsData -> callDealer(selectedDealer) }
        binding.rvCarList.adapter = adapter
        populateList(adapter)
    }

    private fun populateList(adapter: CarListRecyclerViewAdapter) {

        val call = if(isOnline()) {
            viewModel.getCarList()
        } else {
            viewModel.getCarListLocal()
        }
        val disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.setList(it)
                    adapter.notifyDataSetChanged()
                }

        compositeDisposable.add(disposable)
    }

    private fun callDealer(carDetailsData: CarDetailsData){
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + carDetailsData.phone)
                startActivity(intent)
    }

    private fun goToCarDetails(carDetailsData: CarDetailsData){
        val bundle = bundleOf("vin" to carDetailsData.vin)
        navController.navigate(R.id.action_carListDetailsFragment_to_carDetailsFragment, bundle)
    }

    private fun isOnline(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw      = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}