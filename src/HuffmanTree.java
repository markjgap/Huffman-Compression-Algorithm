/**
 * Mark Gapasin
 * University of San Diego
 * COMP 285
 * Spring 2015
 * Instructor: Gautam Wilkins
 *
 * This class implements Huffman trees for use when computing Huffman codes
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class HuffmanTree implements Comparable<HuffmanTree> {


    private int freq;
    private HuffmanTree left;
    private HuffmanTree right;
    private Character value = '\0';
    private boolean isLeaf;
    private int depth;
    private ArrayList<Boolean> code;


    public HuffmanTree(char value, int freq) {
        this.freq = freq;
        this.value = value;
        this.isLeaf = true;
        this.left = null;
        this.right = null;
        this.code = new ArrayList<Boolean>();
    }

    public boolean isLeafNode() {
        return this.isLeaf;
    }

    /**
     * Merges HuffmanTrees left and right under a shared parent, and sets the frequency of the parent
     * to be the sum of the left and right frequencies. Then returns the shared parent.
     */
    public static HuffmanTree merge(HuffmanTree left, HuffmanTree right) {
        return new HuffmanTree(left, right);
    }

    /**
     * Only call this method on a full Huffman Tree. Returns a mapping between Characters and their
     * encodings. The encodings are represented as boolean array lists (e.g. the binary string
     * 00101 would be the boolean arraylist {false, false, true, false, true}
     */
    public HashMap<Character, ArrayList<Boolean>> getEncodings() {

        HashMap<Character, ArrayList<Boolean>> encodings = new HashMap<Character, ArrayList<Boolean>>();
        Stack<HuffmanTree> nodes = new Stack<HuffmanTree>();
        nodes.push(this);
        this.depth = 0;

        while (!nodes.isEmpty()) {
            HuffmanTree next = nodes.pop();

            if (next.right != null) {
                next.right.depth = next.depth + 1;
                next.right.code = new ArrayList<Boolean>(next.code);
                next.right.code.add(next.depth, true);
                nodes.push(next.right);
            }
            if (next.left != null) {
                next.left.depth = next.depth + 1;
                next.left.code = new ArrayList<Boolean>(next.code);
                next.left.code.add(next.depth, false);
                nodes.push(next.left);
            }

            if (next.isLeaf) {
                encodings.put(next.value, next.code);
            }
        }

        return encodings;
    }

    /**
     * Private constructor for use with the merge method
     */
    private HuffmanTree(HuffmanTree left, HuffmanTree right) {
        this.freq = left.freq + right.freq;
        this.isLeaf = false;
        this.left = left;
        this.right = right;
        this.code = new ArrayList<Boolean>();
    }

    /**
     * Private constructor for use with the huffmanTreeFromByteArray method
     */
    HuffmanTree() {
        this.freq = 0;
        this.isLeaf = false;
        this.left = null;
        this.right = null;
        this.code = new ArrayList<Boolean>();
    }

    /**
     * Comparison method: Sorts HuffmanTrees based on increasing frequency
     */
    public int compareTo(HuffmanTree toCompare) {

        if (this.freq > toCompare.freq) {
            return 1;
        } else if (this.freq < toCompare.freq) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Computes a compact binary representation of a HuffmanTree to be stored on a file system
     */
    public static byte[] huffmanTreeToByteArray(HuffmanTree root) {

        Stack<HuffmanTree> path = new Stack<HuffmanTree>();
        path.push(root);

        String bitString = "";

        while (!path.isEmpty()) {
            HuffmanTree next = path.pop();
            if (next.right != null) {
                assert next.left != null;
                path.push(next.right);
                path.push(next.left);

                bitString = bitString.concat("0");
            } else {
                assert next.isLeaf;
                String code = BinaryHelper.binaryAsciiCodeFromChar(next.value);
                bitString = bitString.concat("1".concat(code));
            }
        }
        return BinaryHelper.byteArrayFromBitString(bitString);
    }

    /**
     * Takes the byte array output from huffmanTreeToByteArray and reconstructs the original HuffmanTree
     */
    public static HuffmanTree huffmanTreeFromByteArray(byte[] byteArray) {

        HuffmanTree root = null;
        String bitString = BinaryHelper.bitStringFromByteArray(byteArray);
        Stack<HuffmanTree> path = new Stack<HuffmanTree>();

        for (int i=0; i < bitString.length(); i++) {
            Character c = bitString.charAt(i);
            if (c == '0') {
                HuffmanTree newNode = new HuffmanTree();

                if (root == null) {
                    root = newNode;
                } else if (path.isEmpty()) {
                    break;
                } else if (path.peek().left == null) {
                    path.peek().left = newNode;
                } else if (path.peek().right == null) {
                    path.pop().right = newNode;
                }
                path.push(newNode);

            } else {
                // Read next 8 bits and get character
                String byteString = bitString.substring(i + 1, i + 9);
                //byte[] charBytes = BinaryHelper.byteArrayFromBitString(byteString);
                //char value = (char) charBytes[0];
                char value = (char) BinaryHelper.byteFromBitString(byteString);
                HuffmanTree newNode = new HuffmanTree(value, 0);

                if (root == null) {
                    root = newNode;
                } else if (path.isEmpty()) {
                    break;
                } else if (path.peek().left == null) {
                    path.peek().left = newNode;
                } else if (path.peek().right == null) {
                    path.pop().right = newNode;
                }
                i += 8;
            }
        }
        return root;
    }


    /**
     * Helper method that returns true if tree1 and tree2 will generate identical Huffman Codes
     */
    public static boolean areTreesEquivalent(HuffmanTree tree1, HuffmanTree tree2) {

        Stack<HuffmanTree> s1 = new Stack<HuffmanTree>();
        Stack<HuffmanTree> s2 = new Stack<HuffmanTree>();

        s1.push(tree1);
        s2.push(tree2);

        while (true) {

            if (s1.isEmpty()) {
                if (s2.isEmpty()) {
                    break;
                } else {
                    return false;
                }
            }

            HuffmanTree s1next = s1.pop();
            HuffmanTree s2next = s2.pop();

            if (s1next.value != s2next.value) {
                return false;
            }

            if (s1next.left != null) {
                if (s2next.left == null) {
                    return false;
                }
                s1.push(s1next.left);
                s2.push(s2next.left);
            }

            if (s1next.right != null) {
                if (s2next.right == null) {
                    return false;
                }
                s1.push(s1next.right);
                s2.push(s2next.right);
            }
        }
        return true;
    }


    /**
     * Takes a binaryString that represents characters encoded using codingTree and reconstructs the original String.
     */
    public static String decodeBinaryString(String binaryString, HuffmanTree codingTree) {
        HuffmanTree currentNode = codingTree;
        char[] binStr = binaryString.toCharArray();
        String decoded = "";
        for (int i = 0; i < binStr.length; i++){
            char bit = binStr[i]; // helps navigate tree. 0 is left, 1 is right.
            // go left
            if(bit == '0'){
                currentNode = currentNode.left;
            }
            // bit == 1, go right
            else if(bit == '1'){
                currentNode = currentNode.right;
            }
            // isLeaf means that node holds a character and we are at the bottom of the tree
            if(currentNode.isLeaf){
                String value = currentNode.value.toString(); // change character to string
                decoded += value; // adds character to string
                currentNode = codingTree; //reset back to top
            }
        }
        return decoded;
    }
}
