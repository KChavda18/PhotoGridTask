package com.dev.photogridtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dev.photogridtask.databinding.GridItemBinding

class PhotoListAdapter(private val photoList: List<PhotoGrid>) :
    RecyclerView.Adapter<PhotoListAdapter.PhotoItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoListAdapter.PhotoItemViewHolder {
        return PhotoItemViewHolder(
            GridItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoListAdapter.PhotoItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PhotoItemViewHolder(private val binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                txtTitle.text = photoList[position].title.ifEmpty { "" }

                Glide.with(binding.root.context)
                    .load(photoList[position].thumbnailUrl)
                    .apply(
                        RequestOptions().centerCrop().disallowHardwareConfig().diskCacheStrategy(
                            DiskCacheStrategy.AUTOMATIC
                        ).dontAnimate().placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder)
                    )
                    .into(photoImage)
            }
        }
    }
}