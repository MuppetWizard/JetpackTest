package com.muppet.jetpacktest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.muppet.jetpacktest.DSL.Table
import com.muppet.jetpacktest.DSL.table
import com.muppet.jetpacktest.Lifecycles.MyObserver
import com.muppet.jetpacktest.Other.User
import com.muppet.jetpacktest.Room.AppDatabase
import com.muppet.jetpacktest.ViewModel.MainViewModel
import com.muppet.jetpacktest.ViewModel.MainViewModelFactory
import com.muppet.jetpacktest.WorkManager.SimpleWorker
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences("count_reserved",Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved",0)

        RoomDatabaseExecute()

        //观察生命周期
        lifecycle.addObserver(MyObserver())

        //向 ViewModel 传递数据
        viewModel = ViewModelProvider(this, MainViewModelFactory(countReserved)).get(viewModel::class.java)

        //观察数据变化并更新
        viewModel.user.observe(this, Observer {
            tv_info.text = it.firstName
        })
        viewModel.counter.observe(this, Observer { count ->
            tv_info.text = count.toString()
        })

        val table = Table()
        table.tr {
            td { "Apple" }
            td { "Banana" }
            td { "Orange" }
        }
        table.tr {
            td { "Cherry" }
            td { "Watermelon" }
            td { "Lemon" }
        }

        val html = table{
            tr {
                td { "Apple" }
                td { "Banana" }
                td { "Orange" }
            }
            tr {
                td { "Cherry" }
                td { "Watermelon" }
                td { "Lemon" }
            }
        }

        val html2 = table {
            repeat(2) {
                tr {
                    val fruits = listOf("Apple","Banana","Cherry")
                    for (fruit in fruits) {
                        td { fruit }
                    }
                }
            }
        }

        onClick()
    }

    private fun RoomDatabaseExecute() {
        val userDao = AppDatabase.getDatabase(this).userDao()
        val user1 = User("Tony","ll",11)
        val user2 = User("Tom","SS",22)

        btn_addData.setOnClickListener {
            thread {
                user1.id = userDao.insetUser(user1)
                user2.id = userDao.insetUser(user2)
            }
        }
        btn_updateData.setOnClickListener {
            thread {
                user1.age = 33
                userDao.updateUser(user1)
            }
        }
        btn_deleteData.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("SS")
            }
        }
        btn_queryData.setOnClickListener {
            thread {
                for (user in userDao.loadAllUser()) {
                    Log.d("MainActivity",user.toString())
                }
            }
        }
    }

    private fun onClick() {
        btn_plus.setOnClickListener{
            viewModel.plusOne()
        }
        btn_clear.setOnClickListener {
            viewModel.clear()
        }
        btn_getUser.setOnClickListener {
            val userId = (0..1000).random().toString()
            viewModel.getUser(userId)
        }
        btn_doWork.setOnClickListener {
            val request= OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }

    override fun onPause() {
        super.onPause()
        val edit = sp.edit()
        edit.putInt("count_reserved",viewModel.counter.value ?: 0)
        edit.apply()
    }

}
