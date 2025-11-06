package com.kuikly.thirdparty.kmp.lib.mmkv

import com.tencent.mmkv.MMKV_C_MULTI_PROCESS
import com.tencent.mmkv.MMKV_C_SINGLE_PROCESS

/**
 * MMKV model Android actual
 * 
 */

actual enum class MMKVMode {
    SINGLE_PROCESS {
        override val rawValue: Int = MMKV_C_SINGLE_PROCESS
    },
    MULTI_PROCESS {
        override val rawValue: Int = MMKV_C_MULTI_PROCESS
    };

    abstract val rawValue: Int
}