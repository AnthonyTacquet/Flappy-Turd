package data;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.antwaan.flappyturd.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadWrite {
    /*public static int read() throws Exception {
        URL url = ReadWrite.class.getResource("raw/scores.txt");
        File file = new File(url.toURI());
        if (!file.exists())
            file.createNewFile();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()){
            System.out.println(scanner.nextInt());
        }
        return 0;
    }*/

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int read(Context context){
        InputStream ins = context.getResources().openRawResource(
                context.getResources().getIdentifier("scores",
                        "raw", context.getPackageName()));
        Scanner scanner = new Scanner(ins);
        while (scanner.hasNext()){
            System.out.println(scanner.nextLine());
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<String> readAll() throws FileNotFoundException {
        FileReader fileReader = new FileReader("src/main/res/raw/scores.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Stream<String> lines = bufferedReader.lines();

        List<String> list = lines.collect(Collectors.toList());
        ArrayList<String> arrayList = new ArrayList<String>(list);

        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void write(int score) throws IOException {
        ArrayList<String> arrayList = readAll();
        arrayList.add(Integer.toString(score));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/res/raw/scores.txt"));
        String text = "";
        for (int i = 0; i < arrayList.size(); i++){
            text += arrayList.get(i) + "\n";
        }
        writer.write(text);
        writer.close();
    }
}
