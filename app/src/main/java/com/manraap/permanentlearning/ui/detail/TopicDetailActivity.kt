package com.manraap.permanentlearning.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.manraap.permanentlearning.databinding.ActivityTopicDetailBinding
import com.manraap.permanentlearning.ui.topic.AddEditTopicActivity
import com.manraap.permanentlearning.util.AppViewModelFactory
import com.manraap.permanentlearning.util.Injector
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TopicDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopicDetailBinding
    private val viewModel: TopicDetailViewModel by viewModels {
        AppViewModelFactory(Injector.provideRepository(this))
    }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topicId = intent.getLongExtra("topic_id", -1L)
        if (topicId == -1L) finish()

        historyAdapter = HistoryAdapter()
        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = historyAdapter

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        viewModel.getTopicWithHistory(topicId).observe(this) { wrapper ->
            wrapper ?: return@observe
            val topic = wrapper.topic
            binding.textTitle.text = topic.title
            binding.textKeywords.text = "Keywords: ${topic.keywords}"
            binding.textErrorLog.text = if (topic.errorLog.isBlank()) "No errors logged" else topic.errorLog
            binding.textCreatedAt.text = "Created: ${formatter.format(Date(topic.createdAt))}"
            binding.textNextRevision.text = "Next revision: ${formatter.format(Date(topic.nextRevisionAt))}"
            historyAdapter.submitList(wrapper.history)
        }

        binding.buttonEditTopic.setOnClickListener {
            startActivity(Intent(this, AddEditTopicActivity::class.java).putExtra("topic_id", topicId))
        }
    }
}
