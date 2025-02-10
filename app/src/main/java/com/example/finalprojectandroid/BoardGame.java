package com.example.finalprojectandroid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class BoardGame {

    @Attribute(name = "id")
    private int id;

    @Attribute(name = "type", required = false)
    private String type;

    @Element(name = "name", required = false)
    private Name name;

    @Element(name = "yearpublished", required = false)
    private YearPublished yearPublished;

    // New field to hold the BoardGameDetails
    private BoardGameDetails details;

    // Getter and Setter for BoardGameDetails
    public BoardGameDetails getDetails() {
        return details;
    }

    public void setDetails(BoardGameDetails details) {
        this.details = details;
    }

    // Existing getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name != null ? name.getValue() : "Unknown";
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getYearPublished() {
        return yearPublished != null ? yearPublished.getValue() : "N/A";
    }

    public void setYearPublished(YearPublished yearPublished) {
        this.yearPublished = yearPublished;
    }
}

// Class for name element
@Root(name = "name", strict = false)
class Name {
    @Attribute(name = "value")
    private String value;

    public String getValue() {
        return value;
    }
}

// Class for year published element
@Root(name = "yearpublished", strict = false)
class YearPublished {
    @Attribute(name = "value")
    private String value;

    public String getValue() {
        return value;
    }
}
