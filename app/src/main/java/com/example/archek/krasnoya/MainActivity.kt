package com.example.archek.krasnoya

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.archek.krasnoya.net.KrasService
import com.example.archek.krasnoya.net.ObjectResponse
import com.example.archek.krasnoya.net.RestApi
import java.text.DateFormat
import java.text.DateFormatSymbols

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null//создаём переменные
    private var toolbar: Toolbar? = null
    private var ivNoData: ImageView? = null
    private var tvNoData: TextView? = null
    private var clData: ConstraintLayout? = null
    private val service = RestApi.createService(KrasService::class.java)
    private var call: Call<ObjectResponse>? = null
    private var tvCityVariable: TextView? = null
    private var tvTimeVariable: TextView? = null
    private var tvTempVariable: TextView? = null
    private var current: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clData = findViewById(R.id.clData)
        tvCityVariable = findViewById(R.id.tvCityVariable)
        tvTimeVariable = findViewById(R.id.tvTimeVariable)
        tvTempVariable = findViewById(R.id.tvTempVariable)
        ivNoData = findViewById(R.id.ivNoData)
        tvNoData = findViewById(R.id.tvNoData)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)//инициализируем элементы - активируем тулбар
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.baseline_menu_white)//выставляем "бутерброд"

        setupToolbar()//настраиваем тулбар/копки
        drawerMove();//выставляем реакции на действия с выдвижным меню/экраном

        ivNoData!!.setOnClickListener { loadData() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//метод открывающий меню при нажатии на "бутер"
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadData() {//метод загрузки данных
        ivNoData!!.visibility = View.GONE
        tvNoData!!.visibility = View.GONE
        clData!!.visibility = View.VISIBLE

        call = service.data
        call!!.enqueue(object : Callback<ObjectResponse> {
            override fun onResponse(call: Call<ObjectResponse>, response: Response<ObjectResponse>) {
                val objectResponse = response.body()
                tvCityVariable!!.text = objectResponse!!.name
                adaptedDate(objectResponse.ts)
                tvTimeVariable!!.text = current
                tvTempVariable!!.text = objectResponse.t + "°C"
            }

            override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
                if (call.isCanceled) {
                    Toast.makeText(applicationContext, getString(R.string.Error), Toast.LENGTH_SHORT).show()
                    tvNoData!!.text = getString(R.string.Error);
                }
            }
        })
    }
    fun setupToolbar(){
        mDrawerLayout = findViewById(R.id.drawer_layout)//инициализируем выдвижной экран
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->//делаем активной кнопку загрузить
            menuItem.isChecked = true
            loadData()
            mDrawerLayout!!.closeDrawers()
            true
        }
    }

    fun drawerMove(){
        mDrawerLayout!!.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {//если потянуть слайдер ставим значок крестик
                        toolbar!!.setNavigationIcon(R.drawable.baseline_close_white)
                    }

                    override fun onDrawerOpened(drawerView: View) {
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        toolbar!!.setNavigationIcon(R.drawable.baseline_menu_white)//при закрытии значок крестика вновь становится "бутербродом"
                    }

                    override fun onDrawerStateChanged(newState: Int) {}
                }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun adaptedDate(inputDate: Long?) {//адаптируем дату под заданный формат
        @SuppressLint("SimpleDateFormat")
        val russian = Locale("ru", "RU")
        val ruMonths = arrayOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
        val russSymbol = DateFormatSymbols(russian)
        Locale.setDefault(russian);
        russSymbol.setMonths(ruMonths);
        val dateFormat = SimpleDateFormat("HH:mm, dd MMMM yyyy", russSymbol)
        val date = Date(inputDate!!)
        current = dateFormat.format(date)
    }
}

