package com.kuikly.kuiklymmkv


import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.pager.Pager
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.compose.Button
import com.kuikly.thirdparty.kmp.lib.mmkv.defaultMMKV

/**
 * Created by kamlin on 2023/2/14.
 */
@Page("helloworld") // 页面名
class HelloWorldPage : Pager(){

    private var testResult by observable("MMKV Test")

    override fun body(): ViewBuilder {
        val ctx = this
        return {

            attr {
                backgroundColor(Color.WHITE)
                allCenter()
            }
            Text {
                attr {
                    value(ctx.testResult)
                }
            }
            Button {
                attr {
                    marginTop(16f)
                    titleAttr {
                        value("Click Start")
                    }
                }
                event {
                    click {
                        ctx.testMMKV()
                    }
                }
            }
        }
    }

    private fun testMMKV() {
        val strFromArkts = defaultMMKV().getString("test")
        if(strFromArkts != "abcdef"){
            error("strFromArkts is not abcdef")
        }

        MMKVKotlinTest().apply {
            setUp()

            testInt()
            testBoolean()
            testLong()
            testFloat()
            testDouble()
            testString()
            testStringSet()
            testByteArray()

            testRemove()

            testClearAll()

            testAllKeys()
            testContainsKey()
        }

        testResult = "MMKV Test Done!"

        print("mmkvtest all Done")
    }
}