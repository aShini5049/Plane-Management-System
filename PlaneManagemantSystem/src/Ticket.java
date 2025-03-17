import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class Ticket {

    //Instance Variables
    private String row;
    private int seat;
    private double price;
    private Person person;

    public Ticket(String row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    //Prints the ticket information
    public void printInfo() {
        System.out.println("Row: " + row + " " + "Seat: " + seat +" " + "Price: £" + price);
        System.out.println("Person Information:");
        person.printInfo();
    }

    //Saves the ticket information to a file.
    public static void saveTicket(Ticket ticket) {
        String fileName = ticket.getRow() + ticket.getSeat() + ".txt";

        try (PrintWriter writer = new PrintWriter(fileName)) {

            writer.println("Ticket Information:");
            writer.println("\nRow: " + ticket.getRow());
            writer.println("Seat: " + ticket.getSeat());
            writer.println("Price: £" + ticket.getPrice());

            writer.println("\nPerson Information:");
            writer.println("\nName: " + ticket.getPerson().getName());
            writer.println("Surname: " + ticket.getPerson().getSurname());
            writer.println("Email: " + ticket.getPerson().getEmail());
        } catch (FileNotFoundException e) {
            System.out.println("Failed to save ticket information.");
        }
    }

}

