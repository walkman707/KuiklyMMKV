/*
 * Copyright (C) 2022 Ctrip.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuikly.thirdparty.kmp.lib.mmkv

/**
 * MMKV common source set expect class
 * 
 */

interface MMKV_KMP {

    /**
     * Write value
     */

    operator fun set(key: String, value: String): Boolean

    operator fun set(key: String, value: Boolean): Boolean

    operator fun set(key: String, value: Int): Boolean

    operator fun set(key: String, value: Long): Boolean

    operator fun set(key: String, value: Float): Boolean

    operator fun set(key: String, value: Double): Boolean

    operator fun set(key: String, value: UInt): Boolean

    operator fun set(key: String, value: ULong): Boolean

    operator fun set(key: String, value: ByteArray): Boolean

    operator fun set(key: String, value: Set<String>?): Boolean

    /**
     * Read value
     */

    fun getString(key: String, default: String = ""): String

    fun getBoolean(key: String, default: Boolean = false): Boolean

    fun getInt(key: String, default: Int = 0): Int

    fun getLong(key: String, default: Long = 0): Long

    fun getFloat(key: String, default: Float = 0f): Float

    fun getDouble(key: String, default: Double = 0.0): Double

    fun getUInt(key: String, default: UInt = 0u): UInt

    fun getULong(key: String, default: ULong = 0u): ULong

    fun getByteArray(key: String, default: ByteArray? = null): ByteArray?

    fun getStringSet(key: String, default: Set<String>? = null): Set<String>?

    /**
     * Remove value
     */

    fun removeValueForKey(key: String)

    fun removeValuesForKeys(keys: List<String>)

    /**
     * Size
     */

    val actualSize: Long

    val count: Long

    val totalSize: Long

    /**
     * Clear
     */

    fun clearMemoryCache()

    fun clearAll()

    /**
     * Other
     */

    fun close()

    fun allKeys(): List<String>

    fun containsKey(key: String): Boolean

    fun checkReSetCryptKey(key: String?)

    fun mmapID(): String

    fun async()

    fun sync()

    fun trim()
}