import java.util.*;
import java.io.*;

public class Main {
    private static final String ROOT_FODLER = "res";
    private static final String[] INPUT_FILES = {ROOT_FODLER + File.separator + "train.csv",
            ROOT_FODLER  + File.separator + "validation.csv"};
    private static final String TEST_INPUT_FILE = ROOT_FODLER + File.separator + "test-ids.csv";
    private static final String OUTPUT_FILE = ROOT_FODLER + File.separator + "output.csv";

    public void run() {
        try {
            List<DataItem> learnData = new ArrayList<DataItem>();
            for (String curFile : INPUT_FILES) {
                FastScanner in = new FastScanner(new File(curFile));
                in.nextLine();
                while (true) {
                    String nextData = in.nextLine();
                    if (nextData == null) {
                        break;
                    }
                    long[] items = parseLongs(nextData);
                    learnData.add(new DataItem(items[0], items[1], items[2]));
                }
            }
            SVD svd = new SVD();
            svd.learn(learnData);
            PrintWriter out = new PrintWriter(new File(OUTPUT_FILE));
            FastScanner in = new FastScanner(new File(TEST_INPUT_FILE));
            in.nextLine();
            out.println("id,rating");
            while (true) {
                String nextData = in.nextLine();
                if (nextData == null) {
                    break;
                }
                long[] items = parseLongs(nextData);
                int rating = svd.getRating(new DataItem(items[1], items[2]));
                out.println(items[0] + "," + rating);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long[] parseLongs(String s) {
        String[] items = s.split(",");
        long[] result = new long[items.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Long.parseLong(items[i]);
        }
        return result;
    }

    private class FastScanner {
        private BufferedReader br;
        private StringTokenizer st;

        public FastScanner(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public String nextLine() {
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void main(String[] arg) {
        new Main().run();
    }
}