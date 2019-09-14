package weekly.practice.D190914;

/**
 * @ClassName: MergeLink
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-15 00:24
 **/
public class MergeLink {

    private Node head;

    public class Node {
        public int value;
        public Node next;
    }

    public void relocate() {
        if (null == head || null == head.next) {
            return;
        }
        Node mid = head;
        Node right = head.next;
        while (null != right.next && null != right.next.next) {
            mid = mid.next;
            right = right.next.next;
        }
        right = mid.next;
        mid.next = null;
        mergeLR(head, right);
    }

    public void mergeLR(Node left, Node right) {
        Node next = null;
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
