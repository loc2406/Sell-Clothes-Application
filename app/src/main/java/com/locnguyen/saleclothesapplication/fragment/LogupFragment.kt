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
import com.locnguyen.saleclothesapplication.databinding.LogupFragmentBinding
import com.locnguyen.saleclothesapplication.viewmodel.LogupVM

class LogupFragment: Fragment() {

    private lateinit var binding: LogupFragmentBinding
    private lateinit var navController: NavController
    private lateinit var logupVM: LogupVM
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireContext()) }
    private val localInstance: DataLocal by lazy { DataLocal.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = LogupFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logupVM = ViewModelProvider(requireActivity())[LogupVM::class.java]
        navController = Navigation.findNavController(view)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.logupVM = logupVM

        initObserves()
    }

    private fun initObserves() {
        logupVM.logup.observe(viewLifecycleOwner){ isClicked ->
            if (isClicked){
                loadingDialog.show()
            }
        }

        logupVM.messageInvalid.observe(viewLifecycleOwner){ message ->
            if (message.isNotEmpty()){
                logupVM.logup.value = false
                loadingDialog.dismiss()
                localInstance.showToast(requireContext(), message)
            }
        }

        logupVM.isLoggedUp.observe(viewLifecycleOwner){ isLogged ->
            loadingDialog.dismiss()

            when (isLogged){
                true -> {
                    localInstance.setIsLoggedIn(true)
                    localInstance.showToast(requireContext(), "Đăng kí tài khoản thành công!")
                    navController.navigate(R.id.action_logupFragment_to_mainFragment)
                    navController.popBackStack()
                }
                false -> {
                    localInstance.showToast(requireContext(), "Đăng kí tài khoản thất bại!")
                }
            }
        }
    }
}