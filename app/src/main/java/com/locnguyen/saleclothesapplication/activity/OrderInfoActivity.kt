package com.locnguyen.saleclothesapplication.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.locnguyen.saleclothesapplication.application.DataLocal
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
    private lateinit var listClothes: List<SellClothes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.order_info_activity)
        orderInfoVM = ViewModelProvider(this)[OrderInfoVM::class.java]

        binding.lifecycleOwner = this
        binding.orderInfoVM = orderInfoVM

        listClothes = getFromIntent()

        paymentAdapter = PaymentAdapter(listClothes)

        binding.listClothes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@OrderInfoActivity)
            adapter = paymentAdapter
        }

        initObserves()
    }

    private fun getFromIntent(): List<SellClothes> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableArrayListExtra("LIST_CLOTHES", SellClothes::class.java)?.toList() ?: emptyList()
        }else{
            intent.getParcelableArrayListExtra<SellClothes>("LIST_CLOTHES")?.toList() ?: emptyList()
        }
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
        AlertDialog.Builder(this, R.style.MyAlertDialog)
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
                            DataLocal.getInstance().showToast(this, "Đã tạo đơn hàng thành công!")

                            orderInfoVM.updateCartAfterOrder(listClothes).observe(this){
                                finish()
                            }
                        }

                        false -> DataLocal.getInstance().showToast(this, "Đã tạo đơn hàng thất bại!")
                    }
                }
            }
            .show()
    }

    private fun bindInfo(user: User) {
        binding.apply {
            receiver.text = user.name
            phone.text = Html.fromHtml(resources.getString(R.string.Receiver_phone_regex, user.phone), Html.FROM_HTML_MODE_LEGACY)
            address.text = Html.fromHtml(resources.getString(R.string.Receiver_address_regex, user.location), Html.FROM_HTML_MODE_LEGACY)
            paymentMethod.text = resources.getString(R.string.Money)
        }
    }
}