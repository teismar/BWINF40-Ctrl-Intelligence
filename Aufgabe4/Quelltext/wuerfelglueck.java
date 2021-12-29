import java.io.*;

/**
 * wuerfelglueck
 */
public class wuerfelglueck {
    static wuerfel[] dices; // Speichert alle Würfel
    static messwerte[] finalResults; // Speichert die Finalen Statistiken der Würfel
    static int maxSims; // Die Anzahl an Simulationen

    public static void main(String[] args) {
        maxSims = Integer.valueOf(args[1]);
        initializeValuesFromFile(args[0]);
        spielfeld simulations = new spielfeld(); // Die Simulationsebene fuer "Mensch aergere dich nicht"

        // Jeden Wuerfel durchgehen: (n * (n-1)) Kombinationen
        for (int i = 0; i < dices.length; i++) { // Spieler 1
            messwerte[] m = new messwerte[2];
            m[0] = new messwerte(dices[i], 0, 0, 0, 0);
            m[1] = new messwerte(dices[i], 0, 0, 0, 0);
            for (int j = 0; j < dices.length; j++) { // Spieler 2
                if (i != j) {
                    wuerfel[] temp = { dices[i], dices[j] };
                    gameresult[] result = simulations.simulateNGames(temp, maxSims);
                    System.out.println("In " + maxSims + " Simulationen, spielten ein D" + temp[0].getSides() + "("
                            + temp[0].getFacesInString() + "), Spieler 1," + " und ein D" + temp[1].getSides() + "("
                            + temp[1].getFacesInString() + ")" + ", Spieler 2, miteinander.");
                    m = evaluateSimulation(result, temp); // Erechnet von allen Ergebnissen der Simulation einfach die
                                                          // Summen.
                    finalResults[j].setWuerfel(dices[j]);
                    finalResults[j].addToGames(m[1].getGames());
                    finalResults[j].addToRounds(m[1].getRounds());
                    finalResults[j].addToWins(m[1].getWins());
                    finalResults[j].addToWinsAB(m[1].getWinsAB());
                    // Statistiken der einzelnen Spiele den Wuerfeln zuschreiben
                    finalResults[i].setWuerfel(dices[i]);
                    finalResults[i].addToGames(m[0].getGames());
                    finalResults[i].addToRounds(m[0].getRounds());
                    finalResults[i].addToWins(m[0].getWins());
                    finalResults[i].addToWinsAB(m[0].getWinsAB());
                }
            }

        }

        printFinalResults(finalResults); // Gibt einfach die Mitterlwerte der Ergebnisse aus und bestimmt die
                                         // Wahrscheinlichkeiten sowie den Besten Wuerfel
    }

    private static void printFinalResults(messwerte[] data) {
        System.out.println("\n\n\n\n");

        messwerte highestWinRate = data[0];

        for (messwerte m : data) { // Pures Ausgabenmanagement
            float p = (float) m.getWinsAB() / (float) m.getWins() * 100;
            System.out.println("Der Wuerfel, D" + m.getWuerfel().getSides() + " (" + m.getWuerfel().getFacesInString()
                    + "; Erwartungswert:" + String.format("%.2f", m.getWuerfel().getExpectedValue()) + "; Varianz:"
                    + String.format("%.2f", m.getWuerfel().getVariance()) + ") hat folgenden Werte erzielen koennen:");
            System.out.println("Anzahl der Spiele: " + m.getGames());
            System.out.println(
                    "Anzahl der durchschnittlichen Runden um das Spiel zu beenden: " + (m.getRounds() / m.getGames()));
            System.out.println(
                    "Gewinne: " + m.getWins() + ", das waeren: " + (m.getWins() / (float) m.getGames()) * 100 + "%");
            if (!(m.getWinsAB() == 0 || m.getWins() == 0))
                System.out.println(
                        "Gewinne bei denen man angefangen hat: " + m.getWinsAB() + ", das waeren: " + p + "% \n\n");
            else
                System.out.println("\n\n");
            if ((m.getWins() / (float) m.getGames())
                    * 100 > (highestWinRate.getWins() / (float) highestWinRate.getGames()) * 100)
                highestWinRate = m;
        }

        System.out.println("Es stellt sich also heraus, dass der D" + highestWinRate.getWuerfel().getSides() + " ("
                + highestWinRate.getWuerfel().getFacesInString() + "; Erwartungswert:"
                + String.format("%.2f", highestWinRate.getWuerfel().getExpectedValue()) + "; Varianz:"
                + String.format("%.2f", highestWinRate.getWuerfel().getVariance()) + ") mit einer Siegeschance von: "
                + (highestWinRate.getWins() / (float) highestWinRate.getGames()) * 100
                + "% am besten ist, um das Spiel zu gewinnen.");
    }

