#bin/bash

# On se recale dans le bon répertoire

cd "$(dirname "$0")"/../../.. || exit 1

#définition des constantes de bases

PATH=./src/test/script/launchers:"$PATH"

rouge='\e[31m'
vert='\e[32m'
blanc='\e[0;m'
jaune='\e[33m'

status=0


# Gestion des cas valide

echo -e "${jaune}Cas valide donné ${blanc}"

for i in ./src/test/deca/syntax/valid/provided/*.deca
do
  fichier=$(basename $i)
  if test_lex "$i" 2>&1 | grep -q -e "$fichier"
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    tail -2 $i
    status=1
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/syntax/valid/self/*.deca
do
  fichier=$(basename $i)
  if test_lex "$i" 2>&1 | grep -q -e "$fichier"
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    tail -2 $i
    status=1
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

#Gestion des cas non valide

echo -e "${jaune}Cas non valide donné ${blanc}"

for i in ./src/test/deca/syntax/invalid/provided/*.deca
do
  fichier=$(basename $i)
  if test_lex "$i" 2>&1 | grep -q -e "$fichier"
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${vert} PASSED ${blanc} (failure)"
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${rouge} FAILED ${blanc} (no failure)"
    status=1
  fi
done

echo -e "${jaune}Cas non valide créé ${blanc}"

for i in ./src/test/deca/syntax/invalid/self/*.deca
do
  fichier=$(basename $i)
  if test_lex "$i" 2>&1 | grep -q -e "$fichier"
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${vert} PASSED ${blanc} (failure)"
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$fichier ${rouge} FAILED ${blanc} (no failure)"
    status=1
  fi
done

#On exit sous le bon status

exit ${status}