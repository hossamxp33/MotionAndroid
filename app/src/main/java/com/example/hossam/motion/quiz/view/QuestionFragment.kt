package com.example.hossam.motion.quiz.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModelProviders
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.hossam.motion.R
import com.example.hossam.motion.quiz.Model.RecAnswer
import com.example.hossam.motion.quiz.adapters.RadioAdapter
import com.example.hossam.motion.quiz.data.model.Question
import com.example.hossam.motion.quiz.viewModel.QuizViewModel
import com.github.nkzawa.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.view.*
import org.jetbrains.anko.support.v4.toast
import java.util.concurrent.TimeUnit

class QuestionFragment : Fragment() {
    private lateinit var mLifecycleRegistry: LifecycleRegistry

    private lateinit var viewModel: QuizViewModel
    private lateinit var mAdapter: RadioAdapter
    /*  val  socket = IO.socket("http://192.168.1.2:3000");*/
 /*   private var timer: CountDownTimer? = null

    // create boolean for starting
    private var isViewShown = false*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment1.
     */
    fun newInstance(question: Question, isLast: Boolean): QuestionFragment {
        //cannot access when in companion object
        val myFragment = QuestionFragment()

        val args = Bundle()
        /*       args.putString("number", question.number)
               args.putString("question", question.question)
               args.putString("identifier", question.identifier)
               args.putInt("questionId", question.questionId)
               args.putInt("period", question.period)
               args.putParcelable("answers", question)*/
        args.putBoolean("isLast", isLast)
        args.putParcelable("question", question)

        Log.e("list", question.answers.toString())
        Log.e("array", question.answers.toTypedArray().toString())
       /* timer = null*/
        myFragment.arguments = args

        return myFragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(QuizViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.CREATED)

    }

    private var isLast: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        arguments?.getParcelable<Question>("question")?.apply {
            isLast = arguments!!.getBoolean("isLast")

            val answers = this.answers
            Log.e("fromBundle", this.answers.toString())
            view.question.text = this.question
            view.identifier.text = this.identifier


          /*  val periodMilli = TimeUnit.MINUTES.toMillis(this.period.toLong())
            Log.e("periodMilli", "periodMilli $periodMilli")
         if (timer != null) timer = null
            timer = object : CountDownTimer(periodMilli + 1000, 1000) {

                override fun onTick(secondsUntilDone: Long) {

                    view.number.text = TimeUnit.MILLISECONDS.toSeconds(secondsUntilDone).toString()

                }

                override fun onFinish() {

                }
            }*/

          /*if (view != null) {
                isViewShown = true

                timer?.start()

            }*/

            val appContext = this@QuestionFragment.activity!!.applicationContext
            val answersList = listOf(RecAnswer(answers[0].answer, RadioButton(appContext))
                    , RecAnswer(answers[1].answer, RadioButton(appContext))
                    , RecAnswer(answers[2].answer, RadioButton(appContext))
                    , RecAnswer(answers[3].answer, RadioButton(appContext)))
            mAdapter = RadioAdapter(appContext, answersList)
            mAdapter.notifyDataSetChanged()

            with(view.recyclerAnswers) {
                layoutManager = LinearLayoutManager(appContext)
                itemAnimator = DefaultItemAnimator()
                adapter = mAdapter

            }

            view.btnSubmitAnswer.setOnClickListener { _ ->

                with(android.media.MediaPlayer.create(this@QuestionFragment.context, R.raw.buttonrec)) {
                    start()
                    setOnCompletionListener {
                       stop()
                        reset()
                        this?.release()

                    }
                }

                val intAdapter = mAdapter.mSelectedItem
                if (intAdapter == -1) {
                    viewModel.sendAnswer(viewModel.id
                            , this.questionId, 0)
                    Log.e("viewModel.sendAnswer", "id =${viewModel.id} questionId =${this.questionId} answer_id=0  ")
                } else {
                    viewModel.sendAnswer(viewModel.id
                            , this.questionId, answers.get(intAdapter).id)
                    Log.e("viewModel.sendAnswer", "id =${viewModel.id} questionId =${this.questionId} position=${answers.get(intAdapter).id}  ")
                }


                 this@QuestionFragment.progressView.visibility = View.VISIBLE
            }



        } ?: IllegalArgumentException("QuestionFragment arguments is missing")
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
//        timer?.cancel()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
          //  timer?.start()
        }
    }

}
