package com.example.arsen.sololearntask.utils


import com.google.gson.Gson
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun <T> ResponseBody.getDataList(clazz: Class<T>): List<*> {
    val responseString = this.string()
    val responseJson = JSONObject(responseString)

    if (responseJson.has("response")) {
        val responseObject = responseJson.getString("response")
        val resultJson = JSONObject(responseObject)
        val results = resultJson.getString("results")

        val result = Gson().fromJson(results, clazz)
        return (result as Array<*>).asList()
    }

    return listOf<T>()
}

suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            it.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                it.resume(response.body()!!)
            } else {
                it.resumeWithException(Throwable("Unsuccessful"))
            }
        }

    })
}

