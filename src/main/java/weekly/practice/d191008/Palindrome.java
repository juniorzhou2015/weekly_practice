package weekly.practice.d191008;

import java.util.Stack;

public class Palindrome {

    public class Node {
        private int value;
        private Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public boolean isPalindrome1(Node head) {
        if (null == head || null == head.next) {
            return true;
        }
        Node cur = head;
        Stack<Node> stack = new Stack<>();
        while (null != cur) {
            stack.push(cur);
            cur = cur.next;
        }
        cur = head;
        while (null != cur) {
            if (cur.value != stack.pop().value) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }

}
