package com.graduation.datastructures;

/**
 * Activity 3: Singly Linked List implementation
 * Stores String objects in nodes without using generics
 */
public class SinglyLinkedList {

    /**
     * Node class for singly linked list
     */
    private static class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    /**
     * Constructor - creates empty list
     */
    public SinglyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Add element to the end of the list
     * Time Complexity: O(n) - need to traverse to end
     */
    public void add(String data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Remove element at specified index
     * Time Complexity: O(n) - need to traverse to index
     */
    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false;
        }

        if (index == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
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

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
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
