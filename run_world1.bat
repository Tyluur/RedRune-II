@echo off
@title RedRune World 1
java -Xmx6G -Xms2048m -server -XX:+UseG1GC -cp bin;data/dependencies/* org.redrune.Bootstrap true 1
pause