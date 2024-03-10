package com.example.welcome_freshman.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.welcome_freshman.UserPreferences

import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 *@date 2024/3/10 14:37
 *@author GFCoder
 */
class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
 override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

 override suspend fun readFrom(input: InputStream): UserPreferences =
  try {
   // readFrom is already called on the data store background thread
   UserPreferences.parseFrom(input)
  } catch (exception: InvalidProtocolBufferException) {
   throw CorruptionException("Cannot read proto.", exception)
  }

 override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
  // writeTo is already called on the data store background thread
  t.writeTo(output)
 }
}