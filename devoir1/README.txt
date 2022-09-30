Manping Li:968527(reveinsead), Wenhao Xu: 20150702(K-kiron)
Repositoire: https://github.com/K-kiron/IFT3913

Instructions pour l'exécution du fichier:

En raison d'une certaine ambiguïté dans la demande d'enonce pour une section commit, nous n'avons généré qu'un seul fichier jar (au lieu de quatre). 
Après avoir consulté le professeur Michalis Famelis mardi, il a dit qu'il était acceptable de soumettre un seul jar s'il était trop tard pour le diviser, 
et nous regrettons de ne pas avoir eu le temps de diviser chaque fonction. mesureQuality.jar ne produira que ce que la section egon() produira. 
Si vous voulez vérifier et tester les fonctions des autres sections (jls,nvloc,lcsec), vous pouvez utiliser le fichier zip téléchargé sur Github 
sous le chemin "./IFT3913/devoir1/ift3913-tp1/src/MesureQuality.java" pour les tests, et nous inclurons des directives de débogage pour le fichier java avec celui-ci.

Test de MesureQuality.jar:

Sur la ligne de commande, tapez au format suivant: java -jar MesureQuality.jar [PATH] [seuil]
Exemple: java -jar MesureQuality.jar /Users/kironrothschild/Downloads/ckjm-master/src/gr 0.15
(le seuil doit être saisi en format double)
Une exécution réussie générera un fichier appelé out_[seuil]%.csv dans le chemin courant, contenant la sortie de la section egon().

Test de MesureQuality.java:

Vous pouvez déboguer le fichier java en sélectionnant l'entrée de la ligne de commande dans le même format que le fichier jar, ou en convertissant les commentaires de 
la section "java run version" de la fonction main() en code, et le code de la section "jar version" en commentaires débogage dans intellij.

//java run version(dans intellij)
Chemin du fichier d'exécution : ./ift3913-tp1/src/MesureQuality.java
Chemin du fichier de compilation : ./ift3913-tp1/src/config.json
Chemin du fichier de sortie : ./ift3913-tp1/output.csv

Avant de lancer le fichier, veuillez ajouter ou modifier le chemin que vous devez entrer après "PROJECT_PATH" : dans le fichier config.json. 
Prenons ckjm comme exemple : "PROJECT_PATH" : "/Users/kironrothschild/Downloads/ckjm-master/src/gr" ; 
prenons jfreechart comme exemple : "PROJECT_PATH" : "/Users/kironrothschild/Downloads/jfreechart-master/src/main/java"