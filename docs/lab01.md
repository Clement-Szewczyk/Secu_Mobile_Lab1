# Lab 01 - Projet multiplateforme Kotlin

## Etape 5

1. **Combien de modules Gradle contient le projet ? Lesquels ? Où sont-ils déclarés ? `iosApp` en fait-il partie ? Pourquoi ?**

   Le projet contient trois modules Gradle : `androidApp`, `desktopApp` et `shared`. Ils sont inclus dans `settings.gradle.kts` avec les déclarations `include(":androidApp")`, `include(":desktopApp")` et `include(":shared")`.

   `iosApp` n'est pas un module Gradle de ce projet. Le code partagé déclare les cibles iOS et produit un framework (`Shared`) que peut intégrer une application iOS native, mais aucun module `iosApp` n'est inclus dans les paramètres Gradle.

2. **Quelles versions de Kotlin, Compose Multiplatform, AGP et Gradle sont utilisées ?**

   Les versions de Kotlin (**2.4.20**), Compose Multiplatform (**1.12.1**) et AGP (**9.4.1**) sont définies dans `gradle/libs.versions.toml`. La version de Gradle (**9.6.0**) est indiquée par `distributionUrl` dans `gradle/wrapper/gradle-wrapper.properties`.

3. **Quels plugins applique `shared/build.gradle.kts` ? Et `androidApp/build.gradle.kts` ? Pourquoi `androidApp` n'applique-t-il pas `org.jetbrains.kotlin.android` ?**

   `shared/build.gradle.kts` applique Kotlin Multiplatform (`org.jetbrains.kotlin.multiplatform`), Android Multiplatform Library (`com.android.kotlin.multiplatform.library`), Compose Multiplatform (`org.jetbrains.compose`) et Compose Compiler (`org.jetbrains.kotlin.plugin.compose`).

   `androidApp/build.gradle.kts` applique Android Application (`com.android.application`) et Compose Compiler. Il n'applique pas `org.jetbrains.kotlin.android` parce que cette version d'AGP (9.4.1) intègre la prise en charge de Kotlin pour les projets Android ; le plugin Kotlin Android séparé n'est donc pas nécessaire.

4. **Où sont configurés le `minSdk` de la bibliothèque partagée et celui de l'application ? Sous quelle forme ?**

   Les deux `minSdk` sont configurés dans `shared/build.gradle.kts` et `androidApp/build.gradle.kts` respectivement. Ils utilisent tous deux `libs.versions.android.minSdk`, dont la valeur (**24**) est définie dans `gradle/libs.versions.toml` : ce n'est pas une valeur écrite en dur dans les scripts Gradle.

5. **Quels sont les dossiers de `shared/src` ? Pourquoi n'y a-t-il pas de `iosArm64Main` alors que la cible `iosArm64()` est déclarée ?**

   Les source sets présents sont `commonMain`, `commonTest`, `androidMain`, `androidHostTest`, `iosMain`, `iosTest`, `jvmMain` et `jvmTest`.

   Le code partagé entre les cibles iOS est placé dans `iosMain`. Kotlin Multiplatform n'a pas besoin d'un source set dédié `iosArm64Main` lorsque le code iOS est commun aux cibles iOS déclarées (`iosArm64()` et `iosSimulatorArm64()`).

6. **Où est déclarée `expect fun getPlatform()` ? Combien d'implémentations `actual` existe-t-il, et dans quels fichiers ?**

   La déclaration `expect fun getPlatform()` se trouve dans `shared/src/commonMain/kotlin/fr/m2/pokequiz/Platform.kt`. Il y a trois implémentations :
   - `shared/src/androidMain/kotlin/fr/m2/pokequiz/Platform.android.kt`
   - `shared/src/iosMain/kotlin/fr/m2/pokequiz/Platform.ios.kt`
   - `shared/src/jvmMain/kotlin/fr/m2/pokequiz/Platform.jvm.kt`

