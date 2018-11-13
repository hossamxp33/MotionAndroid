package com.example.agh.hdtask.Data.Network


import com.example.hossam.motion.quiz.Model.JsonQuestion
import com.example.hossam.motion.quiz.data.model.CittiesResponse
import com.example.hossam.motion.quiz.data.model.postAnswerResponse
import com.example.hossam.motion.quiz.data.model.registerResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 * Created by agh on 17/07/18.
 */
interface APIServices {
    //  https://api.theQuestiondb.org/3/discover/Question?api_key=ed9e818b2fef8d30e863b5a55d75391e&page=2


    @POST("students/add.json")
    @FormUrlEncoded
    fun register(@Field("name") username: String,
                 @Field("email") email: String,
                 @Field("status") status: Int,
                 @Field("age") age: Int,
                 @Field("city_id") city_id: Int,
                 @Field("gender") gender: Int): Call<registerResponse>

    @POST("studentanswers/add.json")
    @FormUrlEncoded
    fun postAnswer(@Field("student_id") student_id: Int,
                   @Field("question_id") question_id: Int,
                   @Field("answer_id") answer_id: Int): Call<postAnswerResponse>


    @GET("Questions/getdata.json")
    fun getResults(): Call<JsonQuestion>

    @GET("cities/getcities.json")
    fun getCitties(): Call<CittiesResponse>


    companion object {
        fun create(): APIServices {

            val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    //                .addInterceptor(new RequestInterceptor())
                    .build()


            val gson = GsonBuilder().setLenient().create()
            val retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl("http://motion.codesroots.com/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()


            return retrofit.create(APIServices::class.java)
        }
    }

}