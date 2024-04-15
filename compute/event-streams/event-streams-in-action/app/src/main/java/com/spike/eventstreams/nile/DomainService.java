package com.spike.eventstreams.nile;

import com.google.common.collect.ImmutableList;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DomainService {
    public static final List<String> COUNTRIES = ImmutableList.of("CN", "US");
    public static final List<String> CITIES = ImmutableList.of("Shanghai", "NY");

    public City city(InetAddress ipAddress) {
        int nextInt = ThreadLocalRandom.current().nextInt(COUNTRIES.size());
        City result = new City();
        result.country = COUNTRIES.get(nextInt);
        result.city = CITIES.get(nextInt);
        return result;
    }

    public static class City {
        public String country;
        public String city;
    }

}
