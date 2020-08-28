package com.vietle.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody String requestQuery) {
        try {
            byte[] encodedString = Base64.getDecoder().decode(requestQuery);
            String original = new String(encodedString);
            this.jdbcTemplate.execute(original);
            ResponseEntity entity = new ResponseEntity(original , HttpStatus.OK);
            return entity;
        } catch (DataAccessException dae) {
            System.out.println(dae.getMessage());
        }
        return null;
    }

    @GetMapping("/sql")
    public ResponseEntity<String> sql() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        this.runSQL();
        return responseEntity;
    }

    public void runSQL() {
        try {
            String query = "INSERT INTO tasks(title, des, created) values('java code', 'from java code', now());";
            String createTableQuery = "CREATE TABLE IF NOT EXISTS todo (\n" +
                    "    TODO_ID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    TITLE VARCHAR(255) NOT NULL,\n" +
                    "    DES TEXT,\n" +
                    "    CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                    ")  ENGINE=INNODB;";

            this.jdbcTemplate.execute(createTableQuery);
        } catch (DataAccessException dae) {
            System.out.println(dae.getMessage());
        }

    }
}
