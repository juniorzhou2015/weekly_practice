package weekly.practice.d191013;

public class BitSet {

    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 3, 22, 0, 3, 5, 64};
        java.util.BitSet bitSet = new java.util.BitSet();
        //将数组内容组bitmap
        for (int i = 0; i < array.length; i++) {
            bitSet.set(array[i], true);
        }
        System.out.println(bitSet.size());
        System.out.println(bitSet.get(3));
        System.out.println(bitSet.size());
        System.out.println(bitSet.length());
        System.out.println(bitSet.get(bitSet.length() - 1));
        System.out.println(bitSet);
    }

}
