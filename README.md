## Projekto pavadinimas
Namų prietaisų valdymo sistema - Homesync.
## Sprendžiamo uždavinio aprašymas
### Sistemos paskirtis

Kuriamos sistemos tikslas -  suteikti naudotojams intuityvų ir suvienytą būdą valdyti įvairius prietaisus namų aplinkoje. Sistema leis naudotojams tvarkyti prietaisus pagal kambarius ir valdyti jų veikimą nuotoliniu būdu, užtikrinant patogumą ir energijos vartojimo efektyvumą.


### Funkciniai reikalavimai

 1. Objektų valdymas

    Sistema turi apimti objektų hierarchiją: namai -> kambarys -> įrenginys. Kiekviename name gali būti keli kambariai, o kiekviename kambaryje gali būti keli prietaisai.

 2. CRUD operacijos

    Sistema turi suteikti kiekvieno objekto (namo, kambario, įrenginio) kūrimo, peržiūros, atnaujinimo ir šalinimo (CRUD) galimybes, naudotojai galės visiškai valdyti savo namų prietaisų visumą.
    Iš viso turi būti įgyvendinta 15 skirtingų API metodų, įskaitant tokius metodus, kaip namų sukūrimas, kambarių pridėjimas prie namų ir prietaisų registravimas kambaryje, bei bendra bet kurio objekto sąrašo peržiūra. Namus sistemoje sukūręs naudotojas gali suteikti kitiems sistemos nariams prieigą prie namų redagavimo.

 3. Profilio redagavimas
 Naudotojai turi galimybę redaguoti savo asmeninius duomenis. 

 4. Naudotojo rolės

    Sistema palaikys tris naudotojų roles: narys, savininkas ir administratorius, kurių kiekvienas turi skirtingus prieigos ir leidimų lygius.
**Svečias** – žemiausias prieigos lygis, gali susipažinti su tinklapio funkcionalumu ir susikurti paskyrą ar prisijungti.
**Narys** – pagrindinis prieigos lygis, gali atlikti visas anksčiau minėtas CRUD operacijas. 
**Administratorius** – aukščiausias prieigos lygis, galima atlikti bet kuriam sistemos nariui priklausančiam objektui CRUD operacijas. Taip pat galimas narių paskyrų blokavimas.

 5. Prietaisų būklės valdymas
Prietaisų įjungimas ar išjungimas naudotojo sąsajoje. Taip pat turi būti galimas automatinis prietaisų būklės pakeitimas praėjus numatytam laikui (pasinaudojant CRON jobs). 

 6. Autorizacija
Sistemoje autorizavimas bus vykdomas naudojant JWT (JSON Web Tokens), taip užtikrinant saugią prieigą prie API.
Galima naudotojų registracija, prisijungimas. Administratoriai turi galimybę blokuoti paskyras.

 7. Naudotojo sąsaja
    Bus sukurta paprasta web sąsaja, kad naudotojai galėtų bendrauti su sistema, peržiūrėti savo namų nustatymus ir valdyti įrenginius.
    
### Pasirinktų technologijų aprašymas
Sistemos API bus sukurtas naudojantis Springboot. Springboot – atviro kodo Java karkasas naudojamas RESTful web servizams kurti. Naudotojų duomenims saugoti bus naudojama reliacinė duomenų bazė PostgreSQL. Sistemos naudotojo sąsaja bus įgyvendinta su ReactJS. Šias technologijas pasirinkau, kadangi jau esu su jomis susipažinęs, bei manau, jog jos suteikia visus reikalingus įrankius šiai sistemai įgyvendinti. 
