# Maëva

Maëva est un projet de bot Discord en Java, débuté en 2017.

Maëva propose principalement :
 - une boutique, une monnaie, une récompense journalière et des jeux
 - une recherche de GIF
 - des recettes de cuisine
 - une synchronisation entre un salon Discord et un chat Twitch
 - un système de log de messages
 - des speed datings


## Compilation

Avant de compiler, n'oubliez pas de mettre des tokens (Discord, Giphy, Twitch) dans [src/fr/ptlc/maeva/Tokens.java](src/fr/ptlc/maeva/Tokens.java).

Et de récuperer les dépendances du projet et de les mettre dans le dossier `lib/` :
 - [gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)
 - [pircbot](https://mvnrepository.com/artifact/pircbot/pircbot)
 - [okhttp](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp)
 - [JDA (min 4.2.0)](https://github.com/discord-jda/JDA)

Commande de compilation :

```bash
javac -encoding ISO-8859-1 -cp "lib/*" -d bin src/fr/ptlc/**/*.java src/fr/ptlc/**/**/*.java
```

Puis pour lancer le bot :

```bash
java -cp "bin:lib/*" fr.ptlc.maeva.Main
```