7. **Quel est l'`applicationId` Android ? Quelle est la `mainClass` desktop, et pourquoi se termine-t-elle par `Kt` ?**

   L'`applicationId` Android est `fr.m2.pokequiz`, dans `androidApp/build.gradle.kts`. La `mainClass` desktop est `fr.m2.pokequiz.MainKt`, dans `desktopApp/build.gradle.kts`. Kotlin génère une classe JVM appelée `MainKt` pour les fonctions de niveau supérieur du fichier `main.kt` (le nom du fichier, avec `Kt`).

8. **Où la fonction `App()` est-elle définie ? Depuis combien d'endroits est-elle appelée, et lesquels ?**

   `App()` est définie dans `shared/src/commonMain/kotlin/fr/m2/pokequiz/App.kt`. Elle est appelée à quatre endroits :
   - dans `androidApp/src/main/kotlin/fr/m2/pokequiz/MainActivity.kt`, pour l'interface de l'activité et pour l'aperçu `AppAndroidPreview()` ;
   - dans `desktopApp/src/main/kotlin/fr/m2/pokequiz/main.kt`, dans la fenêtre desktop ;
   - dans `shared/src/iosMain/kotlin/fr/m2/pokequiz/MainViewController.kt`, dans le contrôleur iOS.

9. **Que contient le `.gitignore` généré ? Pourquoi `local.properties` doit-il être ignoré ?**

   Le `.gitignore` ignore notamment les fichiers de projet IntelliJ (`*.iml`, `.idea`), les caches et sorties de build (`.kotlin`, `.gradle`, `**/build/`), les fichiers utilisateur Xcode, les fichiers macOS (`.DS_Store`), les sorties Android natives (`.externalNativeBuild`, `.cxx`), les captures d'écran et `node_modules/`.

   `local.properties` contient des paramètres propres à la machine, notamment le chemin local du SDK Android. Il doit être ignoré pour ne pas partager un chemin invalide ou différent pour les autres développeurs.

10. **Dans `shared/src/commonMain/.../App.kt`, ajoutez temporairement `val f = java.io.File("x")` dans `App()`. Que dit l'IDE ? Pourquoi ?**

   L'IDE signale que `java.io.File` (ou la référence `java`) est introuvable dans `commonMain`. Ce source set doit compiler pour les différentes plateformes, tandis que `java.io.File` est une API propre à la JVM. Il faut donc réserver cette API à un source set JVM ou utiliser une abstraction multiplateforme. La ligne temporaire n'est pas conservée dans le projet.



## Question de fin

1. **Pourquoi est-il intéressant de développer l'UI sur la cible *Desktop* même si le produit final est une app mobile ?**

Développer sur Desktop permert de tester rapidement l'interface sans avoir besoin d'un émulateur Android ou d'un téléphone.

Cela facilite ke débogage, accélère les tests et permet de vérifier le fonctionnement du code commun avant de passer sur mobile. 

2. **Quelle différence faites-vous entre un *Hot Reload* et un redémarrage ? Quels changements ont nécesité un redémarage complet à l'étape 7 ?**

- **Hot Reload** : applique rapidement certaines modifications du code sans relancer complétement l'application
- **Redémarrage complet** : arrête et relance l'application pour prendre en compte les modifications qui nécessitent une nouvelle compilation ou une réinitialisation. 

Un redémareage complet est notament nécessaire après des modifications de configuration Gradle, de dépendances ou de la structure du projet. 

3. **Que se passerait-il si quelqu'un ajoute dans *shared/build.gradle.kts* un bloc *androidTarget {}* trouvé dans un tutoriel de 2023 ?**

Le projet pourrait rencotrer une erreur de configuration ou de compilation. Les anciennes configuration Kotlin Multiplatforme ne correspondent pas forcément aux version récentes du plugin. Il faut utiliser la configuration adaptée à la versiuon actuelle de Kotlin et d'Android. 

4. **Le texte "Hello/Bonjour depuis ..." est différent sur chaque plateforme alors que *Greeting* est un code commum. Expliquez le mécanisme**.

