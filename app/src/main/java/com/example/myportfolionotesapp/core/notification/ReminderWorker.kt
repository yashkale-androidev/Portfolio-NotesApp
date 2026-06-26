package com.example.myportfolionotesapp.core.notification


import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myportfolionotesapp.MainActivity
import kotlin.jvm.java

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_NOTE_ID = "key_note_id"
        const val KEY_NOTE_TITLE = "key_note_title"
        const val KEY_NOTE_CONTENT = "key_note_content"
        const val CHANNEL_ID = "notes_reminder_channel"
        private const val NOTIFICATION_ID_OFFSET = 1000
    }

    override suspend fun doWork(): Result {
        val noteId = inputData.getLong(KEY_NOTE_ID, -1L)
        val noteTitle = inputData.getString(KEY_NOTE_TITLE) ?: "Note Reminder"
        val noteContent = inputData.getString(KEY_NOTE_CONTENT) ?: "You scheduled a reminder for this note."

        if (noteId == -1L) return Result.failure()

        sendNotification(noteId, noteTitle, noteContent)
        return Result.success()
    }

    private fun sendNotification(noteId: Long, title: String, content: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Note Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for notes with scheduled reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("NOTE_ID", noteId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            noteId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(noteId.toInt() + NOTIFICATION_ID_OFFSET, notification)
    }
}
