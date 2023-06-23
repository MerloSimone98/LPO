update_22/06

Ciao Patryk!
Ho dato ieri l'esame di analisi. E' andato male anche a sto giro ma almeno ora posso dedicarmi in tutta serenità al progetto di LPO. Di seguito trovi tutte le informazioni sul file sorgente dell'ultimo laboratorio e come implementerei il tipo vector. Non ho ancora fatto niente di concreto sul codice quindi quello che trovi nella cartella è quello del professore.
Nella cartella trovi le cartelle src, contenente il codice sorgente dell'ultimo laboratorio finito e testato, e out contenente il codice sorgente pre interpretato e tutte le cartelle con i file di test del professore.

Testando il parser del prof ho scoperto che l'ultilizzo dello standard input è pressochè inutile in quanto non arriva mai a EOF dando il risultato. Ho provato in vari modi ma niente da fare. Ti consiglio quindi di fare da terminare usando il comando java sul main seguito da -i nome del file da testare.
Esempio nel mio caso:
cd Desktop\update22.06\lab09_05_11\out
java lab09_05_11/Main -i success\prog1.txt

Per come funziona sta schifezza di java potrebbe non piacergli il comando, il tal caso occorre settare i classPath. Non so spiegarti come, avevo cercato le istruzioni su stack overflow :') o puoi re-interpretare il sorgente con un ide o con javac e usare il comando direttamente dalla cartella con i file ottenuti.

Passando adesso, come ci eravamo accordati, all'implementazione del tipo vector. Metto di seguito quello che è il mio approccio, ci lavoro questi giorni sperando di riuscire a implementarlo correttamente entro domenica.

1 - Aggiungere i token OPEN_SQR e CLOSE_SQR nel file "TokenType" e aggiungerli alla funzione statica di "MyLangTokenizer";

2 - Aggiungere in "MyLangTokenizer" il parametro private int[] arr che sarà usato per inizializzare e implementare l'array se la sintassi data è corretta; (devo ancora capire se il parametro va messo in questo file, il file dedicato o entrambi);

3 - Creare un file nella cartella Ast chiamato Vector per includere la nuova classe;
3.1 - aggiungere i vari override in modo similare a tutti i file contenuti nella cartella;
	(soprattutto la funzione toString per la creazione dell'AST);
3.2 - aggiungere implements exp per renderlo compatibile con il resto del programma;

4 - Aggiungere in "MyLangParser" la funzione parseVector che funzioni secondo la logica descritta dal professore.


Per il momento questo è quello che ho compreso su come aggiungere il tipo vector, mancano dei pezzi qui e là ma dovrebbe essere un inizio.
Ti auguro buona giornata!
o buona serata, non so quando leggerai questo file ahahahah



