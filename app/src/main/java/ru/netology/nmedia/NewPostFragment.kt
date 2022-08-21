package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {

//    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )

    private val args by navArgs<NewPostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewPostBinding.inflate(inflater, container, false).also {binding ->
      //  arguments?.textArg?.let (binding.edit::setText)

        binding.edit.requestFocus()
        binding.edit.setText(args.initialContent)
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
            binding.edit.hideKeyboard()
            }
    }.root


    private fun onOkButtonClicked(binding: FragmentNewPostBinding) {
        val text = binding.edit.text

        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(REQUEST_KEY,resultBundle)
        }
        findNavController().popBackStack()
    }


    companion object {
        const val REQUEST_KEY = "requestkey"
        const val RESULT_KEY = "postContent"


        //var Bundle.textArg: String? by StringArg


    }

}