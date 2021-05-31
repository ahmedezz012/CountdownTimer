package com.linkdev.countdowntimer

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val DELAY = 1000L
    }

    var seconds: Int = 0
    lateinit var txtSelectTimeLabel: TextView
    lateinit var txtRemainingTime: TextView
    lateinit var txtSelectedTime: TextView
    private var stopTimer = false

    var mSelectedTime: Calendar = Calendar.getInstance()
    var mCurrentTime: Calendar = Calendar.getInstance()

    var mCountDown: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        txtSelectTimeLabel.setOnClickListener {
            onSelectTimeClicked()
        }
    }

    private fun onSelectTimeClicked() {
        showTimePickerDialog(onTimeSelected = { hourOfTheDay: Int, minute: Int ->
            onTimePicked(
                hourOfTheDay,
                minute
            )
        })
    }

    private fun onTimePicked(hourOfTheDay: Int, minute: Int) {

        setSelectedTime(hourOfTheDay, minute)
        if (mSelectedTime.after(mCurrentTime)) {
            val difference = mSelectedTime.timeInMillis - mCurrentTime.timeInMillis
            mCountDown.timeInMillis = difference
            bindSelectedTime()
            bindRemainingTime()
            startCountDown(difference)
        } else {
            Toast.makeText(this, R.string.chooseFutureTime, Toast.LENGTH_LONG).show()
        }

    }

    private fun bindSelectedTime() {
        txtSelectedTime.text = DateHelper.formatCalendarToString(
            mCountDown,
            DateHelper.TINE_FORMAT
        )
    }

    fun bindRemainingTime() {
        txtRemainingTime.text = DateHelper.formatCalendarToString(
            mCountDown,
            DateHelper.TINE_FORMAT
        )
    }

    private fun setSelectedTime(hourOfTheDay: Int, minute: Int) {
        mSelectedTime.set(Calendar.HOUR_OF_DAY, hourOfTheDay)
        mSelectedTime.set(Calendar.MINUTE, minute)
    }

    private fun startCountDown(difference: Long) {
        seconds = (difference / DELAY).toInt()
        handler.post(runnable)
    }

    val handler = Handler()
    private val runnable = object : Runnable {
        override fun run() {
            seconds--
            mCountDown.add(Calendar.SECOND, -1)
            bindRemainingTime()
            if (seconds < 0) {
                stopTimer = true;
            }
            if (!stopTimer) {
                handler.postDelayed(this, DELAY);
            }
        }
    }

    private fun initializeViews() {
        txtSelectTimeLabel = findViewById(R.id.txtSelectTimeLabel)
        txtRemainingTime = findViewById(R.id.txtRemainingTime)
        txtSelectedTime = findViewById(R.id.txtSelectedTime)
    }


    private fun showTimePickerDialog(
        selectedTime: Date? = null,
        onTimeSelected: (Int, Int) -> Unit
    ) {
        showTimePickerDialog(
            selectedTime
        ) { _: TimePicker?, hourOfDay: Int, minute: Int ->
            onTimeSelected(hourOfDay, minute)
        }
    }

    private fun showTimePickerDialog(
        selectedTime: Date? = null,
        listener: ((view: TimePicker?, hourOfDay: Int, minute: Int) -> Unit)? = null
    ) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        selectedTime?.let { calendar.time = it }
        val timePickerDialog = TimePickerDialog(
            this,
            listener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        )

        timePickerDialog.show()
    }
}