package weekly.practice.d191026;

import lombok.Data;

import java.util.Stack;

/**
 * 找出二叉搜索树中第K小的数值
 */
public class FindLeastKthNode {

    @Data
    public static class Node {
        private int value;
        private Node left;
        private Node right;

        public Node(int data) {
            this.value = data;
        }

    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        System.out.println(findLeastKthNodeRecursive(root));
        System.out.println(inIterate(root, 3));
        System.out.println(findLeastKthNodeInIterate(root, 3));
    }

    /**
     * 存在死循环，不能用前序遍历类似的写法
     */
    public static Node findLeastKthNodeInIterate(Node root, int n) {
        if (null == root || n <= 0) {
            return null;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        Node result = null;
        while (!stack.isEmpty()) {
            Node node = stack.peek();
            // 这里存在死循环，所以得用下面的方法
            if (null != node.left) {
                stack.push(node.left);
                continue;
            }
            stack.pop();
            if (0 == --n) {
                result = node;
                break;
            }
            if (null != node.right) {
                stack.push(node.right);
            }
        }
        return result;
    }

    public static Node inIterate(Node root, int n) {
        Stack<Node> stack = new Stack<>();
        Node iter = root, result = null;
        while (!stack.isEmpty() || null != iter) {
            if (null != iter) {
                stack.push(iter);
                iter = iter.left;
            } else {
                iter = stack.pop();
                if (0 == --n) {
                    result = iter;
                    break;
                }
                iter = iter.right;
            }
        }
        return result;
    }

    private static int n = 3;

    /**
     * Java的值传递
     * 递归传入Integer类型，子函数修改改变不了父函数
     * Java实现的递归在需要传入n时存在问题
     */
    public static Node findLeastKthNodeRecursive(Node root) {
        if (null == root) {
            return null;
        }
        return recursive(root);
    }

    private static Node recursive(Node root) {
        Node result = null;
        if (null != root.left) {
            result = recursive(root.left);
        }
        if (null == result && --n == 0) {
            result = root;
        }
        if (null == result && null != root.right) {
            result = recursive(root.right);
        }
        return result;
    }

}
