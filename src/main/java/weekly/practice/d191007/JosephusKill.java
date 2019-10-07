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
        Node cur1 = head.next, cur2 = head;
        int count = 0;
        while (cur1 != cur2) {
            if (m == ++count) {
                cur2.next = cur1.next;
                count = 0;
            } else {
                cur2 = cur2.next;
            }
            cur1 = cur2.next;
        }
        return cur1;
    }

}
