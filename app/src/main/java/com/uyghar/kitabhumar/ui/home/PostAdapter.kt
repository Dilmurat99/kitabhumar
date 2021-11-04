package com.uyghar.kitabhumar.ui.home

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uyghar.kitabhumar.MainActivity
import com.uyghar.kitabhumar.databinding.PostItemBinding
import com.uyghar.kitabhumar.models.Post

class PostAdapter(val context: Context): RecyclerView.Adapter<PostAdapter.PostHolder>() {
    val post1 = Post(1,"Esil kitabken", "2021-11-03","abdussalam","ereb","Abduhelil","tewsiye","Ereb-islam ellirining ilim-pen-ijadiyet qudriti","Abdussalam")
    val post2 = Post(2,"Nochi kitabken", "2021-11-02","otkur","iz","Ibrahim","tesirat","Iz","Abdurehim Otkur")
    val post3 = Post(3,"Peyzi kitabken", "2021-11-01","george","qoruq","Abdugheni","tewsiye","Haywanlar qoruqi","George Orwell")
    val post_array = arrayOf(post1,post2,post3)

    inner class PostHolder(val postItemBinding: PostItemBinding): RecyclerView.ViewHolder(postItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val postItemBinding = PostItemBinding.inflate((context as MainActivity).layoutInflater,parent,false)
        return PostHolder(postItemBinding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = post_array.get(position)
        val user_image_id = context.resources.getIdentifier(post.user_image,"drawable",context.packageName)
        val book_image_id = context.resources.getIdentifier(post.book_image,"drawable",context.packageName)
        holder.postItemBinding.userImage.setImageResource(user_image_id)
        holder.postItemBinding.bookImage.setImageResource(book_image_id)
        holder.postItemBinding.bookText.setText(post.book_name + "/" + post.author_name)
        holder.postItemBinding.postDate.setText(post.post_date)
        holder.postItemBinding.userName.setText(post.user_name + " " + post.cat)
        holder.postItemBinding.postText.setText(post.content)

    }

    override fun getItemCount(): Int {
        return post_array.size
    }
}