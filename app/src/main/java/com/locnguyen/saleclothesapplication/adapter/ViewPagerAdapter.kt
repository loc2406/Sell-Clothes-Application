package com.locnguyen.saleclothesapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.locnguyen.saleclothesapplication.fragment.CartFragment
import com.locnguyen.saleclothesapplication.fragment.HomeFragment
import com.locnguyen.saleclothesapplication.fragment.OrderFragment
import com.locnguyen.saleclothesapplication.fragment.AccountFragment

class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val cartFragment: CartFragment by lazy { CartFragment() }
    private val orderFragment: OrderFragment by lazy { OrderFragment() }
    private val accountFragment: AccountFragment by lazy { AccountFragment() }

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            1 -> cartFragment
            2 -> orderFragment
            3 -> accountFragment
            else -> homeFragment
        }
    }
}