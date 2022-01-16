#bin/bash

#script permettant de supprimer tous les fichiers générer par des tests dans le répertoire déca.

cd "$(dirname "$0")"/../../.. || exit 1

cd ./src/test/deca

find . -name "*.lis" -type f -delete
find . -name "*.ass" -type f -delete
find . -name "*.res" -type f -delete
find . -name "*.decap" -type f -delete

echo -e "Fichiers supprimés avec succés"

exit 0