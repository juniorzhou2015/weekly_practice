package weekly.practice.D190915;

import lombok.Data;

/**
 * @ClassName: MergeLinkedListDeadCycle
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-16 15:05
 **/
public class MergeLinkedListIterate {

    public static void main(String[] args) {
        Node list1 = new Node(1);
        Node iter = list1;
        iter.next = new Node(3);
        iter.next.next = new Node(5);

        Node list2 = new Node(2);
        iter = list2;
        iter.next = new Node(4);
        iter.next.next = new Node(6);

        Node merge = merge(list1, list2);
        for (Node x = merge; null != x; x = x.next) {
            System.out.print(x.value);
            System.out.print(" ");
        }

    }

    @Data
    public static class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
        }

        public Node() {

        }

    }

    public static Node merge(Node list1, Node list2) {
        if (null == list1 || null == list2) {
            return null == list1 ? list2 : list1;
        }
        Node head = list1.value < list2.value ? list1 : list2;
        Node cur1 = head == list1 ? list1 : list2;
        Node cur2 = head == list1 ? list2 : list1;
        Node pre = null, next = null;
        while (null != cur1 && null != cur2) {
            if (cur1.value < cur2.value) {
                pre = cur1;
                cur1 = cur1.next;
            } else {
                next = cur2.next;
                cur2.next = cur1;
                pre.next = cur2;
                pre = cur2;
                cur2 = next;
            }
        }
        pre.next = null != cur1 ? cur2 : cur1;
        return head;
    }

}
