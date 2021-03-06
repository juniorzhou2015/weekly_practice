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
        Node pre = null, next = null, cur = head;
        while (null != cur) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public Node reversePart(Node head, int from, int to) {
        if (null == head || null == head.next || 1 > from || from > to) {
            return head;
        }
        int len = 0;
        Node cur = head;
        Node fPre = null, tAfter = null;
        while (null != cur) {
            len++;
            if (len == from - 1) {
                fPre = cur;
            }
            if (len == to + 1) {
                tAfter = cur;
            }
            cur = cur.next;
        }
        if (to > len) {
            return head;
        }
        // from = 1时，null == fPre
        cur = null == fPre ? head : fPre.next;
        // 逆序
        Node pre = null, next = null;
        while (tAfter != cur) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        if (null != fPre) {
            // 倒转首节点指向tAfter
            fPre.next.next = tAfter;
            // fPre指向倒转末节点
            fPre.next = cur;
            return head;
        }
        // 倒转首节点指向tAfter
        head.next = tAfter;
        return cur;
    }


    public class DoubleNode {
        private int value;
        private DoubleNode next;
        private DoubleNode last;

        public DoubleNode(int data) {
            this.value = data;
        }

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
        return pre;
    }

}
