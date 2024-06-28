package com.locnguyen.saleclothesapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.adapter.ViewPagerAdapter
import com.locnguyen.saleclothesapplication.databinding.MainActivityBinding
import com.locnguyen.saleclothesapplication.viewmodel.MainVM

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var mainVM: MainVM
    private val viewPagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        mainVM = ViewModelProvider(this)[MainVM::class.java]

        binding.lifecycleOwner = this

        binding.viewPager.apply {
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object: OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(position){
                        1 -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_cart).setChecked(true)
                        }

                        2 -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_order).setChecked(true)
                        }

                        3 -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_account).setChecked(true)
                        }

                        else -> {
                            binding.bottomNav.menu.findItem(R.id.bottom_nav_home).setChecked(true)
                        }
                    }
                }
            })
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
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

        initObserves()
    }

    private fun initObserves() {

    }
}
