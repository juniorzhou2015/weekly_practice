package weekly.practice.d190920;

/**
 * @ClassName: CommonPart
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-20 10:40
 **/
public class CommonPart {

    public class Node {
        private int value;
        private Node next;
        public Node(int value) {
            this.value = value;
        }
    }

    public void printCommonPart(Node head1, Node head2) {
        if (null == head1 || null == head2) {
            return;
        }
        Node cur1 = head1, cur2 = head2;
        while (null != cur1 && null != cur2) {
            if (cur1.value < cur2.value) {
                cur1 = cur1.next;
            } else if (cur1.value > cur2.value) {
                cur2 = cur2.next;
            } else {
                System.out.print(cur1.value + " ");
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
        }
        System.out.println();
    }

}
