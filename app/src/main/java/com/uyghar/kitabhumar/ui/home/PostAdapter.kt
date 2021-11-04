package com.uyghar.kitabhumar.ui.home

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uyghar.kitabhumar.MainActivity
import com.uyghar.kitabhumar.databinding.PostItemBinding
import com.uyghar.kitabhumar.models.Post

class PostAdapter(val context: Context, val post_array: Array<Post>): RecyclerView.Adapter<PostAdapter.PostHolder>() {
    inner class PostHolder(val postItemBinding: PostItemBinding): RecyclerView.ViewHolder(postItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val postItemBinding = PostItemBinding.inflate((context as MainActivity).layoutInflater,parent,false)
        return PostHolder(postItemBinding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = post_array.get(position)
//        val user_image_id = context.resources.getIdentifier(post.user_image,"drawable",context.packageName)
//        val book_image_id = context.resources.getIdentifier(post.book_image,"drawable",context.packageName)
//        holder.postItemBinding.userImage.setImageResource(user_image_id)
//        holder.postItemBinding.bookImage.setImageResource(book_image_id)
        Picasso.get().load(post.user?.image).into(holder.postItemBinding.userImage)
        Picasso.get().load(post.book?.image).into(holder.postItemBinding.bookImage)
        holder.postItemBinding.bookText.setText(post.book?.title + "\n" + post.book?.author?.name + " " + post.book?.author?.surname)
        holder.postItemBinding.postDate.setText(post.updated_at)
        holder.postItemBinding.userName.setText(post.user?.nickname + " " + post.post_category?.title)
        holder.postItemBinding.postText.setText(post.content)

    }

    override fun getItemCount(): Int {
        return post_array.size
    }
}