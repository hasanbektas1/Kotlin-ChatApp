package com.hasanbektas.chatapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hasanbektas.chatapp.databinding.RecyclerRowBinding

class ChatAdapter(private val chatList: ArrayList<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ChatHolder>(){

    class ChatHolder(val binding : RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

        holder.binding.recyclerViewEmailText.text = chatList.get(position).emailModel
        holder.binding.recyclerViewChatText.text = chatList.get(position).messageModel

    }

    override fun getItemCount(): Int {

        return chatList.size
    }
}