    private static messwerte[] evaluateSimulation(gameresult[] result, wuerfel[] w) { // Rechnet die Werte einzelner
                                                                                      // Experimente zusammen
        float winsPlayer1 = 0, winsPlayer2 = 0;
        float winsWithBeginningPlayer1 = 0, winsWithBeginningPlayer2 = 0;
        float avrgTurns = 0;
        int games = 0;
        for (gameresult r : result) {
            if (r.getWinner() != 2) {
                avrgTurns += r.getTurns();
                if (r.getWinner() == 1)
                    winsPlayer1 += 1;
                else if (r.getWinner() == 0)
                    winsPlayer2 += 1;
                if (r.getWinner() == 1 && r.getBeginner())
                    winsWithBeginningPlayer1 += 1;
                if (r.getWinner() == 0 && r.getBeginner())
                    winsWithBeginningPlayer2 += 1;
                games += 1;
            } else {
                games += 1;
            }
        }
        if (games != result.length && (!w[0].hasAOne() && !w[1].hasAOne())) {
            System.out.println(
                    "Leider ist dieses Spiel nicht immer moeglich. Man kann nur zuverlaessig gewinnen solange mindestens ein Wuerfel mindestens eine Seite mit dem Wert eins hat. Nur "
                            + games + ", konnten durchgespielt werden.");
            if (games == 0) {
                messwerte[] m = new messwerte[2];
                m[0] = new messwerte(null, 0, 0, games, avrgTurns);
                m[1] = new messwerte(null, 0, 0, games, avrgTurns);
                return m;
            }
        } else if (!w[0].hasASix()) {
            System.out.println(
                    "Leider ist dieses Spiel unmoeglich. Man kann nur gewinnen solange ein Wuerfel mindestens eine Seite mit dem Wert sechs hat.");
        }
        System.out.println("Die Spiele wurden in durchschnittlich " + (avrgTurns / games) + " Runden beendet.");
        System.out.println("Spieler 1 hatte mit " + winsPlayer1 + " siegen, eine Siegeschance von "
                + (winsPlayer1 / games) * 100 + "% von welchen " + (winsWithBeginningPlayer1 / winsPlayer1) * 100
                + "% gewonnen wurden, wo Spieler 1 begonnen hatte.\n");

        messwerte[] m = new messwerte[2];
        m[0] = new messwerte(null, (int) winsPlayer1, (int) winsWithBeginningPlayer1, games, avrgTurns);
        m[1] = new messwerte(null, (int) winsPlayer2, (int) winsWithBeginningPlayer2, games, avrgTurns);
        return m;
    }

    public static void initializeValuesFromFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            String line;
            line = reader.readLine();
            int n = Integer.valueOf(line); // Anzahl der Wuerfel wird ausgelesen (1. Zeile)
            dices = new wuerfel[n];
            System.out.println("Es gibt: " + n + " Wuerfel \n\n");

            for (int i = 0; i < n; i++) {
                line = reader.readLine();
                String[] elements = line.split(" ");
                int[] faces = new int[elements.length - 1];

                for (int j = 0; j < faces.length; j++) {
                    faces[j] = Integer.valueOf(elements[j + 1]); // Die einzelnen Zahlenwerte einer Seite eines Wuerfels wird Festgelegt
                }

                dices[i] = new wuerfel(Integer.valueOf(elements[0]), faces); // Die Anzahl der Seiten des Wuerfels
            }

            reader.close();

            finalResults = new messwerte[dices.length];
            for (int i = 0; i < finalResults.length; i++) {
                finalResults[i] = new messwerte(null, 0, 0, 0, 0); // Initialisierung
            }
        } catch (IOException e) {
            System.out.println("Es gab ein Fehler die Datei zu lesen");
        }
    }
    /*
     * ~~~ Anfang Jeder Spieler hat 4 Steiner einer Farbe, Am anfang einer auf Feld
     * A der Rest auf Feld B 1x dann wuerfeln, hoechste Zahl faengt an, ab dann im
     * Uhrzeigersinn
     * 
     * ~~~ Spielablauf Spieler der dran ist wuerfelt und bewegt Figur um zahl nach
     * vorne (kann ueber eigene und gegnerische Figuren gehen) Wer mehrere
     * Spielsteine auf der Laufbahn stehen hat, muss mit dem vordersten Stein
     * ziehen, der gezogen werden kann. Falls man auf gegnerischem Feld landet, wird
     * die gegnerische Figur ins B feld geschickt. Eigene werden nicht ins B feld
     * geschickt, falls man auf eigene landet, muss eine andere vorgeschickt werden
     * Wenn kein Stein bewegt werden kann, verfaellt der Zug. Solange Steine in B
     * Feld, muss das A Feld schnellstmoeglich geraeumt werden Wenn eine 6 gewuerfelt
     * wird kommen Steine vom B Feld auf A Feld (Muss ins Spiel gebracht werden
     * solange noch nicht leer) --> Ist eine eigene Figur zu dem Zeitpunkt auf A,
     * muss diese mit 6 fortbewegt werden, falls gegner darauf wird er geschlagen
     * (in sein B Feld) Wenn 6 nochmal wuerfeln, wenn nochmal 6 nochmal wuerfeln
     * (Falls B Feld leer, darf er die Figur um 6 bewegen) Wenn mit 6 alle
     * Spielsteine im Spiel muss nicht nochmal gewuerfelt werden Wenn eine Runde
     * gelaufen kann aufs Zielfeld (fall direkt vor zielfeldreihe kommt man mit 1
     * auf a, mit 2 auf b, ...) Falls Zielfelder voll, gewonnen
     * 
     */
}