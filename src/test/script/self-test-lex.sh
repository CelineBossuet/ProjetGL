#bin/bash

# On se recale dans le bon répertoire

cd "$(dirname "$0")"/../../.. || exit 1

#définition des constantes de bases

PATH=./src/test/script/launchers:"$PATH"

rouge='\e[31m'
vert='\e[32m'
blanc='\e[0;m'

status=0


# Gestion des cas valide

for i in ./src/test/deca/syntax/valid/provided/*.deca
do
  if test_lex "$i" 2>&1 | grep -q -e 'Error'
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${rouge} FAILED ${blanc} (failure)"
    tail -2 $i
    status=1
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${vert} PASSED ${blanc}"

  fi
done

for i in ./src/test/deca/syntax/valid/self/*.deca
do
  if test_lex "$i" 2>&1 | grep -q -e 'Error'
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${rouge} FAILED ${blanc} (failure)"
    tail -2 $i
    status=1
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${vert} PASSED ${blanc}"

  fi
done

#Gestion des cas non valide

for i in ./src/test/deca/syntax/invalid/provided/*.deca
do
  if test_lex "$i" 2>&1 | grep -q -e 'Error'
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${vert} PASSED ${blanc} (failure)"
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${rouge} FAILED ${blanc} (no failure)"
    status=1
  fi
done

for i in ./src/test/deca/syntax/invalid/self/*.deca
do
  if test_lex "$i" 2>&1 | grep -q -e 'Error'
  then
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${vert} PASSED ${blanc} (failure)"
  else
    test_lex "$i" >& "${i%.deca}".lis
    echo -e "$i ${rouge} FAILED ${blanc} (no failure)"
    status=1
  fi
done

#On exit sous le bon status

exit ${status}