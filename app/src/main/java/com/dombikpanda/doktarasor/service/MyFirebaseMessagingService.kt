package com.dombikpanda.doktarasor.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.dombikpanda.doktarasor.ui.view.activity.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyFirebaseMessagingService : Service() {

    private val channelId = "notification_channel"
    private val channelName = "com.dombikpanda.doktarasor.service"
    private val notificationTitle = "Sorunuz Cevaplanmıştır"
    private val notificationMessage =
        "Doktor tarafından sorunuz cevaplanmıştır.Görmek için tıklayınız"
    private var importance = 0
    private var notifManagerId = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        questionNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate() {
        createNotifChannel()
        super.onCreate()
    }

    private val crudRepository = CrudRepository()
    private fun questionNotification() {
        /*val shared = getSharedPreferences("kontrol", MODE_PRIVATE)
        val control = shared.getLong("date", 0)*/
        /*val collection = Firebase.firestore.collection("questions")
        collection
            .addSnapshotListener { value, error ->
                error.let {

                }
                value?.let { result ->
                    for (document in result) {
                        if (document["userid"] == crudRepository.getFirebaseAuth().uid) {
                            if (document["cevapdurum"] == true && document["messageDurum"] == true *//*&& document["date"] == control*//*) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    createNotification()
                                }
                                break
                            }
                        }
                    }
                }
            }*/
    }


    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_HIGH //normal high
            val notificationChannel =
                NotificationChannel(channelId, channelName, importance).apply {
                    description = notificationMessage
                }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(notifManagerId, notification.build())
            notifManagerId++
        }
        parmanentNotification()
    }

    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    private fun parmanentNotification() {
        val notification=NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Uygulama çalışıyor")
            .setContentText("Arka planda uygulama servisi çalışıyor")
            .build()
        startForeground(1,notification)
    }
}




