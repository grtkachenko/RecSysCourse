import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Grigory
 * Date: 22.11.2014
 */
public class SVD {
    private final static int MAX_ITERATIONS = 10;
    private final static double EPS = 1e-5;
    private final int factorsNum;
    private double mu = 0;
    private double gamma = 0.01;
    private double lambda = 0.01;

    private Map<Long, Double> bu = new HashMap<Long, Double>();
    private Map<Long, double[]> fu = new HashMap<Long, double[]>();

    private Map<Long, Double> bv = new HashMap<Long, Double>();
    private Map<Long, double[]> fv = new HashMap<Long, double[]>();

    public SVD(int factorsNum) {
        this.factorsNum = factorsNum;
    }

    public void learn(List<DataItem> items) {
        double oldRmse = 10;
        double rmse = 0;
        int iteration = 0;
        for (DataItem item : items) {
            addItem(item);
        }

        while (Math.abs(oldRmse - rmse) > EPS) {
            oldRmse = rmse;
            rmse = 0;

            double gl = gamma * lambda;
            for (DataItem curItem: items) {
                long u = curItem.getUserId(), v = curItem.getMovieId(), r = curItem.getRating();
                double err = r - mu - bu.get(u) - bv.get(v) - scalMul(fu.get(u), fv.get(v));
                rmse += err * err;
                double gerr = gamma * err;
                mu += gerr;
                bu.put(u, bu.get(u) + gerr - gl * bu.get(u));
                bv.put(v, bv.get(v) + gerr - gl * bv.get(v));
                for (int i = 0; i < factorsNum; i++) {
                    fu.get(u)[i] += gerr * fv.get(v)[i] - gl * fu.get(u)[i];
                    fv.get(v)[i] += gerr * fu.get(u)[i] - gl * fv.get(v)[i];
                }
            }
            iteration++;
            rmse = Math.sqrt(rmse / items.size());
            gamma *= 0.95;
            System.out.println(iteration + " RMSE " + rmse);
            if (iteration >= MAX_ITERATIONS) {
                break;
            }
        }
    }

    private double scalMul(double[] a, double[] b) {
        double res = 0;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            res += a[i] * b[i];
        }
        return res;
    }

    private void addItem(DataItem item) {
        if (!bu.containsKey(item.getUserId())) {
            bu.put(item.getUserId(), .0);
            fu.put(item.getUserId(), new double[factorsNum]);
        }
        if (!bv.containsKey(item.getMovieId())) {
            bv.put(item.getMovieId(), .0);
            fv.put(item.getMovieId(), new double[factorsNum]);
        }
    }

    public double getRating(DataItem item) {
        addItem(item);
        return mu + bu.get(item.getUserId()) + bv.get(item.getMovieId()) +
                scalMul(fu.get(item.getUserId()), fv.get(item.getMovieId()));
    }

}
