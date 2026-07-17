package com.claude.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response POJO for the {@code /users} resource. Nested address, geo, and
 * company details are modeled as static nested classes rather than separate
 * top-level files, since they only ever exist in the context of a
 * {@link User} and are never independently constructed or persisted.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class User {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "User{id=%s, name='%s', username='%s', email='%s'}".formatted(id, name, username, email);
    }

    /** Postal address associated with a {@link User}. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        public String getStreet() {
            return street;
        }

        public void setStreet(final String street) {
            this.street = street;
        }

        public String getSuite() {
            return suite;
        }

        public void setSuite(final String suite) {
            this.suite = suite;
        }

        public String getCity() {
            return city;
        }

        public void setCity(final String city) {
            this.city = city;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(final String zipcode) {
            this.zipcode = zipcode;
        }

        public Geo getGeo() {
            return geo;
        }

        public void setGeo(final Geo geo) {
            this.geo = geo;
        }
    }

    /** Geographic coordinates for an {@link Address}. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Geo {
        private String lat;
        private String lng;

        public String getLat() {
            return lat;
        }

        public void setLat(final String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(final String lng) {
            this.lng = lng;
        }
    }

    /** Employer/company details associated with a {@link User}. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Company {
        private String name;
        private String catchPhrase;
        private String bs;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getCatchPhrase() {
            return catchPhrase;
        }

        public void setCatchPhrase(final String catchPhrase) {
            this.catchPhrase = catchPhrase;
        }

        public String getBs() {
            return bs;
        }

        public void setBs(final String bs) {
            this.bs = bs;
        }
    }
}
