package org.algohub.engine.type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

@JsonDeserialize(using = ListNode.Deserializer.class)
public class ListNode {
    public int value;
    public ListNode next;

    public ListNode(final int value) {
        this(value, null);
    }
    public ListNode(final int value, final ListNode next) {
        this.value = value;
        this.next = next;
    }

    public ListNode add(final int value) {
        return add(new ListNode(value));
    }

    public ListNode add(final ListNode node) {
        ListNode tail = this;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = node;
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ListNode)) {
            return false;
        }

        final ListNode other = (ListNode) obj;

        ListNode p = this;
        ListNode q = other;
        while (p != null && q != null) {
            if (p.value != q.value) {
                return false;
            }
            p = p.next;
            q = q.next;
        }
        return p == null && q == null;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (ListNode p = this; p != null; p = p.next) {
            hashCode = 31 * hashCode + value;
        }

        return hashCode;
    }

    @Override
    public String toString() {
        final StringBuilder linkedListAsString = new StringBuilder();
        linkedListAsString.append('[').append(value);

        for (ListNode p = next; p != null; p = p.next) {
            linkedListAsString.append(", ").append(p.value);
        }

        linkedListAsString.append(']');

        return linkedListAsString.toString();
    }

    static class Deserializer extends JsonDeserializer<ListNode> {

        @Override
        public ListNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = p.getCodec().readTree(p);

            final ArrayNode elements = (ArrayNode) jsonNode;

            ListNode listNode = null;

            for (final JsonNode e : elements) {
                if (listNode == null) {
                    listNode = new ListNode(e.asInt());
                } else {
                    listNode.add(e.asInt());
                }
            }

            return listNode;
        }
    }
}
