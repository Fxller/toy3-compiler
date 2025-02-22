# toy3-compiler

Questo progetto, denominato **toy3-compiler**, nasce come esempio didattico di come realizzare un compilatore di base in Java utilizzando **JavaCC/CUP** per la parte di parsing, **JFlex** per il lexer e una serie di **Visitor** per l’elaborazione dell’AST (Abstract Syntax Tree).

## Funzionalità principali

- **Lexer** (JFlex)  
  Analizza il testo sorgente e ne identifica i token (parole chiave, identificatori, simboli, operatori, ecc.).

- **Parser** (CUP)  
  Prende in ingresso i token prodotti dal lexer e costruisce un albero sintattico astratto (AST) sulla base della grammatica definita.

- **Visitors** intermedi  
  - **AST Printer**: permette di visitare l’AST e stamparne la struttura (utile per il debugging e per capire la forma del codice sorgente una volta “parslato”).  
  - **Scoping**: gestisce gli ambiti di visibilità delle variabili e delle funzioni.  
  - **Type Checking**: verifica la coerenza dei tipi (per esempio controllando che operazioni e assegnazioni siano valide).

- **Code Generator**  
  L’ultimo passaggio del compilatore. Visita l’AST, genera il codice in linguaggio C e gestisce vari dettagli, come l’utilizzo di buffer per le stringhe e chiamate a funzioni di libreria standard (ad esempio `printf`, `scanf` ecc.).

## Struttura del progetto

1. **Grammatica (CUP)**  
   - Definisce la sintassi formale del linguaggio toy3, stabilendo le regole di produzione (es.: come sono costruite dichiarazioni di variabili, espressioni, istruzioni condizionali e cicli).

2. **Lexer (JFlex)**  
   - Riconosce i token e distingue parole chiave (`if`, `while`, `def`, ecc.), identificatori, numeri, stringhe, caratteri speciali e così via.

3. **Visitors** di analisi intermedia  
   - **AST Printer**: per stampare l’albero generato dal parser.  
   - **Scoping**: crea e gestisce gli scope, verificando ad esempio la dichiarazione corretta di variabili e funzioni.  
   - **Type Checking**: verifica i tipi nelle varie espressioni e istruzioni.

4. **CodeGenerator**  
   - Visita l’AST per produrre il codice C corrispondente. Esegue tutte le trasformazioni necessarie affinché il codice generato compili in un contesto C standard, con il supporto a tipologie di dati (tra cui le stringhe) e costrutti base del linguaggio toy3.

---

## Obiettivi didattici

- Comprendere i concetti di **analisi lessicale** (tokenizzazione) e **analisi sintattica** (costruzione dell’AST).
- Familiarizzare con la costruzione di **Visitor** per l’AST e i relativi compiti (stampa, scoping, type checking).
- Esercitarsi nella **generazione del codice** per un linguaggio target (in questo caso, C), comprendendo particolarità come la gestione delle stringhe e delle funzioni in C.
