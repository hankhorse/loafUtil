package org.net.Class;


import org.net.Util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class FileReader {

     static List<String> numberList = new ArrayList<>();

    public static void main(String[] args) {
        String filePath = "C:\\Users\\YLXT01\\Desktop\\2.txt";
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(StringUtils.isPhone(line)){
                    numberList.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Random random = new Random();

        System.out.println(numberList.get(random.nextInt(numberList.size())));

    }
}
