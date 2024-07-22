package com.locnguyen.saleclothesapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.activity.EditInfoActivity
import com.locnguyen.saleclothesapplication.databinding.AccountFragmentBinding
import com.locnguyen.saleclothesapplication.model.User
import com.locnguyen.saleclothesapplication.viewmodel.MainVM

class AccountFragment: Fragment() {

    private lateinit var binding: AccountFragmentBinding
    private lateinit var mainVM: MainVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = mainVM

        initObserves()
    }

    private fun initObserves() {
        mainVM.user.observe(viewLifecycleOwner){ user ->
            user?.let { bindInfo(it) }
        }

        mainVM.editInfo.observe(viewLifecycleOwner){ isClicked ->
            if (isClicked){
                startActivity(Intent(requireActivity(), EditInfoActivity:: class.java))
                mainVM.editInfo.value = false
            }
        }
    }

    private fun bindInfo(user: User) {
        Glide.with(requireContext())
            .load(user.img)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.clothes_ex)
            .into(binding.img)

        binding.apply {
            name.text = user.name.ifEmpty { "Đặt tên" }
            phone.text = user.phone.ifEmpty { "Đặt số điện thoại" }
            email.text = user.email.ifEmpty { "Đặt email" }
            address.text = user.location.ifEmpty { "Đặt địa chỉ" }
        }
    }

}