# PulseQuiz

A module-based MCQ quiz app. The home screen lists quiz modules fetched from a
gist; pick one to take its 10-question quiz, finish it, and the module list
shows your status and score — persisted locally so it survives a relaunch.

Built for the DailyRounds assignment (R0 → R1).

## Features

- **Module list (home)** — every module from the catalog, each showing its
  title, description, a `Start` / `Review` status button, and a
  `X Questions · Score: Y/10` summary once played.
- **Module quiz** — the full quiz flow, scoped to the picked module:
  - question with four options; tapping one reveals the correct answer and your
    pick (green ✓ / red ✕, rest faded), locks input, then auto-advances after
    2 seconds with a countdown bar. Skip advances immediately.
  - drag the question card to the right to skip; it follows your finger and
    springs back below the threshold.
  - **streak** — consecutive correct answers bump the flame badge. At 3 it
    lights up (glow + dancing flame) with a one-shot "You're on fire!" toast and
    haptic. A wrong answer resets it. Longest streak is tracked.
- **Results** — animated score ring with count-up, stat rows staggering in,
  headline based on score, confetti at 70%+. **Finish** saves the result and
  returns to the module list; **Restart Quiz** replays the module.
- **Persistence** — each module's status, score, and per-question answers are
  written to a local database on finish, and restored on next launch.

## Tech stack

Kotlin · Jetpack Compose (Material 3) · MVVM with unidirectional data flow ·
Coroutines + StateFlow · Navigation-Compose (type-safe) · Retrofit +
kotlinx.serialization · Room

## Architecture

Three layers, state flows down and events flow up.

```
UI (Compose screens + components)
   ↓ observes StateFlow<UiState>   ↑ sends actions
ViewModels
   ├─ ModuleListViewModel  — catalog + saved progress, merged live
   └─ QuizViewModel        — streak logic, 2s reveal timer, save on finish
   ↓ suspend calls returning Result<…>
Repositories
   ├─ ModuleRepository   → Retrofit → module catalog + per-module questions
   └─ ProgressRepository → Room DAO → module_progress table
```

- **Navigation** — two destinations wired with type-safe Navigation-Compose:
  `ModuleListRoute` (start) and `QuizRoute(moduleId, questionsUrl)`. The quiz
  ViewModel reads its arguments straight from `SavedStateHandle`, so the nav
  graph stays declarative.
- **Two-step fetch** — the catalog (`getModules()`) is loaded once; a module's
  questions are fetched lazily by their `questionsUrl` (`@Url`) only when the
  user starts it.
- **Live list** — the module DAO exposes progress as a `Flow`, so finishing a
  quiz writes to Room and the list re-renders itself: `Start` → `Review` with
  the new score, no manual refresh.
- **State modelling** — `QuizUiState` and `ModuleListUiState` are sealed
  interfaces (`Loading / Error / …`), so each screen is one exhaustive `when`
  and illegal states can't exist. Progress is saved the moment the quiz
  completes (not on the Finish tap) so the write can't be cancelled by
  navigating away.
- **DTOs** map to domain models at the repository boundary, keeping the API
  shape out of the UI. Room entities map to domain models the same way.
- **DI** is a small manual `AppContainer` on the Application — Retrofit, the
  Room database, and the repositories built behind interfaces.

## Package structure

```
com.example.dailroundsmarrowassessment1
├── data/
│   ├── remote/        QuizApi, ModuleDto, QuestionDto
│   ├── local/         AppDatabase, ModuleProgressDao, entity, converters
│   ├── ModuleRepositoryImpl
│   └── ProgressRepositoryImpl
├── domain/            Module, Question, ModuleProgress, repository interfaces
├── di/                AppContainer
├── ui/
│   ├── modules/       ModuleListViewModel, ModuleListScreen, contract
│   ├── quiz/          QuizViewModel, QuizFlow, screens, components
│   ├── theme/         Color, Type, Theme
│   ├── AppRoutes      type-safe navigation routes
│   └── PulseQuizApp   NavHost
├── QuizApplication
└── MainActivity
```

## Running

Open in Android Studio, sync, and run the `app` configuration on a device or
emulator (min SDK 24). Internet is needed to load modules and questions; if a
fetch fails you get a retry screen.
