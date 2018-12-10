package com.infoshareacademy.jbusters.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.spi.FileTypeDetector;
import java.util.*;

public class DistrWagesHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);
    private Properties properties;

    public DistrWagesHandler(String file) {
        this.properties = new Properties();
        loadProperties(file);
    }

    private void loadProperties(String file) {
        try {
            Path path = Paths.get(file);
            if(!file.contains(".properties")){
                LOGGER.error("Error - wrong filetype, .properties expected");
                throw new IOException();
            }
            this.properties.load(new FileReader(file));
            LOGGER.info("district property file loaded successfully");
        } catch (IOException e) {
            System.out.println("Plik zawierający parametry potrzebne do porównywania dzielnic nie istnieje.");
            LOGGER.error("Error loading file: {}");
        } catch (NullPointerException e){
            LOGGER.error("Error loading file: null input");
        }
    }

    public Map<String, Integer> getDistrictWages() {
        return new HashMap<String, Integer>((Map) properties);
    }

}