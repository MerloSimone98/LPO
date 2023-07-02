update_02/07

Ciao Patryk!
Nella cartella trovi tutto quello che abbiamo fatto ieri più alcune modifiche qua e là che ho fatto io in serata. Ci sono un paio d bug che devo ancora risolvere, nello specifico:
-1; la funzione print stampa correttamente le espressioni date ma, per motivi a me ancora ignari, stampa prima gli array e dopo altri valori a prescindere dell'ordine in cui si trovano nel file.txt ad esempio
	print [1;2];
	print 15;
	print [1;2]

stamperà [0;1] [0;1] 15 invece di [0;1] 15 [0;1].

-2; Tutto funziona da dio se si utilizzano vector singoli, somme e moltiplicazioni tra soli due vector oppure espressioni di interi, nel momento in cui si fanno cose più complesse tipo [1;2]+[1;2]+[1;2] esplode tutto :')
La ragione mi è chiara e nasce dal fatto che per come è scritto il codice che trovi nella cartella, l'espressione del vettore non per forza è un vettore ma potrebbe essere una somma o prodotto di vettori es exp = [1;2]+[1;2].
Sto cercando di risolvere la cosa ma non ho ancora trovato una soluzione.

A parte queste due cose, sembra funzionare tutto correttamente, risolto ciò rimane giusto il typecheck dinamico e abbiamo finito con i vettori. Tra l'altro devo vedere nel dettaglio che controllo deve saltare il programma se gli viene passato l'argomento -ntc, se quello statico, quello dinamico o entrambi ahaha

Ti direi, se hai tempo col lavoro e tutto, che tu potresti cominciare a vedere come implementare il foreach mentre io finisco con i vettori. Se ti va, ho già incluso il token FOREACH nel programma che quindi viene letto ma potrebbe essere necessario includere un token per "in" della relativa sintassi. A parte questo i file relativi al foreach dovrebbero essere quelli della cartella AST (in quanto il primo step di qualsiasi aggiunta o modifica a sta roba deve passare per l'albero astratto di sintassi) seguiti poi dai file della cartella environment(in quanto il foreach genera un sub-environment contenente il ciclo for associato)

Detto questo, buona serata!

