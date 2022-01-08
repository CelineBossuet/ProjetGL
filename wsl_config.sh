# Use this script to allow our executables to be run under wsl
# Add them to git index to make the index desapear.
# Need installation of dos2unix

dos2unix ./src/main/bin/decac 
dos2unix ./src/test/script/launchers/*
dos2unix ./src/test/script/*.sh