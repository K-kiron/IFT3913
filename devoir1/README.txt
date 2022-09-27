Manping Li:968527, Wenhao Xu: 20150702
Repositoire: https://github.com/K-kiron/IFT3913

Instructions pour l'exécution du fichier:

Chemin du fichier d'exécution : ./ift3913-tp1/src/MesureQuality.java
Chemin du fichier de compilation : ./ift3913-tp1/src/config.json
Chemin du fichier de sortie : ./ift3913-tp1/output.csv

Avant de lancer le fichier, veuillez ajouter ou modifier le chemin que vous devez entrer après "PROJECT_PATH" : dans le fichier config.json. 
Prenons ckjm comme exemple : "PROJECT_PATH" : "/Users/kironrothschild/Downloads/ckjm-master/src/gr" ; 
prenons jfreechart comme exemple : "PROJECT_PATH" : "/Users/kironrothschild/Downloads/jfreechart-master/src/main/java"
(Les fichiers sources des chemins ckjm et jfreechart ont été téléchargés et placés dans le répertoire "./ift3913-tp/lib")

Pour changer la valeur de seuil entrée par la fonction egon() dans la PARTIE 3, il suffit de changer la valeur de la variable seuil dans la 
fonction main() avant d'appeler la fonction egon() (si seuil=0.01, les résultats de 1%, 5%, 10% de seuil seront sortis pour répondre aux 
exigences de la PARTIE 4)