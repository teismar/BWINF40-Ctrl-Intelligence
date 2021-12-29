import java.util.Random;

/**
 * wuerfel
 */
public class wuerfel {
    Random rand = new Random();
    private int sides;
    private int[] faces;

    public wuerfel(int pSides, int[] pFaces) {
        sides = pSides;
        faces = pFaces;
        //printInformation();
    }

    public void printInformation() {
        System.out.println("Der Würfel hat " + sides + " Seiten:");
        for (int i = 0; i < faces.length; i++) {
            System.out.print(" " + faces[i]);
        }
        System.out.println();
    }

    public int getSides() {
        return sides;
    }

    public int[] getFaces() {
        return faces;
    }

    public int getRandomValue() {
        return faces[rand.nextInt(faces.length)];
    }

    public boolean hasASix(){
        boolean hasOne = false;
        for (int e : this.getFaces()) {
            if(e == 6)
            hasOne = true;
        }
        return hasOne;
    }

    public boolean hasAOne(){
        boolean hasOne = false;
        for (int e : this.getFaces()) {
            if(e == 1)
            hasOne = true;
        }
        return hasOne;
    }

    public String getFacesInString() {
        int[] sidesPlayer1 = this.getFaces();
        String facesPlayer1 = "";
        for (int k = 0; k < this.getSides(); k++) {
            facesPlayer1 += "," + Integer.toString(sidesPlayer1[k]);
        }
        return facesPlayer1.substring(1,facesPlayer1.length());
    }

    public float getExpectedValue(){
        float m = 0;
        for (int i : faces) {
            m+= i;
        }
        return (m / faces.length);
    }

    public double getVariance(){
        float m = 0;
        float c = this.getExpectedValue();
        for (int i : faces) {
            m += Math.pow((i-c),2);
        }
        return Math.sqrt(m / faces.length);
    }
}

