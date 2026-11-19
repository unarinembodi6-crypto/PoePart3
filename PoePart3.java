/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepart3;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class PoePart3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
     Login register = new Login();
        Scanner scan = new Scanner(System.in);
        Message msg = new Message();

        System.out.println("\n===== Register =====");

        System.out.print("Please enter the username: ");
        String name = scan.nextLine();

        System.out.print("Enter password: ");
        String password = scan.nextLine();

        System.out.print("Enter cellphone: ");
        String cellphone = scan.nextLine();

        System.out.println(register.registerUser(name, password, cellphone));

        if (!register.registerStatus(name, password, cellphone)) {
            return;
        }

        System.out.println("\n===== Login =====");

        System.out.print("Enter username: ");
        String loginUsername = scan.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = scan.nextLine();

        boolean status = register.loginUser(loginUsername, loginPassword);
        System.out.println(register.returnLoginStatus(status));

        if (status) {
            msg.setCurrentSender(loginUsername);
            int option;

            do {
                System.out.println("\n===== Select any of the option below =====");
                System.out.println("1. Send message");
                System.out.println("2. Show sent messages report");
                System.out.println("3. Exit");
                System.out.println("4. Stored Messages");
                System.out.print("Enter your choice: ");

                while (!scan.hasNextInt()) {
                    System.out.print("Please enter a valid number (1-4): ");
                    scan.nextLine();
                }
                option = scan.nextInt();
                scan.nextLine();

                switch (option) {
                    case 1 -> msg.sendingMessage();
                    case 2 -> System.out.println(msg.displaySentMessagesReport());
                    case 3 -> System.out.println("Good bye!!");
                    case 4 -> msg.storedMessagesMenu();
                    default -> System.out.println("Invalid option!!");
                }

            } while (option != 3);
        }
    }
}   
        
    
    

