/**
 * User: Grigory
 * Date: 22.11.2014
 */
public class DataItem {
    private long userId;
    private long movieId;
    private long rating;

    public DataItem(long userId, long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public DataItem(long userId, long movieId, long rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public long getUserId() {
        return userId;
    }

    public long getMovieId() {
        return movieId;
    }

    public long getRating() {
        return rating;
    }
}
