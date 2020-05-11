package com.josemgu91.work_manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.josemgu91.work_manager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.buttonDispatchWork.setOnClickListener {
            dispatchWork()
        }
    }

    private fun dispatchWork() {
        val workManager = WorkManager.getInstance(this)
        workManager.cancelAllWork()
        val workRequest = OneTimeWorkRequestBuilder<LongWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiresCharging(true)
                    .build()
            )
            .build()
        workManager.enqueue(workRequest)
    }
}
