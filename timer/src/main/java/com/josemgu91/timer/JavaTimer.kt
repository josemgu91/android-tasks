package com.josemgu91.timer

import android.os.Handler
import java.util.*

class JavaTimer(private val delayInMillis: Long = 0, private val task: Runnable) : Timer {

    private val javaTimer = java.util.Timer()
    private val javaTimerTask = object : TimerTask() {
        override fun run() {
            callingThreadHandler.post {
                task.run()
            }
        }
    }
    private val callingThreadHandler = Handler()

    private var isRunning = false

    override fun isRunning() = isRunning

    override fun start(executeImmediately: Boolean) {
        if (isRunning) {
            return
        }
        javaTimer.scheduleAtFixedRate(
            javaTimerTask,
            if (executeImmediately) 0 else delayInMillis,
            delayInMillis
        )
        isRunning = true
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        javaTimer.cancel()
        isRunning = false
    }
}