package com.kuikly.thirdparty.kmp.lib.mmkv


actual fun defaultMMKV(): MMKV_KMP = MMKVImpl()

actual fun defaultMMKV(cryptKey: String): MMKV_KMP = MMKVImpl(cryptKey = cryptKey)

actual fun mmkvWithID(
    mmapId: String,
    mode: MMKVMode,
    cryptKey: String?,
    rootPath: String?,
): MMKV_KMP = MMKVImpl(mmapId, mode, cryptKey, rootPath)