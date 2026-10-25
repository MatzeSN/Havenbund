# Havenbund · Prototyp 0.1

Spielbarer erster Ausschnitt des Hanse-Aufbauspiels. Statisches HTML, CSS und JavaScript ohne Paketinstallation, externe Assets oder Server-Datenbank.

## Lokal starten

Im Ordner `prototype`:

```sh
python -m http.server 8080 --directory dist
```

Dann http://localhost:8080 öffnen. Alternativ die vier Dateien in `dist` auf einem statischen Webserver bereitstellen.

## Enthalten

- Isometrische Inselkarte mit Bauen, Wegen, Gebäudedetails, Zoom und Verschieben.
- Lindenhavn (Wald, Fisch, Ackerland), Salzwiek (Sole, Wald), Lehmau (Lehm, Ackerland).
- Holz/Bretter, Fisch, Getreide/Mehl/Brot, Futter, Salz, Lehm/Ziegel und vereinfachtes Bier.
- Arbeitskräfte, Nahrung, Lagergrenzen, Straßenanbindung und Produktionspausen.
- Feste Hin- und Rückrouten, Mindestbestände, Schiffe und Fuhrwerke mit Futterverbrauch.
- Geführter Start in neun Schritten, Aufstieg von der Siedlung zum Dorf.
- Pause, Zeitraffer und automatischer Spielstand im Browser (`havenbund-v01`).

Tipp: Erst Holzfäller, Sägewerk und Fischversorgung errichten. Gebäude müssen neben einem Weg stehen, der zum Hafen führt. Weitere Höfe helfen, Brotüberschüsse zu erzielen. Neue Orte kosten 16 Holz und 12 Bretter in Lindenhavn. Für die Salzroute eine Saline in Salzwiek errichten; Lebensmittelversorgung der neuen Orte ebenfalls einplanen.

## Bewusste Grenzen

Noch kein vollständiges Spiel: keine Preise, Münzen, KI-Händler, Pferdezucht, Metallverarbeitung, Jahreszeiten oder Stadtstufen oberhalb des Dorfs. Handel ist derzeit Warentransport innerhalb des eigenen Bundes. Fuhrwerke erhalten ihr Gespann beim Bau; Futter wird pro Fahrt verbraucht. Bier nutzt ein vereinfachtes Getreiderezept ohne separate Mälzerei/Hopfen. Lagerbestand wird zwischen angebundenen Betrieben direkt geteilt; Personen und Boote auf der Karte sind Dekoration. Die drei Ortskarten nutzen dieselbe Grundform. Wege sind kostenlos, Abriss erstattet Baukosten bis zur Lagergrenze. Routen lassen sich pausieren, noch nicht bearbeiten/löschen. Kein serverseitiger Speicher, kein Offline-Fortschritt. Kein vollständig barrierefreier Kartenbau. Kein Echtgeld und keine Anmeldung im Spiel.

## Tests und manuelle Abnahme

Automatisch mit Node.js (integrierter Test-Runner):

```sh
node --test tests/engine.test.js
```

12 Tests prüfen Baukosten/Fehlplatzierung, Wege, Produktionsbilanz, Lagergrenzen, Nahrung, Futteranbau, See- und Landtransport, Rückfracht, volle Ziellager, Futter als Ladung, ungültige Reserven, Pause/Speichern und einen erreichbaren Spielverlauf bis zum Dorf und zweiten Ort.

Manuell:

1. Neu starten, Begrüßung schließen. Tutorial erscheint bei Schritt 1.
2. Holzfäller neben Hafenweg bauen. Holz steigt nach acht Spielsekunden. Schritt 2 wird freigeschaltet.
3. Sägewerk zunächst ohne Anbindung bauen: Warnsymbol und keine Produktion. Weg anschließen, Produktion beginnt.
4. Fischer am Ufer anschließen, zwei Wohnhäuser bauen. Bevölkerung erreicht 20.
5. Bauernhof, Mühle und Bäckerei anschließen. Bei Rohstoffmangel weitere Betriebe ergänzen. Brot entsteht.
6. Mit 8 Brot, 12 Brettern und ohne Hunger zum Dorf aufsteigen.
7. Salzwiek gründen, Saline anbinden, Schiffsroute Holz/Salz anlegen. Beide Bestände und Fahrtstatus beobachten.
8. Lehmau gründen, Landroute anlegen. An beiden Enden Futter bereitstellen. Ohne Futter wartet das Gespann.
9. Pause drücken: Bestände und Fahrten bleiben stehen. Speichern und neu laden: gleicher Spielstand.
10. Smartphone: Warenleiste horizontal scrollen, Karte verschieben, Gebäude bauen, Menüs bedienen. Prüfung auf echten Mobilgeräten und Safari noch offen.

## Updates

Statische Dateien gemeinsam ersetzen, Browser neu laden. Diese erste Version hat noch keine frühere Anwendung zu migrieren. Spielstände liegen pro Browser und Webadresse; ein Wechsel von localhost zur bereitgestellten Seite übernimmt sie nicht. Browserdaten nicht löschen, wenn der Spielstand erhalten bleiben soll. Künftige inkompatible Speicheränderungen benötigen eine Migration.
