package br.edu.ifsp.aluno.gloriaporte.photos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.aluno.gloriaporte.photos.R
import br.edu.ifsp.aluno.gloriaporte.photos.adapter.PhotoAdapter
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

    companion object {
        const val PHOTOS_ENDPOINT = "https://jsonplaceholder.typicode.com/photos/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = "Photos"
        })

        amb.photosSp.adapter = photoAdapter
        retrievePhotos()
    }

    private fun retrievePhotos() {
        val photosConnection = URL(PHOTOS_ENDPOINT).openConnection() as HttpURLConnection

        try {
            if(photosConnection.responseCode == HTTP_OK) {
                InputStreamReader(photosConnection.inputStream).readText().let {
                    photoAdapter.addAll(Gson().fromJson(it, PhotoList::class.java).photos)
                }
            } else {
                Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
            }
        } catch (ioe: IOException) {
            Toast.makeText(this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show()
        } catch (jse: JsonSyntaxException) {
            Toast.makeText(this, getString(R.string.response_problem), Toast.LENGTH_SHORT).show()
        } finally {
            photosConnection.disconnect()
        }
    }
}