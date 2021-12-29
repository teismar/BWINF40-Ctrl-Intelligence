@echo off
color f0
echo Simulation fuer Mensch aergere dich nicht
set /p file="Bitte geben sie den Dateinamen an: "
set /p count="Bitte geben sie die Anzahl der gewuenschten Wiederholungen an: "
java -jar Aufgabe4.jar %file% %count%
pause