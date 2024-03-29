public class HashMap<T> {

    private Object[] array = null;

    private int nrOfElements = 0;
    private int capacity = 10;
    private double limit = 0.7;

    public HashMap() {
        array = new Object[capacity];
    }

    public HashMap(int initialCapacity, double limit) {
        this.capacity = initialCapacity;
        this.limit = limit;
        array = new Object[initialCapacity];
    }


    public void add(String k, T v) {
        double load =  nrOfElements / ((double) capacity);
        if (load > limit) {
            grow();
        }
        add(k, v, this.array, this.capacity);
    }

    private void add(String k, T v, Object[] array, int capacity) {
        int idx = k.hashCode() % capacity;
        if(array[idx] == null) {
            array[idx] = new Bucket<T>(k, v);
        } else {
            ((Bucket<T>) array[idx]).add(k, v);
        }
        nrOfElements++;
    }

    private void grow() {
        int newCapacity = 2*capacity;
        Object[] newArray = new Object[newCapacity];
        for(Object o : array) {
            if(o != null) {
                Bucket<T> current = (Bucket<T>) o;
                Bucket<T>.B<T> head = current.head;
                while(head != null) {
                    add(head.k, head.v, newArray, newCapacity);
                    head = head.next;
                }
            }
        }
        this.capacity = newCapacity;
        this.array = newArray;
    }

    public T get(String k) {
        int idx = k.hashCode() % capacity;
        if(array[idx] == null) {
            return null;
        } else {
            return ((Bucket<T>) array[idx]).get(k);
        }
    }

    public T remove(String k) {
        int idx = k.hashCode() % capacity;
        Bucket<T> bucket = (Bucket<T>) array[idx];
        if(bucket != null) {
            T v = bucket.remove(k);
            if (v != null) {
                nrOfElements--;
                return v;
            }
        }
        return null;
    }

    public int getNrOfElements() {
        return this.nrOfElements;
    }

    @Override public String toString() {
        String str = "";
        for(Object b :  array) {
            if(b == null) {
                str += "x\n";
            } else {
                str += "*-> " + ((Bucket<T>) b).toString() + "\n";
            }
        }
        return str;
    }

    private class Bucket<T> {

        public B<T> head = null;
        public int length = 0;

        public Bucket(String k, T v) {
            this.head = new B<T>(k, v);
        }

        public T remove(String k) {
            B<T> current = this.head;
            B<T> next = current.next;
            while(current != null) {
                if(current.k.equals(k)) {
                    this.head = next;
                    return current.v;
                } else if(next != null && next.k.equals(k)) {
                    current.next = next.next;
                    return next.v;
                }
                current = next;
                next = (next != null) ? next.next : null;
            }
            return null;
        }

        public void add(String k, T v) {
            B<T> newHead = new B<T>(k, v);
            newHead.next = this.head;
            this.head = newHead;
            this.length++;
        }

        public T get(String k) {
            return get(k, this.head);
        }

        private T get(String k, Bucket<T>.B<T> head) {
            if(head.k == null){
                return null;
            } else if(head.k == k) {
                return head.v;
            } else {
                return this.get(k, head.next);
            }
        }

        @Override public String toString() {
            return this.head == null ? "x" : this.head.toString();
        }

        private class B<T> {

            public String k;
            public T v;
            public B<T> next = null;

            public B(String k, T v) {
                this.k = k;
                this.v = v;
            }

            @Override public String toString() {
                String str = k == null && v == null ? "x"
                    : (k + ", " + v.toString());
                if (this.next == null)
                    return str + " -> x";
                else
                    return str + " -> " + this.next.toString();
            }
        }
    }

}
