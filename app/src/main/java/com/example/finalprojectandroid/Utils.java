package com.example.finalprojectandroid;

import android.widget.Toast;
import android.content.Context;

import com.example.finalprojectandroid.ui.matches.MyMatchesFragment;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private final static String FILE_NAME = "userInSession.txt";

    // Make the method static so it can be called without creating an instance of Utils
    public static String readUserID(Context context) {
        StringBuilder conteudo = new StringBuilder();
        try {
            // Use the context to access the openFileInput method
            FileInputStream fis = context.openFileInput(FILE_NAME);
            int c;

            while ((c = fis.read()) != -1) {
                conteudo.append((char) c);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao ler o ficheiro!", Toast.LENGTH_SHORT).show();
        }
        return conteudo.toString();  // Return the content as a string
    }

    // Metodo para converter Date em String
    // Método estático para formatar a data
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

}
