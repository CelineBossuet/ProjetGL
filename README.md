
# Projet Génie Logiciel, Ensimag.
Équipe 13

#### Présentation de l'Equipe
Composition de l'équipe :
  - Titouan Lecamp (filière ISI)
  - Romain Pigret Cadou (filière SEOC)
  - Céline Bossuet (filière ISI)
  - Ayoub Miguil (filière IF) 
  - Pedro Alfonso Deitos Regis (filière SEOC) 


### Environnement

Prérequis :
- [maven 3.8.4](https://maven.apache.org/download.cgi)
- [Java 16](https://openjdk.java.net/install/)

Il est nécessaire d'ajouter le dossier `bin` de maven au *PATH*.
Également celui de java si besoin suivant la méthode d'installation.

Pour pouvoir utiliser les commandes du projet directement dans le terminal :
```
source env_settup.sh
```
A exécuter dans chaque terminal


### Compilation 

Pour compiler avec Maven il suffit de tapper dans le terminal 

``
mvn compile
``

ensuite on peut ouvrir le compilateur avec la commande avec ensuite soit le nom du fichier à utiliser (comme hello.deca pour faire le HelloWorld) ou des paramètres comme -b pour afficher la banière

``
./src/main/bin/decac
``

### Exécution des tests

Vous pouvez lancer n'importe quel test en tapant en ligne de commande depuis n'importe quel répertoire.

``
nom-du-test.sh
``

si une erreur de droit apparait, vous pouvez vous rendre dans le répertoire du test et taper:

``
chmod 0755 nom-du-test.sh
``

Le chemin du test est:

``
./src/test/script
``

vous pourrez alors relancer le test.  
Ce dernier ce décompose en 4 partie :
- Les tests valides fournis
- Les tests valide non fournis
- Les tests non valide fournis
- Les tests non valide non fournis

Chaque batterie de test est annoncé en jaune et chaque fichier testé verra le résultat de son test à coté de son nom. Si un cas normalement valide est faux, il y a un lien direct vers le début de la pile d'appel problématique ou un message d'erreur
correpsondant. 


  
Aprés éxécution des tests, un fichier de log de résultat apparait pour lire la réponse éxacte du test.
Il peut être de différentes manières : 
- .lis si le test est un test résultant du lexer ou du parser
- .ass si le fichier .deca a été compilé en assembleur
- .res si le fichier est le résultat d'une éxécution

Ces fichiers contiendront ou bien la réponse du programme justifiant leurs bons fonctionnement, ou bien les erreurs rencontrées par le programme.

Pour supprimer ces fichiers dans les répertoires de tests il suffit de lancer le script

``
suppression-log.sh
``

Les tests exécutent chaque test unitaire dans les répertoires concernés.

Pour tester le bon fonctionnement du lexer, on utilise:

``
self-test-lex.sh
``

Pour tester le bon fonctionnement du parser, on utilise:

``
self-test-pars.sh
``

Pour tester le bon fonctionnement du contexte (étape B) :

``
self-test-context.sh
``




