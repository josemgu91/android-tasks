package com.josemgu91.timer

import android.os.Handler
import android.os.SystemClock

class LoopTimer(private val delayInMillis: Long = 0, private val task: Runnable) : Timer {

    private var isRunning = false
    private val callingThreadHandler = Handler()
    private var timerThread: Thread? = null

    override fun isRunning(): Boolean = isRunning

    override fun start(executeImmediately: Boolean) {
        if (isRunning) {
            return
        }
        timerThread = Thread(Runnable {
            timerThread?.let {
                var lastTime = SystemClock.elapsedRealtime()
                if (executeImmediately) {
                    callingThreadHandler.post(task)
                }
                while (!it.isInterrupted) {
                    val currentTime = SystemClock.elapsedRealtime()
                    if (currentTime - lastTime >= delayInMillis) {
                        lastTime = currentTime
                        callingThreadHandler.post(task)
                    }
                }
            }
        })
        timerThread?.start()
        isRunning = true
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        timerThread?.interrupt()
        timerThread = null
        isRunning = false
    }

}