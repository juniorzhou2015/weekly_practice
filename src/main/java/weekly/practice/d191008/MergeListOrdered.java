package weekly.practice.d191008;

public class MergeListOrdered {

    public static class Node {
        private int value;
        private Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public static Node merge(Node head1, Node head2) {
        if (null == head1 || null == head2) {
            return null == head1 ? head2 : head1;
        }
        Node head = head1.value <= head2.value ? head1 : head2;
        Node pre = head, next = null;
        while (null != head1 && null != head2) {
            if (head1.value <= head2.value) {
                pre = head1;
                head1 = head1.next;
            } else {
                next = head2.next;
                head2.next = head1;
                // 需要判断pre和head2不相等，否则会形成死链
                if (pre != head2) {
                    pre.next = head2;
                }
                pre = head2;
                head2 = next;
            }
        }
        pre.next = null == head1 ? head2 : head1;
        return head;
    }

    public static void main(String[] args) {
        MergeListOrdered.Node head1 = new MergeListOrdered.Node(1);
        MergeListOrdered.Node iter = head1;
        iter.next = new MergeListOrdered.Node(5);
        iter.next.next = new MergeListOrdered.Node(7);

        MergeListOrdered.Node head2 = new MergeListOrdered.Node(2);
        iter = head2;
        iter.next = new MergeListOrdered.Node(4);
        iter.next.next = new MergeListOrdered.Node(6);
        iter.next.next.next = new MergeListOrdered.Node(9);
        iter.next.next.next.next = new MergeListOrdered.Node(10);

        MergeListOrdered.Node list = merge(head1, head2);
        for (MergeListOrdered.Node x = list; null != x; x = x.next) {
            System.out.print(x.value);
            System.out.print(" ");
        }
        System.out.println();

    }

}
