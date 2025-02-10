package com.example.finalprojectandroid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;
import java.io.Serializable;

public class BoardGameName implements Serializable {

    @Attribute(name = "primary", required = false)
    private String primary;  // Can be null if not present

    @Attribute(name = "sortindex", required = false)
    private int sortIndex;  // Can be null if not present

    @Text
    private String name;

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return primary != null && "true".equalsIgnoreCase(primary); // Avoid null errors
    }

    public int getSortIndex() {
        return sortIndex;
    }
}
