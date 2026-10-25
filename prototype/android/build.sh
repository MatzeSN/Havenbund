#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
: "${ANDROID_SDK_ROOT:?Set ANDROID_SDK_ROOT to an Android SDK with platform 35 and build-tools 35.0.0}"
BT="$ANDROID_SDK_ROOT/build-tools/35.0.0"
JAR="$ANDROID_SDK_ROOT/platforms/android-35/android.jar"
OUT="$PWD/android/build"
mkdir -p "$OUT/classes" "$OUT/dex" "$OUT/generated" "$OUT/assets"
cp dist/index.html dist/style.css dist/app.js dist/engine.js "$OUT/assets/"
# Change only host wording; keep the simulation identical to the tested web source.
python3 - "$OUT/assets/index.html" <<'PY'
import sys
from pathlib import Path
p=Path(sys.argv[1]);s=p.read_text().replace('Spielstand nur in diesem Browser. Keine Produktion bei geschlossener Seite.', 'Offline auf deinem Gerät. Spielstand wird lokal gespeichert. Keine Produktion bei geschlossener App.');p.write_text(s)
PY
"$BT/aapt" package -f -m -M android/AndroidManifest.xml -S android/res -A "$OUT/assets" -I "$JAR" -J "$OUT/generated" -F "$OUT/resources.apk"
find android/src "$OUT/generated" -name '*.java' > "$OUT/sources.txt"
javac -source 8 -target 8 -bootclasspath "$JAR" -d "$OUT/classes" @"$OUT/sources.txt"
find "$OUT/classes" -name '*.class' > "$OUT/classes.txt"
"$BT/d8" --lib "$JAR" --min-api 26 --output "$OUT/dex" @"$OUT/classes.txt"
cp "$OUT/resources.apk" "$OUT/unsigned.apk"
(cd "$OUT/dex" && zip -q -j "$OUT/unsigned.apk" classes.dex)
"$BT/zipalign" -f 4 "$OUT/unsigned.apk" "$OUT/aligned.apk"
# Development certificate only; keep the same local key to allow prototype upgrades.
KEY="${HAVENBUND_DEBUG_KEYSTORE:-$HOME/.android/havenbund-debug.keystore}"
mkdir -p "$(dirname "$KEY")"
if [ ! -f "$KEY" ]; then
  keytool -genkeypair -keystore "$KEY" -storepass android -keypass android -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 10000 -dname 'CN=Havenbund Prototype,O=Havenbund,C=DE'
fi
"$BT/apksigner" sign --ks "$KEY" --ks-key-alias androiddebugkey --ks-pass pass:android --key-pass pass:android --out "$OUT/Havenbund-0.1.0-debug.apk" "$OUT/aligned.apk"
"$BT/apksigner" verify --verbose "$OUT/Havenbund-0.1.0-debug.apk"
sha256sum "$OUT/Havenbund-0.1.0-debug.apk"
