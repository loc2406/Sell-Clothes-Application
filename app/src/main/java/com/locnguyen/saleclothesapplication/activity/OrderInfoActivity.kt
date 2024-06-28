package com.locnguyen.saleclothesapplication.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.adapter.PaymentAdapter
import com.locnguyen.saleclothesapplication.databinding.OrderInfoActivityBinding
import com.locnguyen.saleclothesapplication.fragment.CartFragment
import com.locnguyen.saleclothesapplication.model.Order
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.model.User
import com.locnguyen.saleclothesapplication.viewmodel.OrderInfoVM
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class OrderInfoActivity : AppCompatActivity() {
    private lateinit var binding: OrderInfoActivityBinding
    private lateinit var orderInfoVM: OrderInfoVM
    private lateinit var paymentAdapter: PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.order_info_activity)
        orderInfoVM = ViewModelProvider(this)[OrderInfoVM::class.java]

        binding.lifecycleOwner = this
        binding.orderInfoVM = orderInfoVM

        paymentAdapter = PaymentAdapter(listClothes)

        binding.listClothes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@OrderInfoActivity)
            adapter = paymentAdapter
        }

        initObserves()
    }

    private fun initObserves() {
        orderInfoVM.user.observe(this) { user ->
            user?.let {
                bindInfo(it)
            } ?: let {
                Toast.makeText(
                    this,
                    "Có lỗi xảy ra! Vui lòng khởi động lại ứng dụng!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

        orderInfoVM.back.observe(this) { isClicked ->
            if (isClicked) {
                finish()
            }
        }

        orderInfoVM.confirmCreateOrder.observe(this) { isClicked ->
            if (isClicked) {
                handleCreateOrder()
            }
        }

        paymentAdapter.totalOrderValue.observe(this){ value ->
            calculateTotalOrderValue(value)
        }
    }

    private fun calculateTotalOrderValue(value: Long){
        val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)
        binding.totalClothesValue.text = getString(
            R.string.Price_regex,
            numberFormat.format(value)
        )

        binding.shipMoneyValue.text = getString(R.string.Price_regex, numberFormat.format(50000))

        binding.totalMoneyValue.text = getString(
            R.string.Price_regex,
            numberFormat.format(value + 50000)
        )
    }

    private fun handleCreateOrder() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Xác nhận tạo đơn hàng")
            .setMessage("Bạn có chắc muốn tạo đơn hàng này chứ!")
            .setCancelable(false)
            .setNegativeButton("Hủy") { dialog, which ->
                dialog.dismiss()
                orderInfoVM.confirmCreateOrder.value = false
            }
            .setPositiveButton("Đồng ý") { dialog, which ->
                orderInfoVM.createOrder(
                    Order(
                        id = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss")),
                        date = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        listClothes = listClothes,
                        receiver = orderInfoVM.user.value!!.name,
                        phone = orderInfoVM.user.value!!.phone,
                        address = orderInfoVM.user.value!!.location,
                        price = paymentAdapter.totalOrderValue.value!!,
                    )
                ).observe(this) { isCreated ->
                    when (isCreated) {
                        true -> {
                            Toast.makeText(
                                this,
                                "Đã tạo đơn hàng thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                            CartFragment.clearCart()
                            finish()
                        }

                        false -> {
                            Toast.makeText(
                                this,
                                "Đã tạo đơn hàng thất bại!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.blue))
    }

    private fun bindInfo(user: User) {
        binding.apply {
            val builder = SpannableStringBuilder()
            val boldSpan = StyleSpan(android.graphics.Typeface.BOLD)

            builder.append("Người nhận hàng: ")
            var endTitle = builder.length
            builder.append(user.name)

            builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            receiver.text = builder
            builder.clear()

            builder.append("Điện thoại: ")
            endTitle = builder.length
            builder.append(user.phone.toString())

            builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            phone.text = builder

            builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            receiver.text = builder
            builder.clear()

            builder.append("Địa chỉ nhận hàng: ")
            endTitle = builder.length
            builder.append(user.location)

            builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            address.text = builder

            builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            receiver.text = builder
            builder.clear()

            builder.append("Phương thức thanh toán: ")
            endTitle = builder.length
            builder.append("Tiền mặt")

            builder.setSpan(boldSpan, 0, endTitle, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            paymentMethod.text = builder
        }
    }

    companion object {
        private var listClothes: List<SellClothes> = emptyList()

        fun setListClothes(list: List<SellClothes>) {
            listClothes = list
        }
    }
}