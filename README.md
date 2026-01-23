# TAF Mobile Application (Appium + TestNG)

## 📌 Prerequisites
Before running tests, make sure you have installed:

- **Java 17**
- **Maven**
- **Android Studio + Android SDK**
- **Android Emulator (AVD)**
- **Appium Server** (run manually)
- **ADB** available in PATH *(usually: `<Android_SDK>/platform-tools`)*

---

## ✅ Step 1: Install the application on the emulator

### 📍 Where are the application files located?
The application install files are stored here:

📁 **`src/main/resources/app2/`**

Example path from the project root:
```text
./src/main/resources/app2/
```

### ✅ Install app (Split APK format)
Your app is provided as **split APKs** (base APK + config APKs), for example:

- `com.epam.connect.android.apk` *(base apk)*
- `config.en.apk`
- `config.x86_64.apk`
- `config.xxhdpi.apk`

✅ Install all split APKs to emulator with this command (run from project root):

**CMD (Windows):**
```bash
adb install-multiple -r ^
  ./src/main/resources/app2/com.epam.connect.android.apk ^
  ./src/main/resources/app2/config.en.apk ^
  ./src/main/resources/app2/config.x86_64.apk ^
  ./src/main/resources/app2/config.xxhdpi.apk
```

✅ Explanation:
- `install-multiple` installs base + split APKs together
- `-r` reinstalls the app if it already exists

---

### 1) Start your emulator
Example (AVD name = `Phone`):
```bash
emulator -avd Phone
```

### 2) Check that ADB sees the emulator
```bash
adb devices -l
```

### 3) (Optional) Verify app is installed
```bash
adb shell pm list packages | findstr epam
```

---

## ✅ Step 2: Configure emulator name in `test.properties`
Open:

📄 `src/main/resources/test.properties`

Set your emulator name in:
```properties
avd.name=Phone
```

✅ You can check your available AVD names with:
```bash
emulator -list-avds
```

Example output:
```text
Phone
```
---

## ✅ Step 3: Start Appium server manually
Run Appium in terminal (default):
```bash
appium
```

---

## ✅ Step 4: Run tests

### Run from IntelliJ IDEA
- Open `LoginOptionsTests`
- Right click → **Run**

### Run from Maven
Run smoke suite:
```bash
mvn test -Dsuite=smoke
```
