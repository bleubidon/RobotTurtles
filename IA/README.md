#### Il y a deux classes:

- ```Player```: instanci� autant de fois qu'il y a de joueurs
- ```IA```: fait jouer plusieurs joueurs � tour de r�le dans une m�me partie. Il faut renseigner l'id de la partie, et pour chaque joueur son ID (num�ro du joueur, entre 0 et le nombre de joueurs - 1) et son UUID (obtenu lors de la cr�ation d'un joueur avec ```playerConnector.joinGame(playerName)```).
- La m�thode ```IA.getMove()``` renvoie le move � ex�cuter sous forme de String, en fonction du gameState et du PlayerSecret relatif au joueur courant. C'est cette m�thode qui doit a priori contenir l'algorithme de l'IA.