package com.josemgu91.timer

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.josemgu91.timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var timer: Timer? = null
    private var startTime = 0L
    private var tickCounter = 0

    private lateinit var viewBinding: ActivityMainBinding

    private val task = Runnable {
        val timeDifference = (SystemClock.elapsedRealtime() - startTime) / 1000f
        tickCounter++
        viewBinding.textViewStatus.text =
            getString(R.string.time, timeDifference, tickCounter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.buttonSwitchTimer.setOnClickListener {
            timer.let {
                if (it == null || !it.isRunning()) {
                    startTimer()
                } else if (it.isRunning()) {
                    it.stop()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.stop()
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        tickCounter = 0
        try {
            val delayInMilliseconds =
                viewBinding.editTextDelayInMilliseconds.text.toString().toLong()
            timer = when (viewBinding.radioGroupTimerImplementation.checkedRadioButtonId) {
                R.id.radioButtonJavaTimer -> JavaTimer(delayInMilliseconds, task)
                R.id.radioButtonHandlerTimerWithRunnables -> HandlerTimer(
                    delayInMilliseconds,
                    task,
                    false
                )
                R.id.radioButtonHandlerTimerWithMessages -> HandlerTimer(
                    delayInMilliseconds,
                    task,
                    true
                )
                R.id.radioButtonLoopTimer -> LoopTimer(delayInMilliseconds, task)
                else -> throw IllegalArgumentException("Invalid Timer implementation ID.")
            }
            timer?.start(true)
        } catch (e: NumberFormatException) {
            showMessage(getString(R.string.error_no_number))
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
