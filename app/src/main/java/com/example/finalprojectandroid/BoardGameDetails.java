package com.example.finalprojectandroid;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import com.example.finalprojectandroid.BoardGameName;

@Root(name = "boardgame", strict = false)
public class BoardGameDetails implements Serializable {

    @Element(name = "yearpublished", required = false)
    private int yearPublished;

    @Element(name = "minplayers", required = false)
    private int minPlayers;

    @Element(name = "maxplayers", required = false)
    private int maxPlayers;

    @Element(name = "playingtime", required = false)
    private int playingTime;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "image", required = false)
    private String imageUrl;

    @Element(name = "age", required = false)
    private int age;


    @ElementList(name = "boardgamecategory", entry = "boardgamecategory", inline = true, required = false)
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public String getCategory() {
        return (categories != null && !categories.isEmpty()) ? categories.get(0) : "N/A";
    }

    @ElementList(name = "name", entry = "name", inline = true, required = false)
    private List<BoardGameName> names;


//    public List<String> getNames() {
//        return names;
//    }

    public String getName() {
        if (names != null && !names.isEmpty()) {
            // First, try to find the primary name
            for (BoardGameName nameObj : names) {
                if (nameObj.isPrimary()) {
                    return nameObj.getName();
                }
            }
            // If no primary name is found, return the first available name
            return names.get(0).getName();
        }
        return "N/A"; // Default if the list is empty or null
    }


    // Getters
    public int getYearPublished() { return yearPublished; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public int getPlayingTime() { return playingTime; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getAge() { return age; }

}
    //Opção caso queiramos ir buscar número 'optimimizado' de jogadores

    //Depois no BoardGameAdapter fariamos a chamada dentro do String message desta maneira
    // "\nMelhor com: " + (game.getDetails().getBestWithNumbers() != null && !game.getDetails().getBestWithNumbers().isEmpty() ? game.getDetails().getBestWithNumbers() + " jogadores" : "Informação não disponível") +

    //
//    @Path("poll-summary[@name='suggested_numplayers']")
//    @Element(name = "result", required = false)
//    private String bestWith;
//
//    // Getter for bestWith
//    public String getBestWith() {
//        return bestWith;
//    }
//
//    // New method to extract numbers from the "Best with" string
//    public String getBestWithNumbers() {
//        if (bestWith != null && !bestWith.isEmpty()) {
//            // Regular expression to match numbers between "Best with" and "players"
//            Pattern pattern = Pattern.compile("\\d+");
//            Matcher matcher = pattern.matcher(bestWith);
//
//            StringBuilder numbers = new StringBuilder();
//            while (matcher.find()) {
//                // Append each found number (with a comma separating them)
//                if (numbers.length() > 0) {
//                    numbers.append(", ");
//                }
//                numbers.append(matcher.group());
//            }
//
//            // Return the numbers as a string, or a default message if no numbers are found
//            return numbers.length() > 0 ? numbers.toString() : "Informação não disponível";
//        }
//        return "Informação não disponível";
//    }

