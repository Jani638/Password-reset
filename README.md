# Salasanan vaihto sovellus
Tarkoituksena luoda yksinkertainen sovellus, jossa onnistuu seuraavat asiat:

- Rekisteröinti `username` , `password` , `email`
- Kirjautuminen `username` , `password`
- Salasanan vaihto `email`
- `ADMIN` ja `USER` -roolit
- `ADMIN` → Näkee käyttäjien `Id`, `username`, `email`, sekä pystyy muokkaamaan `username`, `email` ja `role`
- `USER` → Pääsy kotisivulle, mutta ei näe käyttäjiä, eikä voi tehdä muutoksia.
## Tietokanta
- H2 (ajonaikainen)
- PostgreSql (ulkoinen)
## Pilvipalvelu
- Heroku
# Toiminta lyhyesti
- Käyttäjä syöttää rekisteröinti tiedot
`Username`, `Password(x2)`, `Email`.
 → Käyttäjän tiedot tallentuu tietokantaan - salasana hashattynä.

- Salasanan unohtuessa klikataan `"Forgot your password?"` -linkkiä
 → Käyttäjä asettaa rekisteröitymisessä käytetyn emailin → lähetetään reset linkki annettuun sähköpostiin, joka sisältää tokenin.

- Käyttäjä menee Endpointiin: `/reset-password?token=OMA TOKEN TÄHÄN`
 → Avautuu `/reset-password`, jossa käyttäjä voi asettaa itselleen uuden salasanan.

- Salasana on asetettu → vanha poistetaan ja uusi tallentuu tietokantaan hashattynä.

- Token merkitään käytetyksi: `used`, mutta jätetään tietokantaan.

## URL: [Password reset-app](https://password-reset-app-a15eeefe2c4e.herokuapp.com/)

## Huomioitavaa:
- Tokeneita voi olla käytössä vain `YKSI` kerrallaan!

- Mikäli pyyntöjä tekee useamman kui yhden → vanha poistetaan aina tietokannasta ja uusi luodaan tilalle.

- Sähköpostin lähetykseen käytettävä sähköposti on tehty sovellusta varten, eikä se ei ole normaalissa käytössä.
# 
![](pics/cyber.png)