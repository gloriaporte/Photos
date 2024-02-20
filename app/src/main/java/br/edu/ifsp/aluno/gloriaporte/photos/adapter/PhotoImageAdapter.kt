package br.edu.ifsp.aluno.gloriaporte.photos.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.aluno.gloriaporte.photos.databinding.ActivityMainBinding
import br.edu.ifsp.aluno.gloriaporte.photos.databinding.TilePhotoImageBinding

class PhotoImageAdapter(val activityContext: Context, val photoImageList: MutableList<Bitmap>):
RecyclerView.Adapter<PhotoImageAdapter.PhotoImageViewHolder>() {

    inner class PhotoImageViewHolder(tpib: TilePhotoImageBinding): RecyclerView.ViewHolder(tpib.photoIv) {
        val photoIv: ImageView = tpib.photoIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoImageViewHolder =
        PhotoImageViewHolder(TilePhotoImageBinding.inflate(LayoutInflater.from(activityContext), parent, false))


    override fun onBindViewHolder(holder: PhotoImageViewHolder, position: Int) =
        holder.photoIv.setImageBitmap(photoImageList[position])


    override fun getItemCount(): Int = photoImageList.size
}