# Salasanan vaihto sovellus
Tarkoituksena luoda yksinkertainen sovellus, jossa onnistuu seuraavat asiat:

- kirjautuminen
- rekisteröinti
- salasanan vaihto. 
## Tietokanta
- H2 (ajonaikainen)
- PostgreSql (ulkoinen)
## Pilvipalvelu
- Heroku
# Toiminta ilman emailia
- Käyttäjä syöttää rekisteröinti tiedot
'Username', Password(x2)', 'Email'.
- -> Käyttäjän tiedot tallentuu tietokantaan - salasana hashattynä.
- Salasanan unohtuessa voidaan klikata "Forgot your password?" -linkkiä
- -> Käyttäjä asettaa rekisteröitymisessä käytetyn emailin, joka luo yhden tokenin (token voimassa X ajan).
- käyttäjä menee Endpointiin: `/reset-password?token=OMA TOKEN TÄHÄN`
- -> Avautuu `/reset-password`, jossa käyttäjä voi asettaa itselleen uuden salasanan.
- -> Kun salasana on asetettu - vanha poistetaan ja uusi tallentuu tietokantaan hashattynä.
- Token merkitään käytetyksi: `used`, mutta jätetään tietokantaan.
- `Tokeneita voi olla käytössä vain YKSI kerrallaan - vanha poistetaan tietokannasta ja uusi luodaan tilalle`