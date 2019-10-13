package weekly.practice.D190914;

import lombok.Data;

import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: BinaryTree
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 23:18
 **/
public class BinaryTree<Key extends Comparable, Value> {

    @Data
    private class Node {
        private Key key;
        private Value value;
        private Node left, right;
        private int size;
    }

    private Node root;

    public void breathSearchFirst() {
        if (null == root) {
            return;
        }
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(root);
        int currentLevelNum = 1, nextLevelNum = 0;
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            System.out.print(n.getValue());
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

    public void depthSearchFirst() {
        if (null == root) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.add(root);
        int currentLevelNum = 1, nextLevelNum = 0;
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            System.out.print(n.getValue());
            if (null != n.left) {
                stack.add(n.left);
                ++nextLevelNum;
            }
            if (null != n.right) {
                stack.add(n.right);
                ++nextLevelNum;
            }
            if (0 == --currentLevelNum) {
                System.out.println();
                currentLevelNum = nextLevelNum;
                nextLevelNum = 0;
            }
        }
    }

    public void zigZag() {
        if (null == root) {
            return;
        }
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        Stack<Node> stack = new Stack<>();
        queue.offer(root);
        int currentLevelNum = 1, nextLevelNum = 0, level = 1;
        if (!queue.isEmpty()) {
            Node n = queue.poll();
            if (0 == level % 2) {
                stack.add(n);
            } else {
                System.out.print(n.getValue());
            }
            --currentLevelNum;
            if (null != n.left) {
                queue.offer(n.left);
                ++nextLevelNum;
            }
            if (null != n.right) {
                queue.offer(n.right);
                ++nextLevelNum;
            }
            if (0 == --currentLevelNum) {
                while (!stack.isEmpty()) {
                    Node m = stack.pop();
                    System.out.print(m.value);
                }
                System.out.println();
                currentLevelNum = nextLevelNum;
                nextLevelNum = 0;
                ++level;
            }
        }
    }

    /**
     * TODO
     * 当只能使用stack一种数据结构时呢？
     */

}
