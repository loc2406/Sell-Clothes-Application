package com.locnguyen.saleclothesapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.adapter.ViewPagerAdapter
import com.locnguyen.saleclothesapplication.databinding.MainFragmentBinding
import com.locnguyen.saleclothesapplication.fragment.MainFragmentDirections
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.viewmodel.MainVM

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var navController: NavController
    private lateinit var mainVM: MainVM
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        navController = Navigation.findNavController(view)
        viewPagerAdapter =ViewPagerAdapter(requireActivity())

        initViewPager()
        initListener()
        initObserves()
    }

    private fun initViewPager() {
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 4

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        1 -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_cart).setChecked(true)
                        }

                        2 -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_order).setChecked(true)
                        }

                        3 -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_account)
                                .setChecked(true)
                        }

                        else -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_home).setChecked(true)
                        }
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> {
                    binding.viewPager.currentItem = 0
                }

                R.id.bottom_nav_cart -> {
                    binding.viewPager.currentItem = 1
                }

                R.id.bottom_nav_order -> {
                    binding.viewPager.currentItem = 2
                }

                R.id.bottom_nav_account -> {
                    binding.viewPager.currentItem = 3
                }
            }

            return@setOnItemSelectedListener true
        }
    }

    private fun initObserves(){
        mainVM.watchClothesInfo.observe(viewLifecycleOwner){ clothes ->
            clothes?.let{handleWatchClothesInfo(it)}
        }
    }

    private fun handleWatchClothesInfo(clothes: Clothes){
        val actionWatchClothesInfo = MainFragmentDirections.actionMainFragmentToClothesInfoFragment(clothes)
        navController.navigate(actionWatchClothesInfo)
        mainVM.watchClothesInfo.value = null
    }
}