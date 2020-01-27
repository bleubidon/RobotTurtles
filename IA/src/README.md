#### Il y a deux classes:

- ```Player```: instanci� autant de fois qu'il y a de joueurs
- ```IA```: fait jouer plusieurs joueurs � tour de r�le dans une m�me partie. Il faut renseigner l'id de la partie, et pour chaque joueur son ID (num�ro du joueur, entre 0 et le nombre de joueurs - 1) et son UUID (obtenu lors de la cr�ation d'un joueur avec ```playerConnector.joinGame(playerName)```).
- La m�thode ```IA.getMove()``` renvoie le move � ex�cuter sous forme de String, en fonction du gameState et du PlayerSecret relatif au joueur courant. C'est cette m�thode qui doit a priori contenir l'algorithme de l'IA.

#### createPlayers
- Ce flag permet de choisir s'il faut cr�er des joueurs (```true```) ou s'il faut utiliser l'identit� de joueurs pr�c�demment cr��s (```false```)
- Pour chaque nouvelle partie, cr�er des joueurs puis renseigner les ```playerId``` et ```playerUUID``` dans ```IA.java``` et repasser le flag � ```false```.

#### scannerMovesMode
Ce flag permet de choisir si les moves sont envoy�s via le ```Scanner``` (```true```) ou via la fonction ```getMove``` (```false```).