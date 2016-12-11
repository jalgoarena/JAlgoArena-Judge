package org.algohub.engine.type

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

import java.io.IOException

class ListNode @JvmOverloads constructor(@JvmField var value: Int, @JvmField var next: ListNode? = null) {

    fun add(value: Int): ListNode {
        return add(ListNode(value))
    }

    fun add(node: ListNode): ListNode {
        var tail: ListNode? = this
        while (tail!!.next != null) {
            tail = tail.next
        }
        tail.next = node
        return this
    }

    override fun equals(other: Any?): Boolean {
        when {
            other === this -> return true
            other is ListNode -> {
                var p: ListNode? = this
                var q: ListNode? = other

                while (p != null && q != null) {
                    if (p.value != q.value) {
                        return false
                    }
                    p = p.next
                    q = q.next
                }

                return p == null && q == null
            }
            else -> return false
        }
    }

    override fun hashCode(): Int {
        var hashCode = 1
        var p: ListNode? = this

        while (p != null) {
            hashCode = 31 * hashCode + value
            p = p.next
        }

        return hashCode
    }

    override fun toString(): String {
        val linkedListAsString = StringBuilder()
        linkedListAsString.append('[').append(value)

        var p = next
        while (p != null) {
            linkedListAsString.append(", ").append(p.value)
            p = p.next
        }

        linkedListAsString.append(']')

        return linkedListAsString.toString()
    }

    class Deserializer : JsonDeserializer<ListNode>() {

        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ListNode? {
            val jsonNode = p.codec.readTree<JsonNode>(p)

            val elements = jsonNode as ArrayNode

            var listNode: ListNode? = null

            for (e in elements) {
                if (listNode == null) {
                    listNode = ListNode(e.asInt())
                } else {
                    listNode.add(e.asInt())
                }
            }

            return listNode
        }
    }
}
