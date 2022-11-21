package com.hasanbektas.chatapp

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hasanbektas.chatapp.databinding.FragmentChatBinding
import java.sql.Timestamp


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private lateinit var messageArrayList : ArrayList<ChatModel>

    private lateinit var chatAdapter : ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        auth = Firebase.auth
        firestore = Firebase.firestore

        messageArrayList = ArrayList<ChatModel>()

        loadData()
        
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recyclerViewFragment.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)
        chatAdapter = ChatAdapter(messageArrayList)
        binding.recyclerViewFragment.adapter = chatAdapter

        binding.sendButton.setOnClickListener {

            if(auth.currentUser != null){

                val messagePost = hashMapOf<String , Any>()

                messagePost.put("userEmail",auth.currentUser!!.email!!)
                messagePost.put("messages",binding.Message.text.toString())
                messagePost.put("date",com.google.firebase.Timestamp.now())

                firestore.collection("MessagePost").add(messagePost).addOnSuccessListener {

                    binding.Message.text.clear()

                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.signout){

            auth.signOut()

            val action = ChatFragmentDirections.actionChatFragmentToLoginFragment()
            view?.let { Navigation.findNavController(it).navigate(action) }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        firestore.collection("MessagePost").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()

            }else{
                if (value != null){
                    if (!value.isEmpty){

                        val documents = value.documents

                        messageArrayList.clear()

                        for (document in documents){

                            val email = document.get("userEmail") as String
                            val message = document.get("messages") as String

                            val imessage = ChatModel(email,message)
                            messageArrayList.add(imessage)

                        }

                        chatAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}