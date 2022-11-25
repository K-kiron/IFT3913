package ua.karatnyk.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.karatnyk.impl.CurrencyConversion;
import ua.karatnyk.impl.ExpensesProgram;
import ua.karatnyk.impl.CurrencyConvertor;

import java.text.ParseException;

import static org.junit.Assert.*;


public class CurrencyConvertorTest extends Object {
    private OfflineJsonWorker manager;
    private CurrencyConversion conversion;

    public CurrencyConvertorTest(){
        manager = new OfflineJsonWorker();
        conversion = manager.parser();
    }

    //Blackbox Test
    //Test null input in parameter from
    @Test(expected = java.text.ParseException.class)
    public void testConvert() throws ParseException {
        double amount=100;
        String from=null;
        String to="CAD";
        CurrencyConvertor.convert(amount,from,to, conversion);
    }
    //Test null input in parameter to
    @Test(expected = java.text.ParseException.class)
    public void testConvert2() throws ParseException {
        double amount=100;
        String from="CAD";
        String to=null;
        CurrencyConvertor.convert(amount,from,to, conversion);
    }

    //Test null input in parameter conversion
    @Test(expected = NullPointerException.class)
    public void testConvert3() throws ParseException {
        double amount=100;
        String from="CAD";
        String to="EUR";
        CurrencyConvertor.convert(amount,from,to, null);
    }

    //Test invalid input in parameter from
    @Test(expected = java.text.ParseException.class)
    public void testConvert4() throws ParseException {
        double amount=100;
        String from="";
        String to="CAD";
        CurrencyConvertor.convert(amount,from,to, conversion);
    }
    //Test invalid input in parameter to
    @Test(expected = java.text.ParseException.class)
    public void testConvert5() throws ParseException {
        double amount=100;
        String from="CAD";
        String to="";
        CurrencyConvertor.convert(amount,from,to, conversion);
    }

    //domain partition,nonvalide parameter from
    @Test
    public void testConvert6() throws ParseException {
        double amount=100;
        String from="FJD";
        String to="EUR";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output==0);
    }

    //domain partition,nonvalide parameter to
    @Test
    public void testConvert7() throws ParseException {
        double amount=100;
        String from="EUR";
        String to="FJD";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output==0);
    }


    //domain partition,amount<0
    @Test
    public void testConvert8() throws ParseException {
        double amount=-5;
        String from="USD";
        String to="USD";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output==0);
    }
    //domain partition,amount>10000
    @Test
    public void testConvert9() throws ParseException {
        double amount=20000;
        String from="EUR";
        String to="EUR";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output==0);
    }

    //domain partition,0<amount<10000
    @Test
    public void testConvert10() throws ParseException {
        double amount=100;
        String from="CAD";
        String to="CAD";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output==amount);
    }

    //Test boundary,amount=0
    @Test
    public void testConvert11() throws ParseException {
        double amount=0;
        String from="AUD";
        String to="GBP";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output==0);
    }

    //Test boundary,amount=10000
    @Test
    public void testConvert12() throws ParseException {
        double amount=10000;
        String from="CHF";
        String to="INR";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output>0);
    }

    //Test parameter valide and within boundary condition
    @Test
    public void testConvert13() throws ParseException {
        double amount=100;
        String from="AUD";
        String to="GBP";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output>0);
    }

    //White box test
    //Couverture des instructions
    @Test
    public void testConvert14() throws ParseException {
        double amount=100;
        String from="AUD";
        String to="CAD";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output>0);
    }
    @Test(expected = java.text.ParseException.class)
    public void testConvert15() throws ParseException {
        double amount=100;
        String from=null;
        String to="CAD";
        CurrencyConvertor.convert(amount,from,to, conversion);
    }

    //couverture des arcs du graphe de flot de controle
    @Test
    public void testConvert16() throws ParseException {
        double amount=100;
        String from="USD";
        String to="CAD";
        double output=CurrencyConvertor.convert(amount,from,to, conversion);
        String message = String.format("Convert %f %s to %s : %f",amount , from, to,output);
        assertTrue(message,output>0);
    }
    @Test(expected = java.text.ParseException.class)
    public void testConvert17() throws ParseException {
        double amount=100;
        String from=null;
        String to="CAD";
        CurrencyConvertor.convert(amount,from,to, conversion);
    }
    @Test(expected = java.text.ParseException.class)
    public void testConvert18() throws ParseException {
        double amount=100;
        String from="CAD";
        String to=null;
        CurrencyConvertor.convert(amount,from,to, conversion);
    }
    //couverture des chemins independants du graphe de flot de controle




}