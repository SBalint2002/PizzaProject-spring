# PizzaVáltó projekt
Ez a projekt egy pizza rendelő alkalmazás rendszerét valósítja meg.
A felhasználók regisztrálhatnak, bejelentkezhetnek, és rendelhetnek pizzát.
Az admin felhasználók felügyelhetik a rendeléseket az étlapot és a felhasználókat.

## Futtatás
___

A projekt a Java 17-ben íródott. A futtatáshoz szükséges:
- Fejleszői környezet: [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Keretrendszer: [Spring Boot](https://spring.io/)
- Adatbázis kezelő: [MariaDB](https://mariadb.org/)
- Csomagkezelő: [Maven](https://mvnrepository.com/artifact/org.springframework.boot)

### Telepítés

Ha nincs telepítve az alkalmazás, akkor a következő lépéseket kell végrehajtani:

- Klónozza le a projektet a Gitből a következő paranccsal:
>git clone https://github.com/SBalint2002/PizzaProject-spring.git
- Futtassa az alkalmazást az [_Application.java_](https://github.com/SBalint2002/PizzaProject-spring/blob/main/src/main/java/hu/pizzavalto/pizzaproject/Application.java) fájlból (hu.pizzavalto.pizzaproject).

### Teszt felhasználó
Az alábbi adatokkal lehet bejelentkezni a teszteléshez egy Adminisztrátor fiókkal:

>Email: tesztelek@gmail.com

>Jelszó: Adminadmin1

## Adatbázis
___
Az alkalmazás az adatokat egy [MariaDB](https://mariadb.org/) adatbázisban tárolja. Ha az adatbázis nem létezik, az alkalmazás automatikusan létrehozza és feltölti az alapértelmezett adatokkal.

## Funkciók
___
Az alkalmazás lehetővé teszi a felhasználók számára, hogy pizzát rendeljenek, valamint nyomon követhessék a rendelés állapotát. Az admin felhasználók kezelni tudják az étlapot és az összes rendelést.

Az alábbiakban a funkciók listája található:

- Regisztráció és bejelentkezés.
- Pizza keresése és rendelése.
- Rendelés állapotának nyomon követése.
- Étlap kezelése.
- Rendelések kezelése.