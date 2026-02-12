package com.manraap.permanentlearning.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.manraap.permanentlearning.databinding.ActivityMainBinding
import com.manraap.permanentlearning.ui.detail.TopicDetailActivity
import com.manraap.permanentlearning.ui.progress.ProgressActivity
import com.manraap.permanentlearning.ui.recall.RecallActivity
import com.manraap.permanentlearning.ui.topic.AddEditTopicActivity
import com.manraap.permanentlearning.util.AppViewModelFactory
import com.manraap.permanentlearning.util.Injector
import com.manraap.permanentlearning.worker.DueRevisionWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels {
        AppViewModelFactory(Injector.provideRepository(this))
    }
    private lateinit var adapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TopicAdapter(
            onRecallClick = { topic ->
                startActivity(Intent(this, RecallActivity::class.java).putExtra("topic_id", topic.id))
            },
            onDetailClick = { topic ->
                startActivity(Intent(this, TopicDetailActivity::class.java).putExtra("topic_id", topic.id))
            }
        )
        binding.recyclerTopics.layoutManager = LinearLayoutManager(this)
        binding.recyclerTopics.adapter = adapter

        viewModel.topics.observe(this) { adapter.submitList(it) }

        binding.fabAddTopic.setOnClickListener {
            startActivity(Intent(this, AddEditTopicActivity::class.java))
        }
        binding.buttonProgress.setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
        }
        binding.buttonNextDue.setOnClickListener {
            viewModel.getNextDueTopic { topic ->
                topic?.let {
                    startActivity(Intent(this, RecallActivity::class.java).putExtra("topic_id", it.id))
                }
            }
        }

        scheduleDueRevisionWorker()
    }

    private fun scheduleDueRevisionWorker() {
        val request = PeriodicWorkRequestBuilder<DueRevisionWorker>(6, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "due_revision_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
