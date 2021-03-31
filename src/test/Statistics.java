package test;

public class Statistics {
    public double avgSpeed;
    public double flow;
    public double density;
    public int boardLength;
    public int boardHeight;
    public double p;
    public int iterations;
    public int countShift;

    public Statistics(
            double density, int boardLength, int boardHeight, double p, int iterations, int countShift){
        this.density = density;
        this.boardLength = boardLength;
        this.boardHeight = boardHeight;
        this.p = p;
        this.iterations = iterations;
        this.countShift = countShift;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "avgSpeed=" + avgSpeed +
                ", flow=" + flow +
                ", density=" + density +
                ", boardLength=" + boardLength +
                ", boardHeight=" + boardHeight +
                ", p=" + p +
                ", iterations=" + iterations +
                ", countShift=" + countShift +
                '}';
    }

    public String[] toCSV() {
        return new String[]{
                String.format("%f", p),
                String.format("%f", density),
                String.format("%f", avgSpeed),
                String.format("%f", flow)
                };
    }
}
