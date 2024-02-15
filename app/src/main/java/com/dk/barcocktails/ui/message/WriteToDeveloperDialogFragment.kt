package com.dk.barcocktails.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dk.barcocktails.databinding.DialogFragmentWriteToDeveloperBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteToDeveloperDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentWriteToDeveloperBinding? = null
    private val binding: DialogFragmentWriteToDeveloperBinding get() = _binding!!
    private val viewModel: WriteToDeveloperViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogFragmentWriteToDeveloperBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initView() {
        with(binding) {
            btnSend.setOnClickListener {
                val message = etMessage.text.toString()
                if (message.isNotEmpty()) {
                    viewModel.sendMessage(message)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    dialog?.dismiss()
                }

                false -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}