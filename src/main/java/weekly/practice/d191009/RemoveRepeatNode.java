package weekly.practice.d191009;

import java.util.HashSet;

public class RemoveRepeatNode {

    public class Node {
        private int value;
        private Node next;
        public Node(int data) {
            this.value = data;
        }
    }

    /**
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     */
    public void removeRepeatNode1(Node head) {
        if (null == head || null == head.next) {
            return;
        }
        HashSet<Integer> nodeSet = new HashSet<>();
        Node pre = head, cur = head.next;
        nodeSet.add(head.value);
        while (null != cur) {
            if (nodeSet.contains(cur.value)) {
                pre.next = cur.next;
            } else {
                nodeSet.add(cur.value);
                pre = cur;
            }
            cur = cur.next;
        }
    }
    /**
     * 时间复杂度：O(N2)
     * 空间复杂度：O(1)
     */
    public void removeRepeatNode2(Node head) {
        if (null == head || null == head.next) {
            return;
        }
        Node compare = head, pre = head, cur = head.next;
        while (null != compare) {
            while (null != cur) {
                if (compare.value == cur.value) {
                    pre.next = cur.next;
                } else {
                    pre = cur;
                }
                cur = cur.next;
            }
            compare = compare.next;
        }
    }

}
