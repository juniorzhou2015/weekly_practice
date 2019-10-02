package weekly.practice.d191002;

public class ReverseLinkedList {

    public class Node {
        private int value;
        private Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public Node reverse(Node head) {
        Node pre = null, next = null;
        while (null != head) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return next;
    }

    public class DoubleNode {
        private int value;
        private DoubleNode next;
        private DoubleNode last;

        public DoubleNode(int data) {
            this.value = data;
        }

        public DoubleNode reverse(DoubleNode head) {
            DoubleNode pre = null, next = null;
            while (null != head) {
                next = head.next;
                head.next = pre;
                head.last = next;
                pre = head;
                head = next;
            }
            return next;
        }

    }



}
