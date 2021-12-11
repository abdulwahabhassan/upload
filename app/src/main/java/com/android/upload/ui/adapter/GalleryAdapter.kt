package com.android.upload.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.upload.databinding.ItemStoreImageBinding
import com.android.upload.domain.entity.MediaStoreImage
import com.bumptech.glide.Glide

class GalleryAdapter(private val onClick: (MediaStoreImage) -> Unit)
    : ListAdapter<MediaStoreImage, GalleryAdapter.ImageViewHolder>(MediaStoreImage.DiffCallback) {

    inner class ImageViewHolder(private val itemBinding : ItemStoreImageBinding, onClick: (MediaStoreImage) -> Unit)
        : RecyclerView.ViewHolder(itemBinding.root){

        init {
            itemBinding.imageView.setOnClickListener {
                val image = itemBinding.root.tag as? MediaStoreImage ?: return@setOnClickListener
                onClick(image)
            }
        }

            fun bind(item: MediaStoreImage, viewHolder: ImageViewHolder) = with(itemBinding) {

                root.tag = item

                Glide.with(itemBinding.root.context)
                    .load(item.contentUri)
                    .thumbnail(0.33f)
                    .centerCrop()
                    .into(imageView)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val viewBinding = ItemStoreImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(viewBinding, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position), holder)
    }
}