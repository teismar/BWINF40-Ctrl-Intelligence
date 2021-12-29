public class spielfeld {
    figur[] player1 = new figur[4]; // Spielfiguren
    figur[] player2 = new figur[4];
    boolean player1Turn;
    wuerfel[] dices;

    public spielfeld() {

    }

    public gameresult[] simulateNGames(wuerfel[] pDices, int simulations) {
        dices = pDices;
        gameresult[] result = new gameresult[simulations];
        for (int i = 0; i < result.length; i++) {
            result[i] = playGame(pDices, ((i % 2) == 0) ? true : false); // etwa 50% fängt der eine und 50% der andere
                                                                         // an
        }
        return result;
    }

    public gameresult playGame(wuerfel[] dicesOfEachPlayer, boolean beginner) { // 0: Spieler1, 1: Spieler2
        if (!dicesOfEachPlayer[0].hasASix()) {
            return new gameresult(0, 0, beginner);
        }
        int turn = 0;
        boolean gameIsFinished = false;

        determineBeginner(dicesOfEachPlayer);
        player1Turn = beginner;
        player1[0].setPos(0); // Startposition der Spielerfigur
        player2[0].setPos(0); // Startposition der Spielerfigur

        long start = System.currentTimeMillis();
        while (!gameIsFinished) {
            if (System.currentTimeMillis() - start > 20) // Fall das Spiel nicht nach 20 Millisekunden Fertig ist wird
                                                         // dieses abgebrochen und keiner Gewinnt (zB. 3 Figuren sind
                                                         // schon im Ziel und es gibt keine Zahl die Gewürfelt werden
                                                         // kann um die Letzte Figur ins Ziel zu bringen)
                return new gameresult(0, 2, beginner);
            turn++;
            if (player1Turn) {
                int roll = dicesOfEachPlayer[0].getRandomValue();
                if (roll == 6) { // Falls eine 6 gewürfelt wurde; Alle Bedingungen werden hier ohne EndTurn() abgeschlossen, so kann man wieder Würfeln
                    if (AIsFree(player1) != 10) { // Falls Sein A Feld von eigenen Steinen belegt ist
                        if (Move(player1[AIsFree(player1)], roll))
                            continue; // Falls Stein auf A wird dieser um 6 bewegt, wenn nicht möglich dann wird der
                                      // nächstbeste bewegt
                        else if (!makeMove(player1, roll)) {
                            continue;
                        }
                    } else { // Wenn A nicht von Eigenen Steinen belegt ist
                             // Versuche Figur die auf a ist um 6 zu bewegen
                        if (getFigureFromB(player1) != 10) { // Wenn auf B mind. ein Spieler ist
                            if (enemyIsNotOnA(player1) == 10) { // Kein Gegner auf A
                                player1[getFigureFromB(player1)].setPos(0);
                                continue;
                            } else // Falls auf A ein Gegner ist, schicke diesen in sein B feld und setze einen aus
                                   // eigenem auf A
                            {
                                player2[enemyIsNotOnA(player1)].setPos(-1);
                                player1[getFigureFromB(player1)].setPos(0);
                                continue;
                            }
                        } else {
                            if (!makeMove(player1, roll)) {
                                continue;
                            } // Bewege nächstbesten
                        }
                    }
                } else { // Wenn was anderes als eine 6 gewürfelt wird
                    if (AIsFree(player1) != 10) { // Falls Sein A Feld von eigenen Steinen belegt ist
                        if (!Move(player1[AIsFree(player1)], roll))
                            if (!makeMove(player1, roll)) {
                                endTurn();
                                continue;
                            }
                    } else {
                        if (!makeMove(player1, roll)) {
                            endTurn();
                            continue;
                        }
                    }
                }
                if (roll != 6)
                    endTurn();
            } else { // Spieler 2
                int roll = dicesOfEachPlayer[1].getRandomValue();
                if (roll == 6) { // Falls eine 6 gewürfelt wurde; Alle Bedingungen werden hier ohne EndTurn() abgeschlossen, so kann man wieder Würfeln
                    if (AIsFree(player2) != 10) { // Falls Sein A Feld von eigenen Steinen belegt ist
                        if (Move(player2[AIsFree(player2)], roll))
                            continue; // Falls Stein auf A wird dieser um 6 bewegt, wenn nicht möglich dann wird der
                                      // nächstbeste bewegt
                        else if (!makeMove(player2, roll)) {
                            continue;
                        }
                    } else { // Wenn A nicht von Eigenen Steinen belegt ist
                             // Versuche Figur die auf a ist um 6 zu bewegen
                        if (getFigureFromB(player2) != 10) { // Wenn auf B mind. ein Spieler ist
                            if (enemyIsNotOnA(player2) == 10) { // Kein Gegner auf A
                                player2[getFigureFromB(player2)].setPos(0);
                                continue;
                            } else // Falls auf A ein Gegner ist, schicke diesen in sein B feld und setze einen aus
                                   // eigenem auf A
                            {
                                player1[enemyIsNotOnA(player2)].setPos(-1);
                                player2[getFigureFromB(player2)].setPos(0);
                                continue;
                            }
                        } else {
                            if (!makeMove(player2, roll)) {
                                continue;
                            } // Bewege nächstbesten
                        }
                    }
                } else { // Wenn was anderes als eine 6 gewürfelt wird
                    if (AIsFree(player2) != 10) { // Falls Sein A Feld von eigenen Steinen belegt ist
                        if (Move(player2[AIsFree(player2)], roll))
                            continue; // Falls Stein auf A wird dieser um 6 bewegt, wenn nicht möglich dann wird der
                                      // nächstbeste bewegt
                        else if (!makeMove(player2, roll)) {
                            endTurn();
                            continue;
                        }
                    } else {
                        if (!makeMove(player2, roll)) {
                            endTurn();
                            continue;
                        }
                    }
                }
                if (roll != 6)
                    endTurn();
            }
            if (hasWon() >= 10)
                gameIsFinished = true;
        }
        return new gameresult(turn, ((hasWon() == 10) ? 1 : 0), beginner);
    }

    private int hasWon() { // Gibt aus welcher Spieler alle 4 Figuren im Endfeld hat (10 - Spieler1, 20 -
                           // Spieler2, 1 - Niemand)
        boolean winPlayer1 = true;
        for (int i = 0; i < player1.length; i++) {
            if (player1[i].getPos() < 40)
                winPlayer1 = false;
        }
        if (winPlayer1)
            return 10;
        boolean winPlayer2 = true;
        for (int i = 0; i < player2.length; i++) {
            if (player2[i].getPos() < 40)
                winPlayer2 = false;
        }
        if (winPlayer2)
            return 20;
        return 1;
    }

    public void endTurn() { // Tauscht einfach die Startbedingung
        player1Turn = (player1Turn) ? false : true;
    }

    public boolean makeMove(figur[] player, int step) {
        // Versucht zunächst den vordersten zu Bewegen
        figur[] temp = player.clone();
        int front = getfurthestFigure(temp);
        if (Move(player[front], step)) // Sucht den der am weitesten vorne ist.
            return true;
        else {
            figur[] temp2 = removeElement(temp, front); // Falls nicht möglich suche den 2. weitesten
            front = getfurthestFigure(temp2);
            if (Move(player[getIndexFromPos(player, temp2[front].getPos())], step))
                return true;
            else {
                figur[] temp3 = removeElement(temp2, front); // Falls nicht möglich suche den 3. weitesten
                front = getfurthestFigure(temp3);
                if (Move(player[getIndexFromPos(player, temp3[front].getPos())], step))
                    return true;
                else {
                    figur[] temp4 = removeElement(temp3, front); // Falls nicht möglich suche den 4. weitesten
                    front = getfurthestFigure(temp4);
                    if (Move(player[getIndexFromPos(player, temp4[front].getPos())], step))
                        return true;
                    else {
                        return false; // Mit Rekursion wäre es schöner aber da es immer 4 Figuren sind ist es in
                                      // diesem Fall akzeptabel
                    }
                }
            }
        }
    }

    public int getIndexFromPos(figur[] player, int pos) { // Gibt den Index einer Figur aus, falls diese sich auf der
                                                          // angegebenen Position befindet
        for (int j = 0; j < player.length; j++) {
            if (player[j].getPos() == pos)
                return j;
        }
        return 10; // befindet sich nicht auf dieser Position
    }

    public figur[] removeElement(figur[] arr, int index) { // Entfernt einfach ein Element aus einem Array anhand des
                                                           // Indexes
        if (arr == null || index < 0 || index >= arr.length)
            return arr;
        figur[] temp = new figur[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (i == index)
                continue;
            temp[j++] = arr[i];
        }
        return temp;
    }

    public int enemyIsNotOnA(figur[] player) {
        for (int i = 0; i < player.length; i++) {
            if ((player[i].getPos() + 20) % 40 == 0) // Schaut ob sich ein gegner auf dem eigenen A Feld befindet
                return i;
        }
        return 10; // Falls sich kein Gegner auf dem eigenen A Feld befindet
    }

    public int getFigureFromB(figur[] player) {
        for (int i = 0; i < player.length; i++) {
            if (player[i].getPos() == -1)
                return i; // Holt eine beliebige Figur die sich im B Feld befindet
        }
        return 10; // Falls B Feld leer
    }

    public boolean Move(figur a, int step) {
        if (a.getPos() > -1) { // Figur muss im Feld sein
            if (a.getPos() + step > 43) // Falls man über das Ziel hinausgeht -> Zug nicht möglich
                return false;
            if (a.isPlayerOne()) {
                if (isInGoal(player1, a.getPos() + step)) // Falls ein Mitspieler schon im Ziel dieser Stelle ist -> Zug
                                                          // nicht möglich
                    return false;
                for (int i = 0; i < player1.length; i++) {
                    if (a.getPos() != player1[i].getPos()) {
                        if (willCollide(a, player1[i], step) == 0) { // Falls man mit eigenem Stein kollidiert kann
                                                                     // man nicht ziehen -> Zug nicht möglich
                            return false;
                        }
                    }
                }
                for (int i = 0; i < player2.length; i++) {
                    if (willCollide(a, player2[i], step) == 1) { // Falls man mit gegnerischen Stein kollidiert kann man
                                                                 // ziehen
                        a.setPosInSteps(step); // Falls man auf dem Feld des Gegners landet, wird dieser nach B
                                               // geschickt
                        player2[i].setPos(-1);
                        return true;
                    }
                }
                a.setPosInSteps(step); // Falls alle obigen abfragen durchgehen, kann der Stein ohne Umwege nach
                                       // vorne gesetzt werden
                return true;
            }
            if (!a.isPlayerOne()) {
                if (isInGoal(player2, a.getPos() + step)) // Falls ein Mitspieler schon im Ziel dieser Stelle ist -> Zug
                                                          // nicht möglich
                    return false;
                for (int i = 0; i < player2.length; i++) {
                    if (a.getPos() != player2[i].getPos()) {
                        if (willCollide(a, player2[i], step) == 0) { // Falls man mit eigenem Stein kollidiert kann
                                                                     // man nicht ziehen -> Zug nicht möglich
                            return false;
                        }
                    }
                }
                for (int i = 0; i < player1.length; i++) {
                    if (willCollide(a, player1[i], step) == 1) { // Falls man mit gegnerischen Stein kollidiert kann man
                                                                 // ziehen
                        a.setPosInSteps(step); // Falls man auf dem Feld des Gegners landet, wird dieser nach B
                                               // geschickt
                        player1[i].setPos(-1);
                        return true;
                    }
                }
                a.setPosInSteps(step); // Falls alle obigen abfragen durchgehen, kann der Stein ohne Umwege nach
                                       // vorne gesetzt werden
                return true;
            }
        }
        return false;
    }

    public int AIsFree(figur[] player) { // Ist das Feld A Frei
        for (int i = 0; i < player.length; i++) {
            if (player[i].getPos() == 0)
                return i; // Fall nein gib den Index der Figur die auf A steht
        }
        return 10;
    }

    public boolean isInGoal(figur[] player, int pos) { // Schaut ob ein Mitspieler schon im Ziel an dieser Position ist
        for (int i = 0; i < player.length; i++) {
            if (player[i].getPos() == pos)
                return true;
        }
        return false;
    }

    public int getfurthestFigure(figur[] player) { // Holt den Spieler, der am weitesten Vorne steht
        int largest = 0;
        int largestIndex = 0;
        for (int i = 0; i < player.length; i++) {
            if (player[i].getPos() >= largest) {
                largest = player[i].getPos();
                largestIndex = i;
            }
        }
        return largestIndex;
    }

    public void determineBeginner(wuerfel[] dicesOfEachPlayer) { // kann einfach ignoriert werden, da die Regel
                                                                 // überschrieben wurde, dass der Spieler anfängt der
                                                                 // eine höhere Zahl geworfen hat. Stattdessen fängt
                                                                 // einfach jede zweite Runde ein anderer an, somit
                                                                 // fängt 50% der Zeit der Spieler1 an und 50% der Zeit
                                                                 // Spieler 2.
        if (dicesOfEachPlayer[0].getRandomValue() > dicesOfEachPlayer[0].getRandomValue()) {
            player1Turn = true;
        } else if (dicesOfEachPlayer[0].getRandomValue() < dicesOfEachPlayer[0].getRandomValue()) {
            player1Turn = false;
        } else if (dicesOfEachPlayer[0].getRandomValue() == dicesOfEachPlayer[0].getRandomValue())
            determineBeginner(dicesOfEachPlayer);
        for (int i = 0; i < 4; i++) { // Initialisierung der Figuren
            player1[i] = new figur(true);
            player1[i].setPos(-1);
            player2[i] = new figur(!true);
            player2[i].setPos(-1);
        }
    }

    public int willCollide(figur a, figur b, int step) { // Mit; 0: Eigene, 1: Gegner, 10: Niemandem
        if (b.getPos() < 0)
            return 10; // Man kann nicht mir einem Gegner kollidieren, des in seinem B Feld steht
        if ((a.isPlayerOne() && b.isPlayerOne()) || (!a.isPlayerOne() && !b.isPlayerOne()))
            if (a.getPos() + step == b.getPos()) // Kollidieren mit eigenen
                return 0;
        if (a.isPlayerOne() && !b.isPlayerOne())
            if ((a.getPos() + step == (b.getPos() + 20) % 40)) // Kollidieren mit gegner
                return 1;
        if (!a.isPlayerOne() && b.isPlayerOne())
            if ((a.getPos() + step == (b.getPos() + 20) % 40)) // Kollidieren mit gegner
                return 1;
        return 10;
    }
}
