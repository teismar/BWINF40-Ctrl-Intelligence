@echo off
color f0
echo Loesen eines Schiebeparkplatzes
set /p file="Bitte geben sie den Dateinamen an: "
java -jar Schiebeparkplatz.jar %file%
pause