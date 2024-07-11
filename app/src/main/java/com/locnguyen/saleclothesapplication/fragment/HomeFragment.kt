package com.locnguyen.saleclothesapplication.fragment

import android.content.Intent
import android.graphics.Color
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
import com.locnguyen.saleclothesapplication.model.ClothesColor
import com.locnguyen.saleclothesapplication.model.Comment
import com.locnguyen.saleclothesapplication.viewmodel.MainVM
import java.time.LocalDateTime
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
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Nam%20AVIANO%20C%E1%BB%95%20Tr%C3%B2n%20C%E1%BB%99c%20Tay%20Ph%E1%BB%91i%20K%E1%BA%BB%20Vi%E1%BB%81n%20Tay%2C%20%C4%90%E1%BB%93%20Th%E1%BB%83%20Thao%20Nam%20Form%20D%C3%A1ng%20Basic%2C%20Tr%E1%BA%AFng.jpg?alt=media&token=f2ea9ac3-ad6d-4849-95b3-09e9d1473991",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Nam%20AVIANO%20C%E1%BB%95%20Tr%C3%B2n%20C%E1%BB%99c%20Tay%20Ph%E1%BB%91i%20K%E1%BA%BB%20Vi%E1%BB%81n%20Tay%2C%20%C4%90%E1%BB%93%20Th%E1%BB%83%20Thao%20Nam%20Form%20D%C3%A1ng%20Basic%2C%20%C4%90en.jpg?alt=media&token=d3c27263-2ccd-4345-b856-f620d941078a",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Nam%20AVIANO%20C%E1%BB%95%20Tr%C3%B2n%20C%E1%BB%99c%20Tay%20Ph%E1%BB%91i%20K%E1%BA%BB%20Vi%E1%BB%81n%20Tay%2C%20%C4%90%E1%BB%93%20Th%E1%BB%83%20Thao%20Nam%20Form%20D%C3%A1ng%20Basic%2C%20X%C3%A1m.jpg?alt=media&token=a5aab65b-16a0-4c1a-a314-919f7dedcda6"
                ),
                name = "Bộ Đồ Nam AVIANO Cổ Tròn Cộc Tay Phối Kẻ Viền Tay, Đồ Thể Thao Nam Form Dáng Basic",
                description =
                        "Màu sắc: Trắng - Đen - Xám\n\n" +
                        "Thương hiệu: AVIANO\n\n" +
                        "Xuất xứ: Việt Nam\n\n" +
                        "Kích thước: M-L-XL-XXL\n\n" +
                        "Mô tả chi tiết sản phẩm:\n" +
                                "\t- Bộ đồ nam được làm từ chất liệu vải Poly Coolmax sợi Poly kết hợp sợi Spandex giúp bạn thoải mái trong mọi hoạt động\n"+
                                "\t- Kết hợp bộ đồ nam được đa dạng phong cách có thể mặc nhà, dạo phố hay chơi thể thao\n" +
                                "\t- Màu sắc đa dạng, basic phù hợp với nhiều lứa tuổi\n" +
                                "\t- Đường may tinh tế, tỉ mỉ trong từng chi tiết.\n",
                group = "Nam",
                colors = listOf(ClothesColor("Trắng", "#ffffff"), ClothesColor("Đen", "#000000"), ClothesColor("Xám", "#aeafaf")),
                sizes = listOf("M", "L", "XL", "XXL"),
                price = 179000,
                sell = 120,
                comments = listOf(
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 1", 5, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 2", 2, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 3", 3, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 4", 1, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 5", 5, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 6", 2, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 7", 3, 234, LocalDateTime.now().toString()),
                    Comment("Nguyễn Thành Lộc", "Đây là bình luận 8", 1, 234, LocalDateTime.now().toString())
                )
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/%C4%90%E1%BB%92%20B%E1%BB%98%20NAM%20TH%E1%BB%82%20THAO%20THUN%20COTTON-NLSAO%2C%20Tr%E1%BA%AFng.jpg?alt=media&token=57e97654-02ca-4cf7-913e-560236d438e1",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/%C4%90%E1%BB%92%20B%E1%BB%98%20NAM%20TH%E1%BB%82%20THAO%20THUN%20COTTON-NLSAO%2C%20Xanh%20d%C6%B0%C6%A1ng.jpg?alt=media&token=35dfbddf-4cd4-4f1c-8a54-9bb0ff965d05",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/%C4%90%E1%BB%92%20B%E1%BB%98%20NAM%20TH%E1%BB%82%20THAO%20THUN%20COTTON-NLSAO%2C%20%C4%90en.jpg?alt=media&token=64694dd2-af84-4e7b-b6c1-73e2414ecc16"
                ),
                name = "ĐỒ BỘ NAM THỂ THAO THUN COTTON-NLSAO",
                description =
                "Chất liệu: Bộ quần áo nam vải 100% từ thiên nhiên , chất vải HOTTREND 2022, lên dáng chuẩn form ,chất xốp nhẹ , không bám dính mồ hôi nên người dùng sẽ cảm thấy rất thoải mái !!! \n" +
                        "Size Bộ thể thao nam : M (45-54kg), L (55-61kg), XL (62-69kg),2XL (70-76kg)\n" +
                        "Đồ Bộ Nam là phụ kiện thời trang đơn giản nhưng không thể thiếu cho mùa hè. Các anh có thể mặc đồ bộ nam ờ nhà, hay dùng làm đồ thể thao, tập gym rất mát mẻ và thoải mái\n" +
                        "Mẹo Nhỏ Giúp Bạn Bảo Quản Quần áo nam : \n" +
                        "\t\t- Đối với sản phẩm quần áo mới mua về, nên giặt tay lần đâu tiên để tránh phai màu sang quần áo khác\n" +
                        "\t\t-  Khi giặt nên lộn mặt trái ra để đảm bảo độ bền \n" +
                        "\t\t-  Sản phẩm phù hợp cho giặt máy/giặt tay\n" +
                        "\t\t- Không giặt chung đồ Trắng và đồ Tối màu\n" +
                        "SHOP CAM KẾT:\n" +
                        "\t\t- Sản phẩm 100% giống mô tả. Kiểu dáng hoàn toàn giống ảnh mẫu\n" +
                        "\t\t- Áo được kiểm tra kĩ càng, cẩn thận và tư vấn nhiệt tình trước khi gói hàng giao cho Quý Khách\n" +
                        "\t\t- Hàng có sẵn, giao hàng ngay khi nhận được đơn \n" +
                        "\t\t- Hoàn tiền nếu sản phẩm không giống với mô tả\n" +
                        "\t\t- Chấp nhận đổi hàng khi sizes không vừa\n" +
                        "\t\t- Giao hàng trên toàn quốc, nhận hàng trả tiền",
                group = "Nam",
                colors = listOf(ClothesColor("Trắng", "#ffffff"), ClothesColor("Xanh dương", "#1b67ff"), ClothesColor("Đen", "#000000")),
                sizes = listOf("M", "L", "XL", "XXL"),
                price = 59000,
                sell = 23800
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20Pijama%20M%E1%BA%B7c%20Nh%C3%A0%20Qu%E1%BA%A7n%20C%E1%BB%99c%20%C3%81o%20C%E1%BB%99c%20Ch%E1%BA%A5t%20Li%E1%BB%87u%20L%E1%BB%A5a%20Satin%20Cao%20C%E1%BA%A5p%20Th%C6%B0%C6%A1ng%20Hi%E1%BB%87u%20XAVIA%20P1%2C%20%C4%90%E1%BB%8F.jpg?alt=media&token=1a10c588-38fa-4f3d-b289-8a3491edcca6",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20Pijama%20M%E1%BA%B7c%20Nh%C3%A0%20Qu%E1%BA%A7n%20C%E1%BB%99c%20%C3%81o%20C%E1%BB%99c%20Ch%E1%BA%A5t%20Li%E1%BB%87u%20L%E1%BB%A5a%20Satin%20Cao%20C%E1%BA%A5p%20Th%C6%B0%C6%A1ng%20Hi%E1%BB%87u%20XAVIA%20P1%2C%20Xanh%20ng%E1%BB%8Dc.jpg?alt=media&token=c9fc8d6a-d794-45ca-874f-05c6f13cdf20",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20Pijama%20M%E1%BA%B7c%20Nh%C3%A0%20Qu%E1%BA%A7n%20C%E1%BB%99c%20%C3%81o%20C%E1%BB%99c%20Ch%E1%BA%A5t%20Li%E1%BB%87u%20L%E1%BB%A5a%20Satin%20Cao%20C%E1%BA%A5p%20Th%C6%B0%C6%A1ng%20Hi%E1%BB%87u%20XAVIA%20P1%2C%20V%C3%A0ng.jpg?alt=media&token=60bcea6c-d7bb-40b3-bd17-0b46f7ab0bbd"
                ),
                name = "Bộ Pijama Mặc Nhà Quần Cộc Áo Cộc Chất Liệu Lụa Satin Cao Cấp Thương Hiệu XAVIA P1",
                description =
                "Mã SP: P1\n" +
                        "Thông số sizes tham khảo:\n" +
                        "Size S: 37-45kg\n" +
                        "Size M: 45-53kg\n" +
                        "Size L: 53-58kg\n" +
                        "Size XL: 58-65kg\n" +
                        "=========================\n" +
                        "Bộ pijama mặc nhà quần cộc ao cộc làm bằng chất liệu lụa satin cao cấp mang lại cho ngược mặc cảm giác mát mẻ không thấy cảm giác ngứa ngáy khó chịu và đặc biệt chất liệu không nhăn, không phai màu như chất liệu khác trên thị trường \n" +
                        "Sản phẩm có những màu:\n" +
                        "\t\t- Đỏ\n" +
                        "\t\t- Xanh Ngọc\n" +
                        "\t\t- Vàng\n" +
                        "Shop Xin Cam Kết: \n" +
                        "\t\t- Ảnh sản phẩm do shop tự chụp, đảm bảo sản phẩm giống hình, chất lượng tốt.\n" +
                        "\t\t- Hình ảnh sản phẩm giống 100%.\n" +
                        "\t\t- Chất lượng sản phẩm tốt 100%.\n" +
                        "\t\t- Sản phẩm được kiểm tra kĩ càng, nghiêm ngặt trước khi giao hàng.\n" +
                        "\t\t- Sản phẩm luôn có sẵn trong kho hàng. \n" +
                        "\t\t- Giao hàng ngay khi nhận được đơn hàng.\n" +
                        "\t\t- Hoàn tiền ngay nếu sản phẩm không giống với mô tả.\n" +
                        "\t\t- Giao hàng toàn quốc, nhận hàng thanh toán. \n" +
                        "\t\t- Hỗ trợ đổi trả theo quy định.\n" +
                        "\t\t- Gửi hàng siêu tốc\n" +
                        "\t\t- Đội ngũ luôn tư vấn tận tâm, chi tiết, hỗ trợ nhiệt tình nhất với khách hàng.\n" +
                        "\t\t- Khách yêu nhận được sản phẩm, vui lòng đánh giá 5* giúp Shop để nhận QUÀ bí mật từ GU nhé!",
                group = "Nữ",
                colors = listOf(ClothesColor("Đỏ", "#ff0000"), ClothesColor("Xanh ngọc", "#48bcaa"), ClothesColor("Vàng", "#ebff00")),
                sizes = listOf("S", "M", "L", "XL"),
                price = 159000,
                sell = 2400
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Ng%E1%BB%A7%20%C3%81o%20Tay%20Ng%E1%BA%AFn%20Ph%E1%BB%91i%20Qu%E1%BA%A7n%20Short%20In%20Ho%E1%BA%A1t%20H%C3%ACnh%20Th%E1%BB%9Di%20Trang%20M%C3%B9a%20H%C3%A8%20Cho%20H%E1%BB%8Dc%20Sinh%2C%20H%E1%BB%93ng.jpg?alt=media&token=fba49b95-d6de-4f1d-bfea-7b4c846c6014",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Ng%E1%BB%A7%20%C3%81o%20Tay%20Ng%E1%BA%AFn%20Ph%E1%BB%91i%20Qu%E1%BA%A7n%20Short%20In%20Ho%E1%BA%A1t%20H%C3%ACnh%20Th%E1%BB%9Di%20Trang%20M%C3%B9a%20H%C3%A8%20Cho%20H%E1%BB%8Dc%20Sinh%2C%20V%C3%A0ng.jpg?alt=media&token=06a1c4e2-ea6b-4ba0-9be0-ac8aab0b3428",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Ng%E1%BB%A7%20%C3%81o%20Tay%20Ng%E1%BA%AFn%20Ph%E1%BB%91i%20Qu%E1%BA%A7n%20Short%20In%20Ho%E1%BA%A1t%20H%C3%ACnh%20Th%E1%BB%9Di%20Trang%20M%C3%B9a%20H%C3%A8%20Cho%20H%E1%BB%8Dc%20Sinh%2C%20X%C3%A1m.jpg?alt=media&token=d1af7348-da7c-4546-a221-be42857847bf"
                ),
                name = "Bộ Đồ Ngủ Áo Tay Ngắn Phối Quần Short In Hoạt Hình Thời Trang Mùa Hè Cho Học Sinh",
                description =
                "M: chiều cao 155-163cm, trọng lượng 40-50kg, L: chiều cao 160-168cm, trọng lượng 50-60kg,XL: cao 165-173cm, nặng 60-70kg,XXL: cao 170-178cm, nặng 70-80kg\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Chào mừng đến với cửa hàng của chúng tôi!Nếu Bạn Quan tâm Đến Cửa Hàng Của Chúng Tôi, Bạn Có Thể Theo Dõi Chúng Tôi Và Nhận Phiếu Giảm Giá.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Sau khi nhận được phiếu giảm giá, giá ưu đãi hơn và có nhiều chiết khấu toàn diện hơn cho bạn lựa chọn.\n" +
                        "\n" +
                        "Dễ dàng phù hợp với quần áo, thích hợp cho công việc và mua sắm\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Hỗ trợ thanh toán khi giao hàng（COD-Cash on Deliver)\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Dịch vụ nhanh nhất\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Luôn đặt danh tiếng đầu tiên\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Lời hứa Hình ảnh sản phẩm thực tế\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Do màn hình và điều kiện ánh sáng khác nhau, màu sắc thực tế của sản phẩm có thể khác 3-5%.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Nếu Bạn Kiểm Tra Bất Kỳ Vấn Đề nào Khi Nhận Được Gói Hàng, Vui Lòng Liên Hệ Với Chúng Tôi Để Được Trợ Giúp, Chúng Tôi Sẽ Trả Lời Bạn Với Sự Hài Lòng.",
                group = "Nữ",
                colors = listOf(ClothesColor("Hồng", "#fa00ff"), ClothesColor("Vàng", "#ebff00"), ClothesColor("Xám", "#aeafaf")),
                sizes = listOf("M", "L", "XL", "XXL"),
                price = 137000,
                sell = 648
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Nam%20AVIANO%20C%E1%BB%95%20Tr%C3%B2n%20C%E1%BB%99c%20Tay%20Ph%E1%BB%91i%20K%E1%BA%BB%20Vi%E1%BB%81n%20Tay%2C%20%C4%90%E1%BB%93%20Th%E1%BB%83%20Thao%20Nam%20Form%20D%C3%A1ng%20Basic%2C%20Tr%E1%BA%AFng.jpg?alt=media&token=f2ea9ac3-ad6d-4849-95b3-09e9d1473991",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Nam%20AVIANO%20C%E1%BB%95%20Tr%C3%B2n%20C%E1%BB%99c%20Tay%20Ph%E1%BB%91i%20K%E1%BA%BB%20Vi%E1%BB%81n%20Tay%2C%20%C4%90%E1%BB%93%20Th%E1%BB%83%20Thao%20Nam%20Form%20D%C3%A1ng%20Basic%2C%20%C4%90en.jpg?alt=media&token=d3c27263-2ccd-4345-b856-f620d941078a",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Nam%20AVIANO%20C%E1%BB%95%20Tr%C3%B2n%20C%E1%BB%99c%20Tay%20Ph%E1%BB%91i%20K%E1%BA%BB%20Vi%E1%BB%81n%20Tay%2C%20%C4%90%E1%BB%93%20Th%E1%BB%83%20Thao%20Nam%20Form%20D%C3%A1ng%20Basic%2C%20X%C3%A1m.jpg?alt=media&token=a5aab65b-16a0-4c1a-a314-919f7dedcda6"
                ),
                name = "Bộ Đồ Nam AVIANO Cổ Tròn Cộc Tay Phối Kẻ Viền Tay, Đồ Thể Thao Nam Form Dáng Basic",
                description =
                "Màu sắc: Trắng - Đen - Xám\n\n" +
                        "Thương hiệu: AVIANO\n\n" +
                        "Xuất xứ: Việt Nam\n\n" +
                        "Kích thước: M-L-XL-XXL\n\n" +
                        "Mô tả chi tiết sản phẩm:\n" +
                        "\t- Bộ đồ nam được làm từ chất liệu vải Poly Coolmax sợi Poly kết hợp sợi Spandex giúp bạn thoải mái trong mọi hoạt động\n"+
                        "\t- Kết hợp bộ đồ nam được đa dạng phong cách có thể mặc nhà, dạo phố hay chơi thể thao\n" +
                        "\t- Màu sắc đa dạng, basic phù hợp với nhiều lứa tuổi\n" +
                        "\t- Đường may tinh tế, tỉ mỉ trong từng chi tiết.\n",
                group = "Nam",
                colors = listOf(ClothesColor("Trắng", "#ffffff"), ClothesColor("Đen", "#000000"), ClothesColor("Xám", "#aeafaf")),
                sizes = listOf("M", "L", "XL", "XXL"),
                price = 179000,
                sell = 120
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/%C4%90%E1%BB%92%20B%E1%BB%98%20NAM%20TH%E1%BB%82%20THAO%20THUN%20COTTON-NLSAO%2C%20Tr%E1%BA%AFng.jpg?alt=media&token=57e97654-02ca-4cf7-913e-560236d438e1",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/%C4%90%E1%BB%92%20B%E1%BB%98%20NAM%20TH%E1%BB%82%20THAO%20THUN%20COTTON-NLSAO%2C%20Xanh%20d%C6%B0%C6%A1ng.jpg?alt=media&token=35dfbddf-4cd4-4f1c-8a54-9bb0ff965d05",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/%C4%90%E1%BB%92%20B%E1%BB%98%20NAM%20TH%E1%BB%82%20THAO%20THUN%20COTTON-NLSAO%2C%20%C4%90en.jpg?alt=media&token=64694dd2-af84-4e7b-b6c1-73e2414ecc16"
                ),
                name = "ĐỒ BỘ NAM THỂ THAO THUN COTTON-NLSAO",
                description =
                "Chất liệu: Bộ quần áo nam vải 100% từ thiên nhiên , chất vải HOTTREND 2022, lên dáng chuẩn form ,chất xốp nhẹ , không bám dính mồ hôi nên người dùng sẽ cảm thấy rất thoải mái !!! \n" +
                        "Size Bộ thể thao nam : M (45-54kg), L (55-61kg), XL (62-69kg),2XL (70-76kg)\n" +
                        "Đồ Bộ Nam là phụ kiện thời trang đơn giản nhưng không thể thiếu cho mùa hè. Các anh có thể mặc đồ bộ nam ờ nhà, hay dùng làm đồ thể thao, tập gym rất mát mẻ và thoải mái\n" +
                        "Mẹo Nhỏ Giúp Bạn Bảo Quản Quần áo nam : \n" +
                        "\t\t- Đối với sản phẩm quần áo mới mua về, nên giặt tay lần đâu tiên để tránh phai màu sang quần áo khác\n" +
                        "\t\t-  Khi giặt nên lộn mặt trái ra để đảm bảo độ bền \n" +
                        "\t\t-  Sản phẩm phù hợp cho giặt máy/giặt tay\n" +
                        "\t\t- Không giặt chung đồ Trắng và đồ Tối màu\n" +
                        "SHOP CAM KẾT:\n" +
                        "\t\t- Sản phẩm 100% giống mô tả. Kiểu dáng hoàn toàn giống ảnh mẫu\n" +
                        "\t\t- Áo được kiểm tra kĩ càng, cẩn thận và tư vấn nhiệt tình trước khi gói hàng giao cho Quý Khách\n" +
                        "\t\t- Hàng có sẵn, giao hàng ngay khi nhận được đơn \n" +
                        "\t\t- Hoàn tiền nếu sản phẩm không giống với mô tả\n" +
                        "\t\t- Chấp nhận đổi hàng khi sizes không vừa\n" +
                        "\t\t- Giao hàng trên toàn quốc, nhận hàng trả tiền",
                group = "Nam",
                colors = listOf(ClothesColor("Trắng", "#ffffff"), ClothesColor("Xanh dương", "#1b67ff"), ClothesColor("Đen", "#000000")),
                sizes = listOf("M", "L", "XL", "XXL"),
                price = 59000,
                sell = 23800
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20Pijama%20M%E1%BA%B7c%20Nh%C3%A0%20Qu%E1%BA%A7n%20C%E1%BB%99c%20%C3%81o%20C%E1%BB%99c%20Ch%E1%BA%A5t%20Li%E1%BB%87u%20L%E1%BB%A5a%20Satin%20Cao%20C%E1%BA%A5p%20Th%C6%B0%C6%A1ng%20Hi%E1%BB%87u%20XAVIA%20P1%2C%20%C4%90%E1%BB%8F.jpg?alt=media&token=1a10c588-38fa-4f3d-b289-8a3491edcca6",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20Pijama%20M%E1%BA%B7c%20Nh%C3%A0%20Qu%E1%BA%A7n%20C%E1%BB%99c%20%C3%81o%20C%E1%BB%99c%20Ch%E1%BA%A5t%20Li%E1%BB%87u%20L%E1%BB%A5a%20Satin%20Cao%20C%E1%BA%A5p%20Th%C6%B0%C6%A1ng%20Hi%E1%BB%87u%20XAVIA%20P1%2C%20Xanh%20ng%E1%BB%8Dc.jpg?alt=media&token=c9fc8d6a-d794-45ca-874f-05c6f13cdf20",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20Pijama%20M%E1%BA%B7c%20Nh%C3%A0%20Qu%E1%BA%A7n%20C%E1%BB%99c%20%C3%81o%20C%E1%BB%99c%20Ch%E1%BA%A5t%20Li%E1%BB%87u%20L%E1%BB%A5a%20Satin%20Cao%20C%E1%BA%A5p%20Th%C6%B0%C6%A1ng%20Hi%E1%BB%87u%20XAVIA%20P1%2C%20V%C3%A0ng.jpg?alt=media&token=60bcea6c-d7bb-40b3-bd17-0b46f7ab0bbd"
                ),
                name = "Bộ Pijama Mặc Nhà Quần Cộc Áo Cộc Chất Liệu Lụa Satin Cao Cấp Thương Hiệu XAVIA P1",
                description =
                "Mã SP: P1\n" +
                        "Thông số sizes tham khảo:\n" +
                        "Size S: 37-45kg\n" +
                        "Size M: 45-53kg\n" +
                        "Size L: 53-58kg\n" +
                        "Size XL: 58-65kg\n" +
                        "=========================\n" +
                        "Bộ pijama mặc nhà quần cộc ao cộc làm bằng chất liệu lụa satin cao cấp mang lại cho ngược mặc cảm giác mát mẻ không thấy cảm giác ngứa ngáy khó chịu và đặc biệt chất liệu không nhăn, không phai màu như chất liệu khác trên thị trường \n" +
                        "Sản phẩm có những màu:\n" +
                        "\t\t- Đỏ\n" +
                        "\t\t- Xanh Ngọc\n" +
                        "\t\t- Vàng\n" +
                        "Shop Xin Cam Kết: \n" +
                        "\t\t- Ảnh sản phẩm do shop tự chụp, đảm bảo sản phẩm giống hình, chất lượng tốt.\n" +
                        "\t\t- Hình ảnh sản phẩm giống 100%.\n" +
                        "\t\t- Chất lượng sản phẩm tốt 100%.\n" +
                        "\t\t- Sản phẩm được kiểm tra kĩ càng, nghiêm ngặt trước khi giao hàng.\n" +
                        "\t\t- Sản phẩm luôn có sẵn trong kho hàng. \n" +
                        "\t\t- Giao hàng ngay khi nhận được đơn hàng.\n" +
                        "\t\t- Hoàn tiền ngay nếu sản phẩm không giống với mô tả.\n" +
                        "\t\t- Giao hàng toàn quốc, nhận hàng thanh toán. \n" +
                        "\t\t- Hỗ trợ đổi trả theo quy định.\n" +
                        "\t\t- Gửi hàng siêu tốc\n" +
                        "\t\t- Đội ngũ luôn tư vấn tận tâm, chi tiết, hỗ trợ nhiệt tình nhất với khách hàng.\n" +
                        "\t\t- Khách yêu nhận được sản phẩm, vui lòng đánh giá 5* giúp Shop để nhận QUÀ bí mật từ GU nhé!",
                group = "Nữ",
                colors = listOf(ClothesColor("Đỏ", "#ff0000"), ClothesColor("Xanh ngọc", "#48bcaa"), ClothesColor("Vàng", "#ebff00")),
                sizes = listOf("S", "M", "L", "XL"),
                price = 159000,
                sell = 2400
            ),
            Clothes(
                imgs = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Ng%E1%BB%A7%20%C3%81o%20Tay%20Ng%E1%BA%AFn%20Ph%E1%BB%91i%20Qu%E1%BA%A7n%20Short%20In%20Ho%E1%BA%A1t%20H%C3%ACnh%20Th%E1%BB%9Di%20Trang%20M%C3%B9a%20H%C3%A8%20Cho%20H%E1%BB%8Dc%20Sinh%2C%20H%E1%BB%93ng.jpg?alt=media&token=fba49b95-d6de-4f1d-bfea-7b4c846c6014",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Ng%E1%BB%A7%20%C3%81o%20Tay%20Ng%E1%BA%AFn%20Ph%E1%BB%91i%20Qu%E1%BA%A7n%20Short%20In%20Ho%E1%BA%A1t%20H%C3%ACnh%20Th%E1%BB%9Di%20Trang%20M%C3%B9a%20H%C3%A8%20Cho%20H%E1%BB%8Dc%20Sinh%2C%20V%C3%A0ng.jpg?alt=media&token=06a1c4e2-ea6b-4ba0-9be0-ac8aab0b3428",
                    "https://firebasestorage.googleapis.com/v0/b/sellclothes-c92e4.appspot.com/o/B%E1%BB%99%20%C4%90%E1%BB%93%20Ng%E1%BB%A7%20%C3%81o%20Tay%20Ng%E1%BA%AFn%20Ph%E1%BB%91i%20Qu%E1%BA%A7n%20Short%20In%20Ho%E1%BA%A1t%20H%C3%ACnh%20Th%E1%BB%9Di%20Trang%20M%C3%B9a%20H%C3%A8%20Cho%20H%E1%BB%8Dc%20Sinh%2C%20X%C3%A1m.jpg?alt=media&token=d1af7348-da7c-4546-a221-be42857847bf"
                ),
                name = "Bộ Đồ Ngủ Áo Tay Ngắn Phối Quần Short In Hoạt Hình Thời Trang Mùa Hè Cho Học Sinh",
                description =
                "M: chiều cao 155-163cm, trọng lượng 40-50kg, L: chiều cao 160-168cm, trọng lượng 50-60kg,XL: cao 165-173cm, nặng 60-70kg,XXL: cao 170-178cm, nặng 70-80kg\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Chào mừng đến với cửa hàng của chúng tôi!Nếu Bạn Quan tâm Đến Cửa Hàng Của Chúng Tôi, Bạn Có Thể Theo Dõi Chúng Tôi Và Nhận Phiếu Giảm Giá.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Sau khi nhận được phiếu giảm giá, giá ưu đãi hơn và có nhiều chiết khấu toàn diện hơn cho bạn lựa chọn.\n" +
                        "\n" +
                        "Dễ dàng phù hợp với quần áo, thích hợp cho công việc và mua sắm\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Hỗ trợ thanh toán khi giao hàng（COD-Cash on Deliver)\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Dịch vụ nhanh nhất\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Luôn đặt danh tiếng đầu tiên\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Lời hứa Hình ảnh sản phẩm thực tế\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Do màn hình và điều kiện ánh sáng khác nhau, màu sắc thực tế của sản phẩm có thể khác 3-5%.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Nếu Bạn Kiểm Tra Bất Kỳ Vấn Đề nào Khi Nhận Được Gói Hàng, Vui Lòng Liên Hệ Với Chúng Tôi Để Được Trợ Giúp, Chúng Tôi Sẽ Trả Lời Bạn Với Sự Hài Lòng.",
                group = "Nữ",
                colors = listOf(ClothesColor("Hồng", "#fa00ff"), ClothesColor("Vàng", "#ebff00"), ClothesColor("Xám", "#aeafaf")),
                sizes = listOf("M", "L", "XL", "XXL"),
                price = 137000,
                sell = 648
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
