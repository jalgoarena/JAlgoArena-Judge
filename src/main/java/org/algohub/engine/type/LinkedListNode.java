package org.algohub.engine.type;

public class LinkedListNode<E> {
    public E value;
    public LinkedListNode<E> next;

    public LinkedListNode() {
        value = null;
        next = null;
    }

    public LinkedListNode(final E value) {
        this.value = value;
        this.next = null;
    }

    public LinkedListNode(final E value, final LinkedListNode<E> next) {
        this.value = value;
        this.next = next;
    }

    public void add(final E value) {
        if (this.value == null) {
            this.value = value;
            return;
        }

        LinkedListNode<E> tail = this;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = new LinkedListNode<>(value);
    }

    @Override
    public String toString() {
        if (value == null) {
            return "";
        }

        final StringBuilder linkedListAsString = new StringBuilder();
        linkedListAsString.append(value);

        for (LinkedListNode<E> p = next; p != null; p = p.next) {
            linkedListAsString.append("->").append(p.value);
        }

        return linkedListAsString.toString();
    }
}
