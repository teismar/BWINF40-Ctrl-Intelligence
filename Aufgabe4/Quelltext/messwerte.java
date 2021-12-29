public class messwerte {
    wuerfel dice;
    int wins,winsAsBeginner,games;
    float rounds;
    public messwerte(wuerfel pDice, int pWins, int pWinsAsBeginner, int pGames, float pRounds){
        dice = pDice; // Zu jedem dieser Variablen, gibt es noch getter und setter
        wins = pWins;
        winsAsBeginner = pWinsAsBeginner;
        games = pGames;
        rounds = pRounds;
    }

    public void addToWins(int i){
        wins = wins + i;
    }
    public void addToWinsAB(int i){
        winsAsBeginner = winsAsBeginner + i;
    }
    public void addToGames(int i){
        games = games + i;
    }
    public void addToRounds(float i){
        rounds = rounds +  i;
    }

    public void setWins(int i){
        wins = i;
    }
    public void setWinsAB(int i){
        winsAsBeginner = i;
    }
    public void setGames(int i){
        games = i;
    }
    public void setRounds(int i){
        rounds = i;
    }

    public int getWins(){
        return wins;
    }
    public int getWinsAB(){
        return winsAsBeginner;
    }
    public int getGames(){
        return games;
    }
    public float getRounds(){
        return rounds;
    }

    public void setWuerfel(wuerfel pW){
        dice = pW;
    }
    public wuerfel getWuerfel(){
        return dice;
    }
}
