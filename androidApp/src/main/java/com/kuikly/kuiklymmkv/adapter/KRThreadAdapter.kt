package com.kuikly.kuiklymmkv.adapter

import com.tencent.kuikly.core.render.android.adapter.IKRThreadAdapter

class KRThreadAdapter : IKRThreadAdapter {
    override fun executeOnSubThread(task: () -> Unit) {
        execOnSubThread(task)
    }
}