import java.util.ArrayList;
import java.util.Scanner;


public class SystemEmulator {
    private User[] usersList;
    private FileObject[] filesList;
    private ArrayList<Rights>[][] matrix;
    private static boolean authorized = false;
    private static User currentUser;

    SystemEmulator(User[] usersList, FileObject[] filesList, ArrayList<Rights>[][] matrix) {
        this.usersList = usersList;
        this.filesList = filesList;
        this.matrix = matrix;
    }


    public void launch() {
        printAccessMatrix();
        authorize();

        if (authorized) {
            int id = indexOf(currentUser);
            printRights(id);

            while (true) {

                Scanner scanner = new Scanner(System.in);
                System.out.print("Введите команду> ");
                String action = scanner.nextLine();

                if (action.equalsIgnoreCase(Rights.GRANT.toString())) performGrant(id);
                else if (action.equalsIgnoreCase(Rights.WRITE.toString())) performWrite(id);
                else if (action.equalsIgnoreCase(Rights.READ.toString())) performRead(id);
                else if (action.equals("exit")) System.exit(1);
                else System.out.println("Неверная команда");

            }

        }


    }

    private void performRead(int userId) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Выберите id файла> ");
        int fileId = scan.nextInt();

        if (fileId >= 0 && fileId < filesList.length) {
            if (matrix[userId][fileId].contains(Rights.READ)) System.out.println("Успешно выполенено чтение файла" + filesList[fileId]);
            else System.out.println("Нет доступа.");
        }
        else System.out.println("Файл не найден.");
    }

    private void performWrite(int userId) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Выберите id файла> ");
        int fileId = scan.nextInt();

        if (fileId >= 0 && fileId < filesList.length) {
            if (matrix[userId][fileId].contains(Rights.WRITE)) System.out.println("Успешно выполнена запись файла" + filesList[fileId]);
            else System.out.println("Нет доступа.");
        }
        else System.out.println("Файл не найден.");
    }

    private void performGrant(int userId) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Выберите id файла> ");
        int fileId = scan.nextInt();

        if (fileId >= 0 && fileId < filesList.length) {
            if (matrix[userId][fileId].contains(Rights.GRANT)) {
                System.out.print("Введите id пользователя> ");
                int userReceiverId = scan.nextInt();

                if (userReceiverId >= 0 && userReceiverId < usersList.length) {
                    System.out.print("Введите права> ");
                    Scanner sc = new Scanner(System.in);
                    String newRights = sc.nextLine();

                    if (validRight(newRights)) {
                        if (!matrix[userReceiverId][fileId].contains(Rights.valueOf(newRights.toUpperCase()))) {
                            matrix[userReceiverId][fileId].add(Rights.valueOf(newRights.toUpperCase()));
                            System.out.println("Права успешно добавлены. Печатаю новую матрицу доступа: ");
                            printAccessMatrix();
                        }
                        else System.out.println("пользователь уже обладает данными правами.");
                    }
                    else System.out.println("Некорректные права.");

                }
                else System.out.println("Пользователя не существует.");

            }
            else System.out.println("Нет доступа.");
        }
        else System.out.println("Файл не найден.");
    }

    private void authorize() {

        Scanner scanner = new Scanner(System.in);
        String userName;
        boolean admin = false;

        System.out.print("\nВведите имя пользователя> ");
        userName = scanner.nextLine();

        boolean authIsCorrect = false;

        for (User user : usersList) {
            if (user.getName().equals(userName)) {
                authIsCorrect = true;
                currentUser = user;
                admin = user.getAdmin();
                authorized = true;
                break;
            }
        }

        if (authIsCorrect) {
            if (admin) System.out.println("Выполнен вход как: " + "\u001B[35m" + userName + " [admin]"+"\u001B[0m");
            else System.out.println("Выполнен вход как: " + userName);
        }
        else {
            System.out.println("Неверный логин");
            authorize();
        }
    }

    private void printRights(int ind) {
        System.out.println("Авторизация успешна. Перечень прав:");
        for (int i = 0; i < filesList.length; i++) {
            System.out.println(filesList[i].getName() + " - " + matrix[ind][i]);
        }
    }

    private int indexOf(User user) {
        for (int i = 0; i < usersList.length; i++) {
            if (usersList[i] == user) return i;
        }
        return -11;
    }


    private static boolean validRight(String test) {

        for (Rights c : Rights.values()) {
            if (c.name().equalsIgnoreCase(test)) {
                return true;
            }
        }

        return false;
    }

    private void printAccessMatrix(){

        System.out.printf("%-17s%-5s", "", "|");
        for (int i = 0; i < filesList.length; i++) {
            System.out.printf("%-30s", "[" + i + "] " + filesList[i].getName());
        }
        System.out.println();

        int length = 30 * filesList.length + 5;
        for (int i = 0; i < length; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < usersList.length; i++) {
            if (usersList[i].getAdmin()) {
                String out = "\u001B[35m" + "["+ i + "] " + usersList[i].getName()+" [admin]"+"\u001B[0m";
                System.out.printf("%-24s%-5s", out, " | ");
            }
            else {
                System.out.printf("%-16s%-5s",  "["+ i + "] " + usersList[i].getName(), " | ");
            }
            for (int j = 0; j < filesList.length; j++) {

                if (matrix[i][j].isEmpty()) System.out.printf("%-30s", "ЗАПРЕТ");
                else if (matrix[i][j].size() == 3) System.out.printf("%-30s","ПОЛНЫЙ ДОСТУП");
                else {
                    StringBuilder res = new StringBuilder();
                    matrix[i][j].forEach(r -> {
                        switch (r) {
                            case WRITE -> res.append("ЗАПИСЬ, ");
                            case READ -> res.append("ЧТЕНИЕ, ");
                            case GRANT -> res.append("ПЕРЕДАЧА, ");
                        }
                    });

                    res.setLength(res.length() - 2);
                    System.out.printf("%-30s", res);
                }
            }
            System.out.println();
        }
    }

}
