import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Schiebeparkplatz {
    static String Datapath="";
    static char[][]al;
    static int i=1;
    static int R=0;
    static int L=0;
    static int rUsedCars=0;
    static int lUsedCars=0;
    public static void main(String[] args) throws IOException {
        Datapath=args[0];
        char[][] parkinglot=convertToArray();
        loesen(parkinglot);
    }
    public static String readLine(String fileLoc, int lin) throws IOException {
        String line1 = Files.readAllLines(Paths.get(fileLoc)).get(lin);
        return line1;
    }
    public static String[] splitLine(String i){
        String[] nachrichtTeil = i.split(" ");
        return nachrichtTeil;
    }
    public static char[][] convertToArray() throws IOException {  //Gibt ein 2 Dimensionales char Array zurück, in dessen 1. Dimension
        String[] in;                                              //die Autos von A bis zum Festgelegten letzten Buchstaben sind.
        in = splitLine(readLine(Datapath, 0));                //in der 2. Dimension sind die Querstehenden Autos gespeichert
        for (char a = 'A', z = in[1].charAt(0); a < z; a++) {     //ein leerer Punkt im Array wird mit einer 0 gefüllt
            i++;
        }
        char[][] lot = new char[i][2];
        al=new char[i][2];
        for (int t = 0; t < i; t++) {
            lot[t][0]=0;
            lot[t][1]=0;
        }
        int y = 0;
        char a;
        char z;
        for (a = 'A', z = in[1].charAt(0); a < z; a++) {
            lot[y][0] = a;
            y++;
        }
        lot[y][0] = a;
        int c = Integer.valueOf(readLine(Datapath, 1));
        for (int q = 2; q < c + 2; q++) {
            in = splitLine(readLine(Datapath, q));
            lot[Integer.valueOf(in[1])][1] = in[0].charAt(0);
            lot[Integer.valueOf(in[1]) + 1][1] = in[0].charAt(0);
        }
        return lot;
    }
    public static void loesen(char[][] pl) {                //Geht alle stellen im Array von A-x durch und überprüft ob
        for (int a = 0; a < i; a++) {                       //das Auto raussfahren kann oder blockiert wird
            if (pl[a][1] ==0) {
                System.out.println(pl[a][0] + ":");
            } else {
                setAl(pl);
                System.out.println(pl[a][0] + ":"+ Bewegen(a,pl));
            }
        }
    }
    public static void setAl(char[][] a){                   //Überschreibt das Globale Array al mit a, damit man nicht
        for(int y=0; y<i;y++){                              //mit einem Bearbeiteten Array weiterarbeitet
            al[y][0]=a[y][0];
            al[y][1]=a[y][1];
        }
    }
    public static String Bewegen(int pos, char[][] pl){     //Setzt die Globalen Zählvariablen für bewegeRechts und
        R=0;                                                //bewegeLinks=0 um nach deren aufruf zu vergleichen, welche
        L=0;                                                //von den Beiden weniger Züge, bzw weniger Autos im falle
        rUsedCars=0;                                        //eines Gleichstandes verwendet hat und gibt die
        lUsedCars=0;                                        //Effizientere bzw überhaupt lösbare Version zurück
        setAl(pl);
        String l=bewegeLinks(pos);
        setAl(pl);
        String r=bewegeRechts(pos);
        setAl(pl);
        String y="";
        if(R<L) {y=r;}
        else if(L<R){y=l;}
        else if(L==R && rUsedCars<lUsedCars){y=r;}
        else if(L==R && lUsedCars<rUsedCars){y=l;}
        if(R>100 && L>100){
            y="Error cant solve";
        }
        return y;
    }
    public static String bewegeRechts(int pos){             //bewegeRechts und bewegeLinks tun fast dasselbe
        String y="";                                        //sie versuchen das Auto so weit wie möglich nach
        int move=0;                                         //rechts bzw links zu verschieben um die Gegebene position
        char a=al[pos][1];                                  //frei zu machen und eben danach die gemachten Bewegungen
        try{                                            //als String in der richtigen Formatierung zurück
            while(al[pos][1]!=0){                       //für den fall dass das Auto von einem Anderen Blockiert
                if(al[pos+1][1]==0){                    //kommt es zum rekursiven Aufruf
                    R++;                                //sollte es in die Gegebene Richtung keine Lösung geben weil
                    move++;                             //die Grenzen des Arrays überschritten werden, wird R bzw L
                    shiftRight(pos);                    //gleich Maximum gesetzt damit es in Bewegung nicht ausgewählt werden kann
                }else if(al[pos+1][1]==al[pos][1] && al[pos+2][1]==0){
                    R++;
                    move++;
                    shiftRight(pos);
                }else if(al[pos+1][1]!=0 && al[pos+1][1]!=al[pos][1]){
                    rUsedCars++;
                    y=bewegeRechts(pos+2);
                }else if(al[pos+1][1]==al[pos][1] && al[pos+2][1]!=0){
                    rUsedCars++;
                    y=bewegeRechts(pos+2);
                }if(R>1000){return "N";}
            }
            y=y+" "+a+" "+move+" "+" right";
        }catch(ArrayIndexOutOfBoundsException e){
            R=2000;
            al[pos][1]=0;
            return "N";
        }
        return y;
    }
    public static String bewegeLinks(int pos){
        String y="";
        int move=0;
        char a=al[pos][1];
        try{
            while(al[pos][1]!=0){
                if(al[pos-1][1]==0){
                    L++;
                    move++;
                    shiftLeft(pos);
                }else if(al[pos-1][1]==al[pos][1] && al[pos-2][1]==0){
                    L++;
                    move++;
                    shiftLeft(pos);
                }else if(al[pos-1][1]!=0 && al[pos-1][1]!=al[pos][1]){
                    lUsedCars++;
                    y=bewegeLinks(pos-2);
                }else if(al[pos-1][1]==al[pos][1] && al[pos-2][1]!=0){
                    lUsedCars++;
                    y=bewegeLinks(pos-2);
                }if(L>1000){return "N";}
            }
            y=y+" "+a+" "+move+" "+" left";
        }catch(ArrayIndexOutOfBoundsException e){
            L=2000;
            al[pos][1]=0;
            return "N";
        }
        return y;
    }
    public static void shiftLeft(int position){             //Bewegt das Auto im Array um 1 nach links
        if(al[position][1]==al[position-1][1]){
            al[position-2][1]=al[position][1];
            al[position][1]=0;
        }else if(al[position][1]==al[position+1][1]){
            al[position-1][1]=al[position][1];
            al[position+1][1]=0;
        }
    }
    public static void shiftRight(int position){            //Bewegt das Auto im Array um 1 nach rechts
        if(al[position][1]==al[position+1][1]){
            al[position+2][1]=al[position][1];
            al[position][1]=0;
        }else if(al[position][1]==al[position-1][1]){
            al[position+1][1]=al[position][1];
            al[position-1][1]=0;
        }
    }
}