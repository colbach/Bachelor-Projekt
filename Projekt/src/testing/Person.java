package testing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Person {
    
    public static void main(String[] args) {
        
        Person liubov = new Person("Muzyka", "Liubov", "W", "06.03.1996");
        Person christian = new Person("Colbach", "Christan", "M", "27.AAA.AAAAA");
        
        Person oldest1 = getOldest1(liubov, christian);
        System.out.println(oldest1);
        
        Person oldest2 = liubov.getOldest2(christian);
        System.out.println(oldest2);
        
        verheirate(liubov, christian);
        System.out.println(liubov);
        System.out.println(christian);
         
        
    }
    
    
    
    
    String name, vorname, geschlecht;
    Date date;
    boolean familienstand;

    public Person(String name, String vorname, String geschlecht, String date) {
        this.name = name;
        this.vorname = vorname;
        this.geschlecht = geschlecht;
        
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException ex) {
            System.err.println("Fehler");
            System.exit(1);
        }
        
        
        this.familienstand = false;
    }
    
    public static Person getOldest1(Person p1, Person p2) {
        if(p1.date.getTime() < p2.date.getTime()) {
            return p1;
        } else {
            return p2;
        }
    }
    
    public Person getOldest2(Person p2) {
        if(this.date.getTime() < p2.date.getTime()) {
            return this;
        } else {
            return p2;
        }
    }
    
    public static void verheirate(Person p1, Person p2) {
        
        Person alt = getOldest1(p1, p2);
        if(alt == p1) {
            p2.name = p1.name;
        } else {
            p1.name = p2.name;
        }
        p1.familienstand = true;
        p2.familienstand = true;
    }

    @Override
    public String toString() {
        return "Person{" + "name=" + name + ", vorname=" + vorname + ", geschlecht=" + geschlecht + ", date=" + date + ", familienstand=" + familienstand + '}';
    }
    
}
