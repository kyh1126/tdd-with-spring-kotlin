package me.jenny.demo.util

class Stem<T : Any>(
    private val thisObj: T,
    private vararg val properties: T.() -> Any?
) {
    fun eq(other: Any?): Boolean {
        if (thisObj === other)
            return true

        if (thisObj.javaClass != other?.javaClass)
            return false

        // cast is safe, because this is T and other's class was checked for equality with T
        @Suppress("UNCHECKED_CAST")
        other as T

        return properties.all { thisObj.it() == other.it() }
    }

    fun hc(): Int {
        // Fast implementation without collection copies, based on java.util.Arrays.hashCode()
        var result = 1

        for (element in properties) {
            val value = thisObj.element()
            result = 31 * result + (value?.hashCode() ?: 0)
        }

        return result
    }
}
