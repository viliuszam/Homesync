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

Toliau pateikiama įrankio OpenAPI dokumentacija (į markdown formatą konvertuota [šiuo įrankiu](https://swagger-markdown-ui.netlify.app/)).

# Homesync
REST API for the Homesync web application.

## Version: 1.0

### /api/rooms/{id}

#### GET
##### Summary:

Get a room by ID

##### Description:

Retrieves a room by its ID

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the room to retrieve | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of room |
| 404 | Room not found |

#### PUT
##### Summary:

Update a room

##### Description:

Updates an existing room

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the room to update | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Room updated successfully |
| 404 | Room not found |

#### DELETE
##### Summary:

Delete a room

##### Description:

Deletes an existing room

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the room to delete | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 204 | Room deleted successfully |
| 404 | Room not found |

### /api/homes/{id}

#### GET
##### Summary:

Get a home by ID

##### Description:

Retrieves a home by its ID

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the home to retrieve | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of home |
| 404 | Home not found |

#### PUT
##### Summary:

Update a home

##### Description:

Updates an existing home

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the home to update | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Home updated successfully |
| 404 | Home not found |

#### DELETE
##### Summary:

Delete a home

##### Description:

Deletes an existing home

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the home to delete | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 204 | Home deleted successfully |
| 404 | Home not found |

### /api/devices/{id}

#### GET
##### Summary:

Get a device by ID

##### Description:

Retrieves a device by its ID

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the device to retrieve | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of device |
| 404 | Device not found |

#### PUT
##### Summary:

Update a device

##### Description:

Updates an existing device

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the device to update | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Device updated successfully |
| 404 | Device not found |
| 422 | Invalid payload |

#### DELETE
##### Summary:

Delete a device

##### Description:

Deletes an existing device

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | ID of the device to delete | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 204 | Device deleted successfully |
| 404 | Device not found |

### /api/rooms

#### GET
##### Summary:

Get all rooms

##### Description:

Retrieves a list of all rooms

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of rooms |

#### POST
##### Summary:

Create a new room

##### Description:

Creates a new room in a specific home

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| homeId | query | ID of the home to add the room to | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Room created successfully |
| 422 | Error creating room |

### /api/homes

#### GET
##### Summary:

Get all homes

##### Description:

Retrieves a list of all homes

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of homes |

#### POST
##### Summary:

Create a new home

##### Description:

Creates a new home

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Home created successfully |
| 422 | Invalid payload |

### /api/devices

#### GET
##### Summary:

Get all devices

##### Description:

Retrieves a list of all devices

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of devices |

#### POST
##### Summary:

Create a new device

##### Description:

Creates a new device in a specific room

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| roomId | query | ID of the room to add the device to | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Device created successfully |
| 422 | Invalid payload |

### /api/rooms/{roomId}/devices

#### GET
##### Summary:

Get devices by room ID

##### Description:

Retrieves all devices for a specific room

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| roomId | path | ID of the room to retrieve devices for | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of devices |
| 404 | Room not found |

### /api/homes/{homeId}/rooms

#### GET
##### Summary:

Get rooms by home ID

##### Description:

Retrieves all rooms for a specific home

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| homeId | path | ID of the home to retrieve rooms for | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful retrieval of rooms |
| 404 | Home not found |

