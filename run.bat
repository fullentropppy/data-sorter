@echo off
cd /d "%~dp0"\target"
java -jar DataSorter-1.0-SNAPSHOT.jar -s -a -p sample- in1.txt in2.txt
pause