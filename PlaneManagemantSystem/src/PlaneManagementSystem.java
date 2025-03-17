import java.util.Scanner;

public class  PlaneManagementSystem {
    //number of rows and seats per row in different sections of the plane
    private static final int numberOfRows = 4;
    private static final int seats_per_row_in_A_D = 14;
    private static final int seats_per_row_in_B_C= 12;

    //represents the seating structure of the plane
    private static final char[][] seatingStructure = {
            new char[seats_per_row_in_A_D],
            new char[seats_per_row_in_B_C],
            new char[seats_per_row_in_B_C],
            new char[seats_per_row_in_A_D]
    };

    //array to hold tickets
    private static final Ticket[] tickets = new Ticket[52];
    //counter for the number of tickets
    private static int ticketCount = 0;

    //displays the menu options and handles the user selections.
    public static void main(String[] args) {
        System.out.println("Welcome to the Plane Management application");

        Scanner option = new Scanner(System.in);
        int menuSelection;

        while (true) {
            System.out.println("*************************************************");
            System.out.println("*                 MENU OPTIONS                  *");
            System.out.println("*************************************************");
            System.out.println("1) Buy a seat");
            System.out.println("2) Cancel a seat");
            System.out.println("3) Find first available seat");
            System.out.println("4) Show seating plan");
            System.out.println("5) Print tickets information and total sales");
            System.out.println("6) Search ticket");
            System.out.println("0) Quit");
            System.out.println("*************************************************");

            System.out.print("Please select an option: ");
            menuSelection = option.nextInt();
            option.nextLine();

            if (menuSelection == 0) {
                break;
            } else if (menuSelection == 1) {
                buy_seat(option);
            } else if (menuSelection == 2) {
                cancel_seat(option);
            } else if (menuSelection == 3) {
                find_first_available_seat();
            } else if (menuSelection == 4) {
                show_seating_plan();
            } else if (menuSelection == 5) {
                print_tickets_info();
            } else if (menuSelection == 6) {
                search_ticket(option);
            } else {
                System.out.println("Invalid Option. Please select again");
            }
        }
    }

    // get the user inputs.calculates the seat price, creates a ticket object, and marks the seat as booked.
    // and call the correct method created in below.
    private static void buy_seat(Scanner option) {
        String rowLetter = getValidRow(option);
        int seatNumber = getValidSeat(option, rowLetter);
        String name = getInput("Please enter your name: ", option);
        String surName = getInput("Please enter your surname: ", option);
        String email = getInput("Please enter your email: ", option);

        Person person = new Person(name, surName, email);
        double seatPrice = calculateSeatPrice(rowLetter, seatNumber);
        Ticket ticket = new Ticket(rowLetter, seatNumber, seatPrice, person);
        tickets[ticketCount] = ticket;
        Ticket.saveTicket(ticket);
        ticketCount++;

        markSeatAsBooked(rowLetter, seatNumber);

        System.out.println("Seat booked successfully!");
    }

    // cancel the seat and remove it from the array
    private static void cancel_seat(Scanner option) {
        String rowLetter = getValidRow(option);
        int seatNumber = getValidSeat(option, rowLetter);

        int rowIndex = rowLetter.charAt(0) - 'A';

        if (seatingStructure[rowIndex ][seatNumber - 1] == 'x') {
            seatingStructure[rowIndex ][seatNumber - 1] = 'O';

            for (int i = 0; i < ticketCount; i++) {
                Ticket ticket = tickets[i];
                if (ticket.getRow().equals(rowLetter) && ticket.getSeat() == seatNumber) {
                    for (int j = i; j < ticketCount - 1; j++) {
                        tickets[j] = tickets[j + 1];
                    }
                    tickets[ticketCount - 1] = null;
                    ticketCount--;
                    break;
                }
            }
            System.out.println("Seat cancelled successfully!");
        } else {
            System.out.println("Seat is not booked. Cannot cancel.");
        }
    }

