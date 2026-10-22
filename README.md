# Aria.app

Native Android app — Kotlin + Jetpack Compose.

**v0.1 ("notification base")**: a private, single-screen app whose only feature is
posting a test notification; requests the Android 13+ `POST_NOTIFICATIONS` runtime
permission on first use, creates its channel on process start.

> Simply: tap the button, get a notification. The server-connected versions (v0.2+)
> will trigger this same `Notifications.post()` from the home server instead of a button.

## Build (CLI-only toolchain, no Android Studio)

- JDK: Temurin 21 (pinned in `gradle.properties` → `~/tools/jdk21`) — system JDK 27 is too new
- SDK: `~/Android/Sdk` (`local.properties`, untracked)
- Android Gradle Plugin 9.x (Kotlin built into AGP), Gradle 9.8.x wrapper

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Detailed toolchain/feature plan: `~/.opencode/plan/aria-notification-base.md`
