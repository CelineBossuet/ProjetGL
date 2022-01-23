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

echo -e "${jaune}Cas valide créé ${blanc}"

for i in ./src/test/deca/bytecode/valid/self/*.deca
do
  fichier=$(basename $i)
  if decac -j "$i" | grep -q -e 'Error'
  then
    decac -j "$i"
    echo -e "$fichier ${rouge} FAILED ${blanc}"
    status=1
  else
    decac -j "$i"
    echo -e "$fichier ${vert} PASSED ${blanc}"
  fi
done


#On exit sous le bon status
exit ${status}
