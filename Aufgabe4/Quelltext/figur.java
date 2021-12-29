/**
 * figur
 */
public class figur {
    private int position;
    private boolean isPlayerOne;
    public figur(boolean pIsPlayerOne){
        position = -1;
        isPlayerOne = pIsPlayerOne;
    }
    
    public void setPosInSteps(int step){
        position += step;
    }

    public void setPos(int step){
        position = step;
    }

    public int getPos(){
        return position;
    }

    public boolean isPlayerOne(){
        return isPlayerOne;
    }

   // public void setisPlayerOne(boolean isInThere){
   //     isPlayerOne = isInThere;
   // }
}