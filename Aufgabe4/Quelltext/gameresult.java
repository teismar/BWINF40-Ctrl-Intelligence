public class gameresult {
    private int winner;
    private int turns;
    private boolean beginner;
    public gameresult(int pTurns, int pWinner, boolean pBeginner){
        winner = pWinner;
        turns = pTurns;
        beginner = pBeginner;
    }

    public int getTurns(){
        return turns;
    }
    public int getWinner(){
        return winner;
    }

    public boolean getBeginner(){
        return beginner;
    }
}





