# PMO-Uno 

## Descrizione

PMO-Uno è un gioco UNO sviluppato in Java seguendo il pattern MVC e le regole della Programmazione ad Oggetti.  
Il progetto utilizza JavaFX per l’interfaccia grafica e Maven per la gestione delle dipendenze e la compilazione.

---

## Struttura del progetto

```
PMO-Uno/
│
├── uno/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                # Codice sorgente Java (model, controller, app)
│   │   │   └── resources/           # Risorse (FXML, immagini)
│   │   └── test/
│   │       └── java/                # Test JUnit
│   ├── pom.xml                      # Configurazione Maven
│   └── target/                      # Cartella di output Maven (JAR)
│
├── UNO-Gioco/                       # Cartella pronta per la distribuzione
│   ├── uno-game.jar                 # Eseguibile del gioco
│   ├── start.bat                    # Script di avvio per Windows
│   └── javafx-sdk-XX.X.X/           # JavaFX SDK (lib)
│
└── README.md                        # Questo file
```

---

## Compilazione del progetto

1. **Prerequisiti:**  
   - Java 17 o superiore  
   - Maven 3.x

2. **Compila il progetto:**  
   Apri il terminale nella cartella `uno` e lancia:
   ```
   mvn clean package
   ```
   Il file eseguibile verrà generato in `uno/target/uno-game.jar`.

---

## Esecuzione del gioco

### **Modalità sviluppatore (con Maven):**
- Da terminale, nella cartella `uno`:
  ```
  mvn javafx:run
  ```

### **Modalità utente finale (cartella UNO-Gioco):**
--Nota: Questa versione è eseguibile solo su sistema operativo Windows--
1. Assicurati di avere Java 17 (o superiore) installato.
3. Apri la cartella `UNO-Gioco`.
4. Fai doppio click su `start.bat`  
   *(oppure esegui da terminale:*
   ```
   java --module-path "javafx-sdk-XX.X.X\lib" --add-modules javafx.controls,javafx.fxml -jar uno-game.jar
   ```
   *)
   
---


## Test

Per eseguire i test automatici:
```
mvn test
```
I test coprono la logica del model (Player, Deck, Game, ecc.).

---

## Pattern e buone pratiche

- **OOP:** ereditarietà, incapsulamento, polimorfismo, override, classi astratte.
- **MVC:** separazione tra Model (logica), View (FXML/JavaFX), Controller (gestione eventi).
- **Eventi:** uso di listener per aggiornare la GUI in risposta ai cambiamenti del model.
- **Test:** copertura delle principali funzionalità con JUnit.

---

## Autori

- Nome: *[Andrii Ursu, Diego Chiodi, Edoardo Guardabascio]*
- Corso: Programmazione ad Oggetti
- Docente: *[Sara Montagna]*

---

## Note

- Per problemi di avvio, assicurarsi che la versione di JavaFX SDK nella cartella corrisponda a quella usata in `pom.xml`.
- Il progetto è stato realizzato per scopi didattici.
