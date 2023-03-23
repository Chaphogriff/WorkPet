# WorkPet

## Introduction
WorkPet est un prototype d'application pour un projet au sein du cours 8INF865 à l'UQAC. 
Il s'agit une gamification de la gestion des tâches quotidiennes. 
L'application est développée avec Android Studio 2022.1.1 (Electric eel), en Java.

[Wiki du projet](https://moodle.uqac.ca/mod/wiki/view.php?pageid=6178) 
| [Trello du projet](https://trello.com/invite/b/lw1z1inM/ATTI940783fe609ae5ea2cc78f33918464ca43883C06/workpet) 

<img src="https://cdn.discordapp.com/attachments/1065376759404576851/1075477400646860840/wokr-removebg-preview.png" width="100" height="100"> <img src="https://cdn.discordapp.com/attachments/1065376759404576851/1075045281105707028/wttw-removebg-preview_1.png" width="100" height="100">



Notre équipe WorkThruTheWeak (WTTW) est composée de 4 membres suivants : 
- Annie Abhay - @JuviaBerry  
- Xavier Palix - @XPalix  
- Mamadou Sounoussy Diallo - @Sunsi13  
- Charles Wang - @Chaphogriff  

## Structure du projet

```               
├── .idea                                 # dossier composé des fichiers propriétés de .idea                 
├── app                                   # dossier contenant le code principal
|    └── src                              # dossier de code source
|         └── main                        # dossier contenant notre application ( ne pas utiliser le dossier test )
|              ├── java                   # dossier contenant les classes java, principalement les Activity
|              ├── res                    # dossier ressources composées des fichiers .xml, les images, le son etc.
|              └── AndroidManifest.xml    # fichier qui gère les permissions nécessaires pour lancer l'application
├── gradle                                # dossier composé des fichiers gradle  
├── .gitignore                            # fichier .gitignore pour git
├── README.md                             # instructions (ce fichier)
├── build.gradle                          # fichier qui gère la compilation et le build de l'application
├── gradle.properties                     # fichier qui gère les propriétés de gradle
├── gradlew                               # fichier script wrapper de gradle // ne pas toucher
├── gradlew.bat                           # fichier script wrapper de gradle // ne pas toucher
└── settings.gradle                       # fichier qui définit les modules à inclure sur gradle
```
