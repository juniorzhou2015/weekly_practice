package weekly.practice.d190923;

public class RemoveLastKthNode {

    public static class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    public Node doRemove(Node head, int lastKth) {
        if (null == head || 0 == lastKth) {
            return head;
        }
        Node cur = head;
        while (null != cur) {
            cur = cur.next;
            lastKth--;
        }
        if (0 == lastKth) {
            head = head.next;
        } else if (lastKth < 0) {
            Node pre = head;
            while (0 != ++lastKth) {
                pre = pre.next;
            }
            pre.next = pre.next.next;
        }
        return head;
    }

    public class DoubleNode {
        private int value;
        private DoubleNode last;
        private DoubleNode next;

        public DoubleNode(int value) {
            this.value = value;
        }
    }

    public DoubleNode doRemove(DoubleNode head, int lastKth) {
        if (null == head || 0 == lastKth) {
            return head;
        }
        DoubleNode cur = head;
        while (null != cur) {
            cur = cur.next;
            lastKth--;
        }
        if (0 == lastKth) {
            head = head.next;
        } else if (lastKth < 0) {
            cur = head;
            while (0 != ++lastKth) {
                cur = cur.next;
            }
            DoubleNode newNext = cur.next.next;
            cur.next = newNext;
            if (null != newNext) {
                newNext.last = cur;
            }
        }
        return head;
    }

}