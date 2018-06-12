package com.example.arsen.sololearntask.job

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.example.arsen.sololearntask.R
import com.example.arsen.sololearntask.api.ApiManager
import com.example.arsen.sololearntask.model.NewsResult
import io.realm.Realm
import kotlinx.coroutines.experimental.async
import android.support.v4.app.NotificationManagerCompat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        async {
            val realm = Realm.getDefaultInstance()
            val newsResult = realm.where(NewsResult::class.java).findAll()
            try {
                val response = ApiManager.getNews(1)
                if (response[0].id != newsResult[0]!!.id) {

                    val notification = NotificationCompat.Builder(context)
                            .setContentTitle("SoloLearn News")
                            .setContentText("You Have New Available News For You")
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setShowWhen(true)
                            .setColor(Color.RED)
                            .setLocalOnly(true)
                            .build()

                    NotificationManagerCompat.from(context)
                            .notify(Random().nextInt(), notification)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }
}