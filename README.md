
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
``
source env_settup.sh
``
A exécuter dans chaque terminal


### Compilation 

``
mvn compile
``


### Exécution
``
./src/main/bin/decac
``

Affichage du débogage Log4j :
- `decac -d <file>` mode INFO
- `decac -d -d <file>` mode DEBUG
- `decac -d -d -d <file>` mode TRACE
- `decac -d -d -d -d <file>` mode ALL




### Exécution des tests

Emplacement des tests `./src/test/script`

Rappel pour rendre exécutable :
``
chmod 0755 nom-du-test.sh
``

4 parties :
- Les tests valides fournis
- Les tests valide non fournis
- Les tests non valide fournis
- Les tests non valide non fournis

Chaque batterie de test est annoncé en jaune et chaque fichier testé verra le résultat de son test à coté de son nom. Si un cas normalement valide est faux, il y a un lien direct vers le début de la pile d'appel problématique ou un message d'erreur correspondant. 


  

Formats de réponse des tests : 
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

``
suppression-log.sh
``

### Automatisation des test

Les tests qui sont présents dans le pom.xml sont des tests qui vont se lancer à chaque appel de:

``
mvn test
``

Pour forcer le lancement de tous les tests et ne pas s'arreter dès qu'il y a une erreur, 
il faut utiliser:

``
mvn test -Dmaven.test.failure.ignore
``

Au passage, il est également possible d'utiliser:

``
mvn verify
``

qui réalise la phase de tests mais qui aussi désinstrumente les classes.

### Couverture des tests

Lors de l'utilisation de 

``
mvn verify
``

Une couverture automatique des tests est alors lancé pour découvrir par où sont passés les 
sont passés les tests. Pour avoir un accés graphique de cet couverture, il suffit de lancé:

``
jacoco-report.sh
``

puis d'ouvrir le fichier index.html 

``
firefox target/site/index.html
``

Pour relancer en mode sans échec, vous pouvez aussi lancer 

``
mvn verify -Dmaven.test.failure.ignore
``

## Compilateur bytecode Java

Notre compilateur permet de compiler des programmes decac en bytecode exécutable par la JVM avec l'option `-j`

### Problèmes fréquents
En cas de Stack Overflow, il peut être nécessaire d'ajuster les limites du nombre de variables et de la taille de pile des méthodes :

Dans le fichier [DecacCompiler.java](src/main/java/fr/ensimag/deca/DecacCompiler.java) à la fin, aux lignes :
```
pS.println(".limit stack 256");
pS.println(".limit locals 256");
```

