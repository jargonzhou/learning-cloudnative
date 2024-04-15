package com.spike.eventstreams.nile;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.spike.eventstreams.AppTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Paths;

public class MaxmindTest {

    // test data
    // https://github.com/maxmind/MaxMind-DB/tree/main/test-data
    // https://github.com/maxmind/MaxMind-DB/blob/main/source-data/GeoIP2-City-Test.json

    // need registration
    // https://dev.maxmind.com/geoip/geolite2-free-geolocation-data

    @Test
    public void testIP() throws IOException, GeoIp2Exception {
        CityResponse city;
        try (DatabaseReader databaseReader = new DatabaseReader
                .Builder(Paths.get(AppTest.BASE_DIR, "GeoIP2-City-Test.mmdb").toFile())
                .build()) {

            city = databaseReader.city(InetAddress.getByName("::175.16.199.0"));
        }
        Assertions.assertThat(city).isNotNull();
        Assertions.assertThat(city.getCity().getName()).isEqualTo("Changchun");
        Assertions.assertThat(city.getCountry().getName()).isEqualTo("China");
    }
}
