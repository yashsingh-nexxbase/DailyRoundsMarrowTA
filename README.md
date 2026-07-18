# PulseQuiz

MCQ quiz app built for the R0 assignment. Fetches 10 questions from the
provided gist, runs a streak-based quiz flow, and ends on a results screen
with a restart option.

## Features

- **Splash / loading** — animated splash while the question JSON is fetched
  and parsed. Network failure shows an error screen with retry.
- **Question flow** — question with four options. Tapping an option reveals
  the correct answer and the selected one (green ✓ / red ✕, rest faded),
  locks input, then auto-advances after 2 seconds with a visible countdown
  bar. Skip advances immediately.
- **Swipe gesture** — dragging the question card to the right skips it; the
  card follows the finger and springs back below the threshold.
- **Streak** — consecutive correct answers bump the flame badge. At 3 it
  lights up (glow + dancing flame) with a one-shot "You're on fire!" toast
  and haptic. A wrong answer resets it to 0. Longest streak is tracked.
- **Results** — animated score ring with count-up, correct / wrong / skipped /
  longest streak rows staggering in, headline based on score, confetti at
  70%+, and Restart Quiz which resets everything back to question 1.

## Tech stack

Kotlin · Jetpack Compose (Material 3) · MVVM with unidirectional data flow ·
Coroutines + StateFlow · Retrofit + kotlinx.serialization

## Architecture

Three layers, state flows down and events flow up:

```
UI (Compose screens + components)
        ↓ observes StateFlow<QuizUiState>   ↑ sends QuizAction
QuizViewModel  — streak logic, 2s reveal timer, counters
        ↓ suspend getQuestions(): Result<List<Question>>
QuestionRepository (interface)
        └─ QuestionRepositoryImpl → Retrofit → gist JSON
```

- `QuizUiState` is a sealed interface (`Loading / Error / Playing / Results`),
  so the UI is one exhaustive `when` and illegal state combinations can't
  exist. `Playing` carries an optional `Reveal`; input is locked whenever a
  reveal is showing, which also kills double-taps.
- The 2-second auto-advance runs as a cancellable coroutine in
  `viewModelScope`, so it survives rotation and Restart can cancel it. The
  countdown bar in the UI only mirrors it.
- One-shot events (streak ignited / streak lost) go through a `Channel` so
  they fire exactly once and don't replay on configuration change.
- DTO (`QuestionDto`, matches the gist fields) is mapped to a domain model
  (`Question`) at the repository boundary, so the API shape stays out of the
  UI layer.
- DI is a small manual `AppContainer` on the Application class — constructor
  injection behind interfaces without pulling in Hilt for a one-screen app.
  The seams are there if it ever grows.

## Package structure

```
com.example.dailroundsmarrowassessment1
├── data/
│   ├── remote/            QuizApi, QuestionDto
│   └── QuestionRepositoryImpl
├── domain/                Question, QuestionRepository
├── di/                    AppContainer
├── ui/
│   ├── quiz/              QuizViewModel, QuizContract, screens
│   │   └── components/    OptionRow, StreakBadge, ScoreRing,
│   │                      SegmentedProgress, Confetti
│   └── theme/             Color, Type, Theme
├── QuizApplication
└── MainActivity
```

## Running

Open in Android Studio, sync, and run the `app` configuration on a device or
emulator (min SDK 24). Internet is needed to load the questions; if the fetch
fails you get a retry screen.
