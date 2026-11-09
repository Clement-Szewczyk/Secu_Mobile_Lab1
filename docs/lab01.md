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



