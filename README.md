# ProSafety Application

ProSafety is a native Android personal-safety application developed as a Final Year Project. It helps users prepare a trusted safety circle, find directions, access emergency calling, and send an existing shake-triggered SMS alert with location information.

The interface has been redesigned around the original ProSafety FYP identity. The app now uses the project’s circular SOS logo, a calm navy-and-emerald visual system, clearer navigation, accessible forms, and consistent confirmation dialogs.

## Main features

- Firebase email registration, sign-in, password reset, and profile storage
- Trusted-contact management for up to five contacts
- Existing three-shake emergency SMS workflow with location information
- Emergency dialer shortcut for `999`
- Directions through the device’s maps application
- Flashlight control on supported devices
- Safety guide and safety-news screens
- Profile editing and profile-picture upload

## Redesigned experience

- A polished side navigation drawer with grouped sections, dividers, current-screen highlighting, and clear emergency styling
- The original ProSafety FYP logo across the launcher, splash, toolbar, drawer, authentication, empty state, and logout screens
- A clearer Home screen that prioritizes trusted contacts and emergency calling
- Consistent typography, spacing, cards, inputs, buttons, and color hierarchy
- Improved sign-in, registration, password-reset, profile, directions, contacts, and guidance screens
- App-owned dialogs with rounded cards, contextual icons, descriptive messages, and explicit actions
- Non-blocking feedback banners for brief success and status messages
- Improved empty states, validation messages, loading feedback, and contact-removal controls

Android permission prompts, the contact picker, image picker, maps chooser, and other operating-system surfaces keep their standard Android appearance.

## Technology

- Java
- Android XML layouts
- AndroidX and Material Components
- Firebase Authentication, Realtime Database, Storage, and Analytics
- Google Play Services Location and Maps
- SQLite for trusted contacts
- Picasso for profile images

## Requirements

- Android Studio
- JDK 17 for the included Gradle setup
- Android SDK Platform 30
- Android SDK Build-Tools 30.0.3
- An Android device or emulator running API 16 or newer

## Run the project

1. Clone the repository:

   ```bash
   git clone https://github.com/Mierul01/ProSafety-Application.git
   ```

2. Open `ProSafety-Application` in Android Studio.
3. Select JDK 17 under **Settings > Build, Execution, Deployment > Build Tools > Gradle**.
4. Install Android SDK Platform 30 and Build-Tools 30.0.3 from the SDK Manager.
5. Allow Gradle sync to finish.
6. Select the `app` run configuration and an Android device or emulator.
7. Press **Run**.

The Firebase configuration currently included in the repository belongs to the original project. Firebase-backed functions require that project and its enabled services to remain available.

## Review the UI without credentials

Debug builds include an **Explore the design (no login needed)** entry on Sign in and Home. It previews the real Android layouts using sample data without authenticating, accessing contacts, requesting permissions, making calls, sending SMS messages, or controlling hardware.

The preview selector includes Home, trusted contacts, directions, safety guidance, profile, authentication screens, support, and a pop-up gallery. The pop-up gallery safely demonstrates logout, contact removal, account errors, password-reset confirmation, profile editing, and feedback banners.

The preview activity exists only under `src/debug` and is excluded from release builds.

## Build

From Android Studio, use **Build > Build Bundle(s) / APK(s) > Build APK(s)**. From a terminal:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
gradlew.bat assembleDebug
```

The standard output is `app/build/outputs/apk/debug/app-debug.apk`. Android Studio deployment builds may also appear under `app/build/intermediates/apk/debug/`.

## Verification

The redesigned debug application has been compiled, installed, and visually reviewed on a Pixel 8a emulator running Android 16. Automated local checks validate resource XML, local resource references, Java view IDs, shared styles, and XML click handlers.

Firebase authentication and registration, real SMS delivery, location permissions, background shake detection, contact access, and flashlight behavior should also be tested on a physical Android device before production use.

## Safety note

ProSafety supports personal-safety preparation, but it does not replace emergency services. SMS delivery, GPS availability, permissions, mobile coverage, battery state, and device restrictions can affect emergency features. Always verify the emergency number appropriate for the intended country or region.

## Original project

Created by [Mierul01](https://github.com/Mierul01) as a Final Year Project. This repository preserves the original application logic while improving its UI, UX, navigation, validation, feedback, and documentation.
