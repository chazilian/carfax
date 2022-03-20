package com.foobear.carfax.ui.carlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.FragmentCarListBinding
import com.foobear.carfax.ui.OnCarClickListener
import com.foobear.carfax.ui.cardetails.CarDetailsViewModel
import com.foobear.carfax.ui.cardetails.CarDetailsViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


/**
 * A fragment representing a list of Items.
 */
class CarListFragment : Fragment(), OnCarClickListener, KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CarDetailsViewModelFactory by instance()

    private lateinit var viewModel: CarDetailsViewModel

    private lateinit var navController: NavController

    private lateinit var adapter: CarListRecyclerViewAdapter

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

        binding.rvCarList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCarList.itemAnimator = DefaultItemAnimator()
        adapter = CarListRecyclerViewAdapter(this)
        binding.rvCarList.adapter = adapter


        viewModel.getCarListLocal().observe(viewLifecycleOwner, Observer { cars ->
            adapter.setList(cars)
            adapter.notifyDataSetChanged()
        })

        initRecyclerView()
    }

    private fun initRecyclerView(){
    }

    override fun onCarClick(position: Int) {
        val car = adapter.getItem(position)
        val bundle = bundleOf("vin" to car.vin)
        navController.navigate(R.id.action_carListDetailsFragment_to_carDetailsFragment, bundle)
    }

    override fun onDealerClick(position: Int) {
        val car = adapter.getItem(position)
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + car.phone)
        startActivity(intent)
    }
}