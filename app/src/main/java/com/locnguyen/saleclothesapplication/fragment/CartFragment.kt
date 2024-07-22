package com.locnguyen.saleclothesapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.activity.OrderInfoActivity
import com.locnguyen.saleclothesapplication.adapter.CartAdapter
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.CartFragmentBinding
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.viewmodel.MainVM

class CartFragment : Fragment() {

    private lateinit var binding: CartFragmentBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var mainVM: MainVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = mainVM
        cartAdapter = CartAdapter(emptyList(), viewLifecycleOwner)

        binding.listCart.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        initObserves()
    }

    private fun initObserves() {
        mainVM.listClothesInCart.observe(viewLifecycleOwner) { list ->
            cartAdapter.setList(list)
        }

        mainVM.createOrder.observe(viewLifecycleOwner) { isClicked ->
            if (isClicked) {
                cartAdapter.orderList.value?.let { createOrder(it) }
            }
        }

        cartAdapter.orderList.observe(viewLifecycleOwner) { listClothes ->
            var totalValue: Long = 0

            listClothes.forEach { clothes ->
                totalValue += clothes.price * clothes.quantity
                Log.d("CART", clothes.size + "-----" + clothes.price + "-----" + clothes.quantity)
            }

            val numberFormat = DataLocal.getInstance().priceFormat
            val formattedTotalValue = numberFormat.format(totalValue)
            binding.cartValue.text =
                requireContext().getString(R.string.Price_regex, formattedTotalValue)
        }

        cartAdapter.removeFromCart.observe(viewLifecycleOwner) { clothes ->
            clothes?.let {
                handleRemoveClothesFromCart(it)
            }
        }
    }

    private fun handleRemoveClothesFromCart(clothes: SellClothes) {
        AlertDialog.Builder(requireContext(), R.style.MyAlertDialog)
            .setTitle("Xác nhận xóa khỏi giỏ hàng")
            .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng chứ!")
            .setCancelable(false)
            .setNegativeButton("Hủy") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Đồng ý") { dialog, which ->
                mainVM.removeFromCart(clothes).observe(viewLifecycleOwner) { isRemoved ->
                    when (isRemoved) {
                        true -> DataLocal.getInstance()
                            .showToast(requireContext(), "Xóa khỏi giỏ hàng thành công!")

                        false -> DataLocal.getInstance()
                            .showToast(requireContext(), "Xóa khỏi giỏ hàng thất bại!")
                    }
                }
            }
            .show()
    }

    private fun createOrder(list: List<SellClothes>) {
        val user = mainVM.user.value

        user?.let {
            if (it.name.isEmpty() || it.phone.isEmpty() || it.location.isEmpty()) {
                handleCanNotCreateOrder()
            } else {
                startActivity(Intent(requireContext(), OrderInfoActivity::class.java).apply {
                    putParcelableArrayListExtra("LIST_CLOTHES", list as ArrayList<SellClothes>)
                })
            }
        }
    }

    private fun handleCanNotCreateOrder() {
        DataLocal.getInstance().getAlertDialog(
            requireContext(),
            "Không thể tạo đơn hàng!",
            "Bạn cần thêm đầy đủ thông tin cá nhân để có thể tạo đơn hàng!",
            "Hủy"
        ).show()
    }
}