package com.example.evo.trialapplication.product

import android.annotation.SuppressLint
import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.ProductDataBase
import com.example.evo.trialapplication.database.ProductFactory
import com.example.evo.trialapplication.database.ProductRepository
import com.example.evo.trialapplication.database.ProductViewModel
import com.example.evo.trialapplication.databinding.ActivityMainBinding
import com.example.evo.trialapplication.interview.HomeFragment
import com.example.evo.trialapplication.util.KeyboardHelper
import com.example.servicepractice.IMyAidlInterface
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: ProductViewModel
    private var cartCount=0
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
    var iRemoteService: IMyAidlInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val studentRepository = ProductRepository(ProductDataBase.getDatabase(this))
        val factory1 = ProductFactory(studentRepository)
        viewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        Toast.makeText(this@MainActivity, "onCreate", Toast.LENGTH_SHORT).show()

        Log.i(TAG, "onCreate: ")
        bindService(Intent().setComponent(ComponentName("com.example.servicepractice","com.example.servicepractice.MyFirstService")), mConnection, Context.BIND_AUTO_CREATE)

        setDrawer()
        cartSelection()
        hideDrawer()
        demo("2324")


    }
    fun demo(x: Any) {
        if (x !is String) return

        Log.i(TAG, "demoString:${x.length} ")
    }
    private fun hideDrawer() {
        try {
            val inputMethodManager =
                this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            binding.drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(view: View, v: Float) {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }

                override fun onDrawerOpened(view: View) {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }

                override fun onDrawerClosed(view: View) {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }

                override fun onDrawerStateChanged(i: Int) {

                }
            })
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun cartSelection() {
        lifecycleScope.launch {
            viewModel.getCartData().collect{_list->
                if (_list.isEmpty()){

                    binding.badgeCounter.visibility=View.INVISIBLE
                }else{
                    cartCount=_list.size
                    binding.badgeCounter.visibility=View.VISIBLE
                    binding.badgeCounter.text=cartCount.toString()
                }

            }
        }
        binding.cartLayout.setOnClickListener {
            navController.navigate(R.id.cartList)
        }
    }

    private fun setDrawer() {

        try {
            appBarConfiguration = AppBarConfiguration(setOf(
                R.id.home,
                R.id.addProduct,
                R.id.homeFragment
            ), binding.drawer)

            KeyboardHelper.hideKeyboard(binding.drawer, this)

            setSupportActionBar(binding.tool)
            setupWithNavController(binding.navigationView, navController)
            setupActionBarWithNavController(navController, appBarConfiguration)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration)

    }
    val mConnection = object : ServiceConnection {

        // Called when the connection with the service is established.
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // Following the preceding example for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service.
            Log.i(TAG, "onServiceConnected: ")
            iRemoteService = IMyAidlInterface.Stub.asInterface(service)
           var data= iRemoteService?.data
            Toast.makeText(this@MainActivity, "onServiceConnected", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "onServiceConnected:$data ")
        }

        // Called when the connection with the service disconnects unexpectedly.
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            iRemoteService = null
        }
    }


    override fun onDestroy() {
        unbindService(mConnection)
        Log.i(TAG, "onDestroy: ")
        Toast.makeText(this@MainActivity, "onDestroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }


}

