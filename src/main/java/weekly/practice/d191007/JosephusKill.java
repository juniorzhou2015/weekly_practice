package weekly.practice.d191007;

import lombok.Data;

public class JosephusKill {

    @Data
    public class Node {
        private int value;
        private Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public Node josephusKill1(Node head, int m) {
        if (null == head || null == head.next || m < 1) {
            return head;
        }
        Node next = head.next, cur = head;
        int count = 0;
        while (next != cur) {
            if (m == ++count) {
                cur.next = next.next;
                count = 0;
            } else {
                cur = cur.next;
            }
            next = next.next;
        }
        return next;
    }

}
