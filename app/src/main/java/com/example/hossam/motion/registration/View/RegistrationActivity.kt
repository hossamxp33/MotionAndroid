package com.example.hossam.motion.registration.View

import android.app.PendingIntent.getActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSpinner
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.hossam.motion.LocaleUtils
import com.example.hossam.motion.R
import com.example.hossam.motion.quiz.view.QuizActivity
import com.example.hossam.motion.registration.Repository.RegistrationRepository
import com.example.hossam.motion.registration.ViewModel.RegistrationViewModel
import kotlinx.android.synthetic.main.activity_registration.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*


class RegistrationActivity : AppCompatActivity() {
     var registrationRepository: RegistrationRepository

    private val viewModel: RegistrationViewModel by lazy {
        ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
    }

    init {
        LocaleUtils.updateConfig(this)
        registrationRepository = RegistrationRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("JF-Flat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

// TODO change text size for small screen sizes
        /*     if (resources.configuration.screenLayout and
                     Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE) {
                 // on a large screen device ...

             }*/
        setContentView(R.layout.activity_registration)

        viewModel.getCitties()
        viewModel.citties.observe(this, Observer {
            val citties = it?.map { it.name }?.toTypedArray()
            initializeSpinner(spinCity, stringSpinnerArray = citties)
            spinCity.onItemSelected {
                viewModel.city.postValue(it.toInt() + 1)
            }

        })

        initializeSpinner(spinAge, intSpinnerArray = (6..14).toList().toTypedArray() as Array<Int>?)
        initializeSpinner(spinGender, stringSpinnerArray = arrayOf("ذكر", "أنثى") as Array<String>?)




        etname.afterTextChanged { viewModel.username.postValue(it) }
        etMail.afterTextChanged { viewModel.email.postValue(it) }
        spinAge.onItemSelected { viewModel.age.postValue((it.toInt() + 6)) }
        spinGender.onItemSelected { viewModel.gender.postValue(it.toInt()) }


        btnRegister.setOnClickListener {
            viewModel.registerUser()
        progressView.visibility = View.VISIBLE

     /*       Handler().postDelayed({
                runOnUiThread {
                    progressView.visibility = View.GONE
                    toast("تأكد من ملأ جميع الخانات ")
                }
            }, 10000)*/

        }

        viewModel.error.observe(this, Observer {


            if (it.equals("empty")){  progressView.visibility = View.GONE   }
        })
        viewModel.coderesponse.observe(this, Observer {

            if ( ! it!!.equals(200) && ! it!!.equals(201)   ){
              hideProgress()
            }

        })


        viewModel.toast.observe(this, Observer {
            toast(it!!)

        })
        viewModel.studentID.observe(this, Observer {
            progressView.visibility = View.GONE
            val intent = Intent(this@RegistrationActivity, QuizActivity::class.java)
            intent.putExtra("studentID", it)
            startActivity(intent)
            finish()
        })
    }

    fun hideProgress() {
        progressView.visibility = View.GONE
        toast("رجاء تأكد من صحة البيانات")
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    fun AppCompatSpinner.onItemSelected(onItemSelected: (String) -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onItemSelected.invoke(position.toString())
            }
        }
    }

    private fun initializeSpinner(_spinner: AppCompatSpinner, stringSpinnerArray: Array<String>? = null, intSpinnerArray: Array<Int>? = null) {
        Log.e(":intSpinnerArray ", intSpinnerArray?.get(0).toString())
        val spinnerArrayAdapter by lazy {
            if (stringSpinnerArray != null && intSpinnerArray == null) {
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringSpinnerArray)
            } else if (intSpinnerArray != null && stringSpinnerArray == null) {
                ArrayAdapter<Int>(this, android.R.layout.simple_spinner_dropdown_item, intSpinnerArray)
            } else {
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("else", "else"))
            }
        }

        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _spinner.adapter = spinnerArrayAdapter

        _spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                val selectedItem = parent.getItemAtPosition(position).toString()
                _spinner.setSelection(position)
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


}
