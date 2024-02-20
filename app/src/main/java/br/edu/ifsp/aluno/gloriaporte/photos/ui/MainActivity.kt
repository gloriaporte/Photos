package br.edu.ifsp.aluno.gloriaporte.photos.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.aluno.gloriaporte.photos.R
import br.edu.ifsp.aluno.gloriaporte.photos.adapter.PhotoAdapter
import br.edu.ifsp.aluno.gloriaporte.photos.adapter.PhotoImageAdapter
import br.edu.ifsp.aluno.gloriaporte.photos.databinding.ActivityMainBinding
import br.edu.ifsp.aluno.gloriaporte.photos.model.Api
import br.edu.ifsp.aluno.gloriaporte.photos.model.Photo
import br.edu.ifsp.aluno.gloriaporte.photos.model.PhotoList
import com.android.volley.toolbox.ImageRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val photoList: MutableList<Photo> = mutableListOf()
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(this, photoList)
    }

    private val photoImageList: MutableList<Bitmap> = mutableListOf()
    private val photoImageAdapter: PhotoImageAdapter by lazy {
        PhotoImageAdapter(this, photoImageList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = context.getString(R.string.photos)
        })

        amb.photosSp.apply {
            adapter = photoAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val size = photoImageList.size
                    photoImageAdapter.clear()
                    photoImageAdapter.notifyItemRangeRemoved(0, size)

                    retrievePhotos(photoList[position].url, amb.imagePhoto)
                    retrievePhotos(photoList[position].thumbnailUrl, amb.imageThumbnail)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }

        retrievePhotos()
    }

    private fun retrievePhotos() = Api.PhotoListRequest(
        { photoList ->
            photoList.also {
                photoAdapter.addAll(it)
            }
        },
        {
            Toast.makeText(this, R.string.request_problem, Toast.LENGTH_SHORT).show()
        }).also {
            Api.getInstance(this).addToRequestQueue(it)
    }

    private fun retrievePhotosImages(imageUrl: String, view: ImageView) =
        ImageRequest(imageUrl,
            { response ->
                view.setImageBitmap(response)
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, {
                Toast.makeText(this, R.string.request_problem, Toast.LENGTH_SHORT).show()
            }).also {
                Api.getInstance(this).addToRequestQueue(it)
        }
}