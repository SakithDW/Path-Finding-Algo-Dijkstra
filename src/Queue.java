import java.util.List;

public class Queue implements Comparable<Queue> {
    int distance;
    int row;
    int col;
    List<Integer[]> coordinates;

    public Queue(int distance, int row, int col, List<Integer[]> coordinates) {
        this.distance = distance;
        this.row = row;
        this.col = col;
        this.coordinates = coordinates;
    }


    @Override
    public int compareTo(Queue Queue) {
        return Integer.compare(distance,Queue.distance);
    }
}
