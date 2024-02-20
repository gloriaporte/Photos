package br.edu.ifsp.aluno.gloriaporte.photos.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.aluno.gloriaporte.photos.R
import br.edu.ifsp.aluno.gloriaporte.photos.adapter.PhotoAdapter
import br.edu.ifsp.aluno.gloriaporte.photos.adapter.PhotoImageAdapter
import br.edu.ifsp.aluno.gloriaporte.photos.databinding.ActivityMainBinding
import br.edu.ifsp.aluno.gloriaporte.photos.model.Photo
import br.edu.ifsp.aluno.gloriaporte.photos.model.PhotoList
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL

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

    companion object {
        const val PHOTOS_ENDPOINT = "https://jsonplaceholder.typicode.com/photos/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = "Photos"
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
                    TODO("Not yet implemented")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }

        amb.imagePhoto.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = photoImageAdapter
        }

        retrievePhotos()
    }

    private fun retrievePhotos() {
        Thread {
            val photosConnection = URL(PHOTOS_ENDPOINT).openConnection() as HttpURLConnection

            try {
                if (photosConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val responseText = InputStreamReader(photosConnection.inputStream).readText()
                    runOnUiThread {
                        try {
                            val photoArray = Gson().fromJson(responseText, Array<Photo>::class.java)
                            val photoList = photoArray.toList()
                            photoAdapter.addAll(photoList)
                        } catch (e: JsonSyntaxException) {
                            Toast.makeText(this, getString(R.string.response_problem), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (ioe: IOException) {
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show()
                }
            } finally {
                photosConnection.disconnect()
            }
        }.start()
    }

}