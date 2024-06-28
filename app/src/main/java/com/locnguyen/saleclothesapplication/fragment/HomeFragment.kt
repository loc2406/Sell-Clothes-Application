package com.locnguyen.saleclothesapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.activity.ClothesInfoActivity
import com.locnguyen.saleclothesapplication.adapter.ClothesAdapter
import com.locnguyen.saleclothesapplication.databinding.HomeFragmentBinding
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.viewmodel.MainVM
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var mainVM: MainVM
    private var itemClothesClicked: ((Clothes) -> Unit) = { clothes ->
        handleItemClothesClicked(clothes)
    }
    private val clothesAdapter: ClothesAdapter by lazy {
        ClothesAdapter(
            getList(),
            itemClothesClicked
        )
    }

    private fun getList(): ArrayList<Clothes> {
        return arrayListOf(
            Clothes(
                img = "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/img_1.jpg?alt=media&token=d0133343-73ba-4d42-8e65-fde9e07e5247",
                name = "Bộ Đồ Nam AVIANO Cổ Tròn Cộc Tay Phối Kẻ Viền Tay, Đồ Thể Thao Nam Form Dáng Basic",
                description = "Quần áo đôi dành cho thiếu nữ có nhiều màu và size khác nhau",
                group = "Nữ",
                color = listOf(R.color.black, R.color.white, R.color.gray),
                size = listOf("M", "L", "XL", "XXL"),
                price = 179000
            ),
            Clothes(
                img = "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/img_2.jpg?alt=media&token=5c486ba1-a894-46ca-a84e-2d31f041af66",
                name = "Quần áo nữ 2",
                group = "Nữ",
                color = listOf(R.color.yellow, R.color.green, R.color.black),
                description = "Quần áo đôi dành cho thiếu nữ có nhiều màu và size khác nhau",
                size = listOf("L", "XL", "XXL"),
                price = 120000
            ),
            Clothes(
                img = "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/img_4.jpg?alt=media&token=f05d6cc5-1820-4751-a6d2-e594cc59b068",
                name = "Quần áo nữ 3",
                group = "Nữ",
                color = listOf(R.color.red, R.color.white, R.color.purple),
                description = "Quần áo đôi dành cho thiếu nữ có nhiều màu và size khác nhau",
                size = listOf("L", "XL", "XXL"),
                price = 200000
            ),
            Clothes(
                img = "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/img_1.jpg?alt=media&token=d0133343-73ba-4d42-8e65-fde9e07e5247",
                name = "Quần áo nữ 4",
                group = "Nữ",
                color = listOf(R.color.red, R.color.sky, R.color.pink),
                description = "Quần áo đôi dành cho thiếu nữ có nhiều màu và size khác nhau",
                size = listOf("L", "XL", "XXL"),
                price = 100000
            ),
            Clothes(
                img = "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/img_2.jpg?alt=media&token=5c486ba1-a894-46ca-a84e-2d31f041af66",
                name = "Quần áo nữ 5",
                group = "Nữ",
                color = listOf(R.color.yellow, R.color.green, R.color.black),
                description = "Quần áo đôi dành cho thiếu nữ có nhiều màu và size khác nhau",
                size = listOf("L", "XL", "XXL"),
                price = 120000
            ),
            Clothes(
                img = "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/img_4.jpg?alt=media&token=f05d6cc5-1820-4751-a6d2-e594cc59b068",
                name = "Quần áo nữ 6",
                group = "Nữ",
                color = listOf(R.color.red, R.color.white, R.color.purple),
                description = "Quần áo đôi dành cho thiếu nữ có nhiều màu và size khác nhau",
                size = listOf("L", "XL", "XXL"),
                price = 200000
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        mainVM = ViewModelProvider(this)[MainVM::class.java]

        binding.lifecycleOwner = this
        binding.mainVM = mainVM

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listClothes.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = clothesAdapter
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (clothesAdapter.defaultList.isNotEmpty()) handleSearchByKeyword(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (clothesAdapter.defaultList.isNotEmpty()) handleSearchByKeyword(it)
                }
                return true
            }
        })
    }

    private fun handleSearchByKeyword(keyword: String) {
        val searchedList: List<Clothes>

        if (keyword.isNotEmpty()) {
            searchedList = listContainKeyword(keyword)
            clothesAdapter.setListClothes(searchedList as ArrayList<Clothes>)
        } else {
            clothesAdapter.setListClothes(clothesAdapter.defaultList)
        }
    }

    private fun listContainKeyword(keyword: String) = clothesAdapter.defaultList.filter { clothes ->
        clothes.name.contains(keyword) ||
                clothes.price.toString().contains(keyword) ||
                clothes.group.contains(keyword)
    }

    private fun handleItemClothesClicked(clothes: Clothes) {
        startActivity(Intent(requireContext(), ClothesInfoActivity::class.java).apply {
            putExtra("CLOTHES", clothes)
        })
    }
}
