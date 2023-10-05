package com.example.bangkitnavigationapi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitnavigationapi.R
import com.example.bangkitnavigationapi.data.local.entity.GithubUserItemEntity
import com.example.bangkitnavigationapi.databinding.ItemGithubUserBinding

class GithubUserAdapter(
    private val onFavoriteClick: (GithubUserItemEntity) -> Unit,
    private val onItemClick: (username: String) -> Unit
) :
    ListAdapter<GithubUserItemEntity, GithubUserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val githubUserList = getItem(position)
        holder.bind(githubUserList)

        val ivFavorite = holder.binding.ivFavorite

        if (githubUserList.isFavorited) ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorited))
        else ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_not_favorited))

        ivFavorite.setOnClickListener {
            onFavoriteClick(githubUserList)
        }

        holder.binding.root.setOnClickListener {
            onItemClick(githubUserList.username)
        }
    }

    inner class UserViewHolder(val binding: ItemGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(githubUserModel : GithubUserItemEntity) {
            Glide.with(itemView)
                .load(githubUserModel.avatarUrl)
                .into(binding.profPic)

            binding.Name.text = githubUserModel.username
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GithubUserItemEntity> =
            object : DiffUtil.ItemCallback<GithubUserItemEntity>() {
                override fun areItemsTheSame(oldItem: GithubUserItemEntity, newItem: GithubUserItemEntity): Boolean {
                    return oldItem.username == newItem.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: GithubUserItemEntity, newItem: GithubUserItemEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}