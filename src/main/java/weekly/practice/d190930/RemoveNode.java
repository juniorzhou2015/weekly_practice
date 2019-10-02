package weekly.practice.d190930;

public class RemoveNode {

    public class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
        }

    }

    public Node removeMidNode(Node head) {
        if (null == head || null == head.next) {
            return head;
        }
        if (null == head.next.next) {
            return head.next;
        }
        Node pre = head, cur = head.next.next;
        while (null != cur.next && null != cur.next.next) {
            pre = pre.next;
            cur = cur.next.next;
        }
        pre.next = pre.next.next;
        return head;
    }

    public Node removeRatioNode(Node head, int a, int b) {
        if (null == head || null == head.next) {
            return head;
        }
        int n = 0;
        Node cur = head;
        while (null != cur) {
            n++;
            cur = cur.next;
        }
        n = (int) Math.ceil((double) (a * n) / (double) b);
        if (1 == n) {
            head = head.next;
        }
        if (1 < n) {
            cur = head;
            while (--n != 1) {
                cur = cur.next;
            }
            cur.next = cur.next.next;
        }
        return head;
    }

}
