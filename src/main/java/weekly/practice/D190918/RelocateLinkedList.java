package weekly.practice.D190918;

/**
 * @ClassName: RelocateLinkedList
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-18 11:28
 **/
public class RelocateLinkedList {

    public class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
        }

    }

    public void relocate(Node head) {
        if (null == head || null == head.next) {
            return;
        }
        // 左半区最后一个节点，之后需要断开
        Node mid = head;
        Node right = head.next;
        while (null != right.next && null != right.next.next) {
            mid = mid.next;
            right = right.next.next;
        }
        right = mid.next;
        mid.next = null;
        merge(head, right);
    }

    private void merge(Node left, Node right) {
        Node next;
        while (null != left.next) {
            next = right.next;
            right.next = left.next;
            left.next = right;
            left = right.next;
            right = next;
        }
        left.next = right;
    }

}
