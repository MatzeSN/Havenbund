# Havenbund Android-Prototyp 0.1.0

Installierbare Offline-APK für Android ab 8.0 (API 26). Der vorhandene Canvas/JavaScript-Spielkern läuft in einer Android-WebView mit ausschließlich lokal eingebundenen Dateien. Kein Browserstart, keine Anmeldung, keine Internetberechtigung, keine Serververbindung und keine GitHub Actions. Noch keine native Spiele-Engine.

## Installation

1. `Havenbund-0.1.0-debug.apk` auf das Android-Gerät herunterladen.
2. APK öffnen. Falls Android nachfragt, der verwendeten Download-/Datei-App die Installation dieser APK erlauben.
3. Havenbund über das App-Symbol starten. Tutorial beginnen.
4. Im Flugmodus prüfen: Das vollständige Spiel muss weiterhin laden.

Die APK ist mit einem Entwicklungsschlüssel signiert und kein Play-Store-Release. Ein aktuelles Android System WebView ist für die JavaScript-Funktionen erforderlich. App-Daten bleiben lokal; Deinstallation bzw. Löschen der App-Daten löscht den Spielstand. Browser-Spielstände werden nicht automatisch übernommen.

## Lokal bauen (Linux)

Voraussetzungen: JDK 17, Python 3, zip, Android SDK Platform 35, Build Tools 35.0.0.

```sh
cd prototype
export ANDROID_SDK_ROOT=/pfad/zum/android-sdk
./android/build.sh
```

Ergebnis: `android/build/Havenbund-0.1.0-debug.apk`.

Der Build nutzt direkt die Android-SDK-Werkzeuge (aapt, javac, d8, zipalign, apksigner), ohne Gradle-Downloads. Er übernimmt die vier Dateien aus `dist` und passt die Startmeldung für Android an. Der Entwicklungsschlüssel liegt außerhalb des Repos unter `~/.android/havenbund-debug.keystore`. Für wiederholte Updates denselben Schlüssel behalten oder `HAVENBUND_DEBUG_KEYSTORE` setzen. Ein späteres Release braucht einen dauerhaft verwalteten Signaturschlüssel. Bei anderem Schlüssel kann Android die App nicht über die bestehende Installation aktualisieren; vorher Spielstand berücksichtigen.

## Prüfstatus und Geräteabnahme

Die JavaScript-Simulation hat zwölf bestandene Tests. APK-Build, Manifest, gebündelte Dateien, Ausrichtung und Signatur werden lokal geprüft. Installation und Laufzeit auf einem echten Android-Gerät bzw. Emulator sind noch nicht geprüft.

1. APK installieren, App-Symbol und Start ohne Anmeldung prüfen.
2. Flugmodus einschalten, App schließen und erneut starten. Tutorial/Karte laden offline.
3. Holzfäller neben dem Hafenweg bauen, Produktion beobachten; Wege bauen und Karte per Finger verschieben.
4. Zwischen Hoch-/Querformat wechseln: Bestand und Gebäude bleiben erhalten, Systemleisten verdecken keine Bedienelemente.
5. Speichern, App in den Hintergrund legen und erneut öffnen. Spielstand bleibt erhalten; kein Offline-Fortschritt.
6. Zurück-Taste/Geste: Beenden bestätigen oder weiterspielen. Nach Neustart bleibt der zuletzt gespeicherte Stand bestehen.
7. Zweiten Ort gründen und Route einrichten. Formulare mit Bildschirmtastatur bedienen.
8. Mit identischem Signaturschlüssel gebaute Folge-APK als Update installieren; Spielstand bleibt bestehen.

Noch offen: umfassende Touch-Optimierung, Tests verschiedener Displaygrößen/Android-Versionen, Spielbalance und sämtliche in `../README.md` benannten Spielinhalte.
