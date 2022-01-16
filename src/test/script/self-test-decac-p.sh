#bin/bash

# On se recale dans le bon répertoire

cd "$(dirname "$0")"/../../.. || exit 1

#définition des constantes de bases

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

rouge='\e[31m'
vert='\e[32m'
blanc='\e[0;m'
jaune='\e[33m'

status=0


# Gestion des cas valide

echo -e "${jaune}Cas valide donné ${blanc}"

for i in ./src/test/deca/context/valid/provided/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    head -1 "${i%.deca}".decap
    status=1
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/context/valid/self/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    head -1 "${i%.deca}".decap
    status=1
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

#Gestion des cas non valide

echo -e "${jaune}Cas valide donné ${blanc}"

for i in ./src/test/deca/syntax/valid/provided/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    head -1 "${i%.deca}".decap
    status=1
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/syntax/valid/self/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    head -1 "${i%.deca}".decap
    status=1
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

echo -e "${jaune}Cas non valide donné ${blanc}"

for i in ./src/test/deca/syntax/invalid/provided/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc} (failure)"
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (no failure)"
    status=1
  fi
done

echo -e "${jaune}Cas non valide créé ${blanc}"

for i in ./src/test/deca/syntax/invalid/self/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc} (failure)"
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (no failure)"
    status=1
  fi
done

for i in ./src/test/deca/codegen/valid/provided/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    head -1 "${i%.deca}".decap
    status=1
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/codegen/valid/self/*.deca
do
  fichier=$(basename $i)
  if decac -p "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc} (failure)"
    head -1 "${i%.deca}".decap
    status=1
  else
    decac -p "$i" > "${i%.deca}".decap 2>&1 &
    echo -e "$fichier ${vert} PASSED ${blanc}"

  fi
done


exit ${status}