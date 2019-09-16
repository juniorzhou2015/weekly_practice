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
        if (null == list1) {
            return list2;
        }
        if (null == list2) {
            return list1;
        }
        Node a = list1, b = list2;
        Node head = new Node();
        Node iter = head;
        while (null != a || null != b) {
            if (0 == iter.getValue()) {
                if (null == a) {
                    iter = b;
                    break;
                }
                if (null == b) {
                    iter = a;
                    break;
                }
                if (a.value <= b.value) {
                    iter = a;
                    a = a.next;
                } else {
                    iter = b;
                    b = b.next;
                }
            } else {
                if (null == a) {
                    iter.next = b;
                    break;
                }
                if (null == b) {
                    iter.next = a;
                    break;
                }
                if (a.value <= b.value) {
                    iter.next = a;
                    a = a.next;
                } else {
                    iter.next = b;
                    b = b.next;
                }
            }
            iter = iter.next;
        }
        return head;
    }

}
