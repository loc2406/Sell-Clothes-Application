package com.locnguyen.saleclothesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.application.LoadingDialog
import com.locnguyen.saleclothesapplication.databinding.LoginFragmentBinding
import com.locnguyen.saleclothesapplication.viewmodel.LoginVM

class LoginFragment: Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var navController: NavController
    private lateinit var loginVM: LoginVM
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireContext()) }
    private val localInstance: DataLocal by lazy { DataLocal.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = LoginFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginVM = ViewModelProvider(requireActivity())[LoginVM::class.java]
        navController = Navigation.findNavController(view)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginVM = loginVM

        initObserves()
    }

    private fun initObserves() {
        loginVM.login.observe(viewLifecycleOwner){ isClicked ->
            if (isClicked){
                loadingDialog.show()
            }
        }

        loginVM.logup.observe(viewLifecycleOwner){ isClicked ->
            if (isClicked){
                navController.navigate(R.id.action_loginFragment_to_logupFragment)
                loginVM.logup.value = false
            }
        }

        loginVM.messageInvalid.observe(viewLifecycleOwner){ message ->
            if (message.isNotEmpty()){
                loginVM.login.value = false
                loadingDialog.dismiss()
                localInstance.showToast(requireContext(), message)
            }
        }

        loginVM.isLoggedIn.observe(viewLifecycleOwner){ isLoggedIn ->
            when{
                isLoggedIn.first -> {
                    DataLocal.getInstance().setIsLoggedIn(true)
                    loadingDialog.dismiss()
                    localInstance.showToast(requireContext(), "Đăng nhập thành công!")
                    navController.navigate(R.id.action_loginFragment_to_mainFragment)
                    navController.popBackStack()
                }

                !isLoggedIn.first -> {
                    loadingDialog.dismiss()
                    localInstance.showToast(requireContext(), isLoggedIn.second)
                }
            }
        }
    }
}