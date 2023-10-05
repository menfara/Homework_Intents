package com.example.homework_intents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.homework_intents.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private var millisLeft: Long = 0
    private lateinit var timer: CountDownTimer
    private var isRunning = false
    private var isReceivedIntentData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let { restoreState(it) }
        if (!isReceivedIntentData) intent.extras?.let { handleIntentData(it) }

        setupButtonListeners()

        Log.d("TAG", "onCreate Called")
    }

    private fun restoreState(savedState: Bundle) {
        millisLeft = savedState.getLong(SavedStateKey.MILLISECONDS.name)
        isRunning = savedState.getBoolean(SavedStateKey.IS_RUNNING.name)
        isReceivedIntentData = savedState.getBoolean(SavedStateKey.IS_RECEIVED.name)
        if (isRunning) setOnTimeReceived(millisLeft)
        else displayTime(millisLeft)
    }

    private fun handleIntentData(intentData: Bundle) {
        Log.d("TAG", "INTENTTTT")
        setOnTimeReceived(intentData.getLong(ArgumentKey.NAME.name) * 1000)
        isReceivedIntentData = true
    }

    private fun setupButtonListeners() {
        binding.apply {
            pauseButton.setOnClickListener { timerPause() }
            startButton.setOnClickListener { if (!isRunning) setOnTimeReceived(millisLeft) }
            resetButton.setOnClickListener { resetTimer() }
        }
    }

    private fun displayTime(milliseconds: Long) {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val timeString = String.format("%02d:%02d", minutes, remainingSeconds)
        binding.textView.text = timeString
    }

    private fun timerPause() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }

    private fun resetTimer() {
        isRunning = false
        timer.cancel()
        millisLeft = 0
        displayTime(millisLeft)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TAG", "onSaveInstanceState Called")
        outState.putLong(SavedStateKey.MILLISECONDS.name, millisLeft)
        outState.putBoolean(SavedStateKey.IS_RECEIVED.name, isReceivedIntentData)
        outState.putBoolean(SavedStateKey.IS_RUNNING.name, isRunning)
    }


    private fun setOnTimeReceived(milliseconds: Long) {
        timer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                isRunning = true
                millisLeft = millisUntilFinished
                displayTime(millisUntilFinished)
            }

            override fun onFinish() {
                binding.textView.text = "00:00"
            }
        }.start()
    }

    enum class SavedStateKey {
        MILLISECONDS, IS_RECEIVED, IS_RUNNING
    }
}
