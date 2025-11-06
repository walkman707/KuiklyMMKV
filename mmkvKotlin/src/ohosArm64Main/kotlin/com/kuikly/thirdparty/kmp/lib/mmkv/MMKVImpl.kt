package com.kuikly.thirdparty.kmp.lib.mmkv

import com.tencent.mmkv.mmkv_c_actual_size
import com.tencent.mmkv.mmkv_c_all_keys
import com.tencent.mmkv.mmkv_c_check_reset_crypt_key
import com.tencent.mmkv.mmkv_c_clear_all
import com.tencent.mmkv.mmkv_c_clear_memory_cache
import com.tencent.mmkv.mmkv_c_close
import com.tencent.mmkv.mmkv_c_contains_key
import com.tencent.mmkv.mmkv_c_count
import com.tencent.mmkv.mmkv_c_default_mmkv
import com.tencent.mmkv.mmkv_c_get_bool
import com.tencent.mmkv.mmkv_c_get_byte_array
import com.tencent.mmkv.mmkv_c_get_double
import com.tencent.mmkv.mmkv_c_get_float
import com.tencent.mmkv.mmkv_c_get_int32
import com.tencent.mmkv.mmkv_c_get_int64
import com.tencent.mmkv.mmkv_c_get_string
import com.tencent.mmkv.mmkv_c_get_string_array
import com.tencent.mmkv.mmkv_c_get_uint32
import com.tencent.mmkv.mmkv_c_get_uint64
import com.tencent.mmkv.mmkv_c_mmapID
import com.tencent.mmkv.mmkv_c_mmkv_with_id
import com.tencent.mmkv.mmkv_c_remove_value_for_key
import com.tencent.mmkv.mmkv_c_set_bool
import com.tencent.mmkv.mmkv_c_set_byte_array
import com.tencent.mmkv.mmkv_c_set_double
import com.tencent.mmkv.mmkv_c_set_float
import com.tencent.mmkv.mmkv_c_set_int32
import com.tencent.mmkv.mmkv_c_set_int64
import com.tencent.mmkv.mmkv_c_set_string
import com.tencent.mmkv.mmkv_c_set_string_array
import com.tencent.mmkv.mmkv_c_set_uint32
import com.tencent.mmkv.mmkv_c_set_uint64
import com.tencent.mmkv.mmkv_c_sync
import com.tencent.mmkv.mmkv_c_total_size
import com.tencent.mmkv.mmkv_c_trim
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.cstr
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.toKString


/**
 */
