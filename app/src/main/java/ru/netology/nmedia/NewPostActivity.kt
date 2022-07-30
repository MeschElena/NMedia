package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent ?: return

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (text.isNullOrBlank()) {
            binding.edit.requestFocus()
        } else {
            binding.edit.setText(text)
        }

        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.edit.text?.toString())
        }
    }

    private fun onOkButtonClicked(postContent: String?) {

        if (postContent.isNullOrBlank()) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            val resultIntent = Intent()
            resultIntent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }

    private companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContract: ActivityResultContract<Unit, String?>() {

        override fun createIntent(context: Context, input: Unit): Intent =
            Intent(context, NewPostActivity::class.java)


        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)

        }

    }

    object ResultContractEdit: ActivityResultContract<String, String?>() {

        override fun createIntent(context: Context, input: String): Intent =
            Intent(context, NewPostActivity::class.java).putExtra(Intent.EXTRA_TEXT,input)


        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)

        }

    }

}