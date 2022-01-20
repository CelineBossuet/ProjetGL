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
    decac "$i" > "${i%.deca}".ass 2>&1
    ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
    status=1
  else
    decac "$i" > "${i%.deca}".ass 2>&1
    if ima "${i%.deca}".ass 2>&1 | grep -q -e "$fichier" -e 'Error' -e '** IMA'
    then
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${rouge} FAILED ${blanc}"
      status=1
    else
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${vert} PASSED ${blanc}"
    fi
  fi
done

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/codegen/valid/self/*.deca
do
  fichier=$(basename $i)
  if [ "$fichier" = "Readfloat.deca" ] || [ "$fichier" = "readInt.deca" ] || [ "$fichier" = "sansObjet.deca" ]
  then
    echo -e "$fichier ${jaune} Non exécutable ${blanc}"
  else
    if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
    then
      decac "$i" > "${i%.deca}".ass 2>&1
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${rouge} FAILED ${blanc}"
      status=1
    else
      decac "$i" > "${i%.deca}".ass 2>&1
      if ima "${i%.deca}".ass 2>&1 | grep -q -e "$fichier" -e 'Error' -e '** IMA'
      then
        ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
        echo -e "$fichier ${rouge} FAILED ${blanc}"
        status=1
      else
        ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
        echo -e "$fichier ${vert} PASSED ${blanc}"
      fi
    fi
  fi
done
#Gestion des cas non valide

echo -e "${jaune}Cas performance donné ${blanc}"

for i in ./src/test/deca/codegen/perf/provided/*.deca
do
  fichier=$(basename $i)
  if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac "$i" > "${i%.deca}".ass 2>&1
    ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
    status=1
  else
    decac "$i" > "${i%.deca}".ass 2>&1
    if ima "${i%.deca}".ass 2>&1 | grep -q -e "$fichier" -e 'Error' -e '** IMA'
    then
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${rouge} FAILED ${blanc}"
      status=1
    else
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${vert} PASSED ${blanc}"
    fi
  fi
done

echo -e "${jaune}Cas performance créé ${blanc}"

for i in ./src/test/deca/codegen/perf/self/*.deca
do
  fichier=$(basename $i)
  if decac "$i" 2>&1 | grep -q -e "$fichier" -e 'Error'
  then
    decac "$i" > "${i%.deca}".ass 2>&1
    ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
    echo -e "$fichier ${rouge} FAILED ${blanc}"
    status=1
  else
    decac "$i" > "${i%.deca}".ass 2>&1
    if ima "${i%.deca}".ass 2>&1 | grep -q -e "$fichier" -e 'Error' -e '** IMA'
    then
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${rouge} FAILED ${blanc}"
      status=1
    else
      ima "${i%.deca}".ass > "${i%.deca}".res 2>&1 &
      echo -e "$fichier ${vert} PASSED ${blanc}"
    fi
  fi
done

#On exit sous le bon status
exit ${status}
