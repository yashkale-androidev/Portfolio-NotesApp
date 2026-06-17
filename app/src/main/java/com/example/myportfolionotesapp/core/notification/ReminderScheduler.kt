package com.example.myportfolionotesapp.core.notification

import android.content.Context
import androidx.work.*
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class ReminderScheduler(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(noteId: Long, title: String, content: String, triggerTimeMillis: Long) {
        val delayMillis = triggerTimeMillis - System.currentTimeMillis()
        if (delayMillis <= 0) return

        // Cancel existing reminder if any
        cancelReminder(noteId)

        val data = Data.Builder()
            .putLong(ReminderWorker.KEY_NOTE_ID, noteId)
            .putString(ReminderWorker.KEY_NOTE_TITLE, title)
            .putString(ReminderWorker.KEY_NOTE_CONTENT, content)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .addTag("note_reminder_$noteId")
            .build()

        workManager.enqueueUniqueWork(
            "unique_note_reminder_$noteId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(noteId: Long) {
        workManager.cancelUniqueWork("unique_note_reminder_$noteId")
        workManager.cancelAllWorkByTag("note_reminder_$noteId")
    }
}
