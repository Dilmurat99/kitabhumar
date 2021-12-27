package com.uyghar.kitabhumar.ui.home

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.uyghar.kitabhumar.MainActivity
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.databinding.PostItemBinding
import com.uyghar.kitabhumar.models.Post

class PostAdapter(val context: Context, val fragment: Fragment, val post_array: Array<Post>): RecyclerView.Adapter<PostAdapter.PostHolder>() {
    inner class PostHolder(val postItemBinding: PostItemBinding): RecyclerView.ViewHolder(postItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val postItemBinding = PostItemBinding.inflate((context as MainActivity).layoutInflater,parent,false)
        return PostHolder(postItemBinding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = post_array.get(position)
        Picasso.get().load(post.user?.image).into(holder.postItemBinding.userImage)
        Picasso.get().load(post.book?.image).into(holder.postItemBinding.bookImage)
        holder.postItemBinding.bookText.setText(post.book?.title + "\n" + post.book?.author?.name + " " + post.book?.author?.surname)
        holder.postItemBinding.postDate.setText(post.updated_at)
        holder.postItemBinding.userName.setText(post.user?.nickname + " " + post.post_category?.title)
        holder.postItemBinding.postText.setText(post.content)
        holder.postItemBinding.root.setOnClickListener {
            val navController = fragment.findNavController()
            val bundle = Bundle()
            bundle.putParcelable("post", post)
            navController.navigate(R.id.detailFragment, bundle)
        }
        holder.postItemBinding.buttonLike.setOnClickListener {
            holder.postItemBinding.buttonLike.isSelected = !holder.postItemBinding.buttonLike.isSelected
            Snackbar.make(holder.itemView,post.id.toString(),Snackbar.LENGTH_LONG).show()
        }

    }

    override fun getItemCount(): Int {
        return post_array.size
    }
}