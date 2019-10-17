package weekly.practice.d191013;

import lombok.Data;
import weekly.practice.D190915.MergeLinkedListIterate;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 最长链为这棵二叉树中一条最长的简单路径，即不经过重复结点的一条路径
 */
public class FindMaxLen {

    public static void main(String[] args) {
        Node root = new Node(0);
        root.left = new Node(1);
        root.right = new Node(2);
        root.left.right = new Node(3);
        root.right.left = new Node(4);
        root.right.left.left = new Node(5);
        root.right.left.right = new Node(6);
        breathSearchFirst(root);
        System.out.println();
        findMaxLen(root);
        System.out.println(maxLen);
    }

    @Data
    public static class Node {
        private int value;
        private Node left, right;
        private int maxLeft;
        private int maxRight;

        public Node(int data) {
            this.value = data;
        }

    }

    private static int maxLen;

    public static void findMaxLen(Node root) {
        if (null == root) {
            return;
        }
        if (null == root.left) {
            root.maxLeft = 0;
        }
        if (null == root.right) {
            root.maxRight = 0;
        }
        if (null != root.left) {
            findMaxLen(root.left);
        }
        if (null != root.right) {
            findMaxLen(root.right);
        }
        if (null != root.left) {
            root.maxLeft = Math.max(root.left.maxLeft, root.left.maxRight) + 1;
        }
        if (null != root.right) {
            root.maxRight = Math.max(root.right.maxLeft, root.right.maxRight) + 1;
        }
        if (root.maxLeft + root.maxRight > maxLen) {
            maxLen = root.maxLeft + root.maxRight;
        }
    }

    public static void breathSearchFirst(Node root) {
        if (null == root) {
            return;
        }
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(root);
        int currentLevelNum = 1, nextLevelNum = 0;
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            System.out.print(n.getValue() + " ");
            if (null != n.left) {
                queue.offer(n.left);
                ++nextLevelNum;
            }
            if (null != n.right) {
                queue.offer(n.right);
                ++nextLevelNum;
            }
            if (0 == --currentLevelNum) {
                System.out.println();
                currentLevelNum = nextLevelNum;
                nextLevelNum = 0;
            }
        }
    }

}
