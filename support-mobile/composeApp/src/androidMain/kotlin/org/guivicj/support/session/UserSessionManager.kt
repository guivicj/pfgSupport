package org.guivicj.support.session

import android.content.Context
import androidx.core.content.edit

object UserSessionManager {
    private const val PREF_NAME = "session"
    private const val USER_ID_KEY = "user_id"

    fun saveUserId(context: Context, userId: Long) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit() {
                putLong(USER_ID_KEY, userId)
            }
    }

    fun getUserId(context: Context): Long? {
        val id = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getLong(USER_ID_KEY, -1L)
        return if (id != -1L) id else null
    }
}