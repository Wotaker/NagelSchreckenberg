package test;

public class ReportEngine {

    public Statistics[] runBoothTests(double density, int iterations, double p,
                                      int maxSpeed, int length, int height, int countShift){

        Statistics statisticsMultilane =
                new Statistics(density, length, height, p, iterations, countShift);
        Statistics statisticsSinglelane =
                new Statistics(density, length, height, p, iterations, countShift);

        BoardTest boardMultilane =
                new BoardTest(p, maxSpeed, length, height);
        BoardTestBasic boardSinglelane =
                new BoardTestBasic(p, maxSpeed, length, height);
        boardMultilane.initialize();
        boardSinglelane.initialize();

        int cars = (int)(density*length*height);
        boardMultilane.placeCars(cars);
        boardSinglelane.placeCars(cars);

        for (int i = 0; i < iterations + countShift; i+=1){
            if (i == countShift) {
                boardMultilane.countFlag = true;
                boardSinglelane.countFlag = true;
            }
            boardMultilane.iteration();
            boardSinglelane.iteration();
        }
        statisticsMultilane.avgSpeed = boardMultilane.getVelocitySum()/iterations;
        statisticsMultilane.flow = boardMultilane.getCarsPassed()/iterations;
        statisticsSinglelane.avgSpeed = boardSinglelane.getVelocitySum()/iterations;
        statisticsSinglelane.flow = boardSinglelane.getCarsPassed()/iterations;
        boardMultilane.clear();
        boardSinglelane.clear();
        Statistics[] result = {statisticsMultilane, statisticsSinglelane};
        return result;
    }

    public Statistics runTest(
            boolean multiLane,
            double density,
            int iterations,
            double p,
            int maxSpeed,
            int length,
            int height,
            int countShift
    ) {
        Statistics statistics =
                new Statistics(density, length, height, p, iterations, countShift);

        int cars = (int)(density*length*height);

        // Multi-Lane case
        if (multiLane){
            BoardTest board =
                    new BoardTest(p, maxSpeed, length, height);
            board.initialize();
            board.placeCars(cars);
            for (int i = 0; i < iterations + countShift; i+=1){
                if (i == countShift) {
                    board.countFlag = true;
                }
                board.iteration();
            }
            statistics.avgSpeed = board.getVelocitySum()/iterations;
            statistics.flow = board.getCarsPassed()/iterations;
            board.clear();
        }

        // Single-Lane case
        else {
            BoardTestBasic boardBasic =
                    new BoardTestBasic(p, maxSpeed, length, height);
            boardBasic.initialize();
            boardBasic.placeCars(cars);
            for (int i = 0; i < iterations + countShift; i+=1){
                if (i == countShift) {
                    boardBasic.countFlag = true;
                }
                boardBasic.iteration();
            }
            statistics.avgSpeed = boardBasic.getVelocitySum()/iterations;
            statistics.flow = boardBasic.getCarsPassed()/iterations;
            boardBasic.clear();
        }

        return statistics;
    }

}
