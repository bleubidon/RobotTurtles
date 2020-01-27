## Comment importer les d�pendences ?
- Cr�er un projet de type **Maven** et installer les d�pendences sp�cifi�es dans **pom.xml**.
- Ne pas inclure **client-1.0-SNAPSHOT-jar-with-dependencies.jar**.

## Comment fonctionne le module IA ?
#### Il y a 3 classes:

- ```Player```: instanci� autant de fois qu'il y a de joueurs
- ```IARun```: fait jouer plusieurs joueurs � tour de r�le dans une m�me partie. Il faut renseigner l'id de la partie, et pour chaque joueur son ID (num�ro du joueur, entre 0 et le nombre de joueurs - 1) et son UUID (obtenu lors de la cr�ation d'un joueur avec ```playerConnector.joinGame(playerName)```).
-  ```IAProfiles```: contient les diff�rents profils d'IA (les algos). Chaque profil est une fonction qui renvoie le move � ex�cuter sous forme de String, en fonction du gameState et du PlayerSecret relatif au joueur courant. C'est � ```IARun``` d'associer � chaque joueur un profil d'IA.

#### IARun.createPlayers
- Ce flag permet de choisir s'il faut cr�er des joueurs (```true```) ou s'il faut utiliser l'identit� de joueurs pr�c�demment cr��s (```false```)
- Pour chaque nouvelle partie, cr�er des joueurs puis renseigner les ```playerId``` et ```playerUUID``` dans ```IA.java``` et repasser le flag � ```false```.

#### IARun.scannerMovesMode
Ce flag permet de choisir si les moves sont envoy�s via le ```Scanner``` (```true```) ou via le r�sultat d'un profil d'IA.

#####Credit to https://github.com/tdebroc/robot-turtles-ia-client
