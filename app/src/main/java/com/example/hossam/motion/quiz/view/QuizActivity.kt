package com.example.hossam.motion.quiz.view

import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.hossam.motion.FinishingActivity
import com.example.hossam.motion.LocaleUtils
import com.example.hossam.motion.R
import com.example.hossam.motion.quiz.adapters.CubeOutRotationTransformation
import com.example.hossam.motion.quiz.adapters.QuestionsPagerAdapter
import com.example.hossam.motion.quiz.data.model.Question
import com.example.hossam.motion.quiz.viewModel.QuizViewModel
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.fragment_question.*
import org.jetbrains.anko.toast
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.IOException
import java.net.URISyntaxException


class QuizActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var mLifecycleRegistry: LifecycleRegistry
    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private lateinit var pagerAdapter: QuestionsPagerAdapter
    private lateinit var viewModel: QuizViewModel

    companion object {
        lateinit var mSocket: Socket

    }

    init {
        LocaleUtils.updateConfig(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("JF-Flat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        setContentView(R.layout.activity_quiz)

        viewModel = ViewModelProviders.of(this).get(QuizViewModel::class.java)
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.CREATED)



        intent.extras?.apply { viewModel.id = getInt("studentID") }
                ?: Log.e("studentID", "null null null")
        try {

            mSocket = IO.socket("http://rtsnode.codesroots.com:3300/")

        } catch (e: URISyntaxException) {
            Log.e("socket exception", e.stackTrace.toString())
        }
        if (!mSocket.connected()) mSocket.connect()
        val onNewMessage = Emitter.Listener {
            runOnUiThread {
                val currentItem = viewPager.currentItem

                if (currentItem + 1 == pagerAdapter.count) {
                    startActivity(Intent(this, FinishingActivity::class.java))
                    finish()
                } else {
                    viewPager.currentItem = (currentItem + 1)
                }
            }
        }
        mSocket.on("next", onNewMessage)

        val onConnect = Emitter.Listener { runOnUiThread { toast("CONNECTED") } }

        with(mSocket) {

            on(Socket.EVENT_CONNECT, onConnect)

            connect()

        }

        viewModel.getQuestions()
        viewModel.getResponse().observe(this,
                Observer { dataList ->
                    val apiQuestions = dataList!!.filter { it.answers.isNotEmpty() }

                    Log.e("it", dataList.toString())
                    val questions = arrayListOf<Question>()

                    for (question in apiQuestions) {
                        with(question) {
                            val questionText = this.question
                            val questionID = id
                            val answers = answers
                            val period = period

                            val number = id
                            val identifier = question_sort ?: ""


                            questions.add(Question(questionText, questionID, answers, number.toString(), identifier, period))
                        }

                    }


                    pagerAdapter = QuestionsPagerAdapter(supportFragmentManager, questions)
                    with(viewPager) {
                        adapter = pagerAdapter
                        setPageTransformer(true, CubeOutRotationTransformation())
                    }


                })

        viewModel.currentPage.observe(this, Observer {
            Log.e("it ", it.toString())
            if (it == -1) toast("رجاء اختر اجابة أولا")
            else {
                toast(it.toString())
                val currentFragment = pagerAdapter.getCurrentFragment()
                currentFragment?.apply {
                    progressView.visibility = View.GONE
                    overlay.visibility = View.VISIBLE

                    btnSubmitAnswer.visibility = View.GONE
                    btnSubmitAnswerInactive.visibility = View.VISIBLE

                }


            }
        })
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mSocket.disconnect()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
