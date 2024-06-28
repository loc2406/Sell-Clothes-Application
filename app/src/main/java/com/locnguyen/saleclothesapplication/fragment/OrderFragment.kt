package com.locnguyen.saleclothesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.adapter.OrderAdapter
import com.locnguyen.saleclothesapplication.databinding.OrderFragmentBinding
import com.locnguyen.saleclothesapplication.viewmodel.MainVM

class OrderFragment: Fragment() {

    private lateinit var binding: OrderFragmentBinding
    private lateinit var mainVM: MainVM
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        orderAdapter = OrderAdapter(emptyList())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.listOrder.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        initObserves()
    }

    private fun initObserves() {
        mainVM.listOrder.observe(viewLifecycleOwner){list ->
            orderAdapter.setList(list)
        }
    }

}