package com.josemgu91.timer

import android.os.Handler
import android.os.Message

class HandlerTimer(
    private val delayInMillis: Long = 0,
    private val task: Runnable,
    private val useMessages: Boolean
) : Timer {

    private val handler: Handler = MyHandler(task, delayInMillis)

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            task.run()
            handler.postDelayed(this, delayInMillis)
        }
    }

    private var isRunning = false

    override fun isRunning() = isRunning

    override fun start(executeImmediately: Boolean) {
        if (isRunning) {
            return
        }
        if (useMessages) {
            executeWithMessages(executeImmediately)
        } else {
            executeWithPostRunnable(executeImmediately)
        }
        isRunning = true
    }

    private fun executeWithPostRunnable(executeImmediately: Boolean) {
        if (executeImmediately) {
            if (handler.looper.thread.id == Thread.currentThread().id) {
                runnable.run()
            } else {
                handler.post(runnable)
            }
        } else {
            handler.postDelayed(runnable, delayInMillis)
        }
    }

    private fun executeWithMessages(executeImmediately: Boolean) {
        if (executeImmediately) {
            handler.sendEmptyMessage(0)
        } else {
            handler.sendEmptyMessageDelayed(0, delayInMillis)
        }
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        handler.removeCallbacksAndMessages(null)
        isRunning = false
    }

    private class MyHandler(private val task: Runnable, private val delayInMillis: Long) :
        Handler() {
        override fun handleMessage(msg: Message) {
            task.run()
            sendEmptyMessageDelayed(0, delayInMillis)
        }
    }
}