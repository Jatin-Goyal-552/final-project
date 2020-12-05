
package LMS;


import java.io.*;
import java.util.*;
import java.sql.*;

public class Main 
{
    
    public static void clearScreen()
    {
        for (int i = 0; i < 20; i++)
            System.out.println();
    }

    
    public static int takeInputFromUser(int min, int max)
    {    
        int  choice;
        Scanner input = new Scanner(System.in);        
        
        while(true)
        {
            System.out.println("\nPlease enter a valid Choice: ");
            choice=input.nextInt();
            if(choice>min&&choice<max) return  choice;
           System.out.println("\nThe input is invalid please enter a number between ."+min+""+max);
        }
          
    }

    
    public static void allFunctionalities(Person person, int choice) throws IOException
    {
        Library library = Library.getInstance();
        
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        
        
        switch (choice){
            case 1:
                library.searchForBooks();
                break;
            case 2:
                addNewHoldRequest(library,person);
                break;
            case 3:
                viewBorrowerInfo(library,person);
                break;
            case 4:
               computeFineForBorrower(library,person);
                break;
            case 5:
                printHoldRequestsForBook(library,person);
                break;
            case 6:
                issueBook(library,person);
                break;
            case 7:
                takeBookReturn(library,person);
                break;
            case 8:
                renewIssuedBook(library,person);
                break;
            case 9:
                library.createNewPerson('b');
                break;
            case 10:
                Borrower bor = library.findBorrower();
                if(bor != null)
                    bor.updateBorrowerInfo();
                break;
            case 11:
                addNewBookToLibrary(library,person);
                break;
            case 12:
                removeBookFromLibrary(library,person);
                break;
            case 13:
                changeBookInfo(library,person);
                break;
        }

        

        
        System.out.println("\nEnter any key to continue........\n");
        scanner.next();
    }
    public  static void changeBookInfo(Library library,Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();

        if (books!=null)
        {
            int input = takeInputFromUser(-1,books.size());

            books.get(input).changeBookInfo();
        }
    }
    public static  void removeBookFromLibrary(Library library,Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();

        if (books != null)
        {
            int input = takeInputFromUser(-1,books.size());

            library.removeBookfromLibrary(books.get(input));
        }
    }
    public static void addNewBookToLibrary(Library library,Person person) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nplease provide title:");
        String title = reader.readLine();

        System.out.println("\nplease provide subject:");
        String subject = reader.readLine();

        System.out.println("\nplease provide Author:");
        String author = reader.readLine();

