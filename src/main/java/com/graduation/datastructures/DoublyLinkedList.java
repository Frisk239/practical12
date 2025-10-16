package com.graduation.datastructures;

/**
 * Activity 3: Doubly Linked List implementation
 * Stores String objects in nodes without using generics
 */
public class DoublyLinkedList {

    /**
     * Node class for doubly linked list
     */
    private static class Node {
        String data;
        Node next;
        Node previous;

        Node(String data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    /**
     * Constructor - creates empty list
     */
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Add element to the end of the list
     * Time Complexity: O(1) - direct access to tail
     */
    public void add(String data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Add element to the beginning of the list
     * Time Complexity: O(1) - direct access to head
     */
    public void addFirst(String data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.previous = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Add element to the end of the list (alias for add)
     * Time Complexity: O(1)
     */
    public void addLast(String data) {
        add(data);
    }

    /**
     * Remove element at specified index
     * Time Complexity: O(n) - need to traverse to index
     */
    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false;
        }

        Node nodeToRemove;
        if (index == 0) {
            nodeToRemove = head;
            head = head.next;
            if (head != null) {
                head.previous = null;
            } else {
                tail = null;
            }
        } else if (index == size - 1) {
            nodeToRemove = tail;
            tail = tail.previous;
            tail.next = null;
        } else {
            nodeToRemove = head;
            for (int i = 0; i < index; i++) {
                nodeToRemove = nodeToRemove.next;
            }
            nodeToRemove.previous.next = nodeToRemove.next;
            nodeToRemove.next.previous = nodeToRemove.previous;
        }
        size--;
        return true;
    }

    /**
     * Remove first element
     * Time Complexity: O(1)
     */
    public boolean removeFirst() {
        if (isEmpty()) {
            return false;
        }
        head = head.next;
        if (head != null) {
            head.previous = null;
        } else {
            tail = null;
        }
        size--;
        return true;
    }

    /**
     * Remove last element
     * Time Complexity: O(1)
     */
    public boolean removeLast() {
        if (isEmpty()) {
            return false;
        }
        tail = tail.previous;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return true;
    }

    /**
     * Get element at specified index
     * Time Complexity: O(n) - need to traverse to index
     */
    public String get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node current;
        // Optimize: start from head or tail depending on index
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.previous;
            }
        }
        return current.data;
    }

    /**
     * Get current size of the list
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Check if list is empty
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Convert list to string representation
     * Time Complexity: O(n)
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
