package com.dk.barcocktails.ui.newcocktail

import android.database.Cursor
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.dk.barcocktails.AddCocktailService
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentNewCocktailBinding
import com.dk.barcocktails.databinding.ItemIngredientBinding
import kotlin.collections.set

class NewCocktailFragment : Fragment() {

    private var _binding: FragmentNewCocktailBinding? = null
    private val binding: FragmentNewCocktailBinding get() = _binding!!
    private lateinit var uriImage: String

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
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
                uriImage = res ?: ""
                Log.d("PhotoPicker", "Selected URI: $res")
            } else {
                Log.d("PhotoPicker", "No media selected")
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

    }

    private fun initViews() {
        with(binding) {
            addView()

            ivImageCocktail.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            fabAddIngredient.setOnClickListener {
                addView()
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
                        if (name.isNullOrEmpty()) {
                            etName.error = "Required"
                        } else if (ingredientName.isNullOrEmpty()) {
                            etIngredientName.error = "Required"
                        } else if (ingredientValue.isNullOrEmpty()) {
                            etIngredientValue.error = "Required"
                        } else {
                            ingredients[ingredientName] = ingredientValue.toInt()
                        }
                    }
                }

                requireActivity().startService(
                    AddCocktailService.newIntent(
                        requireContext(), name, uriImage, ingredients, method, garnier
                    )
                )
                findNavController().popBackStack()

            }
        }
    }

    private fun addView() {
        var id = 0
        with(binding) {
            val ingredient = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_ingredient, llIngredients, false)
            val bindingItem = ItemIngredientBinding.bind(ingredient)
            bindingItem.ivDelete.setOnClickListener {
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show()
                llIngredients.removeView(ingredient)
            }

            ingredient.id = id++
            llIngredients.addView(ingredient)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}