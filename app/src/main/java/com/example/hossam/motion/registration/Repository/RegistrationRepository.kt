package com.example.hossam.motion.registration.Repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.agh.hdtask.Data.Network.APIServices
import com.example.hossam.motion.quiz.data.model.City
import com.example.hossam.motion.quiz.data.model.Data
import com.example.hossam.motion.quiz.data.model.registerResponse
import com.example.hossam.motion.registration.View.RegistrationActivity
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import android.support.v4.util.Consumer;

public class RegistrationRepository {
    var error = MutableLiveData<String>()
    private var codeResbonse: Consumer<Int>? = null


    init {
        error = MutableLiveData()
    }
    suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { cont ->
        cont.invokeOnCompletion {
            error.postValue("fff")
            cancel() }
        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>?, t: Throwable) {
                error.postValue("fff")

                cont.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>?, response: Response<T>) {
                Log.e("response" , response.code().toString())
                if (response.code() != 200 ) {
                    if (codeResbonse != null)
                        codeResbonse!!.accept(response.code())
                }


                error.postValue(response.code().toString())

                if (response.isSuccessful) {
                    cont.resume(response.body()!!)
/*                    if (response.errorBody()!=null)    this@RegistrationRepository.error.postValue(response.errorBody().toString())
                    if (response.code()==422)    this@RegistrationRepository.error.postValue(response.errorBody().toString())*/
                } else {

                    error.postValue("hhj")
                    cont.resumeWithException(HttpException(response))



                    Log.e("response Exception" , HttpException(response).localizedMessage)
                    Log.e("response Exception" ,response.errorBody().toString())


                }
            }
        })
    }

    suspend fun registerUser(username: String, email: String,  status: Int, age: Int,   city_id: Int, gender: Int ): registerResponse
                         =   APIServices.create().register(username,email, status, age, city_id, gender).await()

    suspend fun getCitties(): List<City>
                         =   APIServices.create().getCitties().await().data

    fun setcodeSuccess(code: Consumer<Int>) {
        this.codeResbonse = code
    }


}