# Huffman-Compression-Algorithm

A Huffman Coding is a technique that creates and assigns a unique prefix-free code to a unique character/symbol that occurs in our input.  Huffman Coding compresses data by replacing each fixed length input symbol/character with a corresponding variable length prefix-free output. This algorithm creates a table based on the frequency of occurrence for each symbol/character. Using this table we can generate a Huffman Tree that basically is a binary tree of nodes using a priority queue to start construction. Using the smallest or less frequent symbol we construct our Huffman Tree, which becomes our “encoder”. The compression happens because this encoder will give us the prefix-free binary code that would represent each symbol in our input. 

The HuffmanCode.frequenciesFromArray method takes in an array of ASCII symbols and generates a frequency table. Using a map structure to create our table, our method iterates through each character in the array and creates a mapping where the key is the character and value how many times it finds this character. Each time it comes across a symbol it adds it to our map or if that symbol has already been mapped it increases its frequency value by 1.  In the end we should have a list of characters and how many times it occurs in our array.

The HuffmanCode.generateCodingTree method then takes our frequency table and creates a HuffmanTree holding values of character and frequency. Using a priority queue, it then stores each newly created HuffmanTree in ascending order by it frequency of occurrence.  Once our priority queue is filled up we can now construct a larger HuffmanTree with the elements from our priority queue. Taking the two smallest HuffmanTree values in our queue it combines then together as left and right children under a parent whose frequency is the combine value of its children. With this tree you can construct our binary code similar to our original array of ASCII symbols but in a compressed form. This compression happens because it constructs a code using the minimal amount of binary strings needed to represent the character using a prefix-free technique.

The HuffmanTree.decodeBinaryString method does the opposite of our generateCodingTree method, which instead of creating our coding it decodes our HuffmanTree. Using the prefix-free binary code we created using the HuffmanTree, we can now traverse our structure and decode. We take our binary string and follow it, with the notion that 0 means to go left and 1 go right we follow it until we get to the node holding a the character value. Once we find out that character we start again at the top of the HuffmanTree and continue decoding until we have gone through the entire binary string. You can view this process where the HuffmanTree is our map and our binary string is the directions. In the end the characters decoded should be equivalent to the original array of characters used before compression.

