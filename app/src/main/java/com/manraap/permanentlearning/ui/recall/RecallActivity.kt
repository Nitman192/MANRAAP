package com.manraap.permanentlearning.ui.recall

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manraap.permanentlearning.data.RecallQuality
import com.manraap.permanentlearning.databinding.ActivityRecallBinding
import com.manraap.permanentlearning.util.AppViewModelFactory
import com.manraap.permanentlearning.util.Injector

class RecallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecallBinding
    private val viewModel: RecallViewModel by viewModels {
        AppViewModelFactory(Injector.provideRepository(this))
    }
    private var topicId: Long = -1L
    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topicId = intent.getLongExtra("topic_id", -1L)
        if (topicId == -1L) finish()

        viewModel.getTopic(topicId).observe(this) { topic ->
            topic ?: return@observe
            binding.textTopicName.text = topic.title
            binding.textQuestions.text = topic.questions
            binding.textAnswer.text = topic.explanation
            binding.textAnswer.visibility = View.GONE
        }

        binding.buttonShowAnswer.setOnClickListener {
            binding.textAnswer.visibility = View.VISIBLE
        }

        binding.switchTimer.setOnCheckedChangeListener { _, checked ->
            if (checked) startRecallTimer() else stopRecallTimer()
        }

        binding.buttonFail.setOnClickListener {
            submitFeedback(RecallQuality.DID_NOT_REMEMBER)
        }
        binding.buttonPartial.setOnClickListener {
            submitFeedback(RecallQuality.PARTIALLY_REMEMBERED)
        }
        binding.buttonFull.setOnClickListener {
            submitFeedback(RecallQuality.FULLY_REMEMBERED)
        }
    }

    private fun startRecallTimer() {
        stopRecallTimer()
        countdownTimer = object : CountDownTimer(60_000L, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textTimer.text = "Think: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                binding.textTimer.text = "Timer complete"
                Toast.makeText(this@RecallActivity, "Recall window complete", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun stopRecallTimer() {
        countdownTimer?.cancel()
        binding.textTimer.text = "Timer off"
    }

    private fun submitFeedback(quality: RecallQuality) {
        val errorNote = binding.inputRecallError.text.toString().trim()
        viewModel.submitFeedback(topicId, quality, errorNote) {
            finish()
        }
    }

    override fun onDestroy() {
        stopRecallTimer()
        super.onDestroy()
    }
}
