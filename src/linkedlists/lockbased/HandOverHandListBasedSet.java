package linkedlists.lockbased;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import contention.abstractions.AbstractCompositionalIntSet;

public class HandOverHandListBasedSet extends AbstractCompositionalIntSet {
    private class Node {
        protected int key;
        protected AtomicReference<Node> next;
        private ReentrantLock mutex;

        Node(int key, Node next) {
            this.key = key;
            this.next = new AtomicReference<>(next);
            mutex = new ReentrantLock();
        }

        public void lock() {
            mutex.lock();
        }

        public void unlock() {
            mutex.unlock();
        }
    }

    // sentinel nodes
    private Node head;
    private Node tail;

    public HandOverHandListBasedSet() {
        tail = new Node(Integer.MAX_VALUE, null);
        head = new Node(Integer.MIN_VALUE, tail);
    }

    @Override
    public boolean addInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next.get();

        try {
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next.get();
                    curr.lock();
                }
                if (curr.key == x) {
                    return false;
                }
                Node node = new Node(x, curr);
                pred.next.set(node);
                return true;
            } finally {
                if (curr != null)
                    curr.unlock();
            }
        } finally {
            if (pred != null)
                pred.unlock();
        }
    }

    @Override
    public boolean removeInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next.get();

        try {
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next.get();
                    curr.lock();
                }
                if (curr.key == x) {
                    pred.next.set(curr.next.get());
                    return true;
                }
                return false;
            } finally {
                if (curr != null)
                    curr.unlock();
            }
        } finally {
            if (pred != null)
                pred.unlock();
        }
    }

    @Override
    public boolean containsInt(int x) {
        Node pred = head;
        Node curr = pred.next.get();

        while (curr.key < x) {
            pred = curr;
            curr = pred.next.get();
        }
        return curr.key == x;
    }

    @Override
    public int size() {
        int count = 0;

        Node curr = head.next.get();
        while (curr.key != Integer.MAX_VALUE) {
            curr = curr.next.get();
            count++;
        }
        return count;
    }

    @Override
    public void clear() {
        tail = new Node(Integer.MAX_VALUE, null);
        head = new Node(Integer.MIN_VALUE, tail);
        head.next.set(tail);
    }
}