    /*check if the user input a valid row letter .
    if not ask user to enter a valid row letter.*/
    private static String getValidRow(Scanner option) {
        String rowLetter;
        do {
            System.out.print("Please enter row letter (A to D): ");
            rowLetter = option.nextLine();
            if (!isValidRow(rowLetter)) {
                System.out.println("Invalid row. Please enter a valid row letter.");
            }
        } while (!isValidRow(rowLetter));
        return rowLetter;
    }

    /*check if the user input a valid seat number .
    if not ask user to enter a valid seat number.*/
    private static int getValidSeat(Scanner option, String rowLetter) {
        int seatNumber;
        do {
            System.out.print("Please enter seat number (1 to 14): ");
            seatNumber = option.nextInt();
            option.nextLine();
            if (!isValidSeat(seatNumber, rowLetter)) {
                System.out.println("Invalid seat. Please enter a valid seat number.");
            }
        } while (!isValidSeat(seatNumber, rowLetter));
        return seatNumber;
    }

    // get user inputs from this method.
    private static String getInput(String message, Scanner option) {
        System.out.print(message);
        return option.nextLine();
    }

    private static void markSeatAsBooked(String rowLetter, int seatNumber) {
        int rowIndex  = rowLetter.charAt(0) - 'A';
        int seatIndex = seatNumber - 1;
        seatingStructure[rowIndex ][seatIndex] = 'x';
    }

    //iterates through the seatingStructure array and finds the first seat that is not booked.
    private static void find_first_available_seat() {
        for (int row = 0; row < seatingStructure.length; row++) {
            for (int column = 0; column < seatingStructure[row].length; column++) {
                if (seatingStructure[row][column] != 'x') {
                    System.out.println("First available seat: " + (char) ('A' + row) + (column + 1));
                    return;
                }
            }
        }

        System.out.println("No available seats.");
    }

    //displays the current seating plan after updated or before .
    private static void show_seating_plan() {
        System.out.println();
        for (int row = 0; row < seatingStructure.length; row++) {
            System.out.print((char) ('A' + row) + " ");

            for (int column = 0; column < seatingStructure[row].length; column++) {
                if (seatingStructure[row][column] != 'x') {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }
            }

            System.out.println();
        }
    }

    //prints the information of each ticket's.
    // And calculates the total sales to print.
    private static void print_tickets_info() {
        double totalSales = 0;

        for (int i = 0; i < ticketCount; i++) {
            Ticket ticket = tickets[i];
            ticket.printInfo();
            totalSales += ticket.getPrice();
        }

        System.out.println("Total Sales: £" + totalSales);
    }

    //user to enter the name and surname on the ticket and searches for a matching ticket in the tickets array•
    private static void search_ticket(Scanner option) {
        System.out.print("Please enter the name on the ticket: ");
        String name = option.nextLine();

        System.out.print("Please enter the surname on the ticket: ");
        String surName = option.nextLine();

        for (int i = 0; i < ticketCount; i++) {
            Ticket ticket = tickets[i];
            Person person = ticket.getPerson();

            if (person.getName().equalsIgnoreCase(name) && person.getSurname().equalsIgnoreCase(surName)) {
                ticket.printInfo();
                return;
            }
        }

        System.out.println("Ticket not found.");
    }

    // checking the row letter within A-D
    private static boolean isValidRow(String rowLetter) {
        return rowLetter.matches("[A-D]");
    }

    // checking the seat number is according as in the arrays created above.
    private static boolean isValidSeat(int seatNumber, String rowLetter) {
        int rowIdx = rowLetter.charAt(0) - 'A';

        if (rowIdx == 0 || rowIdx == numberOfRows - 1) {
            return seatNumber >= 1 && seatNumber <= seats_per_row_in_A_D;
        } else {
            return seatNumber >= 1 && seatNumber <= seats_per_row_in_B_C;
        }
    }

    // to calculate ticket prices.
    private static double calculateSeatPrice(String rowLetter, int seatNumber) {
        int rowIndex = rowLetter.charAt(0) - 'A';

        if (rowIndex < 0 || rowIndex >= numberOfRows) {
            return 0;
        }

        if (seatNumber >= 1 && seatNumber <= 5) {
            return 200;
        } else if (seatNumber >= 6 && seatNumber <= 9) {
            return 150;
        } else {
            return 180;
        }
    }
}
