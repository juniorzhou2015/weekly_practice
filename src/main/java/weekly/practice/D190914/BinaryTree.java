package weekly.practice.D190914;

import lombok.Data;

import java.util.Queue;
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
        if (!queue.isEmpty()) {
            Node n = queue.poll();
            System.out.print(n.getValue());
            --currentLevelNum;
            if (null != n.left) {
                queue.offer(n.left);
                ++nextLevelNum;
            }
            if (null != n.right) {
                queue.offer(n.right);
                ++nextLevelNum;
            }
            if (0 == currentLevelNum) {
                System.out.println();
                currentLevelNum = nextLevelNum;
                nextLevelNum = 0;
            }
        }
    }

}
