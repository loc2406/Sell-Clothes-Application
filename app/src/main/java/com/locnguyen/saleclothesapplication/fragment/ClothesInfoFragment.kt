package com.locnguyen.saleclothesapplication.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.adapter.AutoLoadImgAdapter
import com.locnguyen.saleclothesapplication.adapter.CommentAdapter
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.ClothesInfoFragmentBinding
import com.locnguyen.saleclothesapplication.fragment.ClothesInfoFragmentArgs
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.model.ClothesColor
import com.locnguyen.saleclothesapplication.model.Comment
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.viewmodel.ClothesInfoVM
import com.locnguyen.saleclothesapplication.viewmodel.MainVM
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executor

class ClothesInfoFragment : Fragment() {
    private val clothesColorIds: ArrayList<Int> by lazy { ArrayList() }
    private val clothesSizeIds: ArrayList<Int> by lazy { ArrayList() }
    private val autoLoadImgTimer: Timer by lazy { Timer() }
    private lateinit var autoLoadImgExecutor: Executor
    private var selectedColor: ClothesColor = ClothesColor()
    private var selectedImg: String = ""
    private var selectedSize: String = ""
    private val localData: DataLocal by lazy { DataLocal.getInstance() }

    private val arguments: ClothesInfoFragmentArgs by navArgs()
    private var clothes: Clothes? = null
    private lateinit var autoLoadImgAdapter: AutoLoadImgAdapter
    private lateinit var binding: ClothesInfoFragmentBinding
    private lateinit var clothesInfoVM: ClothesInfoVM
    private lateinit var mainVM: MainVM
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ClothesInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoLoadImgExecutor = ContextCompat.getMainExecutor(requireContext())
        clothesInfoVM = ViewModelProvider(this)[ClothesInfoVM::class.java]
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        navController = Navigation.findNavController(view)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.clothesInfoVM = clothesInfoVM

        clothes = arguments.clothesInfo

