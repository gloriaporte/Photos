package br.edu.ifsp.aluno.gloriaporte.photos.model

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import java.net.HttpURLConnection.HTTP_NOT_MODIFIED
import java.net.HttpURLConnection.HTTP_OK

class Api (context: Context){
    companion object {
        const val PHOTOS_ENDPOINT = "https://jsonplaceholder.typicode.com/photos"

        @Volatile
        private var INSTANCE: Api? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Api(context).also {
                INSTANCE = it
            }
        }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun<T> addToRequestQueue(request: Request<T>) {
        requestQueue.add((request))
    }

    class PhotoListRequest(
        private val responseListener: Response.Listener<List<Photo>>, // Agora esperamos uma lista de Photo
        errorListener: Response.ErrorListener
    ) : Request<List<Photo>>(Method.GET, PHOTOS_ENDPOINT, errorListener) {

        override fun parseNetworkResponse(response: NetworkResponse?): Response<List<Photo>> =
            if (response?.statusCode == HTTP_OK || response?.statusCode == HTTP_NOT_MODIFIED) {
                val jsonArray = JSONArray(String(response.data))
                val photoList = mutableListOf<Photo>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val photo = Gson().fromJson(jsonObject.toString(), Photo::class.java)
                    photoList.add(photo)
                }
                Response.success(
                    photoList,
                    HttpHeaderParser.parseCacheHeaders(response)
                )
            } else {
                Response.error(VolleyError())
            }

        override fun deliverResponse(response: List<Photo>?) {
            responseListener.onResponse(response)
        }
    }
}