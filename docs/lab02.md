# Réponses — Lab 2

## 1. Pourquoi pas `java.text.Normalizer` dans `commonMain` ?
`commonMain` doit compiler pour toutes les cibles, y compris iOS (Kotlin/Native) qui n'a pas de JDK. Il ne voit donc que la stdlib Kotlin commune. Même entre Android et Desktop, les bibliothèques Java ne sont pas strictement identiques. D'où `expect/actual`.

## 2. Pourquoi des interfaces plutôt que des `expect class` ?
- Le domaine définit un contrat, l'implémentation est injectée (inversion de dépendances).
- On peut les remplacer par des faux dans `commonTest`.
- Plusieurs implémentations possibles (API, cache, mémoire), alors qu'`expect class` impose un seul `actual` par cible.
- L'implémentation peut elle-même être commune (Ktor, SQLDelight).

## 3. Une cible sans `actual` ?
La compilation échoue (message du type « Expected function 'removeDiacritics' has no actual declaration in module shared_jvmMain for JVM »). Avec `wasmJs()`, la même erreur apparaîtrait tant qu'on n'ajoute pas un `actual` dans `wasmJsMain` (par exemple via `normalize("NFD")` de JavaScript). *À vérifier : relevez le message exact chez vous.*

## 4. Pourquoi `elapsed` en paramètre ?
Les tests restent déterministes (pas d'horloge réelle, cas limite « exactement 10 s » testable). Le chronomètre est un détail d'UI, et le moteur ne gère que les règles du jeu.

## 5. `Nidoran♀` / `Nidoran♂` : bug ou fonctionnalité ?
Un peu des deux : pratique pour le joueur (symboles difficiles à taper), mais on ne distingue plus deux espèces différentes. Évolution : remplacer `♀` par `f` et `♂` par `m` avant le filtrage, tout en acceptant `nidoran` seul.

## 6. Que vérifie de plus `commonTest` que `jvmTest` ?
Le test s'exécute sur chaque cible (JVM, Android host, iOS), donc il vérifie que chaque `actual` se comporte de la même façon. Un test dans `jvmTest` ne vérifierait que la JVM.

## 7. `check` vs `require`
`require` (`IllegalArgumentException`) valide les arguments : une liste vide est une donnée invalide. `check` (`IllegalStateException`) valide l'état de l'objet : `submit` est appelé alors que la partie est finie, alors que ses arguments sont valides.