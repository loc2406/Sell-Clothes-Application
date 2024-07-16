package com.locnguyen.saleclothesapplication.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.material3.HorizontalDivider
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.adapter.AutoLoadImgAdapter
import com.locnguyen.saleclothesapplication.adapter.CommentAdapter
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.ClothesInfoActivityBinding
import com.locnguyen.saleclothesapplication.fragment.CartFragment
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.model.ClothesColor
import com.locnguyen.saleclothesapplication.model.Comment
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.viewmodel.ClothesInfoVM
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executor

class ClothesInfoActivity : AppCompatActivity() {

    private val clothesColorIds: ArrayList<Int> by lazy { ArrayList() }
    private val clothesSizeIds: ArrayList<Int> by lazy { ArrayList() }
    private val autoLoadImgTimer: Timer by lazy { Timer() }
    private val autoLoadImgExecutor: Executor by lazy { ContextCompat.getMainExecutor(this) }
    private var selectedColor: ClothesColor = ClothesColor()
    private var selectedImg: String = ""
    private var selectedSize: String = ""

    private lateinit var autoLoadImgAdapter: AutoLoadImgAdapter
    private lateinit var binding: ClothesInfoActivityBinding
    private lateinit var clothesInfoVM: ClothesInfoVM
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.clothes_info_activity)
        clothesInfoVM = ViewModelProvider(this)[ClothesInfoVM::class.java]

        binding.lifecycleOwner = this
        binding.clothesInfoVM = clothesInfoVM

        clothesInfoVM.clothes.value = getFromIntent()

        initObserves()
    }

    private fun bindClothesOnView(clothes: Clothes) {
        bindListImg(clothes.imgs)
        bindListColor(clothes.colors)
        bindListSize(clothes.sizes)

        binding.apply {
            clothesName.text = clothes.name
            clothesSellValue.text = clothes.sell.toString()
            clothesGroupValue.text = clothes.group
            clothesDescriptionValue.text = clothes.description
            val numberFormat = DataLocal.getInstance().priceFormat
            val formattedNumber = numberFormat.format(clothes.price)
            clothesPrice.text = getString(R.string.Price_regex, formattedNumber)
        }

        bindComment(clothes.comments)
    }

    private fun bindComment(comments: List<Comment>) {
        val builder = SpannableStringBuilder()
        val blackColorSpan = ForegroundColorSpan(Color.BLACK)
        val boldTextSpan = StyleSpan(Typeface.BOLD)

        builder.append(getString(R.string.Comment_title))
        builder.setSpan(boldTextSpan, 0, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val endTitle = builder.length

        builder.append(" ${comments.size} bình luận.")
        builder.setSpan(
            blackColorSpan,
            endTitle,
            builder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.clothesCommentTitle.text = builder

        binding.fiveStarValue.text = getString(
            R.string.Comment_value_regex,
            comments.filter { comment -> comment.star == 5 }.size
        )
        binding.fourStarValue.text = getString(
            R.string.Comment_value_regex,
            comments.filter { comment -> comment.star == 4 }.size
        )
        binding.threeStarValue.text = getString(
            R.string.Comment_value_regex,
            comments.filter { comment -> comment.star == 3 }.size
        )
        binding.twoStarValue.text = getString(
            R.string.Comment_value_regex,
            comments.filter { comment -> comment.star == 2 }.size
        )
        binding.oneStarValue.text = getString(
            R.string.Comment_value_regex,
            comments.filter { comment -> comment.star == 1 }.size
        )

        commentAdapter = CommentAdapter(comments)

        binding.listComment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ClothesInfoActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@ClothesInfoActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = commentAdapter
        }
    }

    private fun bindListSize(sizes: List<String>) {
        val density = DataLocal.getInstance().densityValue
        val itemSizeParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins((10 * density).toInt(), 0, 0, 0)
        }

        binding.apply {
            sizes.forEach { size ->
                val itemSize = layoutInflater.inflate(
                    R.layout.item_clothes_size,
                    clothesSizeSpace,
                    false
                ) as TextView?

                itemSize?.apply {
                    val randomId = View.generateViewId()
                    clothesSizeIds.add(randomId)

                    id = randomId
                    layoutParams = itemSizeParam
                    text = size

                    setOnClickListener {
                        this@ClothesInfoActivity.clothesInfoVM.selectSize(randomId)
                    }
                }
                clothesSizeSpace.addView(itemSize)
            }
        }
    }

    private fun bindListColor(colors: List<ClothesColor>) {
        val density = DataLocal.getInstance().densityValue
        val itemColorParam =
            LinearLayout.LayoutParams((30 * density).toInt(), (30 * density).toInt()).apply {
                setMargins((10 * density).toInt(), 0, 0, 0)
            }

        binding.apply {
            colors.forEach { color ->
                val itemColor =
                    layoutInflater.inflate(R.layout.item_clothes_color, clothesColorSpace, false)

                itemColor.apply {
                    val randomId = View.generateViewId()
                    clothesColorIds.add(randomId)

                    id = randomId
                    layoutParams = itemColorParam

                    findViewById<ImageView>(R.id.clothes_color)?.apply {
                        setBackgroundColor(Color.parseColor(color.hexCode))
                        setOnClickListener {
                            this@ClothesInfoActivity.clothesInfoVM.selectColor(randomId)
                        }
                    }
                }
                clothesColorSpace.addView(itemColor)
            }
        }
    }

    private fun bindListImg(imgs: List<String>) {
        binding.apply {
            autoLoadImgAdapter = AutoLoadImgAdapter(imgs)
            clothesImgSpace.adapter = autoLoadImgAdapter

            imgIndicator.apply {
                setViewPager(clothesImgSpace)
                tintIndicator(
                    resources.getColor(R.color.blue, null),
                    resources.getColor(R.color.black, null)
                )
            }
            autoLoadImgAdapter.registerAdapterDataObserver(imgIndicator.adapterDataObserver)

            autoLoadImgTimer.schedule(object : TimerTask() {
                override fun run() {
                    autoLoadImgExecutor.execute {
                        var currentImg = clothesImgSpace.currentItem
                        val totalImgs = imgs.size - 1

                        if (currentImg < totalImgs) {
                            currentImg++
                            clothesImgSpace.currentItem = currentImg
                        } else {
                            clothesImgSpace.currentItem = 0
                        }
                    }
                }
            }, 1000, 3000)
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
                bindClothesOnView(it)
            } ?: let {
                Toast.makeText(this, "Không thể xem thông tin quần áo!", Toast.LENGTH_SHORT).show()
            }
        }

        clothesInfoVM.back.observe(this) { isClicked ->
            if (isClicked) {
                finish()
            }
        }

        clothesInfoVM.selectedColorId.observe(this) { viewId ->
            setClothesColorSelected(viewId)
        }

        clothesInfoVM.selectedSizeId.observe(this) { viewId ->
            setClothesSizeSelected(viewId)
        }

        clothesInfoVM.selectedFilterId.observe(this) { filterId ->
            setCommentFilter(filterId)
        }

        clothesInfoVM.addCart.observe(this) { isAdded ->
            if (isAdded) {
                handleAddToCart()
            }
        }
    }

    private fun setCommentFilter(filterId: Int) {
        when (filterId) {
            R.id.filter_all -> {
                setSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(binding.filterFiveStar, binding.fiveStarTitle, binding.fiveStarValue)
                setUnSelectedFilter(binding.filterFourStar, binding.fourStarTitle, binding.fourStarValue)
                setUnSelectedFilter(binding.filterThreeStar, binding.threeStarTitle, binding.threeStarValue)
                setUnSelectedFilter(binding.filterTwoStar, binding.twoStarTitle, binding.twoStarValue)
                setUnSelectedFilter(binding.filterOneStar, binding.oneStarTitle, binding.oneStarValue)

                val filterList = clothesInfoVM.clothes.value!!.comments
                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    commentAdapter.setFilterList(filterList)
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_five_star -> {
                setSelectedFilter(binding.filterFiveStar, binding.fiveStarTitle, binding.fiveStarValue)
                setUnSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(binding.filterFourStar, binding.fourStarTitle, binding.fourStarValue)
                setUnSelectedFilter(binding.filterThreeStar, binding.threeStarTitle, binding.threeStarValue)
                setUnSelectedFilter(binding.filterTwoStar, binding.twoStarTitle, binding.twoStarValue)
                setUnSelectedFilter(binding.filterOneStar, binding.oneStarTitle, binding.oneStarValue)

                val filterList = clothesInfoVM.clothes.value!!.comments.filter { comment -> comment.star == 5 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_four_star -> {
                setSelectedFilter(binding.filterFourStar, binding.fourStarTitle, binding.fourStarValue)
                setUnSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(binding.filterFiveStar, binding.fiveStarTitle, binding.fiveStarValue)
                setUnSelectedFilter(binding.filterThreeStar, binding.threeStarTitle, binding.threeStarValue)
                setUnSelectedFilter(binding.filterTwoStar, binding.twoStarTitle, binding.twoStarValue)
                setUnSelectedFilter(binding.filterOneStar, binding.oneStarTitle, binding.oneStarValue)

                val filterList =
                    clothesInfoVM.clothes.value!!.comments.filter { comment -> comment.star == 4 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_three_star -> {
                setSelectedFilter(binding.filterThreeStar, binding.threeStarTitle, binding.threeStarValue)
                setUnSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(binding.filterFiveStar, binding.fiveStarTitle, binding.fiveStarValue)
                setUnSelectedFilter(binding.filterFourStar, binding.fourStarTitle, binding.fourStarValue)
                setUnSelectedFilter(binding.filterTwoStar, binding.twoStarTitle, binding.twoStarValue)
                setUnSelectedFilter(binding.filterOneStar, binding.oneStarTitle, binding.oneStarValue)

                val filterList =
                    clothesInfoVM.clothes.value!!.comments.filter { comment -> comment.star == 3 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_two_star -> {
                setSelectedFilter(binding.filterTwoStar, binding.twoStarTitle, binding.twoStarValue)
                setUnSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(
                    binding.filterFiveStar,
                    binding.fiveStarTitle,
                    binding.fiveStarValue
                )
                setUnSelectedFilter(
                    binding.filterFourStar,
                    binding.fourStarTitle,
                    binding.fourStarValue
                )
                setUnSelectedFilter(
                    binding.filterThreeStar,
                    binding.threeStarTitle,
                    binding.threeStarValue
                )
                setUnSelectedFilter(
                    binding.filterOneStar,
                    binding.oneStarTitle,
                    binding.oneStarValue
                )

                val filterList =
                    clothesInfoVM.clothes.value!!.comments.filter { comment -> comment.star == 2 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_one_star -> {
                setSelectedFilter(binding.filterOneStar, binding.oneStarTitle, binding.oneStarValue)
                setUnSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(binding.filterFiveStar, binding.fiveStarTitle, binding.fiveStarValue)
                setUnSelectedFilter(binding.filterFourStar, binding.fourStarTitle, binding.fourStarValue)
                setUnSelectedFilter(binding.filterThreeStar, binding.threeStarTitle, binding.threeStarValue)
                setUnSelectedFilter(binding.filterTwoStar, binding.twoStarTitle, binding.twoStarValue)

                val filterList = clothesInfoVM.clothes.value!!.comments.filter { comment -> comment.star == 1 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }
        }
    }

    private fun setSelectedFilter(viewGroup: LinearLayout?, title: TextView?, value: TextView?) {
        viewGroup?.setBackgroundResource(R.drawable.background_blue_rectangle_10dp_4_corners)
        title?.let {
            it.setTextColor(Color.WHITE)
            if (it.id == R.id.filter_all) {
                it.setBackgroundResource(R.drawable.background_blue_rectangle_10dp_4_corners)
            }
        }
        value?.setTextColor(Color.WHITE)
    }

    private fun setUnSelectedFilter(viewGroup: LinearLayout?, title: TextView?, value: TextView?) {
        viewGroup?.setBackgroundResource(R.drawable.background_white_rectangle_10dp_4_corners_1dp_blue_stroke)
        title?.let {
            it.setTextColor(Color.BLACK)
            if (it.id == R.id.filter_all) {
                it.setBackgroundResource(R.drawable.background_white_rectangle_10dp_4_corners_1dp_blue_stroke)
            }
        }
        value?.setTextColor(Color.BLACK)
    }

    private fun handleAddToCart() {
        if (selectedImg.isEmpty() || selectedSize.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin sản phẩm!", Toast.LENGTH_SHORT)
                .show()
        } else {
            AlertDialog.Builder(this, R.style.MyAlertDialog)
                .setTitle("Xác nhận thêm giỏ hàng")
                .setMessage("Bạn có chắc muốn thêm vào giỏ hàng chứ!")
                .setCancelable(false)
                .setNegativeButton("Hủy") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Đồng ý") { dialog, which ->

                    val newSellClothes = SellClothes(
                        img = selectedImg,
                        name = clothesInfoVM.clothes.value!!.name,
                        group = clothesInfoVM.clothes.value!!.group,
                        description = clothesInfoVM.clothes.value!!.description,
                        size = selectedSize,
                        price = clothesInfoVM.clothes.value!!.price,
                        quantity = clothesInfoVM.clothesQuantity.value!!,
                        color = selectedColor
                    )

                    clothesInfoVM.isExistedClothes(newSellClothes).observe(this) { isExisted ->
                        when (isExisted) {
                            true -> clothesInfoVM.updateClothesQuantity("${newSellClothes.name}, màu ${newSellClothes.color.name}, size ${newSellClothes.size}", newSellClothes.quantity)
                                .observe(this){ isUpdated ->
                                when(isUpdated){
                                    true -> DataLocal.getInstance().showToast(this, "Đã thêm giỏ hàng thành công!")
                                    false -> DataLocal.getInstance().showToast(this, "Đã thêm giỏ hàng thất bại!")
                                }
                            }

                            false -> clothesInfoVM.addClothesToCart(newSellClothes)
                                .observe(this) { isAdded ->
                                    when (isAdded) {
                                        true -> DataLocal.getInstance().showToast(this, "Đã thêm giỏ hàng thành công!")
                                        false -> DataLocal.getInstance().showToast(this, "Đã thêm giỏ hàng thất bại!")
                                    }
                                }
                        }
                    }
                }
                .show()
        }
    }

    private fun setClothesSizeSelected(viewId: Int) {
        for (i in 0..<clothesSizeIds.size) {
            val selectedSizeView = findViewById<TextView>(clothesSizeIds[i])

            if (clothesSizeIds[i] == viewId) {
                selectedSizeView?.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_clothes_size_selected,
                    null
                )
                selectedSizeView?.setTextColor(Color.WHITE)
                selectedSize = clothesInfoVM.clothes.value!!.sizes[i]
            } else {
                selectedSizeView?.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_clothes_size_unselected,
                    null
                )
                selectedSizeView?.setTextColor(Color.BLACK)
            }
        }
    }

    private fun setClothesColorSelected(viewId: Int) {
        for (i in 0..<clothesColorIds.size) {
            val selectedColorView =
                findViewById<CardView>(clothesColorIds[i])?.findViewById<ImageView>(R.id.clothes_color)

            if (clothesColorIds[i] == viewId) {
                selectedColorView?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.clothes_color_selected,
                        null
                    )
                )
                binding.clothesImgSpace.currentItem = i
                selectedColor = clothesInfoVM.clothes.value!!.colors[i]
                selectedImg = clothesInfoVM.clothes.value!!.imgs[i]
            } else {
                selectedColorView?.setImageDrawable(null)
            }
        }
    }
}