Même si `Greeting` est écrit dasn `commonMain`, il peut utilsier du code spécifique à chaque plateforme. Kotlin multiplatform permet de définir un comportement commun et une implémentation différente en fonction de la plateforme. 

5. **Le même *Greeting* est compilé en DEX (Android), en bytecode JVM (desktop) et en binaire natif (IOS). Selon vous, laquelle de ces formes est la plus facliel à analyser pour un attaquant ? Pourquoi est-ce important si l'on met à jour du code sensible dans *comminMain*?**

Le *bytecide JVM* est le plus facile à analyser car il existe des outils permettant de le décompiler relativement facilement. 

Le code sensible placé dans `commonMain` peut être distribué sur plusieurs plateformes. Il faut donc éviter d'y stocker des mots de passe, des clés secrètes ou des informations confidentielles. 

6. **Tâches Graddle : listez les tâches liée à l'exécution : `./gradlew tasks --all | grep -i run`**

Les tâches liées à l'exécution sont notamment :

- `desktopApp:run`
- `desktopApp:hotRun`
- `desktopApp:hotDev`
- `desktopApp:hotRunAsync`
- `shared:jvmRun`
- `shared:hotRunJvm`
- `shared:hotDevJvm`
- `desktopApp:createRuntimeImage`

Elles permettent de lancer l'application, de l'exécuter en mode hot reload, ou de préparer un runtime pour la cible desktop.

7. **Sorties de compilation : après un `./gradlew : shared:jvmJar` (et `:shared:copileKotlinIosSimulationArm64` sur MAC), explorer `shared/build/` . Où sont les *.class*? Trouvez-vous des fichiers ou dossiers *klib* ? A quoi correspondent-ils ?**

Après le build JVM, les fichiers `.class` sont dans :

- `shared/build/classes/kotlin/jvm/main/...`
- `shared/build/classes/kotlin/android/main/...`

Le jar final est produit dans :

- `shared/build/libs/shared-jvm.jar`

Les classes Java/Kotlin compilées correspondent au bytecode exécuté par la JVM. Les `*.klib` ne sont pas visibles après un simple `jvmJar` ; ils concernent plutôt les artefacts Kotlin/Native pour les cibles iOS/Apple. Un `.klib` est une bibliothèque Kotlin native (équivalent d'un fichier de bibliothèque compilée pour les cibles native), utilisée par le compilateur et le runtime Kotlin/Native. Sur macOS, `:shared:compileKotlinIosSimulationArm64` produit des artefacts de ce type, qui sont ensuite assemblés dans un framework ou un binaire ciblé.

8. **Dépendances : affichez l'abre de dépendances runtime de la cible desktop du module partagé : `./gradlew : shared:dependencies --configuration jvmRuntimeClasspath`**
**Quelle bibliothèuqe *skiko* apparaît ? Pourquoi**

La dépendance importante qui apparaît est :

- `org.jetbrains.skiko:skiko:0.150.1`

Elle est pullée via les bibliothèques Compose Desktop (`org.jetbrains.compose...`) et est utilisée pour le rendu graphique. Skiko est le binding Kotlin/Compose vers Skia, la bibliothèque de dessin 2D utilisée pour afficher les fenêtres, les boutons et les composants UI sur la cible desktop. Sans Skiko, Compose Desktop ne pourrait pas rendre l'interface graphique sur JVM.

9. **Preflight checks : explorer les réglages de l'outil (relance, détails)**

Les Preflight Checks sont des vérifications automatiques avant exécution. Ils permettent de contrôler que le projet, le SDK, les plugins, la configuration Gradle et l'environnement local sont cohérents. Dans ce projet, cela aide à prévenir des erreurs de lancement (versions incompatibles, cibles manquantes, mauvais niveau de build, etc.). L'outil peut proposer de relancer la vérification ou afficher les détails de l'erreur pour corriger le problème de manière ciblée.

10. **IOS (MAC) : lancer l'app sur un simulateur et comparez le rendu du bouton avec un Android**

Je suis sous Linux
