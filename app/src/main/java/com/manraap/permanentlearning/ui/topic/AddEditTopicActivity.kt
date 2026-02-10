package com.manraap.permanentlearning.ui.topic

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manraap.permanentlearning.databinding.ActivityAddEditTopicBinding
import com.manraap.permanentlearning.util.AppViewModelFactory
import com.manraap.permanentlearning.util.Injector

class AddEditTopicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditTopicBinding
    private val viewModel: AddEditTopicViewModel by viewModels {
        AppViewModelFactory(Injector.provideRepository(this))
    }
    private var topicId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topicId = intent.getLongExtra("topic_id", -1L).takeIf { it != -1L }

        topicId?.let { id ->
            viewModel.getTopic(id).observe(this) { topic ->
                topic ?: return@observe
                binding.inputTitle.setText(topic.title)
                binding.inputExplanation.setText(topic.explanation)
                binding.inputKeywords.setText(topic.keywords)
                binding.inputQuestions.setText(topic.questions)
                binding.inputErrorLog.setText(topic.errorLog)
            }
        }

        binding.buttonSaveTopic.setOnClickListener {
            val questions = binding.inputQuestions.text.toString()
                .lines()
                .map { it.trim() }
                .filter { it.isNotBlank() }

            if (questions.size !in 2..5) {
                Toast.makeText(this, "Add 2 to 5 recall questions (one per line)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveTopic(
                topicId = topicId,
                title = binding.inputTitle.text.toString().trim(),
                explanation = binding.inputExplanation.text.toString().trim(),
                keywords = binding.inputKeywords.text.toString().trim(),
                questions = questions,
                errorLog = binding.inputErrorLog.text.toString().trim(),
                onComplete = { finish() }
            )
        }
    }
}
