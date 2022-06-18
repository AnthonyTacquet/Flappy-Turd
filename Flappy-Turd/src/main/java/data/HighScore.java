package data;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HighScore {
    public static int read() throws FileNotFoundException {
        int highest = 0;
        FileReader fileReader = new FileReader("src/main/resources/Files/scores.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Stream<String> lines = bufferedReader.lines();

        List<String> list = lines.collect(Collectors.toList());
        ArrayList<String> arrayList = new ArrayList<String>(list);

        for (int i = 0; i < arrayList.size(); i++){
            if (Integer.parseInt(arrayList.get(i)) > highest){
                highest = Integer.parseInt(arrayList.get(i));
            }
        }
        return highest;
    }

    public static ArrayList<String> readAll() throws FileNotFoundException {
        FileReader fileReader = new FileReader("src/main/resources/Files/scores.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Stream<String> lines = bufferedReader.lines();

        List<String> list = lines.collect(Collectors.toList());
        ArrayList<String> arrayList = new ArrayList<String>(list);

        return arrayList;
    }

    public static void write(int score) throws IOException {
        ArrayList<String> arrayList = readAll();
        arrayList.add(Integer.toString(score));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Files/scores.txt"));
        String text = "";
        for (int i = 0; i < arrayList.size(); i++){
            text += arrayList.get(i) + "\n";
        }
        writer.write(text);
        writer.close();
    }
}
