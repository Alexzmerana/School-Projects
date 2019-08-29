import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class WordCounter {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @replaces strSet
     * @ensures strSet = entries(str)
     * @return A set of characters that are separators.
     */
    private static Set<Character> generateElements(String str) {
        Set<Character> foo = new Set1L<Character>();
        for (int i = 0; i < str.length(); i++) {
            if (!foo.contains(str.charAt(i))) {
                foo.add(str.charAt(i));
            }
        }
        return foo;
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        StringBuilder result = new StringBuilder();
        if (separators.contains(text.charAt(position))) {
            for (int i = position; i < text.length()
                    && separators.contains(text.charAt(i)); i++) {
                result.append(text.charAt(i));
            }
        } else {
            for (int i = position; i < text.length()
                    && !separators.contains(text.charAt(i)); i++) {
                result.append(text.charAt(i));
            }
        }
        return result.toString();

    }

    /**
     * Takes in data from text file and puts it in a queue.
     *
     * @param in
     *            SimplerReader.
     * @return a queue of words
     */
    public static Queue<String> dataEntry(SimpleReader in) {
        String separators = " .,?!\t ";

        Set<Character> separatorsSet = generateElements(separators);
        Queue<String> words = new Queue1L<String>();
        while (!in.atEOS()) {
            int position = 0;
            String line = in.nextLine();
            while (position < line.length()) {
                String data = nextWordOrSeparator(line, position,
                        separatorsSet);
                if (!data.contains(pp)) {
                    words.enqueue(data);
                }
                position += data.length();
            }
        }

        return words;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L("data/getty.txt");
        SimpleWriter out = new SimpleWriter1L();
        Queue<String> words = dataEntry(in);
        out.println(words.length());
        for (int i = 0; i < words.length(); i++) {
            out.print(words.dequeue());
        }
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
