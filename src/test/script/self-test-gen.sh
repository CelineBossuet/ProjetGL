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

for i in ./src/test/deca/codegen/valid/provided/*.deca
do
  fichier=$(basename $i)
  if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
    head -1 "${i%.deca}".ass
    status=1
  else
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/codegen/valid/self/*.deca
do
  fichier=$(basename $i)
  if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
    head -1 "${i%.deca}".ass
    status=1
  else
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

#Gestion des cas non valide

echo -e "${jaune}Cas performance donné ${blanc}"

for i in ./src/test/deca/codegen/perf/provided/*.deca
do
  fichier=$(basename $i)
  if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
  else
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"
    status=1
  fi
done

echo -e "${jaune}Cas performance créé ${blanc}"

for i in ./src/test/deca/codegen/perf/self/*.deca
do
  fichier=$(basename $i)
  if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
  else
    decac "$i" > "${i%.deca}".ass 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"
    status=1
  fi
done

#On exit sous le bon status
exit ${status}
