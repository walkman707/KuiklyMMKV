package com.kuikly.thirdparty.kmp.lib.mmkv

import com.tencent.mmkv.MMKV_C_LOG_DEBUG
import com.tencent.mmkv.MMKV_C_LOG_ERROR
import com.tencent.mmkv.MMKV_C_LOG_INFO
import com.tencent.mmkv.MMKV_C_LOG_NONE
import com.tencent.mmkv.MMKV_C_LOG_WARNING

/**
 * Log level Android actual
 * 
 */

typealias TencentMMKVLogLevel = Int

actual enum class MMKVLogLevel {
    LevelDebug {
        override val rawValue: TencentMMKVLogLevel = MMKV_C_LOG_DEBUG
    },
    LevelInfo {
        override val rawValue: TencentMMKVLogLevel = MMKV_C_LOG_INFO
    },
    LevelWarning {
        override val rawValue: TencentMMKVLogLevel = MMKV_C_LOG_WARNING
    },
    LevelError {
        override val rawValue: TencentMMKVLogLevel = MMKV_C_LOG_ERROR
    },
    LevelNone {
        override val rawValue: TencentMMKVLogLevel = MMKV_C_LOG_NONE
    };

    abstract val rawValue: TencentMMKVLogLevel
}