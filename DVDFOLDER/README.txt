For at k�re programmet, skal f�lgende g�re;
1. Eventuelle k�rende MySQL servere stoppes.

2. Den medf�lgende MySQL server udpakkes (_SERVER.rar)

3. Den udpakkede mappe _SERVER �bnes

4. Scriptet MySQL.bat startes

5. Der ventes til der skrives;
    130531 11:49:11 [Note] mysql\bin\mysqld: ready for connections.
    Version: '5.5.9'  socket: ''  port: 3306  MySQL Community Server (GPL)
5a. Hvis dette ikke kommer, er der et problem med at starte MySQL serveren

6. Exproxy certifikatet loades (dobbelt klik p� cert.cer starter wizard).
    Dette kan ignores, hvilket dog resultere i certifikat fejl.

7. Default Browseren ops�ttes til at bruge proxy, ved at ops�tte https proxy til port 8001.

8. Programmet startes, ved dobbelt klik p� sso.jar

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

For at teste programmet, skal f�lgende g�res;

1. Der logges ind p� programmet, med brugeren 'Skeen' og tomt kodeord.
    Login afsluttes med 'Forbind' knappen.

1a. Hvis der opst�r problemer, kommer en fejlmeddelelse op.

2. Status vinduet �bnes, og status for forskellige services opdatere.

3. 'Tilf�j' knappen, kan benyttes til at tilf�je flere services.
   'Fjern' knappen, kan benyttes til at fjerne den selectede services.
   'Rediger' knappen, kan benyttes til at redigere login oplysninger for den selectede services.

4. Dobbelt-klik venstre-click p� ElevPlan logo'et, �bner siden;
        "https://www.elevplan.dk/SSO_AUTO_LOGIN", der resultere i et automatisk
        login til elevplan.dk p� IT lederens bruger.

5. Hvis der logges ud, ses en 'SSO Login' knap, som kan lave automatisk login.
