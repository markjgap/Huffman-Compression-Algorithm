/**
 * This class implements Huffman codes for files containing ASCII characters. It can read and compress a file and also
 * decode a compressed file and covert it back to its original ASCII characters.
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        // Read characters from input file
        ArrayList<Character> characterArrayList = HuffmanCode.charactersFromFile("test4.txt");
        String fileContents = "";
        for (Character c : characterArrayList) {
            fileContents = fileContents.concat(c.toString());
        }

        // Generate frequency table
        HashMap<Character, Integer> freq = HuffmanCode.frequenciesFromArray(characterArrayList);
        // Generate tree that we can derive binary code for each character
        HuffmanTree fullTree = HuffmanCode.generateCodingTree(freq);

        // Generate encodings for the characters
        HashMap<Character, ArrayList<Boolean>> encoding = fullTree.getEncodings();
        String encodedString = HuffmanCode.encodeCharacters(characterArrayList, encoding);

        // Convert binary strings to byte arrays
        byte[] byteArray = BinaryHelper.byteArrayFromBitString(encodedString);
        byte[] huffmanTreeEncoding = HuffmanTree.huffmanTreeToByteArray(fullTree);

        // Write byte arrays to file
        BinaryHelper.writeBytesToFile("test2-htree.txt", huffmanTreeEncoding);
        BinaryHelper.writeBytesToFile("test2-encoded.txt", byteArray);

        // Decode strings from file and check that it matches the original encoded string
        byte[] testByteArray = BinaryHelper.readBytesFromFile("test2-encoded.txt");
        String encodedStringFromFile = BinaryHelper.bitStringFromByteArray(testByteArray);

        byte[] huffmanTreeEncoded = BinaryHelper.readBytesFromFile("test2-htree.txt");
        HuffmanTree treeFromFile = HuffmanTree.huffmanTreeFromByteArray(huffmanTreeEncoded);

        // Take an coded binary string and HuffmanTree of character to re-create original string
        String original = HuffmanTree.decodeBinaryString(encodedStringFromFile, treeFromFile);

        // This should print 0 if implementation is working
        System.out.println(original.compareTo(fileContents));





    }
}
