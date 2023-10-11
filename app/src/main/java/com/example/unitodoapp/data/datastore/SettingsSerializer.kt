package com.example.unitodoapp.data.datastore

import android.util.Log
import androidx.datastore.core.Serializer
import com.example.unitodoapp.ui.screens.settings.SettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsState> {
    override val defaultValue: SettingsState
        get() = SettingsState()

    override suspend fun readFrom(input: InputStream): SettingsState {
        return try {
            Json.decodeFromString(
                deserializer = SettingsState.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Log.e("datastore", "An error occurred while loading data")
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsState, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = SettingsState.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
