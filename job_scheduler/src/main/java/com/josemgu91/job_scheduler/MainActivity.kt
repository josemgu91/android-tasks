package com.josemgu91.job_scheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.josemgu91.job_scheduler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.buttonScheduleJob.setOnClickListener {
            scheduleJob()
        }
    }

    private fun scheduleJob() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
        val jobInfo = JobInfo.Builder(
            LongJob.LONG_BACKGROUND_JOB_ID,
            ComponentName(this, LongJob::class.java)
        )
            .setRequiresCharging(true)
            .setPersisted(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
            .build()
        jobScheduler.schedule(jobInfo)
    }
}
