Ehilà Patryk, questo dovrebbe essere il progetto finito anche se prima d inviarlo tra oggi e domani volevo riscrivere in modo più bellino alcune parti di codice, e cambiare il nome del pacchetto perchè chiamato proprio come l'ultimo laboratorio fa brutto brutto ahahahah

Ho corretto il bug sulla stampa in disordine delle cose, implementato il foreach e corretto u paio di bug rispetto alla precedentemente versione. L'unica cosa che mi turba è se sia necessario implementare il foreach anche con variabili non inizializzate perchè i test di success non ne hanno bisogno e i due di failure si arrestano prima di arrivare a "environmental error, variable not inizialized" ma di questo ne parliamo stasera con calma.

Per quanto riguarda l'eseguire sto affare, io uso l'IDE intellij IDEA Community Edition, che è quello che hai visto settimana scorsa sul mio portatile, se usi lo stessi se segui i seguenti passi non dovresti avere problemi:

1- Apri un nuovo progetto e cancella il file Main incluso di default;
2- Muovi il cursore sul nome del progetto, tasto destro, copy path, tasto sinistro su absolute path;
3- Entra col path nella cartella del progetto, apri la cartella src e piazza dentro la cartella lab09_05_11 che trovi nella cartella update, non mettere te i file uno ad uno perchè la cartella è il pacchetto al quale fanno riferimento;
4- riapri il progetto e dovrebbero essere comparsi tutti i file;
5- In alto vai su Run e Edit configuration;
6- crea una nuova configurazione impostata come Application e seleziona il file Main;
7- sotto al nome del file Main scrivi -ntc(se necessario per il test) e -i "filePath";
8- esci dalla configurazione premendo ok e avvia il programma cliccando su Run, Run Main;

Ogni volta che fai un esperimento cambi il contenuto del file di test e avvii il programma con Run Main.
Buona giornata, a stasera!