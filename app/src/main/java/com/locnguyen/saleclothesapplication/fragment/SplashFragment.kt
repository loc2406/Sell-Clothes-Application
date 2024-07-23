package com.locnguyen.saleclothesapplication.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.SplashFragmentBinding

class SplashFragment: Fragment() {

    private lateinit var splashScreen: SplashScreen
    private lateinit var binding: SplashFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashFragmentBinding.inflate(layoutInflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            splashScreen = requireActivity().installSplashScreen()
            splashScreen.setKeepOnScreenCondition{true}
        }
        else{
            requireActivity().setTheme(R.style.Theme_SaleClothesApplication)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        checkLogin()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            splashScreen.setKeepOnScreenCondition{false}
        }
    }

    private fun checkLogin() {
        if (DataLocal.getInstance().getIsLoggedIn()){
            goMainScreen()
        }
        else{
            goLoginScreen()
        }
    }

    private fun goLoginScreen() {
        navController.navigate(R.id.action_splashFragment_to_loginFragment)
    }

    private fun goMainScreen() {
        navController.navigate(R.id.action_splashFragment_to_mainFragment)
    }
}