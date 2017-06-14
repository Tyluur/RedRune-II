@echo off
@title RedRune World 2
java -Xmx6G -Xms2048m -server -XX:+UseG1GC -cp bin;data/dependencies/* org.redrune.Bootstrap true 2 false
pause