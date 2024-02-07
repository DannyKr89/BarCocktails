package com.dk.barcocktails.ui.newcocktail

import android.database.Cursor
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.dk.barcocktails.domain.cocktails.LoadingState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.set

class NewCocktailFragment : Fragment() {

    private var _binding: FragmentNewCocktailBinding? = null
    private val binding: FragmentNewCocktailBinding get() = _binding!!
    private lateinit var uriImage: String
    private val viewModel: NewCocktailViewModel by viewModel()

    private var ingredientViewId = 0

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                parseUri(uri)
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
                val name = etName.text.toString()
                val method = etMethod.text.toString()
                val garnier = etGarnier.text.toString()
                val ingredients = HashMap<String, Int>()

                llIngredients.forEach {
                    val bindingItem = ItemIngredientBinding.bind(it)
                    with(bindingItem) {
                        val ingredientName = etIngredientName.text.toString()
                        val ingredientValue = etIngredientValue.text.toString()
                        if (name.isEmpty()) {
                            etName.error = "Required"
                        } else if (ingredientName.isEmpty()) {
                            etIngredientName.error = "Required"
                        } else if (ingredientValue.isEmpty()) {
                            etIngredientValue.error = "Required"
                        } else {
                            ingredients[ingredientName] = ingredientValue.toInt()
                        }
                    }
                }
                viewModel.addCocktail(name, uriImage, ingredients, method, garnier)
            }
        }
    }

    private fun initViewModel() {
        viewModel.loadImageLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Error -> Toast.makeText(
                    requireContext(),
                    state.error.message,
                    Toast.LENGTH_SHORT
                ).show()

                LoadingState.Loading -> {
                    imageLoadingVisibility(0f, false)
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }

                is LoadingState.Success -> {
                    imageLoadingVisibility(1f, true)
                    uriImage = state.data
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.addCocktailLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Error -> {}
                LoadingState.Loading -> {}
                is LoadingState.Success -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun parseUri(uri: Uri) {
        binding.ivImageCocktail.load(uri)
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            requireActivity().getContentResolver().query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(column_index)
            }
        }
        cursor?.close()
        viewModel.loadImage(res ?: "")
        Log.d("PhotoPicker", "Selected URI: $res")
    }

    private fun imageLoadingVisibility(saturation: Float, boolean: Boolean) {
        val matrix = ColorMatrix()
        matrix.setSaturation(saturation)
        val filter = ColorMatrixColorFilter(matrix)
        binding.ivImageCocktail.colorFilter = filter
        binding.btnAddCocktail.isEnabled = boolean
        binding.progressImageLoading.isVisible = !boolean
    }

    private fun addIngredientView() {
        with(binding) {
            val ingredient = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_ingredient, llIngredients, false)
            val bindingItem = ItemIngredientBinding.bind(ingredient)
            bindingItem.ivDelete.setOnClickListener {
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show()
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