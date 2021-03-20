/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.co.seti.sandbox.web.app2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author andreslavado
 */
@Singleton
@Startup
public class CarDBInit {
    @PostConstruct
    public void initialize() {
        System.out.println("com.co.seti.sandbox.web.app2.CarDBInit.initialize()");
        Connection connection;
        try {
            System.out.println("Searching for Car table");
            connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement("SHOW TABLES LIKE 'Car'");
            ResultSet result = statement.executeQuery();
            if (!result.next()){
                System.out.println("Car table don't found.");
                result.close();
                statement.close();
                connection.close();
                connection = ConnectionFactory.getConnection();
                connection.setAutoCommit(false);
                System.out.println("Create Car table");
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS Car(\n" +
                "	car_id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "	cname VARCHAR(60) NOT NULL,\n" +
                "	color VARCHAR(60),\n" +
                "	speed INTEGER,\n" +
                "	Manufactured_Country VARCHAR(100),\n" +
                "	PRIMARY KEY(car_id))";
                Statement statementCreate = connection.createStatement();
                statementCreate.executeUpdate(sqlCreateTable);
                connection.commit();
                statementCreate.close();
                System.out.println("Insert Car data");
                Statement insertStatement = connection.createStatement();
                insertStatement.addBatch("INSERT INTO Car VALUES(1,'TC','Andres',10000000,'BTG')");
                insertStatement.addBatch("INSERT INTO Car VALUES(2,'Debito','Juan',20000000,'BTG')");
                insertStatement.addBatch("INSERT INTO Car VALUES(3,'Cta Corriente','Camilo',3000000,'BTG')");
                insertStatement.addBatch("INSERT INTO Car VALUES(4,'Prestamo LI','Laura',4000000,'BTG')");
                insertStatement.addBatch("INSERT INTO Car VALUES(5,'Prestamo Hipotecario','Sandra',50000000,'BTG')");
                insertStatement.addBatch("INSERT INTO Car VALUES(6,'Fiduciaria','Alejandra',60000000,'BTG')");
                insertStatement.addBatch("INSERT INTO Car VALUES(7,'Qualis','Jhony',7000000,'BTG')");
                int [] updateCounts = insertStatement.executeBatch();
                connection.commit();
                insertStatement.close();
                System.out.println("Create and insert end");
            } else {
                result.close();
                statement.close();
            }
            connection.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CarDBInit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CarDBInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
