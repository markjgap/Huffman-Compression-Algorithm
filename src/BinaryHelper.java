/**
 * Mark Gapasin
 * University of San Diego
 * COMP 285
 * Spring 2015
 * Instructor: Gautam Wilkins
 *
 * This class implements helper methods for converting between strings of 1's and 0's and
 * their corresponding byte array representations
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class BinaryHelper {

    /**
     * Converts a list of boolean values into its corresponding bit string.
     * e.g. {true, false, true, true, false} corresponds to the string:
     * "10110"
     *
     * @param bits List of boolean values
     * @return String of 1's and 0's corresponding to the binary representation of bits
     */
    public static String bitStringFromBooleanList(List<Boolean> bits) {
        String boolStr = "";
        for (Boolean bit : bits) {
            if (bit) {
                boolStr = boolStr.concat("1");
            } else {
                boolStr = boolStr.concat("0");
            }
        }
        return boolStr;
    }

    /**
     * Converts a byte into a String of length 8, consisting of 0s and 1s that
     * corresponds to the binary representation of the byte
     *
     * @param b byte to convert
     * @return String of 0's and 1's that is the binary representation of b
     */
    public static String bitStringFromByte(byte b) {
        String tail = Integer.toBinaryString(b & 0xFF).replace(' ', '0');
        int tailLen = tail.length();
        for (int i=0; i<8-tailLen; i++) {
            tail = "0".concat(tail);
        }
        return tail;
    }

    /**
     * Converts an array of bytes into its corresponding bit string. This method is the
     * inverse of the byteArrayFromBitString method. The first byte in byteArray is not
     * treated as part of the string, but instead indicates if any of the bits in the last
     * byte should be ignored.
     *
     * @param byteArray array of bytes
     * @return String of 0's and 1's that is the binary representation of byteArray
     */
    public static String bitStringFromByteArray(byte[] byteArray) {
        String bitString = "";
        for (int i = 1; i < byteArray.length; i++) {
            bitString = bitString.concat(BinaryHelper.bitStringFromByte(byteArray[i]));
        }

        // First byte is a mask which indicates if any bits in the last byte should be ignored.
        // This would happen if we wanted to write a non byte-aligned string
        // (e.g. 0110110 would be stored as 01101100 where the extra zero at the
        // end is needed to make the string byte-aligned. Then the first byte would be
        // 00000001, which indicates that the last bit in the last byte should be ignored.
        int extra = (int)byteArray[0];
        bitString = bitString.substring(0,bitString.length()-extra);

        return bitString;
    }

    /**
     * Converts a string of 0's and 1's into an array of the corresponding bytes.
     *
     * If the length of bitString is not divisible by 8 then it adds extra 0's to the end
     * to make the length a multiple of 8. The first byte in the output array indicates
     * how many zeros (up to 7) that were added.
     *
     */
    public static byte[] byteArrayFromBitString(String bitString) {
        int extra = bitString.length() % 8;
        int numBytes = bitString.length()/8 + (extra == 0 ? 0 : 1);
        byte[] byteArray = new byte[numBytes+1];

        for (int i=0; i < bitString.length()/8; i++) {
            String byteString = bitString.substring(8 * i, 8 * i + 8);
            Integer val = Integer.parseInt(byteString,2);
            val = (val > 127 ? val-256 : val);
            byteArray[i+1] = Byte.parseByte(val.toString(), 10);
        }

        if (extra != 0) {
            int start = 8*(bitString.length()/8);
            String byteString = bitString.substring(start, start + extra);
            for (int i=0; i<8-extra; i++) {
                byteString = byteString.concat("0");
            }

            Integer val = Integer.parseInt(byteString, 2);
            val = (val > 127 ? val-256 : val);
            byteArray[bitString.length() / 8 + 1] = Byte.parseByte(val.toString(),10);
            byteArray[0] = (byte) (8-extra);

        } else {
            byteArray[0] = 0;
        }

        return byteArray;
    }

    /**
     * Converts a String 0's and 1's of length 8 into the corresponding byte value
     */
    public static byte byteFromBitString(String bits) {
        assert bits.length() == 8;
        Integer val = Integer.parseInt(bits,2);
        val = (val > 127 ? val-256 : val);
        return Byte.parseByte(val.toString(), 10);
    }

    /**
     * Returns a String of 1's and 0's of length 8 that represents the binary representation of the
     * character. Assumes that input character is an ASCII character.
     */
    public static String binaryAsciiCodeFromChar(char c) {
        String tail = Integer.toBinaryString(c & 0xFF).replace(' ', '0');
        int tailLen = tail.length();
        for (int i=0; i<8-tailLen; i++) {
            tail = "0".concat(tail);
        }
        return tail;
    }

    /**
     * Writes contents of a byte array to the file specified by fileName
     */
    public static void writeBytesToFile(String fileName, byte[] bytes) {
        File f = new File(fileName);
        try {
            FileOutputStream fs = new FileOutputStream(f);
            fs.write(bytes);
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not write to file: " + fileName);
        }
    }

    /**
     * Opens a file and returns the contents of the entire file as a byte array
     */
    public static byte[] readBytesFromFile(String fileName) {
        File f = new File(fileName);
        ArrayList<Byte> bytes = new ArrayList<Byte>();

        try {
            FileInputStream fr = new FileInputStream(f);
            int nextChar;
            int i = 0;
            while ((nextChar = fr.read()) != -1) {
                bytes.add(i, (byte)nextChar);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Could not read file: " + fileName);
            return null;
        }

        byte[] byteArray = new byte[bytes.size()];
        for (int i=0; i<bytes.size(); i++) {
            byteArray[i] = bytes.get(i);
        }

        return byteArray;
    }



}
