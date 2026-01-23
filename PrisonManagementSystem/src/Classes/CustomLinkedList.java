package Classes;

// Data structure 

import java.util.Iterator;
import java.util.NoSuchElementException;


public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private int size;

    public CustomLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * إضافة عنصر جديد إلى نهاية القائمة
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * الحصول على حجم القائمة
     */
    public int size() {
        return size;
    }

    /**
     * التحقق مما إذا كانت القائمة فارغة
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * مسح جميع عناصر القائمة
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * الحصول على عنصر في موقع محدد
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }
}
