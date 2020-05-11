package com.josemgu91.job_scheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class LongJob : JobService() {

    private var longTaskThread: Thread? = null

    override fun onStopJob(params: JobParameters?): Boolean {
        longTaskThread?.interrupt()
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        // This runs on main thread.
        longTaskThread = Thread(Runnable {
            val success = doLongTask()
            jobFinished(params, !success)
        })
        longTaskThread?.start()
        return false
    }

    private fun doLongTask(): Boolean {
        try {
            Log.d("LongJob", "Long task started.")
            Thread.sleep(10000)
            Log.d("LongJob", "Long task finished.")
            return true
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Log.d("LongJob", "Long task was interrupted!")
        }
        return false
    }

    companion object {
        const val LONG_BACKGROUND_JOB_ID = 1
    }
}