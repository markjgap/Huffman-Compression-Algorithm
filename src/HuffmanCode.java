/**
 * This class implements Huffman codes for files containing ASCII characters. It can read and compress a file and also
 * decode a compressed file and covert it back to its original ASCII characters.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HuffmanCode{

    /**
     * Opens a file containing ascii characters and returns them as an ArrayList of characters (e.g.
     * if the file contents were: "abc\nd ef" then the resulting ArrayList would be:
     * {'a', 'b', 'c', '\n', 'd', ' ', 'e', 'f'}
     */
    public static ArrayList<Character> charactersFromFile(String fileName) {
        ArrayList<Character> fileChars = new ArrayList<Character>();
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int next;
            while ((next = fr.read()) != -1) {
                Character current = (char) next;
                fileChars.add(current);
            }

        } catch (IOException e) {
            System.out.println("Could not open file: " + fileName);
            System.exit(1);
        }
        return fileChars;
    }

    /**
     * Takes an array of characters and returns a HashMap where each key is a character in the array
     * the value is how many times that character appears in the array. (e.g. for {a, b, c, a, a, d, c}
     * the HashMap would be:
     * {a->3, b->1, c->2, d->1}
     */
    public static HashMap<Character, Integer> frequenciesFromArray(ArrayList<Character> charArray) {
        HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
        for(Character c : charArray){
            if(freq.containsKey(c)){
                freq.put(c, freq.get(c) + 1);
            }
            else{
                freq.put(c, 1);
            }
        }
        return freq;
    }

    /**
     * Takes a HashMap with character frequencies and generates a HuffmanTree to optimally encode
     * the individual characters.
     */
    public static HuffmanTree generateCodingTree(HashMap<Character, Integer> charFreq) {
        // priority queue sorts its HuffmanTree according to each nodes value(freq).
        // treeCompare method implements this comparison. See method for implementation details.
        PriorityQueue<HuffmanTree> q = new PriorityQueue<HuffmanTree>(treeCompare);

        HuffmanTree finalTree = new HuffmanTree(); // HuffmanTree to return after construction

        for(Map.Entry<Character, Integer> arr : charFreq.entrySet()){
            Character c = arr.getKey(); // ascii character
            Integer frequency = arr.getValue(); // number of times character found
            HuffmanTree root = new HuffmanTree(c, frequency); // create HuffmanTree for each character
            q.add(root);
        }
        // construct a HuffmanTree
        while(!q.isEmpty()){
            // remove HuffmanTrees with 2 smallest frequencies according to priority queue
            HuffmanTree leftTree = q.remove();
            HuffmanTree rightTree = q.remove();
            // merge and create a new tree with 2 smallest HuffmanTrees as left and right child
            HuffmanTree parent = HuffmanTree.merge(leftTree, rightTree);
            // if q is empty HuffmanTree construction is done
            if(q.isEmpty()){
                finalTree = parent;
            }
            // if q is not empty add constructed HuffmanTree back to priority queue
            else {
                q.add(parent);
            }
        }
        return finalTree;
    }

    /**
     * Used when adding HuffmanTrees to priority queue by comparing freq attribute.
     * They are sorting where the lowest freq has the highest priority creating a ascending queue.
     * Overrides Comparator of priority queue to handle HuffmanTrees.
     */
    public static Comparator<HuffmanTree> treeCompare = new Comparator<HuffmanTree>() {
        @Override
        public int compare(HuffmanTree o1, HuffmanTree o2) {
            // compareTo method in HuffmanTree class
            return o1.compareTo(o2);
        }
    };

    /**
     * Takes an ArrayList of characters and a HashMap of character encodings and then
     * generates the String encoding for charArray (e.g. if
     * charArray = {a, b, c, c, a, a}
     * and codeMap = {a->{true}, b->{false, true, false}, c->{false, false}}
     * then the resulting string will be:
     * 1010000011, where
     *
     * 1 010 00 00 1 1
     * a  b  c  c  a a
     */
    public static String encodeCharacters(ArrayList<Character> charArray, HashMap<Character, ArrayList<Boolean>> codeMap) {
        String bitString = "";
        for (Character c : charArray) {
            String cstring = BinaryHelper.bitStringFromBooleanList(codeMap.get(c));
            bitString = bitString.concat(cstring);
        }
        return bitString;
    }

    /**
     * Helper method to print character encodings in a quickly readable format
     */
    public static void printEncoding(HashMap<Character, ArrayList<Boolean>> encoding) {
        for (Map.Entry<Character, ArrayList<Boolean>> entry : encoding.entrySet()) {
            System.out.println(entry.getKey() + ": " + BinaryHelper.bitStringFromBooleanList(entry.getValue()));
        }
    }

}
