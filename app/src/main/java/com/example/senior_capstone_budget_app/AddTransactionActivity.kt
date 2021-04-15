package com.example.senior_capstone_budget_app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.transaction_item.*

class AddTransactionActivity : AppCompatActivity() {

    private var popupTitle = ""
    private var transactionAmount = ""
    private var transactionPayee = ""
    private var transactionDate = ""
    private var transactionCategory = ""
    private var popupButton = ""
    private var darkStatusBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_add_transaction)

        // Fade animation for the background of Popup Window
        val alpha = 50 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()


        popup_window_title.text = "Add Transaction"
        transaction_amount.text = "Transaction Amount:"
        payment_to.text = "Payment To:"
        payment_date.text = "Date of Transaction:"
        payment_category.text = "Category:"
        add_button.text = "Add Transaction"
        transcation_description.text = "Transaction Details"

        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }

        // Close the Popup Window when you press the button
        add_button.setOnClickListener {

            Toast.makeText(this, "Added Transaction $${transaction_amount_edit_text.text} To ${transaction_to_edit_text.text}", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
        close_popup_button.setOnClickListener {
            onBackPressed()
        }

        add_transaction_calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            hideKeyboard()

        }
    }

    private fun hideKeyboard() {
        //hide keyboard on tab selected
        try {
            val inputMethodManager: InputMethodManager = this.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken, 0
            )

        } catch (error: Exception) {
            //keyboard was not open
            error.printStackTrace()
            return
        }
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }

    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

//        // Fade animation for the Popup Window when you press the back button
//        popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
//            DecelerateInterpolator()
//        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
}