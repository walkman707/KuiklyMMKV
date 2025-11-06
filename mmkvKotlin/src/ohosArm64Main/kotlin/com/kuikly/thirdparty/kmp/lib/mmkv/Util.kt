package com.kuikly.thirdparty.kmp.lib.mmkv

import com.tencent.mmkv.mmkv_c_backup_all_to_directory
import com.tencent.mmkv.mmkv_c_backup_one_to_directory
import com.tencent.mmkv.mmkv_c_set_log_level


/**
 * Other top-level function
 * 
 */

actual fun backupOneToDirectory(
    mmapID: String, dstDir: String, rootPath: String?
): Boolean = mmkv_c_backup_one_to_directory(mmapID, dstDir, rootPath) == 1

actual fun backupAllToDirectory(dstDir: String, rootPath: String?): Boolean =
    mmkv_c_backup_all_to_directory(dstDir, rootPath) == 1

actual fun pageSize(): Long = throw UnsupportedOperationException("ohos not support this operation now")

actual fun setLogLevel(logLevel: MMKVLogLevel) = mmkv_c_set_log_level(logLevel.rawValue)

actual fun version(): String = throw UnsupportedOperationException("ohos not support this operation now")

actual fun unregisterHandler() {
    throw UnsupportedOperationException("ohos not support this operation now")
}