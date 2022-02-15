package com.foobear.carfax.ui.cardetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.FragmentCarDetailsBinding
import com.foobear.carfax.databinding.FragmentCarListBinding
import com.foobear.carfax.ui.carlist.CarListDetailsRecyclerViewAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 * Use the [CarDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarDetailsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CarDetailsViewModelFactory by instance()

    private lateinit var viewModel: CarDetailsViewModel

    private lateinit var binding: FragmentCarDetailsBinding

    private lateinit var navController: NavController

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(CarDetailsViewModel::class.java)

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_car_details,
                container,
                false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initView()
    }

    private fun initView() {
        val disposable = viewModel.getSingleCarDetail("JC1NFAEK5J0138717")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { carDetailsData ->
                Glide.with(requireContext())
                    .load(carDetailsData.firstPhoto)
                    .into(binding.ivCarPhoto)
                binding.tvYear.text = carDetailsData.year.toString()
                binding.tvMake.text = carDetailsData.make
                binding.tvModel.text = carDetailsData.model
                binding.tvTrim.text = carDetailsData.trim
                binding.tvPrice.text = "$" + carDetailsData.currentPrice.toString()
                binding.tvMileage.text = carDetailsData.mileage.toString() + " mi"
                binding.tvCityState.text = carDetailsData.city + ", " + carDetailsData.state
                binding.tvExteriorColor.text = carDetailsData.exteriorColor
                binding.tvInteriorColor.text = carDetailsData.interiorColor
                binding.tvDriveType.text = carDetailsData.drivetype
                binding.tvTransmission.text = carDetailsData.transmission
                binding.tvBodyType.text = carDetailsData.bodytype
                binding.tvEngine.text = carDetailsData.engine

                binding.tvCallDealer.setOnClickListener {
                    if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:" + carDetailsData.phone)
                        startActivity(intent)
                    }
                }

            }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}