class MMKVImpl internal constructor(
    private val mmapId: String? = null,
    mode: MMKVMode = MMKVMode.SINGLE_PROCESS,
    cryptKey: String? = null,
    rootPath: String? = null
) : MMKV_KMP {

    private val mmkvHandle: Long by lazy {
        mmapId?.let {
            mmkv_c_mmkv_with_id(it, mode.rawValue, cryptKey, rootPath, 0)
        } ?: mmkv_c_default_mmkv(
            mode.rawValue,
            cryptKey
        )
    }

    /**
     * Write value
     */

    override operator fun set(key: String, value: String): Boolean = mmkv_c_set_string(mmkvHandle, key, value) == 1

    override operator fun set(key: String, value: Boolean): Boolean =
        mmkv_c_set_bool(mmkvHandle, key, if (value) 1 else 0) == 1

    override operator fun set(key: String, value: Int): Boolean = mmkv_c_set_int32(mmkvHandle, key, value) == 1

    override operator fun set(key: String, value: Long): Boolean = mmkv_c_set_int64(mmkvHandle, key, value) == 1

    override operator fun set(key: String, value: Float): Boolean =
        mmkv_c_set_float(mmkvHandle, key, value) == 1

    override operator fun set(key: String, value: Double): Boolean =
        mmkv_c_set_double(mmkvHandle, key, value) == 1

    override fun set(key: String, value: UInt): Boolean = mmkv_c_set_uint32(mmkvHandle, key, value) == 1

    override fun set(key: String, value: ULong): Boolean = mmkv_c_set_uint64(mmkvHandle, key, value) == 1

    override operator fun set(key: String, value: ByteArray): Boolean =
        mmkv_c_set_byte_array(mmkvHandle, key, value.toCValues(), value.size) == 1

    override operator fun set(key: String, value: Set<String>?): Boolean {
        return memScoped {
            mmkv_c_set_string_array(
                mmkvHandle,
                key,
                value?.map { it.cstr.getPointer(this) }?.toCValues(),
                value?.size ?: 0
            ) == 1
        }
    }

    /**
     * Read value
     */

    override fun getString(key: String, default: String): String =
        mmkv_c_get_string(mmkvHandle, key)?.toKString() ?: default

    override fun getBoolean(key: String, default: Boolean): Boolean =
        mmkv_c_get_bool(mmkvHandle, key, if (default) 1 else 0) != 0

    override fun getInt(key: String, default: Int): Int = mmkv_c_get_int32(mmkvHandle, key, default)

    override fun getLong(key: String, default: Long): Long = mmkv_c_get_int64(mmkvHandle, key, default)

    override fun getFloat(key: String, default: Float): Float =
        mmkv_c_get_float(mmkvHandle, key, default)

    override fun getDouble(key: String, default: Double): Double = mmkv_c_get_double(mmkvHandle, key, default)

    override fun getUInt(key: String, default: UInt): UInt = mmkv_c_get_uint32(mmkvHandle, key, default)

    override fun getULong(key: String, default: ULong): ULong = mmkv_c_get_uint64(mmkvHandle, key, default)

    override fun getByteArray(key: String, default: ByteArray?): ByteArray? {
        return mmkv_c_get_byte_array(mmkvHandle, key)?.pointed?.let {
            it.items?.readBytes(it.size)
        }
    }

    override fun getStringSet(key: String, default: Set<String>?): Set<String>? {
        return mmkv_c_get_string_array(mmkvHandle, key)?.pointed?.let {
            cPointerOfPointer2StringCollection(
                it.size,
                it.items,
                mutableSetOf<String>()
            )
        }
    }

    /**
     * Remove value
     */
    override fun removeValueForKey(key: String) {
        mmkv_c_remove_value_for_key(mmkvHandle, key)
    }

    override fun removeValuesForKeys(keys: List<String>) {
        throw UnsupportedOperationException("ohos not support this operation now")
        //c 层调用总是返回 0，看起来是 api 有问题，暂不支持
//        val result = mmkv_c_remove_values_for_keys(
//            mmkvHandle,
//            keys.map { it.cstr.getPointer(scope) }.toCValues(),
//            keys.size
//        )
//        print("mmkvtest removeValuesForKeys result is $result")
    }

    /**
     * Size
     */

    override val actualSize: Long
        get() = mmkv_c_actual_size(mmkvHandle)

    override val count: Long
        get() = mmkv_c_count(mmkvHandle, 0)

    override val totalSize: Long
        get() = mmkv_c_total_size(mmkvHandle)

    /**
     * Clear
     */

    override fun clearMemoryCache() = mmkv_c_clear_memory_cache(mmkvHandle)

    override fun clearAll() = mmkv_c_clear_all(mmkvHandle)

    /**
     * Other
     */

    override fun close() = mmkv_c_close(mmkvHandle)

    override fun allKeys(): List<String> {
        return mmkv_c_all_keys(mmkvHandle)?.pointed?.let {
            cPointerOfPointer2StringCollection(it.size, it.items, mutableListOf())
        } ?: emptyList()
    }

    override fun containsKey(key: String): Boolean = mmkv_c_contains_key(mmkvHandle, key) != 0

    override fun checkReSetCryptKey(key: String?) =
        mmkv_c_check_reset_crypt_key(mmkvHandle, key)

    override fun mmapID(): String = mmkv_c_mmapID(mmkvHandle)?.toKString() ?: (mmapId ?: "")

    override fun async() = throw UnsupportedOperationException("ohos not support this operation now")

    override fun sync() = mmkv_c_sync(mmkvHandle)

    override fun trim() = mmkv_c_trim(mmkvHandle)

    private fun <C : MutableCollection<String>> cPointerOfPointer2StringCollection(
        length: Int,
        result: CPointer<CPointerVar<ByteVar>>?,
        collection: C
    ): C {
        for (i in 0 until length) {
            collection.add(result?.get(i)?.toKString() ?: "${null}")
        }
        return collection
    }
}