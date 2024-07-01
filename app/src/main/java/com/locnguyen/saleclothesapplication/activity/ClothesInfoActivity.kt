package com.locnguyen.saleclothesapplication.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioGroup.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.Application
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.ClothesInfoActivityBinding
import com.locnguyen.saleclothesapplication.fragment.CartFragment
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.model.ClothesColor
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.viewmodel.ClothesInfoVM
import java.text.NumberFormat
import java.util.Locale

class ClothesInfoActivity : AppCompatActivity() {

    private lateinit var binding: ClothesInfoActivityBinding
    private lateinit var clothesInfoVM: ClothesInfoVM
    private val clothesColorIds: ArrayList<Int> by lazy { ArrayList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.clothes_info_activity)
        clothesInfoVM = ViewModelProvider(this)[ClothesInfoVM::class.java]

        binding.lifecycleOwner = this
        binding.clothesInfoVM = clothesInfoVM

        clothesInfoVM.clothes.value = getFromIntent()

        initObserves()
    }

    private fun bindView(clothes: Clothes) {
        binding.apply {
            DataLocal.getInstance().bindImg(this@ClothesInfoActivity, clothes.img[0], clothesImg)

            clothesName.text = clothes.name
            clothesGroupValue.text = clothes.group
            clothesDescriptionValue.text = clothes.description

            val density = DataLocal.getInstance().densityValue
            val linearParam = LinearLayout.LayoutParams((30 * density).toInt(), (30 * density).toInt()).apply {
                    setMargins((10 * density).toInt(), 0, 0, 0)
                }
            clothes.color.forEach { color ->
                val colorView = layoutInflater.inflate(R.layout.item_clothes_color, clothesColorSpace, false)

                colorView.apply {
                        val randomId = View.generateViewId()
                        clothesColorIds.add(randomId)

                        id = randomId
                        layoutParams = linearParam

                        findViewById<ImageButton>(R.id.clothes_color)?.apply{
                            setBackgroundColor(Color.parseColor(color.hexCode))
                            setOnClickListener {
                                this@ClothesInfoActivity.clothesInfoVM.selectColor(randomId)
                            }
                        }
                    }
                clothesColorSpace.addView(colorView)
            }

            firstSize.text = clothes.size[0]
            secondSize.text = clothes.size[1]
            thirdSize.text = clothes.size[2]

            val numberFormat = DataLocal.getInstance().priceFormat
            val formattedNumber = numberFormat.format(clothes.price)
            clothesPrice.text = getString(R.string.Price_regex, formattedNumber)
        }
    }

    private fun getFromIntent(): Clothes? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("CLOTHES", Clothes::class.java)
        } else {
            intent.getSerializableExtra("CLOTHES") as Clothes
        }
    }

    private fun initObserves() {
        clothesInfoVM.clothes.observe(this) { clothes ->
            clothes?.let {
                bindView(it)
            } ?: let {
                Toast.makeText(this, "Không thể xem thông tin quần áo!", Toast.LENGTH_SHORT).show()
            }
        }

        clothesInfoVM.back.observe(this) { isClicked ->
            if (isClicked) {
                finish()
            }
        }

        clothesInfoVM.selectedColor.observe(this) { viewId ->
            setClothesColorSelected(viewId)
        }

        clothesInfoVM.selectedSize.observe(this) { viewId ->
            setClothesSizeSelected(viewId)
        }

        clothesInfoVM.addCart.observe(this) { isAdded ->
            if (isAdded) {
                handleAddToCart()
            }
        }
    }

    private fun handleAddToCart() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Xác nhận thêm giỏ hàng")
            .setMessage("Bạn có chắc muốn thêm vào giỏ hàng chứ!")
            .setCancelable(false)
            .setNegativeButton("Hủy") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Đồng ý") { dialog, which ->
                val selectedColor = getSelectedColor()
                val selectedImg = getImgOfSelectedColor(selectedColor)

                CartFragment.add(
                    SellClothes(
                        img = selectedImg,
                        name = clothesInfoVM.clothes.value!!.name,
                        group = clothesInfoVM.clothes.value!!.group,
                        description = clothesInfoVM.clothes.value!!.description,
                        size = getSelectedSize(),
                        price = clothesInfoVM.clothes.value!!.price,
                        quality = 1,
                        color = selectedColor,
                    )
                )
                Toast.makeText(this, "Đã thêm giỏ hàng thành công!", Toast.LENGTH_SHORT).show()
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.blue))
    }

    private fun getImgOfSelectedColor(selectedColor: ClothesColor): String {
        return clothesInfoVM.getImgName(
            clothesInfoVM.clothes.value!!.img,
            clothesInfoVM.clothes.value!!.name,
            selectedColor.name
        )
    }

    private fun setClothesSizeSelected(viewId: Int) {
        when (viewId) {
            R.id.first_size -> {
                setSizeSelected(binding.firstSize)
                setSizeUnselected(binding.secondSize)
                setSizeUnselected(binding.thirdSize)
            }

            R.id.second_size -> {
                setSizeSelected(binding.secondSize)
                setSizeUnselected(binding.firstSize)
                setSizeUnselected(binding.thirdSize)
            }

            R.id.third_size -> {
                setSizeSelected(binding.thirdSize)
                setSizeUnselected(binding.secondSize)
                setSizeUnselected(binding.firstSize)
            }
        }
    }

    private fun setSizeUnselected(view: TextView) {
        view.apply {
            background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.background_clothes_size_unchecked,
                null
            )
            setTextColor(resources.getColor(R.color.black, null))
        }
    }

    private fun setSizeSelected(view: TextView) {
        view.apply {
            background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.background_clothes_size_checked,
                null
            )
            setTextColor(resources.getColor(R.color.white, null))
        }
    }

    private fun setClothesColorSelected(viewId: Int) {
        clothesColorIds.forEach { id ->
            val colorImgButt = findViewById<CardView>(id)?.findViewById<ImageButton>(R.id.clothes_color)

            if (id == viewId) {
                colorImgButt?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.clothes_color_selected,
                        null
                    )
                )
            } else {
                colorImgButt?.setImageDrawable(null)
            }
        }
    }

    private fun getSelectedColor(): ClothesColor {
        return when (clothesInfoVM.selectedColor.value) {
            R.id.first_color -> clothesInfoVM.clothes.value!!.color[0]
            R.id.second_color -> clothesInfoVM.clothes.value!!.color[1]
            else -> clothesInfoVM.clothes.value!!.color[2]
        }
    }

    private fun getSelectedSize(): String {
        return when (clothesInfoVM.selectedSize.value) {
            R.id.first_size -> clothesInfoVM.clothes.value!!.size[0]
            R.id.second_size -> clothesInfoVM.clothes.value!!.size[1]
            else -> clothesInfoVM.clothes.value!!.size[2]
        }
    }
}