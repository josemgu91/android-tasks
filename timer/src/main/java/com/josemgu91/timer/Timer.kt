package com.josemgu91.timer

interface Timer {

    /**
     * @return True if the timer is running.
     */
    fun isRunning(): Boolean

    /**
     * Executes the timer.
     * @param executeImmediately True if you don't want an initial delay.
     */
    fun start(executeImmediately: Boolean)

    /**
     * Stops the timer.
     */
    fun stop()

}