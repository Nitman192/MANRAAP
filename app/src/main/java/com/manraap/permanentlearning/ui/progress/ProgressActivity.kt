package com.manraap.permanentlearning.ui.progress

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manraap.permanentlearning.databinding.ActivityProgressBinding
import com.manraap.permanentlearning.util.AppViewModelFactory
import com.manraap.permanentlearning.util.Injector

class ProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding
    private val viewModel: ProgressViewModel by viewModels {
        AppViewModelFactory(Injector.provideRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.shortTerm.observe(this) {
            binding.textShortTerm.text = "Short-term memory topics: $it"
        }
        viewModel.longTerm.observe(this) {
            binding.textLongTerm.text = "Long-term memory topics (>=30 days): $it"
        }
        viewModel.weakTopics.observe(this) {
            binding.textWeak.text = "Weak topics (recent failed recall): $it"
        }
    }
}
