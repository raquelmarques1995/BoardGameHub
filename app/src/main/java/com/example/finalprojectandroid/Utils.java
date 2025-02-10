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

    public static String readUserID(Context context) {
        StringBuilder conteudo = new StringBuilder();
        try {
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
        return conteudo.toString();
    }

    // Static method to format the date
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

}
