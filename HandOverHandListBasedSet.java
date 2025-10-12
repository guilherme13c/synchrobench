package linkedlists.lockbased;

import java.util.concurrent.locks.ReentrantLock;

import contention.abstractions.AbstractCompositionalIntSet;

public class HandOverHandListBasedSet extends AbstractCompositionalIntSet {
    private class Node {
        protected int key;
        protected Node next;
        private ReentrantLock mutex;

        Node(int item) {
            key = item;
            next = null;
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
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
    }

    @Override
    public boolean addInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next;

        try {
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.lock();
                }
                if (curr.key == x) {
                    return false;
                }
                Node node = new Node(x);
                node.next = curr;
                pred.next = node;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean removeInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next;

        try {
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.lock();
                }
                if (curr.key == x) {
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean containsInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next;

        try {
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.lock();
                }
                return curr.key == x;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public int size() {
        int count = 0;

        Node curr = head.next;
        while (curr.key != Integer.MAX_VALUE) {
            curr = curr.next;
            count++;
        }
        return count;
    }

    @Override
    public void clear() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
    }
}
