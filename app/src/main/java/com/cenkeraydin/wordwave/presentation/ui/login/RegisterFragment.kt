package com.cenkeraydin.wordwave.presentation.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.databinding.FragmentRegisterBinding
import com.cenkeraydin.wordwave.presentation.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val userName = binding.etUserName.text.toString()
            val email = binding.etEmailRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()
            val confirmPassword = binding.etPasswordConfirm.text.toString()

            if (fullName.isBlank() || userName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Full Name, username, email and password can not be empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords doesn't match", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            binding.etPasswordRegister.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (binding.etPasswordRegister.right - binding.etPasswordRegister.compoundDrawables[2].bounds.width())) {
                        togglePasswordVisibilityFirst()
                        return@setOnTouchListener true
                    }
                }
                false
            }

            binding.etPasswordConfirm.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (binding.etPasswordConfirm.right - binding.etPasswordConfirm.compoundDrawables[2].bounds.width())) {
                        togglePasswordVisibilityConfirm()
                        return@setOnTouchListener true
                    }
                }
                false
            }

            registerUser(email, password, userName, fullName)
        }

        binding.tvToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun registerUser(email: String, password: String, userName: String, fullName: String) {
        authViewModel.register(email, password, userName,fullName) { isSuccess, message ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Register Successful", Toast.LENGTH_SHORT).show()
                navigateToHomeOnRegister()
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomeOnRegister() {
        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
    }

    private fun togglePasswordVisibilityFirst() {
        if (isPasswordVisible) {

            binding.etPasswordRegister.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etPasswordRegister.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.baseline_visibility_24,
                0
            )
        } else {

            binding.etPasswordRegister.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etPasswordRegister.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.baseline_visibility_off_24,
                0
            )
        }

        binding.etPasswordRegister.setSelection(binding.etPasswordRegister.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun togglePasswordVisibilityConfirm() {
        if (isPasswordVisible) {

            binding.etPasswordConfirm.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etPasswordConfirm.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.baseline_visibility_24,
                0
            )
        } else {

            binding.etPasswordConfirm.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etPasswordConfirm.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.baseline_visibility_off_24,
                0
            )
        }

        binding.etPasswordConfirm.setSelection(binding.etPasswordConfirm.text.length)
        isPasswordVisible = !isPasswordVisible
    }

}