        clothes?.let {
            bindClothesOnView(it)
        } ?: let {
            localData.showToast(requireContext(), "Không thể xem thông tin quần áo!")
        }

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
            clothesPrice.text = getString(R.string.Price_regex, numberFormat.format(clothes.price))
        }

        bindComment(clothes.comments)
    }

    private fun bindListImg(list: List<String>) {
        binding.apply {
            autoLoadImgAdapter = AutoLoadImgAdapter(list)
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
                        val totalImgs = list.size - 1

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

    private fun bindListColor(list: List<ClothesColor>) {
        val density = DataLocal.getInstance().densityValue
        val itemColorParam =
            LinearLayout.LayoutParams((30 * density).toInt(), (30 * density).toInt()).apply {
                setMargins((10 * density).toInt(), 0, 0, 0)
            }

        binding.apply {
            list.forEach { color ->
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
                            this@ClothesInfoFragment.clothesInfoVM.selectColor(randomId)
                        }
                    }
                }
                clothesColorSpace.addView(itemColor)
            }
        }
    }

    private fun bindListSize(list: List<String>) {
        val density = DataLocal.getInstance().densityValue
        val itemSizeParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins((10 * density).toInt(), 0, 0, 0)
        }

        binding.apply {
            list.forEach { size ->
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
                        this@ClothesInfoFragment.clothesInfoVM.selectSize(randomId)
                    }
                }
                clothesSizeSpace.addView(itemSize)
            }
        }
    }

    private fun bindComment(list: List<Comment>) {
        binding.clothesCommentTitle.text = Html.fromHtml(requireContext().getString(R.string.Total_comments_reqex, list.size.toString()), Html.FROM_HTML_MODE_LEGACY)

        binding.fiveStarValue.text = getString(
            R.string.Comment_value_regex,
            list.filter { comment -> comment.star == 5 }.size
        )
        binding.fourStarValue.text = getString(
            R.string.Comment_value_regex,
            list.filter { comment -> comment.star == 4 }.size
        )
        binding.threeStarValue.text = getString(
            R.string.Comment_value_regex,
            list.filter { comment -> comment.star == 3 }.size
        )
        binding.twoStarValue.text = getString(
            R.string.Comment_value_regex,
            list.filter { comment -> comment.star == 2 }.size
        )
        binding.oneStarValue.text = getString(
            R.string.Comment_value_regex,
            list.filter { comment -> comment.star == 1 }.size
        )

        commentAdapter = CommentAdapter(list)

        binding.listComment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = commentAdapter
        }
    }

    private fun initObserves() {
        clothesInfoVM.back.observe(viewLifecycleOwner) { isClicked ->
            if (isClicked) {
                navController.popBackStack()
            }
        }

        clothesInfoVM.selectedColorId.observe(viewLifecycleOwner) { viewId ->
            setClothesColorSelected(viewId)
        }

        clothesInfoVM.selectedSizeId.observe(viewLifecycleOwner) { viewId ->
            setClothesSizeSelected(viewId)
        }

        clothesInfoVM.selectedFilterId.observe(viewLifecycleOwner) { filterId ->
            setCommentFilter(filterId)
        }

        clothesInfoVM.addCart.observe(viewLifecycleOwner) { isAdded ->
            if (isAdded) {
                handleAddToCart()
            }
        }
    }

    private fun setClothesColorSelected(viewId: Int) {
        for (i in 0..<clothesColorIds.size) {
            val selectedColorView = view?.findViewById<CardView>(clothesColorIds[i])
                ?.findViewById<ImageView>(R.id.clothes_color)

            if (clothesColorIds[i] == viewId) {
                selectedColorView?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.clothes_color_selected,
                        null
                    )
                )
                binding.clothesImgSpace.currentItem = i
                selectedColor =clothes!!.colors[i]
                selectedImg =clothes!!.imgs[i]
            } else {
                selectedColorView?.setImageDrawable(null)
            }
        }
    }

    private fun setClothesSizeSelected(viewId: Int) {
        for (i in 0..<clothesSizeIds.size) {
            val selectedSizeView = view?.findViewById<TextView>(clothesSizeIds[i])

            if (clothesSizeIds[i] == viewId) {
                selectedSizeView?.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_clothes_size_selected,
                    null
                )
                selectedSizeView?.setTextColor(Color.WHITE)
                selectedSize =clothes!!.sizes[i]
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

    private fun setCommentFilter(filterId: Int) {
        when (filterId) {
            R.id.filter_all -> {
                setSelectedFilter(null, binding.filterAll, null)
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
                    binding.filterTwoStar,
                    binding.twoStarTitle,
                    binding.twoStarValue
                )
                setUnSelectedFilter(
                    binding.filterOneStar,
                    binding.oneStarTitle,
                    binding.oneStarValue
                )

                val filterList =clothes!!.comments
                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    commentAdapter.setFilterList(filterList)
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_five_star -> {
                setSelectedFilter(
                    binding.filterFiveStar,
                    binding.fiveStarTitle,
                    binding.fiveStarValue
                )
                setUnSelectedFilter(null, binding.filterAll, null)
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
                    binding.filterTwoStar,
                    binding.twoStarTitle,
                    binding.twoStarValue
                )
                setUnSelectedFilter(
                    binding.filterOneStar,
                    binding.oneStarTitle,
                    binding.oneStarValue
                )

                val filterList =
                   clothes!!.comments.filter { comment -> comment.star == 5 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_four_star -> {
                setSelectedFilter(
                    binding.filterFourStar,
                    binding.fourStarTitle,
                    binding.fourStarValue
                )
                setUnSelectedFilter(null, binding.filterAll, null)
                setUnSelectedFilter(
                    binding.filterFiveStar,
                    binding.fiveStarTitle,
                    binding.fiveStarValue
                )
                setUnSelectedFilter(
                    binding.filterThreeStar,
                    binding.threeStarTitle,
                    binding.threeStarValue
                )
                setUnSelectedFilter(
                    binding.filterTwoStar,
                    binding.twoStarTitle,
                    binding.twoStarValue
                )
                setUnSelectedFilter(
                    binding.filterOneStar,
                    binding.oneStarTitle,
                    binding.oneStarValue
                )

                val filterList =
                   clothes!!.comments.filter { comment -> comment.star == 4 }
                commentAdapter.setFilterList(filterList)

                if (filterList.isEmpty()) {
                    clothesInfoVM.setHaveCommentsValue(false)
                } else {
                    clothesInfoVM.setHaveCommentsValue(true)
                }
            }

            R.id.filter_three_star -> {
                setSelectedFilter(
                    binding.filterThreeStar,
                    binding.threeStarTitle,
                    binding.threeStarValue
                )
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
                    binding.filterTwoStar,
                    binding.twoStarTitle,
                    binding.twoStarValue
                )
                setUnSelectedFilter(
                    binding.filterOneStar,
                    binding.oneStarTitle,
                    binding.oneStarValue
                )

                val filterList =
                   clothes!!.comments.filter { comment -> comment.star == 3 }
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
                   clothes!!.comments.filter { comment -> comment.star == 2 }
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
                    binding.filterTwoStar,
                    binding.twoStarTitle,
                    binding.twoStarValue
                )

                val filterList =
                   clothes!!.comments.filter { comment -> comment.star == 1 }
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
            localData.showToast(requireContext(), "Vui lòng chọn đầy đủ thông tin sản phẩm!")
        } else {
            localData.getAlertDialog(
                requireContext(),
                "Xác nhận thêm giỏ hàng",
                "Bạn có chắc muốn thêm vào giỏ hàng chứ!",
                "Đồng ý",
                "Hủy",
                {
                    val newSellClothes = SellClothes(
                        img = selectedImg,
                        name =clothes!!.name,
                        group =clothes!!.group,
                        description =clothes!!.description,
                        size = selectedSize,
                        price =clothes!!.price,
                        quantity = clothesInfoVM.clothesQuantity.value!!,
                        color = selectedColor
                    )

                    clothesInfoVM.isExistedClothes(newSellClothes)
                        .observe(viewLifecycleOwner) { isExisted ->
                            when (isExisted) {
                                true -> clothesInfoVM.updateClothesQuantity(
                                    "${newSellClothes.name}, màu ${newSellClothes.color.name}, size ${newSellClothes.size}",
                                    newSellClothes.quantity
                                )
                                    .observe(viewLifecycleOwner) { isUpdated ->
                                        when (isUpdated) {
                                            true -> DataLocal.getInstance().showToast(
                                                requireContext(),
                                                "Đã thêm giỏ hàng thành công!"
                                            )

                                            false -> DataLocal.getInstance().showToast(
                                                requireContext(),
                                                "Đã thêm giỏ hàng thất bại!"
                                            )
                                        }
                                    }

                                false -> clothesInfoVM.addClothesToCart(newSellClothes)
                                    .observe(viewLifecycleOwner) { isAdded ->
                                        when (isAdded) {
                                            true -> DataLocal.getInstance().showToast(
                                                requireContext(),
                                                "Đã thêm giỏ hàng thành công!"
                                            )

                                            false -> DataLocal.getInstance().showToast(
                                                requireContext(),
                                                "Đã thêm giỏ hàng thất bại!"
                                            )
                                        }
                                    }
                            }
                        }
                }).show()
        }
    }
}