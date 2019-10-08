package weekly.practice.d191008;

import java.util.Stack;

public class AddLists {

    public static class Node {
        private int value;
        private Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public static Node addListsStack(Node head1, Node head2) {
        if (null == head1 && null == head2) {
            return null;
        }
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        while (null != head1) {
            s1.push(head1.value);
            head1 = head1.next;
        }
        while (null != head2) {
            s2.push(head2.value);
            head2 = head2.next;
        }
        int ca = 0, n1 = 0, n2 = 0, n = 0;
        Node head = null, pre = null;
        while (!s1.isEmpty() || !s2.isEmpty()) {
            n1 = s1.isEmpty() ? 0 : s1.pop();
            n2 = s2.isEmpty() ? 0 : s2.pop();
            n = n1 + n2 + ca;
            pre = head;
            head = new Node(n % 10);
            head.next = pre;
            ca = n / 10;
        }
        if (1 == ca) {
            pre = head;
            head = new Node(1);
            head.next = pre;
        }
        return head;
    }

    public static Node addListsReverse(Node head1, Node head2) {
        if (null == head1 && null == head2) {
            return null;
        }
        head1 = reverse(head1);
        head2 = reverse(head2);
        int ca = 0, n1 = 0, n2 = 0, n = 0;
        Node pre = null, head = null, cur1 = head1, cur2 = head2;
        while (null != cur1 || null != cur1) {
            n1 = null == cur1 ? 0 : cur1.value;
            n2 = null == cur2 ? 0 : cur2.value;
            n = n1 + n2 + ca;
            pre = head;
            head = new Node(n % 10);
            head.next = pre;
            ca = n / 10;
            cur1 = null == cur1 ? null : cur1.next;
            cur2 = null == cur2 ? null : cur2.next;
        }
        if (1 == ca) {
            pre = head;
            head = new Node(1);
            head.next = pre;
        }
        reverse(head1);
        reverse(head2);
        return head;
    }

    private static Node reverse(Node head) {
        Node pre = null, next = null;
        while (null != head) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        AddLists.Node head1 = new AddLists.Node(1);
        AddLists.Node iter = head1;
        iter.next = new AddLists.Node(3);
        iter.next.next = new AddLists.Node(5);

        AddLists.Node head2 = new AddLists.Node(2);
        iter = head2;
        iter.next = new AddLists.Node(4);
        iter.next.next = new AddLists.Node(6);

        AddLists.Node addListsStack = addListsStack(head1, head2);
        System.out.println("addListsStack");
        printLinkedList(head1);
        printLinkedList(head2);
        printLinkedList(addListsStack);

        AddLists.Node addListsReverse = addListsReverse(head1, head2);
        System.out.println("addListsReverse");
        printLinkedList(head1);
        printLinkedList(head2);
        printLinkedList(addListsReverse);

    }

    private static void printLinkedList(Node merge) {
        for (Node x = merge; null != x; x = x.next) {
            System.out.print(x.value);
            System.out.print(" ");
        }
        System.out.println();
    }

}
