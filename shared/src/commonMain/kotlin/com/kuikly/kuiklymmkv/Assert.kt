package com.kuikly.kuiklymmkv

fun assertEquals(left: Boolean, right: Boolean) {
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: Int, right: Int) {
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: Float, right: Float) {
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: Long, right: Long) {
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: Double, right: Double) {
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: String, right: String) {
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: Set<String>?, right: Set<String>?) {
//        print("assert: " + left?.toString() + "===" + right?.toString())
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertEquals(left: List<String>?, right: List<String>?) {
//        print("assert: " + left?.toString() + "===" + right?.toString())
    if(left != right) {
        error("assert not equal left=$left right=$right")
    }
}

fun assertContentEquals(left: ByteArray?, right: ByteArray?) {
    if(!left.contentEquals(right)) {
        error("assert not equal left=$left right=$right")
    }
}
