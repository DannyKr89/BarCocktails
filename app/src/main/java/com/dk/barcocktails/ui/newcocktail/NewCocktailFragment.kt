package com.dk.barcocktails.ui.newcocktail

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentNewCocktailBinding
import com.dk.barcocktails.databinding.ItemIngredientBinding
import com.dk.barcocktails.domain.cocktails.state.LoadingState
import org.koin.android.ext.android.get
import kotlin.collections.set

class NewCocktailFragment : Fragment() {

    private var _binding: FragmentNewCocktailBinding? = null
    private val binding: FragmentNewCocktailBinding get() = _binding!!
    private var uriImage: String = ""
    private val viewModel: NewCocktailViewModel = get()
    private var ingredientViewId = 0
    private var name = ""
    private var method = ""
    private var garnier = ""
    private var description = ""
    private val ingredients = HashMap<String, Int>()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uriImage = uri.toString()
                binding.ivImageCocktail.imageTintMode = null
                binding.ivImageCocktail.load(uriImage)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewCocktailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()

    }

    private fun initViews() {
        with(binding) {
            addIngredientView()

            ivImageCocktail.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            fabAddIngredient.setOnClickListener {
                addIngredientView()
            }
            btnAddCocktail.setOnClickListener {
                name = etName.text.toString().trim()
                method = etMethod.text.toString().trim()
                garnier = etGarnier.text.toString().trim()
                description = etDescription.text.toString().trim()

                llIngredients.forEach {
                    val bindingItem = ItemIngredientBinding.bind(it)
                    with(bindingItem) {
                        val ingredientName = etIngredientName.text.toString().trim()
                        val ingredientValue = etIngredientValue.text.toString().trim()
                        if (name.isNotEmpty() && ingredientName.isNotEmpty() && ingredientValue.isNotEmpty()) {
                            ingredients[ingredientName] = ingredientValue.toInt()
                            if (uriImage.isNotEmpty()) {
                                uploadImage()
                            } else {
                                createCocktail()
                            }
                        } else {
                            etName.error = showError(name)
                            etIngredientName.error = showError(ingredientName)
                            etIngredientValue.error = showError(ingredientValue)
                        }
                    }
                }
            }
        }
    }

    private fun uploadImage() {
        val bitmap = MediaStore.Images.Media.getBitmap(
            requireActivity().contentResolver,
            Uri.parse(uriImage)
        )
        viewModel.loadImage(bitmap, name)
    }

    private fun showError(input: String): CharSequence? {
        return when (input.isEmpty()) {
            true -> resources.getString(R.string.require_field)
            false -> null
        }
    }

    private fun initViewModel() {
        viewModel.loadImageLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Error -> Toast.makeText(
                    requireContext(), state.error.message, Toast.LENGTH_SHORT
                ).show()

                is LoadingState.Loading -> {
                    imageLoadingVisibility(false, state.progress)
                }

                is LoadingState.Success -> {
                    imageLoadingVisibility(true, 100)
                    uriImage = state.data
                    createCocktail()
                }
            }
        }
        viewModel.addCocktailLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Error -> {
                    Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
                }

                is LoadingState.Loading -> {
                    showProgressBar(true)
                }

                is LoadingState.Success -> {
                    showProgressBar(false)
                    findNavController().popBackStack()
                }
            }
        }
    }
    private fun createCocktail() {
        with(binding) {
            ivImageCocktail.load(uriImage)
            viewModel.addCocktail(name, uriImage, ingredients, method, garnier, description)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        with(binding) {
            progressbar.isVisible = isLoading
        }
    }

    private fun imageLoadingVisibility(boolean: Boolean, progress: Long?) {
        with(binding) {
            btnAddCocktail.isEnabled = boolean
            progressImageLoading.isVisible = !boolean
            if (progress != null) {
                progressImageLoading.progress = progress.toInt()
            }
        }

    }

    private fun addIngredientView() {
        with(binding) {
            val ingredient = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_ingredient, llIngredients, false)
            val bindingItem = ItemIngredientBinding.bind(ingredient)
            bindingItem.ivDelete.setOnClickListener {
                llIngredients.removeView(ingredient)
            }
            ingredient.id = ingredientViewId++
            llIngredients.addView(ingredient)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}