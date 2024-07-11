package com.locnguyen.saleclothesapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.activity.OrderInfoActivity
import com.locnguyen.saleclothesapplication.adapter.CartAdapter
import com.locnguyen.saleclothesapplication.databinding.CartFragmentBinding
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.viewmodel.MainVM

class CartFragment: Fragment() {

    private lateinit var binding: CartFragmentBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var mainVM: MainVM

    companion object{
        private val listClothes: MutableLiveData<List<SellClothes>> = MutableLiveData(emptyList())

        fun add(newClothes: SellClothes){
            val existedClothes = listClothes.value!!.find { clothes ->
                clothes.name == newClothes.name && clothes.size == newClothes.size && clothes.color == newClothes.color
            }

           if (existedClothes != null){
               existedClothes.quantity += newClothes.quantity
               listClothes.value = listClothes.value
           }
            else{
               val updatedList =  (listClothes.value!! + newClothes)
               listClothes.value = updatedList
           }
        }

        fun clearCart(){
            listClothes.value  = emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false)
        cartAdapter = CartAdapter(listClothes.value!!)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = mainVM

        binding.listCart.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            binding.listCart.adapter = cartAdapter
        }

        initObserves()
    }

    private fun initObserves(){
        listClothes.observe(viewLifecycleOwner){ list ->
            cartAdapter.setList(list)
        }

        mainVM.deleteState.observe(viewLifecycleOwner){ state ->
            cartAdapter.setDeleteState(state)

            if (state){
                binding.removeCart.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_close_blue, null))
                binding.createOrder.apply {
                    isClickable = false
                    alpha = 0.3f
                }
                binding.confirmDeleteCart.visibility = VISIBLE
            }
            else{
                binding.removeCart.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_remove_cart, null))
                binding.createOrder.apply {
                    isClickable = true
                    alpha = 1f
                }
                binding.confirmDeleteCart.visibility = GONE
            }
        }

        mainVM.confirmDelete.observe(viewLifecycleOwner){ isConfirmed ->
            if (isConfirmed){
                handleConfirmDelete()
            }
        }

        mainVM.createOrder.observe(viewLifecycleOwner){ isClicked ->
            if (isClicked){
                createOrder()
            }
        }
    }

    private fun createOrder() {
        OrderInfoActivity.setListClothes(cartAdapter.getList())
        startActivity(Intent(requireContext(), OrderInfoActivity::class.java))
    }

    private fun handleConfirmDelete() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận xóa quần áo")
            .setMessage("Bạn có chắc muốn xóa khỏi giỏ hàng chứ!")
            .setCancelable(false)
            .setNegativeButton("Hủy"){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Đồng ý"){ dialog, which ->
               cartAdapter.deleteClothesFromCart()
                mainVM.offDeleteState()
                Toast.makeText(requireContext(), "Đã xóa khỏi giỏ hàng!", Toast.LENGTH_SHORT).show()
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(requireContext().getColor(R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(requireContext().getColor(R.color.blue))
    }
}