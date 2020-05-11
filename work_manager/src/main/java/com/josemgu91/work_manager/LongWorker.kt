package com.josemgu91.work_manager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class LongWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        // This runs on a background thread.
        val success = doLongTask()
        return if (success) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    private fun doLongTask(): Boolean {
        try {
            Log.d("LongJob", "Long task started.")
            Thread.sleep(5000)
            Log.d("LongJob", "Long task finished.")
            return true
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Log.d("LongJob", "Long task was interrupted!")
        }
        return false
    }
}