package com.hasanbektas.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hasanbektas.chatapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth :FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val currentuser = auth.currentUser

        if (currentuser != null){

            val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val passaword = binding.passawordEditText.text.toString()

            if (email.isNotEmpty() && passaword.isNotEmpty()){

                auth.signInWithEmailAndPassword(email,passaword).addOnSuccessListener { result ->

                    val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                    Navigation.findNavController(it).navigate(action)

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }else{

                Toast.makeText(requireContext(),"Email ve Passaword giriniz",Toast.LENGTH_LONG).show()
            }
        }

        binding.signButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val passaword = binding.passawordEditText.text.toString()

            if(email.isNotEmpty() && passaword.isNotEmpty()){

                auth.createUserWithEmailAndPassword(email,passaword).addOnSuccessListener { result ->

                    val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                    Navigation.findNavController(it).navigate(action)

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(requireActivity(),"Email ve passaword giriniz",Toast.LENGTH_LONG).show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

}