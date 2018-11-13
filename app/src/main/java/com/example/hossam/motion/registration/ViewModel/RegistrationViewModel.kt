package com.example.hossam.motion.registration.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.util.Consumer
import android.util.Log
import com.example.hossam.motion.quiz.data.model.City
import com.example.hossam.motion.registration.Repository.RegistrationRepository
import kotlinx.coroutines.experimental.async


class RegistrationViewModel : ViewModel() {
    internal var registrationRepository: RegistrationRepository

    lateinit var spinnersData: Array<Array<out Any>>

    val response: MutableLiveData<ArrayList<String>>
    var coderesponse = MutableLiveData<Int>()

    var username = MutableLiveData<String>()
    var email = MutableLiveData<String>()

    var age = MutableLiveData<Int>()
    var city = MutableLiveData<Int>()
    var gender = MutableLiveData<Int>()
    var error = MutableLiveData<String>()
    val toast = MutableLiveData<String>()

    var studentID = MutableLiveData<Int>()


    var citties = MutableLiveData<List<City>>()


    init {

        registrationRepository = RegistrationRepository()
        email = MutableLiveData()
        error = MutableLiveData()
        coderesponse = MutableLiveData()
        username = MutableLiveData()
        response = MutableLiveData()
        studentID = MutableLiveData()


        age = MutableLiveData()
        city = MutableLiveData()
        gender = MutableLiveData()
        citties = MutableLiveData()

        registrationRepository.setcodeSuccess(object : Consumer<Int> {
            override fun accept(t: Int) {
                Log.e("codecode",t.toString())
                coderesponse.postValue(t)
            }
        })

    }


    fun registerUser() {
        Log.e("", "registerUser() called with: username = [$username], email = [$email]")

        if (username.value.isNullOrBlank() || email.value.isNullOrBlank()) {
            toast.postValue("تأكد من ملأ جميع الخانات ")
            error.postValue("empty")
        } else {
            async {

                val registerResponse = registrationRepository.registerUser(username.value!!, email.value!!, 1,
                        age.value!!, city.value!!, gender.value!!)

                if (registerResponse.success == false) error.postValue("error")

                Log.e(" messsadge", registerResponse.toString())

                registerResponse.data.id?.apply {
                    toast.postValue(this.toString())
                    studentID.postValue(this)
                } ?: Log.e("null", " id is null")
                /* ?:data.message?.apply { toast.postValue(this)  }*/
                registerResponse.data.code?.apply {
                    toast.postValue(this.toString())
                    toast.postValue("خطأ في البيانات المدخلة")
                } ?: Log.e("null", " code is null" + registerResponse.toString())


            }

            Log.e("Registration info", "age${age.value.toString()} " +
                    "city${city.value.toString()} " +
                    "gender${gender.value.toString()}  ${username.value}   ${email.value}")

        }

    }

    fun getCitties() {

        async {
            val data = registrationRepository.getCitties()
            val city = data.map { it.name }

            Log.e("Citties", "data$data")

            // for (i in 0..data.size) { cittiesNames.add(i , data.get(i).name)  }
            citties.postValue(data)


        }


    }

}