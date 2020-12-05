package com.muppet.jetpacktest.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muppet.jetpacktest.Other.Repository
import com.muppet.jetpacktest.Other.User

class MainViewModel(countReserved: Int) : ViewModel() {

   // 1 val user = User("zhu","haha",21)

    val counter : LiveData<Int>
        get() =_counter

    private val _counter = MutableLiveData<Int>()

    //  private val userLiveData = MutableLiveData<User>()

    /*  val userName : LiveData<String> = Transformations.map(userLiveData){
        "${user.firstName} ${user.lastName}"
    }*/

    private val userIdLiveData = MutableLiveData<String>()

    val user: LiveData<User> = Transformations.switchMap(userIdLiveData) { userId ->
        Repository.getUser(userId)
    }

    init {
        _counter.value = countReserved
    }

    fun getUser(userId: String) {
        userIdLiveData.value = userId
    }

    fun plusOne() {
        val count = _counter.value ?: 0
        _counter.value = count + 1
    }
    fun clear() {
        _counter.value = 0
    }
}