        library.createBook(title, subject, author);
    }
    
    public static void addNewHoldRequest(Library library,Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();

        if (books != null)
        {
            int input = takeInputFromUser(-1,books.size());

            Book b = books.get(input);

            if("Librarian".equals(person.getClass().getSimpleName()))
            {
                Borrower bor = library.findBorrower();

                if (bor != null)
                    b.makeHoldRequest(bor);
            }
            else
                b.makeHoldRequest((Borrower)person);
        }
    }
   
    public static void viewBorrowerInfo(Library library,Person person){
        if(  "Librarian".equals(person.getClass().getSimpleName()))
        {
            Borrower bor = library.findBorrower();

            if(bor!=null)
                bor.printInfo();
        }
        else
            person.printInfo();
    }
    public  static  void computeFineForBorrower(Library library, Person person){
        if("Librarian".equals(person.getClass().getSimpleName()))
        {
            Borrower bor = library.findBorrower();

            if(bor!=null)
            {
                double totalFine = library.computeFine2(bor);
                System.out.println("\nYour total computed Fine to be paid is : Rs " + totalFine );
            }
        }
        else
        {
            double totalFine = library.computeFine2((Borrower)person);
            System.out.println("\nFinal Fine is : Rs " + totalFine );
        }
    }
    public static void printHoldRequestsForBook(Library library, Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
         int input=0;
        if (books != null)
        {
            input = takeInputFromUser(-1,books.size());
            books.get(input).printHoldRequests();
        }
    }
    public static void issueBook(Library library, Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        int input=0;
        if (books != null)
        {
            input = takeInputFromUser(-1,books.size());
            Book book = books.get(input);

            Borrower borrower = library.findBorrower();

            if(borrower!=null)
            {
                book.issueBook(borrower, (Staff)person);
            }
        }
    }
    public static void takeBookReturn(Library library,Person person){
        Borrower bor = library.findBorrower();
        int input=0;
        if(bor!=null)
        {
            bor.printBorrowedBooks();
            ArrayList<Loan> loans = bor.getBorrowedBooks();

            if (!loans.isEmpty())
            {
                input = takeInputFromUser(-1,loans.size());
                Loan l = loans.get(input);

                l.getBook().returnBook(bor, l, (Staff)person);
            }
            else
                System.out.println("\nThis specific borrower " + bor.getName() + " does not have any book to return.");
        }
    }
    public static void renewIssuedBook(Library library, Person person){
        Borrower bor = library.findBorrower();
        int input=0;
        if(bor!=null)
        {
            bor.printBorrowedBooks();
            ArrayList<Loan> loans = bor.getBorrowedBooks();

            if (!loans.isEmpty())
            {
                input = takeInputFromUser(-1,loans.size());

                loans.get(input).renewIssuedBook(new java.util.Date());
            }
            else
                System.out.println("\nThis specific borrower " + bor.getName() + " doesn't have any issued book which he can renew.");
        }
    }
   
    public static void main(String[] args)
    {
        Scanner admin = new Scanner(System.in);
        
        
        
        Library library = Library.getInstance();
        
        
        library.setLateFinePerDay(10);
        library.setRequestExpiry(7);
        library.setReturnDeadline(5);
        library.setName("IIITV Library");
        
      
        Connection connectionWithDatabase = library.makeConnectionWithDatabase();
        
        if (connectionWithDatabase == null)    
        {
            System.out.println("\nConfigured Error while associating to Database. leaving.");
            return;
        }

        
        try {

        library.populateTheLibrary(connectionWithDatabase);   
         
        boolean stop = false;
        while(!stop)
        {   
            clearScreen();

            
                    
            System.out.println("--------------------------------------------------------");
            System.out.println("\tWelcome to IIITV Library Management System");
            System.out.println("--------------------------------------------------------");
            
            System.out.println("Following Functionalities are available: \n");
            System.out.println("1- Login");
            System.out.println("2- Exit");
            System.out.println("3- Admininstrative Functions"); 
            
            System.out.println("-----------------------------------------\n");        
            
            int choice = 0;

            choice = takeInputFromUser(0,4);
            
            if (choice == 3)
            {                   
                System.out.println("\nPlease Enter Admininstrative Password: ");
                String aPass = admin.next();
                
                if(aPass.equals("library"))
                {
                    while (true)    
                    {
                        clearScreen();

                        System.out.println("--------------------------------------------------------");
                        System.out.println("\tWelcome to the World of Admin's Portal");
                        System.out.println("--------------------------------------------------------");
                        System.out.println("Functionalities which are available are as follows : \n");
                        System.out.println("1- Add Librarian");
                        System.out.println("2- View Issued Books History");
                        System.out.println("3- View All Books in Library");
                        System.out.println("4- Logout");

                        System.out.println("---------------------------------------------");

                        choice = takeInputFromUser(0,5);

                        if (choice == 4)
                            break;

                        if (choice == 1)
                            library.createNewPerson('l');

                        else if (choice == 2)
                            library.viewBookIssuedHistory();

                        else if (choice == 3)
                            library.viewAllBooksInLibrary();
                        
                        System.out.println("\nplease enter any key to continue.........\n");
                        admin.next();                        
                    }
                }
                else
                    System.out.println("\nOOPPS  password entered by you is incorrect.");
            }
 
            else if (choice == 1)
            {
                Person person = library.login();

                if (person == null){}
                
                else if (person.getClass().getSimpleName().equals("Borrower"))
                {                    
                    while (true)    
                    {
                        clearScreen();
                                        
                        System.out.println("--------------------------------------------------------");
                        System.out.println("\tWelcome to the World of Borrower's Portal");
                        System.out.println("--------------------------------------------------------");
                        System.out.println(" Functionalities which are available are as follows : \n");
                        System.out.println("1- Search a Book");
                        System.out.println("2- Place a Book on hold");
                        System.out.println("3- Check Personal Info of Borrower");
                        System.out.println("4- Check Total Fine of Borrower"); 
                        System.out.println("5- Check Hold Requests Queue of a Book");                         
                        System.out.println("6- Logout");
                        System.out.println("--------------------------------------------------------");
                        
                        choice = takeInputFromUser(0,7);

                        if (choice == 6)
                            break;
                        
                        allFunctionalities(person,choice);
                    }
                }
                

                else if (person.getClass().getSimpleName().equals("Librarian"))
                {
                    while(true) 
                    {
                        clearScreen();
                                        
                        System.out.println("--------------------------------------------------------");
                        System.out.println("\tWelcome to the World of Librarian's Portal");
                        System.out.println("--------------------------------------------------------");
                        System.out.println("Functionalities which are available are as follows : \n");
                        System.out.println("1- Search a Book");
                        System.out.println("2- Place a Book on hold");
                        System.out.println("3- Check Personal Information of Borrower");
                        System.out.println("4- Check Total Fine of Borrower");      
                        System.out.println("5- Check Hold Requests Queue of a Book");                        
                        System.out.println("6- Check out a Book");
                        System.out.println("7- Check in a Book");                        
                        System.out.println("8- Renew a Book");
                        System.out.println("9- Add a new Borrower");
                        System.out.println("10- Update a Borrower's Information");
                        System.out.println("11- Add new Book");
                        System.out.println("12- Remove a Book");
                        System.out.println("13- Change a Book's Information");
                        System.out.println("14- Logout");
                        System.out.println("--------------------------------------------------------");
                        
                        choice = takeInputFromUser(0,15);

                        if (choice == 14)
                            break;
                                               
                        allFunctionalities(person,choice);                        
                    }                    
                }
                
            }

            else
                stop = true;

            System.out.println("\nPlease enter any key to continue......\n");
            Scanner scanner = new Scanner(System.in);
            scanner.next();            
        }
        
        
        library.fillItBack(connectionWithDatabase);
        }
        catch(Exception e)
        {
            System.out.println("\nExiting...\n"+e);
        }   
       
    }    
    
}   

