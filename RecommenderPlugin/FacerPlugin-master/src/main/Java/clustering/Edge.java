package clustering;

public class Edge {
    private int mid1;
    private int mid2;
    private double score;

    public Edge(int mid1, int mid2, double score) {
        this.mid1 = mid1;
        this.mid2 = mid2;
        this.score = score;
    }

    public int getMid1() {
        return mid1;
    }

    public void setMid1(int mid1) {
        this.mid1 = mid1;
    }

    public int getMid2() {
        return mid2;
    }

    public void setMid2(int mid2) {
        this.mid2 = mid2;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
