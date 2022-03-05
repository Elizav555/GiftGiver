package com.example.giftgiver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.giftgiver.databinding.ActivityMainBinding
import com.example.giftgiver.firebase.RealtimeDB
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val database = RealtimeDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.account,
                R.id.wishlists,
                R.id.friends_list,
                R.id.calendar,
                R.id.cart
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val event = Event(0, "New Year", Date(2023, 1, 1), "Happy New Year")
//        val gift = Gift(
//            0,
//            "IceCream",
//            "Mint with chocolate chops",
//            "https://i0.wp.com/pastryartsmag.com/wp-content/uploads/2019/06/Organic-Mint-Chocolate-Chip-Ice-Cream.jpg?resize=1200%2C800&ssl=1"
//        )
//        val calendar = Calendar(0, listOf(event))
//        val user = User(
//            VK.getUserId().value.toInt(),
//            "Elizaveta Garkina",
//            "https://sun9-82.userapi.com/impg/b8AFtKLAYYGerGifSrPw6vhNIhZjvussyuMGiA/ETb7g2DxKg0.jpg?size=604x453&quality=96&sign=8d0fda0a89b0cc5b2fad0797f2a80141&c_uniq_tag=Qubu4ImCWQx1aaMI2cEJBBfnIoYnha1JO4IYRstVpIc&type=album"
//        )
//        VK.execute(
//            VKUserInfoRequest(user.id),
//            object : VKApiCallback<UserInfo> {
//                override fun success(result: UserInfo) {
//                    user.info = result
//                }
//
//                override fun fail(error: Exception) {
//                    Log.println(Log.ERROR, "", error.toString())
//                }
//            }
//        )
//        val wishlist = Wishlist(0, "New Year Wishlist", listOf(gift))
//        val cart = Cart(0, listOf(gift))
//        database.addNewCalendar(calendar.id, calendar.events)
//        database.addNewCart(cart.id, cart.gifts)
//        database.addNewClient(VK.getUserId().value, calendar, user, cart, listOf())
//        database.addNewEvent(event.id, event.name, event.date, event.desc)
//        database.addNewGift(gift.id, gift.name, gift.desc, gift.imageUrl, gift.isChosen)
//        database.addNewWishlist(wishlist.id, wishlist.name, wishlist.gifts)
    }
}
