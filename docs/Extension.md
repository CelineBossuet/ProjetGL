---
title: "Spécification de l'extension"
author: "Projet gl groupe 13"
date: "12 janvier 2022"
output: pdf_document
---

## Le bytecode Java, qu'est-ce que c'est?

Le bytecode java, c'est une série d'instruction binaire éxécutable par une
machine virtuelle java (JVM). C'est les fichiers .class que l'on retrouve 
lorsque l'on compile du code java. Générer du bytecode depuis un autre language
(deca dans notre cas), permet d'éxécuter notre code par une machine
virtuelle java. En général, cela est trés utile si dans un même projet,
l'on souhaite faire de l'échange d'information entre plusieurs language.
Savoir générer du bytecode permet donc d'améliorer la portabilité d'une 
application car n'importe quelle JVM sur n'importe quel système 
d'exploitation pourra exécuter le programme deca entrer en entrée.

## Comment générer du bytecode Java

Pour générer du bytecode Java, il faut passer par un assembleur de bytecode.
Nous avons choisi pour cela Jasmin qui est le plus ancien et celui de référence.
Jasmin prend en entré un fichier .j en pseudo-assembleur. Ensuite, il suffit 
de générer un .class grace à une commande jasmin. Ce .class pourra être
éxécuté par une JVM. 

## Intégration à notre logiciel

Le but ici est d'implémenter une option au compilateur qui pourra alors
générer le .class pour certaines fonctionnalités de notre compilateur. Ainsi, 
il faudra pour cela recréer une étape C bis qui traduira non pas en language
assembleur .ass mais en un fichier .j qui sera éxécutable par la jasmin
qui créera un .class. Ce .class pourra alors être éxécutable par une JVM.

![Conséquences de l'extension](Utilisation de l'extension.png)

## Procédé de notre implémentation

Le but sera de pouvoir implémenter au fur et à mesure les différents types
d'instruction, en commençant par les affichages, puis des simples calcul et
enfin la génération de classe etc...

## Pourquoi cette solution

C'est la solution la plus pratique est la plus performante que nous avons trouvé. 
En effet, elle permet de réutiliser toute une partie de la génération du code
assembleur de déca qui est trés intéressantes, et l'outil clé en main jasmin
est reconnu dans la communauté de développeur comme étant le principal assembleur
de bytecode java. Utiliser cette solution permet de compacter le code et de 
gagner en rapidité d'éxécution. 
