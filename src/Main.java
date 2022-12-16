import java.io.Console;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int usersAm = 4, filesAm = 6;
    static User[] users = new User[usersAm];
    static FileObject[] fileObjects = new FileObject[filesAm];

    public static void main(String[] args) {

        ArrayList<Rights>[][] accessMatrix;

        //Generating users
        {
            users[0] = new User("Dima", true);
            users[1] = new User("Misha", false);
            users[2] = new User("Vanya", false);
            users[3] = new User("Lena", false);
        }

        //Generating files
        {
            fileObjects[0] = new FileObject("Book1.txt");
            fileObjects[1] = new FileObject("Song1.mp3");
            fileObjects[2] = new FileObject("VeryValuableFile");
            fileObjects[3] = new FileObject("Film1.mkv");
            fileObjects[4] = new FileObject("Book2.txt");
            fileObjects[5] = new FileObject("CD/DVD");
        }

        accessMatrix = generateAccessMatrix();

        SystemEmulator emulator = new SystemEmulator(users, fileObjects, accessMatrix);

        emulator.launch();

    }

    static ArrayList<Rights>[][] generateAccessMatrix(){
        ArrayList<Rights>[][] matrix = new ArrayList[usersAm][filesAm];

        for (int i = 0; i < usersAm; i++) {
            for (int j = 0; j < filesAm; j++) {
                matrix[i][j] = getRights(users[i].getAdmin());
            }
        }

        return matrix;
    }

    static ArrayList<Rights> getRights(boolean admin){
        ArrayList<Rights> rightsList = new ArrayList<>();

        if (admin) {
            rightsList.add(Rights.READ);
            rightsList.add(Rights.WRITE);
            rightsList.add(Rights.GRANT);
        }
        else  {
            Random rand = new Random();
            if (rand.nextBoolean()) rightsList.add(Rights.READ);
            if (rand.nextBoolean()) rightsList.add(Rights.WRITE);
            if (rand.nextBoolean()) rightsList.add(Rights.GRANT);
        }

        return rightsList;
    }



}