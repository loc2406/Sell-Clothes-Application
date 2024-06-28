package com.locnguyen.saleclothesapplication.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.databinding.EditInfoActivityBinding
import com.locnguyen.saleclothesapplication.repo.UserRepo

class EditInfoActivity: AppCompatActivity() {

    private lateinit var binding:EditInfoActivityBinding
    private val userRepo = UserRepo()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.edit_info_activity)
        binding.lifecycleOwner = this

        binding.cancel.setOnClickListener {
            finish()
        }

        binding.confirm.setOnClickListener {
            val map = mapOf(Pair("name", binding.name.text.toString()), Pair("phone", Integer.parseInt(binding.phone.text.toString()).toLong()), Pair("location", binding.address.text.toString()))

            userRepo.update(map).observe(this){ isUpdated ->
                when(isUpdated){
                    true -> Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    false -> Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}