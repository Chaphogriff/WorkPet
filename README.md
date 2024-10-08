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

## Fonctionnement de la version alpha de WorkPet

Le menu présente les différentes tâches si il y en a ( "Bien débuter"  est crée automatiquement ).
La bar de navigation permet d'accéder aux différentes pages : 
- la page du calendrier qui affiche une liste des tâches associées à la dates sur laquelle on clique.
- la page des tâches présente une liste des tâches, on peut en ajouter et les modifier. Pour le moment la suppression des tâches en cliquant longtemps dessus enlève seulement l'affichage d'une tâches ( si le fichier local est recharger les tâches supprimer comme ça réapparaitront. Lorsque l'on quitte une page montrant une liste de tâches, toutes les tâches marquées comme faite sont réellement supprimées, ce modèle de suppression est temporaire. 
- la page de l'avatar, avec les boutons d'achat et de customisation qui ne sont pas encore fonctionnel. On peut interagir avec le chat au toucher ( il ronronne et fais vibrer l'appareil
- la page de setting avec plusieurs options pour le moment non implémentées.

## Ressources 
- Navigation bar icons : https://fonts.google.com/icons
- Brown Cat (Pet) : https://www.deviantart.com/pix202/art/Orange-Cat-24x24-GIF-698462335
- Panda (Pet) : https://thumbs.gfycat.com/IllSelfassuredButterfly-max-1mb.gif 
- Black Cat (Pet) : https://66.media.tumblr.com/tumblr_maw6psZPRo1rfjowdo1_500.gif 
- Dog (Pet) : https://cdnb.artstation.com/p/assets/images/images/049/893/639/original/cameron-munro-doggodawg-1200.gif?1653565596
- Rabbit (Pet) : https://media.tenor.com/VsI4oUh4-wQAAAAC/pixel-rabbit-rabbit.gif 
- Cat Cookie (Food) : https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/698063ad3d47c0b.png
- Food in Can (Food) : https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/48e0732c40fc80a.png 
- Meat (Food) : https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/f3d68c5fd3dc146.png 
- Bread (Food) : https://static.vecteezy.com/system/resources/previews/019/527/060/non_2x/an-8-bit-retro-styled-pixel-art-illustration-of-bread-free-png.png
- Star Fruit (Food) : https://i.redd.it/6equawpssdr81.png 
