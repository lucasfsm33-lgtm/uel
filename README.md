# BlueGPT Video Editor & Designer 🎬

A modern mobile video editing studio built with Kotlin and Jetpack Compose featuring a sleek Cobalt Blue theme, CapCut-style multi-track timeline, visual effects, filters, and the BlueGPT AI video assistant.

---

## 📲 Pre-built APK Location

A pre-compiled APK is included in the project:
* **Direct File Path**: `apk/BlueGPT.apk` (24 MB)

When you click **Download ZIP**, open the ZIP and look inside the **`apk/`** folder to find **`BlueGPT.apk`**!

---

## 📲 How to Download & Install the APK

### Option 1: Inside the Downloaded ZIP
1. Click **Download ZIP** in AI Studio.
2. Unzip the downloaded file.
3. Open the **`apk/`** folder.
4. Install **`BlueGPT.apk`** on your Android device!

### Option 2: On GitHub (Direct or Automated Builds)
* The pre-built APK is located at `apk/BlueGPT.apk` in your GitHub repository.
* In addition, the automated CI workflow in `.github/workflows/build-apk.yml` builds fresh APKs on every push under the **Actions** tab.

### Option 2: Directly in Google AI Studio
1. In the top-right corner of the AI Studio window, click the **Settings (gear / three dots)** menu.
2. Select **Generate APK / Download APK**.
3. Once generated, the `.apk` file will download directly to your computer or phone.

### Option 3: Build Locally via Terminal
If you cloned the repository to your machine:
```bash
./gradlew assembleDebug
```
The output file will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## ✨ Features
- **CapCut-Style Video Timeline**: Multi-track editing for video clips, text overlays, and audio waveforms with scrubbable playhead needle.
- **Top Bar**: Resolution picker (`720p`, `1080p`, `2K`, `4K`) and quick Export button.
- **Video Preview Player**: Rounded viewport with beach scene, real-time color filters, stickers, text overlays, and timecode counter (`00:07 / 00:24`).
- **CapCut Toolbar**: Edit (Split, Trim, Speed, Volume), Audio, Text, Stickers, Effects, Filters, and Captions.
- **BlueGPT AI Hub**: Prompt-to-video script generator, project library, and AI assistant chat for hooks and viral video tips.
