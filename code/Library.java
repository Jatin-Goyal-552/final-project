
package LMS;



import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Library {
    
    private String name;                                
    private Librarian librarian;                                              
    private ArrayList <Person> persons;                  
    private ArrayList <Book> booksInLibrary;            
    
    private ArrayList <Loan> bookLoans;                     
        
    public int bookReturnDeadLine;                   
    public double lateFinePerDay;
    public int holdRequestExpiry;                    
    
    
    
    private static Library obj;
   
    public static Library getInstance()
    {
        if(obj==null) obj = new Library();
        
        return obj;
    }
   
    
    private Library()   
    {
        name = null;
        librarian = null;
        persons = new ArrayList();
    
        booksInLibrary = new ArrayList();
        bookLoans = new ArrayList();
    }

    
    
    
    public void setReturnDeadline(int deadline)
    {
        bookReturnDeadLine = deadline;
    }

    public void setLateFinePerDay(float perDayFine)
    {
        this.lateFinePerDay = perDayFine;
    }

    public void setRequestExpiry(int holdRequestExpiry)
    {
        this.holdRequestExpiry = holdRequestExpiry;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    


   

    
    
    public int getHoldRequestExpiry()
    {
        return holdRequestExpiry;
    }
    
    public ArrayList<Person> getPersons()
    {
        return persons;
    }
    
    public Librarian getLibrarian()
    {
        return librarian;
    }
      
    public String getLibraryName()
    {
        return name;
    }

    public ArrayList<Book> getBooks()
    {
        return booksInLibrary;
    }
    
    
    
    
    public boolean addLibrarian(Librarian librarian)
    {
        
        if (this.librarian == null)
        {
            this.librarian = librarian;
            persons.add(this.librarian);
            return true;
        }
        else
            System.out.println("\nApologies, the library as of now has one librarian." +
                    " New Librarian can't be made.");
        return false;
    }

    public void addBorrower(Borrower b)
    {
        persons.add(b);
    }

    
    public void addLoan(Loan l)
    {
        bookLoans.add(l);
    }
    
    
      
    
    public Borrower findBorrower()
    {
        System.out.println("\nPlease Type Borrower's ID: ");
        
        int id = 0;
        
        Scanner scanner = new Scanner(System.in);
        
        try{
            id = scanner.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\nThe input is invalid");
        }

        for (int i = 0; i < persons.size(); i++)
        {
            if (persons.get(i).getID() == id &&
                persons.get(i).getClass().getSimpleName().equals("Borrower"))
                return (Borrower)(persons.get(i));
        }
        
        System.out.println("\nSorry this ID didn't coordinate any Borrower's ID in our information base.");
        return null;
    }
    

    
    
    public void addBookinLibrary(Book b)
    {
        booksInLibrary.add(b);
    }
    
    
    public void removeBookfromLibrary(Book book)
    {
        boolean delete = true;
        
        
        for (int i = 0; i < persons.size() && delete; i++)
        {
            if (persons.get(i).getClass().getSimpleName().equals("Borrower"))
            {
                ArrayList<Loan> borrowedBooks = ((Borrower)(persons.get(i))).getBorrowedBooks();
                
                for (int j = 0; j < borrowedBooks.size() && delete; j++)
                {
                    if (borrowedBooks.get(j).getBook() == book)
                    {
                        delete = false;
                        System.out.println("This specific book is at present acquired by some borrower.");
                    }
                }              
            }
        }
        
        if (delete)
        {
            System.out.println("\nAs of now this specific book is at present not acquired by any borrower.");
            ArrayList<HoldRequest> holdRequests = book.getHoldRequests();
            
            if(!holdRequests.isEmpty())
            {
                System.out.println("\nThis book may be waiting solicitations by certain borrowers." +
                        "Erasing this book will erase the important hold demands as well.");
                System.out.println("Would you actually like to erase the book? (y/n)");
                
                Scanner sc = new Scanner(System.in);
                
                while (true)
                {
                    String choice = sc.next();
                    
                    if(choice.toLowerCase().equals("y") || choice.toLowerCase().equals("n"))
                    {
                        if(choice.toLowerCase().equals("n"))
                        {
                            System.out.println("\nAccording to your solicitation cancellation of book id stoped.");
                            return;
                        }                            
                        else
                        {
                            
                            for (int i = 0; i < holdRequests.size() && delete; i++)
                            {
                                HoldRequest hr = holdRequests.get(i);
                                hr.getBorrower().removeHoldRequest(hr);
                                book.removeHoldRequest();
                            }
                        }
                    }
                    else
                        System.out.println("Input is Invalid.Enter (y/n): ");
                }
                
            }
            else
                System.out.println("This specific book has no hold demands.");
                
            booksInLibrary.remove(book);
            System.out.println("The specific book is successfully deleted.");
        }
        else
            System.out.println("\nUnable to delete the specific book.");
    }
    
    
    
    
    public ArrayList<Book> searchForBooks() throws IOException
    {
       int  choice;
        String title = "", CategoryOfBook = "", author = "";
                
        Scanner sc = new Scanner(System.in);  
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while (true)
        {
            System.out.println("\nEnter either '1' for search by Title ,  '2' for search by Subject and '3' for search by Author of Book .");  
            choice = sc.nextInt();
            
            if (choice==1|| choice==2 || choice==3)
                break;
            else
                System.out.println("\nWrong Input!");
        }

         switch (choice){
             case 1:
                 System.out.println("\nPlease provide the Title of the specfic Book: ");
                 title = reader.readLine();
                 break;
             case 2:
                 System.out.println("\nPlease provide the Subject of the Specific Book:  ");
                 CategoryOfBook = reader.readLine();
                 break;
             case 3:
                 System.out.println("\nPlease provide  the Author of the specific Book: ");
                 author = reader.readLine();
                 break;
         }
        ArrayList<Book> matchedBooks = new ArrayList();
        
        
        for(int i = 0; i < booksInLibrary.size(); i++)
        {
            Book book = booksInLibrary.get(i);
            
            if (choice==1)
            { 
                if (book.getTitle().equals(title))
                    matchedBooks.add(book);
            }
            else if (choice==2)
            { 
                if (book.getCategoryOfBook().equals(CategoryOfBook))
                    matchedBooks.add(book);
            }
            else
            {
                if (book.getAuthor().equals(author))
                    matchedBooks.add(book);
            }
        }
        
        
        if (!matchedBooks.isEmpty())
        {
            System.out.println("\nThese specific books are found: \n");
                        
            System.out.println("------------------------------------------------------------------------------");            
            System.out.println("Number.\t\tBook's Title\t\t\tBook's Author\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");
            
            for (int i = 0; i < matchedBooks.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                matchedBooks.get(i).printInfo();
                System.out.print("\n");
            }
            
            return matchedBooks;
        }
        else
        {
            System.out.println("\nApologies.There are no books available related to your search.");
            return null;
        }
    }
    
    
    
    
     public void viewAllBooksInLibrary()
    {
        if (booksInLibrary.isEmpty()){
            System.out.println("\nAs of now , Library does not contain any books.");
            return;
        }
            System.out.println("\nBooks which are available :");
            
            System.out.println("------------------------------------------------------------------------------");            
            System.out.println("Number.\t\tBook's Title\t\t\tBook's Author\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");
            
            for (int i = 0; i < booksInLibrary.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                booksInLibrary.get(i).printInfo();
                System.out.print("\n");
            }
    }

     
    
    public double computeFine2(Borrower borrower)
    {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------");            
        System.out.println("Number.\t\tTitle of Book\t\tName of Borrower\t\t\tIssued Date is\t\t\tReturned Date is\t\t\t\tFine(Rs) is");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");        
        
        double totalFine = 0;        
        double perLoanFine = 0;
        
        for (int i = 0; i < bookLoans.size(); i++)
        {
            Loan l = bookLoans.get(i);
            
            if ((l.getBorrower() == borrower))
            {
                perLoanFine = l.computeFine1();
                System.out.print(i + "-" + "\t\t" + bookLoans.get(i).getBook().getTitle() + "\t\t\t" + bookLoans.get(i).getBorrower().getName() + "\t\t" + bookLoans.get(i).getIssuedDateOfBook() +  "\t\t\t" + bookLoans.get(i).getReturnDate() + "\t\t\t\t" + perLoanFine  + "\n");
                
                totalFine += perLoanFine;
            }            
        }
        
        return totalFine;
    }
    
    
    public void createNewPerson(char x)
    {
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
              
        System.out.println("\nPlease Enter Name: ");
        String n = "";
        try {
            n = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Please Enter Address: ");
        String address = "";
        try {
            address = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int phone = 0;
        
        try{
            System.out.println(" Please Enter Phone Number: ");
            phone = sc.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\n Input is Invalid .");
        }

        
         if (x == 'l')
        {
            double salary = 0;            
            try{
                System.out.println("Please Enter Salary: ");
                salary = sc.nextDouble();
            }
            catch (java.util.InputMismatchException e)
            {
                System.out.println("\nInput is Invalid.");
            }
            
            Librarian l = new Librarian(-1,n,address,phone,salary,-1); 
            if(addLibrarian(l))
            {
                System.out.println("\nLibrarian with name " + n + " created successfully.");
                System.out.println("\nYour Unique ID is : " + l.getID());
                System.out.println("Your Unique Password is : " + l.getPassword());
            }
        }

        
        else
        {
            Borrower b = new Borrower(-1,n,address,phone);
            addBorrower(b);            
            System.out.println("\nBorrower with name " + n + " is created successfully.");

            System.out.println("\nYour Unique ID is : " + b.getID());
            System.out.println("Your Unique Password is : " + b.getPassword());            
        }        
    }
     

       
    public void createBook(String title, String CategoryOfBook, String author)
    {
        Book b = new Book(-1,title,CategoryOfBook,author,false);
        
        addBookinLibrary(b);
        
        System.out.println("\nBook with Title " + b.getTitle() + "  successfully created.");
    }
    

    
    
    public Person login()
    {
        Scanner input = new Scanner(System.in);
        
        int id = 0;
        String password = "";
        
        System.out.println("\nplease provide unique ID: ");
        
        try{
            id = input.nextInt();
        }
        catch (java.util.InputMismatchException e)
        {
            System.out.println("\nThe Input is Invalid ");
        }
        
        System.out.println("please provide unique Password: ");
        password = input.next();
        
        for (int i = 0; i < persons.size(); i++)
        {
            if (persons.get(i).getID() == id && persons.get(i).getPassword().equals(password))
            {
                System.out.println("\nCongrats You Login Successfully");
                return persons.get(i);
            }
        }
        
        if(librarian!=null)
        {
            if (librarian.getID() == id && librarian.getPassword().equals(password))
            {
                System.out.println("\nCongrats You Login Successfully");
                return librarian;
            }
        }
        
        System.out.println("\nApologies password or ID entered by you is Incorrect");        
        return null;
    }
    
    
    
    public void viewBookIssuedHistory()
    {
        if (!bookLoans.isEmpty())
        { 
            System.out.println("\nThe Books that are Issued are: ");
            
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");            
            System.out.println("Number\tTitle of book\tName of the borrower\tName of issuer\t\tDate of issue\t\t\tName of the reciever\t\tDate of return\t\tFine Paid");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            
            for (int i = 0; i < bookLoans.size(); i++)
            {    
                if(bookLoans.get(i).getIssuerOfBook()!=null)
                    System.out.print(i + "-" + "\t" + bookLoans.get(i).getBook().getTitle() + "\t\t\t" + bookLoans.get(i).getBorrower().getName() + "\t\t" + bookLoans.get(i).getIssuerOfBook().getName() + "\t    " + bookLoans.get(i).getIssuedDateOfBook());
                
                if (bookLoans.get(i).getReceiver() != null)
                {
                    System.out.print("\t" + bookLoans.get(i).getReceiver().getName() + "\t\t" + bookLoans.get(i).getReturnDate() +"\t   " + bookLoans.get(i).getFineStatus() + "\n");
                }
                else
                    System.out.print("\t\t" + "--" + "\t\t\t" + "--" + "\t\t" + "--" + "\n");
            }
        }
        else
            System.out.println("\nNo books are issued.");                        
    }
    
    
    
    public Connection makeConnectionWithDatabase()
    {        
        try
        {
            String host = "jdbc:derby://localhost:1527/LIBRARY";
            String uName = "LIBRARY";
            String uPass= "LIBRARY";
            Connection con = DriverManager.getConnection( host, uName, uPass );
            return con;
        }
        catch ( SQLException err ) 
        {
            System.out.println( err.getMessage( ) );
            return null;
        }   
    }
    
    
    // Loading all info in code via Database.
    public void populateTheLibrary(Connection con) throws SQLException, IOException
    {       
            Library lib = this;
            Statement stmt = con.createStatement( );
            
            /* --- Populating Book ----*/
            String SQL = "SELECT * FROM BOOK";
            ResultSet rs = stmt.executeQuery( SQL );
            
            if(!rs.next())
            {
               System.out.println("\nNo Books Found in Library"); 
            }
            else
            {
                int maxID = 0;
                
                do
                {
                    if(rs.getString("TITLE") !=null && rs.getString("AUTHOR")!=null && rs.getString("CATEGORYOFBOOK")!=null && rs.getInt("ID")!=0)
                    {
                        String title=rs.getString("TITLE");
                        String author=rs.getString("AUTHOR");
                        String subject=rs.getString("CATEGORYOFBOOK");
                        int id= rs.getInt("ID");
                        boolean issue=rs.getBoolean("IS_ISSUED");
                        Book b = new Book(id,title,subject,author,issue);
                        addBookinLibrary(b);
                        
                        if (maxID < id)
                            maxID = id;
                    }
                }while(rs.next());
                
                // setting Book Count
                Book.setIDCount(maxID);              
            }
            
            /* ----Populating Clerks--
           
            SQL="SELECT ID,PNAME,ADDRESS,PASSWORD,PHONE_NO,SALARY,DESK_NO FROM PERSON INNER JOIN CLERK ON ID=C_ID INNER JOIN STAFF ON S_ID=C_ID";
            
            rs=stmt.executeQuery(SQL);
                      
            if(!rs.next())
            {
               System.out.println("No clerks Found in Library"); 
            }
            else
            {
                do
                {
                    int id=rs.getInt("ID");
                    String cname=rs.getString("PNAME");
                    String adrs=rs.getString("ADDRESS"); 
                    int phn=rs.getInt("PHONE_NO");
                    double sal=rs.getDouble("SALARY");
                    int desk=rs.getInt("DESK_NO");
                    Clerk c = new Clerk(id,cname,adrs,phn,sal,desk);
                    
                    addClerk(c);
                }
                while(rs.next());
                                
            }
            
            -----Populating Librarian---*/
            SQL="SELECT ID,PNAME,ADDRESS,PASSWORD,PHONE_NO,SALARY,OFFICE_NO FROM PERSON INNER JOIN LIBRARIAN ON ID=L_ID INNER JOIN STAFF ON S_ID=L_ID";
            
            rs=stmt.executeQuery(SQL);
            if(!rs.next())
            {
               System.out.println("No Librarian Found in Library"); 
            }
            else
            {
                do
                {
                    int id=rs.getInt("ID");
                    String lname=rs.getString("PNAME");
                    String adrs=rs.getString("ADDRESS"); 
                    int phn=rs.getInt("PHONE_NO");
                    double sal=rs.getDouble("SALARY");
                    int off=rs.getInt("OFFICE_NO");
                    Librarian l= new Librarian(id,lname,adrs,phn,sal,off);

                    addLibrarian(l);
                    
                }while(rs.next());
           
            }
                                    
            /*---Populating Borrowers (partially)!!!!!!--------*/
            
            SQL="SELECT ID,PNAME,ADDRESS,PASSWORD,PHONE_NO FROM PERSON INNER JOIN BORROWER ON ID=B_ID";
            
            rs=stmt.executeQuery(SQL);
                      
            if(!rs.next())
            {
               System.out.println("No Borrower Found in Library"); 
            }
            else
            {
                do
                {
                        int id=rs.getInt("ID");
                        String name=rs.getString("PNAME");
                        String adrs=rs.getString("ADDRESS"); 
                        int phn=rs.getInt("PHONE_NO"); 
                        
                        Borrower b= new Borrower(id,name,adrs,phn);
                        addBorrower(b);
                                                
                }while(rs.next());
                                
            }
            
            /*----Populating Loan----*/
            
            SQL="SELECT * FROM LOAN";
            
            rs=stmt.executeQuery(SQL);
            if(!rs.next())
            {
               System.out.println("No Books Issued Yet!"); 
            }
            else
            {
                do
                    {
                        int borid=rs.getInt("BORROWER");
                        int bokid=rs.getInt("BOOK");
                        int iid=rs.getInt("ISSUER");
                        Integer rid=(Integer)rs.getObject("RECEIVER");
                        int rd=0;
                        Date rdate;
                        
                        Date idate=new Date (rs.getTimestamp("ISS_DATE").getTime());
                        
                        if(rid!=null)    // if there is a receiver 
                        {
                            rdate=new Date (rs.getTimestamp("RET_DATE").getTime()); 
                            rd=(int)rid;
                        }
                        else
                        {
                            rdate=null;
                        }
                        
                        boolean fineStatus = rs.getBoolean("FINE_PAID");
                        
                        boolean set=true;
                        
                        Borrower bb = null;
                       
                        
                        for(int i=0;i<getPersons().size() && set;i++)
                        {
                            if(getPersons().get(i).getID()==borid)
                            {
                                set=false;
                                bb=(Borrower)(getPersons().get(i));
                            }
                        }
                        
                        set =true;
                        Staff s[]=new Staff[2];
                        
                        if(iid==getLibrarian().getID())
                        {
                            s[0]=getLibrarian();
                        }
                            
                        else
                        {                                
                            for(int k=0;k<getPersons().size() && set;k++)
                            {
                                if(getPersons().get(k).getID()==iid && getPersons().get(k).getClass().getSimpleName().equals("Clerk"))
                                {
                                    set=false;
                                    s[0]=(Clerk)(getPersons().get(k));
                                }
                            }
                        }       
                        
                        set=true;
                        // If not returned yet...
                        if(rid==null)
                        {
                            s[1]=null;  // no reciever 
                            rdate=null;      
                        }
                        else
                        {
                            if(rd==getLibrarian().getID())
                                s[1]=getLibrarian();

                            else
                            {    //System.out.println("ff");
                                 for(int k=0;k<getPersons().size() && set;k++)
                                {
                                    if(getPersons().get(k).getID()==rd && getPersons().get(k).getClass().getSimpleName().equals("Clerk"))
                                    {
                                        set=false;
                                        s[1]=(Clerk)(getPersons().get(k));
                                    }
                                }
                            }     
                        }
                        
                        set=true;
                        
                        ArrayList<Book> books = getBooks();
                        
                        for(int k=0;k<books.size() && set;k++)
                        {
                            if(books.get(k).getID()==bokid)
                            {
                              set=false;   
                              Loan l = new Loan(bb,books.get(k),s[0],s[1],idate,rdate,fineStatus);
                              bookLoans.add(l);
                            }
                        }
                        
                    }while(rs.next());
            }
            
            /*----Populationg Hold Books----*/
            
            SQL="SELECT * FROM ON_HOLD_BOOK";
            
            rs=stmt.executeQuery(SQL);
            if(!rs.next())
            {
               System.out.println("No Books on Hold Yet!"); 
            }
            else
            {
                do
                    {
                        int borid=rs.getInt("BORROWER");
                        int bokid=rs.getInt("BOOK");
                        Date off=new Date (rs.getDate("REQ_DATE").getTime());
                        
                        boolean set=true;
                        Borrower bb =null;
                        
                        ArrayList<Person> persons = lib.getPersons();
                        
                        for(int i=0;i<persons.size() && set;i++)
                        {
                            if(persons.get(i).getID()==borid)
                            {
                                set=false;
                                bb=(Borrower)(persons.get(i));
                            }
                        }
                                              
                        set=true;
                        
                        ArrayList<Book> books = lib.getBooks();
                        
                        for(int i=0;i<books.size() && set;i++)
                        {
                            if(books.get(i).getID()==bokid)
                            {
                              set=false;   
                              HoldRequest hbook= new HoldRequest(bb,books.get(i),off);
                              books.get(i).addHoldRequest(hbook);
                              bb.addHoldRequest(hbook);
                            }
                        }
                        }while(rs.next());
            }
            
            /* --- Populating Borrower's Remaining Info----*/
            
            // Borrowed Books
            SQL="SELECT ID,BOOK FROM PERSON INNER JOIN BORROWER ON ID=B_ID INNER JOIN BORROWED_BOOK ON B_ID=BORROWER ";
            
            rs=stmt.executeQuery(SQL);
                      
            if(!rs.next())
            {
               System.out.println("No Borrower has borrowed yet from Library"); 
            }
            else
            {
                
                do
                    {
                        int id=rs.getInt("ID");      // borrower
                        int bid=rs.getInt("BOOK");   // book
                        
                        Borrower bb=null;
                        boolean set=true;
                        boolean okay=true;
                        
                        for(int i=0;i<lib.getPersons().size() && set;i++)
                        {
                            if(lib.getPersons().get(i).getClass().getSimpleName().equals("Borrower"))
                            {
                                if(lib.getPersons().get(i).getID()==id)
                                {
                                   set =false;
                                    bb=(Borrower)(lib.getPersons().get(i));
                                }
                            }
                        }
                        
                        set=true;
                        
                        ArrayList<Loan> books = bookLoans;
                        
                        for(int i=0;i<books.size() && set;i++)
                        {
                            if(books.get(i).getBook().getID()==bid &&books.get(i).getReceiver()==null )
                            {
                              set=false;   
                              Loan bBook= new Loan(bb,books.get(i).getBook(),books.get(i).getIssuerOfBook(),null,books.get(i).getIssuedDateOfBook(),null,books.get(i).getFineStatus());
                              bb.addBorrowedBook(bBook);
                            }
                        }
                                 
                    }while(rs.next());               
            }
                      
            ArrayList<Person> persons = lib.getPersons();
            
            /* Setting Person ID Count */
            int max=0;
            
            for(int i=0;i<persons.size();i++)
            {
                if (max < persons.get(i).getID())
                    max=persons.get(i).getID();
            }

            Person.setIDCount(max);  
    }
    
    
    // Filling Changes back to Database
    public void fillItBack(Connection con) throws SQLException,SQLIntegrityConstraintViolationException
    {
            /*-----------Loan Table Cleared------------*/
            
            String template = "DELETE FROM LIBRARY.LOAN";
            PreparedStatement stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                        
            /*-----------Borrowed Books Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.BORROWED_BOOK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                       
            /*-----------OnHoldBooks Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.ON_HOLD_BOOK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Books Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.BOOK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                       
            /*-----------Clerk Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.CLERK";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Librarian Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.LIBRARIAN";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
                       
            /*-----------Borrower Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.BORROWER";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Staff Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.STAFF";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
            
            /*-----------Person Table Cleared------------*/
            
            template = "DELETE FROM LIBRARY.PERSON";
            stmts = con.prepareStatement(template);
            
            stmts.executeUpdate();
           
            Library lib = this;
            
        /* Filling Person's Table*/
        for(int i=0;i<lib.getPersons().size();i++)
        {
            template = "INSERT INTO LIBRARY.PERSON (ID,PNAME,PASSWORD,ADDRESS,PHONE_NO) values (?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1, lib.getPersons().get(i).getID());
            stmt.setString(2, lib.getPersons().get(i).getName());
            stmt.setString(3,  lib.getPersons().get(i).getPassword());
            stmt.setString(4, lib.getPersons().get(i).getAddress());
            stmt.setInt(5, lib.getPersons().get(i).getPhoneNumber());
            
            stmt.executeUpdate();
        }
        
        /* Filling Clerk's Table and Staff Table*/
        for(int i=0;i<lib.getPersons().size();i++)
        {
            if (lib.getPersons().get(i).getClass().getSimpleName().equals("Clerk"))
            {
                template = "INSERT INTO LIBRARY.STAFF (S_ID,TYPE,SALARY) values (?,?,?)";
                PreparedStatement stmt = con.prepareStatement(template);

                stmt.setInt(1,lib.getPersons().get(i).getID());
                stmt.setString(2,"Clerk");
                stmt.setDouble(3, ((Clerk)(lib.getPersons().get(i))).getSalary());

                stmt.executeUpdate();

                template = "INSERT INTO LIBRARY.CLERK (C_ID,DESK_NO) values (?,?)";
                stmt = con.prepareStatement(template);

                stmt.setInt(1,lib.getPersons().get(i).getID());
                stmt.setInt(2, ((Clerk)(lib.getPersons().get(i))).deskNo);

                stmt.executeUpdate();
            }
        
        }
        
        if(lib.getLibrarian()!=null)    // if  librarian is there
            {
            template = "INSERT INTO LIBRARY.STAFF (S_ID,TYPE,SALARY) values (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
             
            stmt.setInt(1, lib.getLibrarian().getID());
            stmt.setString(2,"Librarian");
            stmt.setDouble(3,lib.getLibrarian().getSalary());
            
            stmt.executeUpdate();
            
            template = "INSERT INTO LIBRARY.LIBRARIAN (L_ID,OFFICE_NO) values (?,?)";
            stmt = con.prepareStatement(template);
            
            stmt.setInt(1,lib.getLibrarian().getID());
            stmt.setInt(2, lib.getLibrarian().officeNo);
            
            stmt.executeUpdate();  
            }
        
        /* Filling Borrower's Table*/
        for(int i=0;i<lib.getPersons().size();i++)
        {
            if (lib.getPersons().get(i).getClass().getSimpleName().equals("Borrower"))
            {
                template = "INSERT INTO LIBRARY.BORROWER(B_ID) values (?)";
                PreparedStatement stmt = con.prepareStatement(template);

                stmt.setInt(1, lib.getPersons().get(i).getID());

                stmt.executeUpdate();    
            }
        }
                       
        ArrayList<Book> books = lib.getBooks();
        
        /*Filling Book's Table*/
        for(int i=0;i<books.size();i++)
        {
            template = "INSERT INTO LIBRARY.BOOK (ID,TITLE,AUTHOR,CATEGORYOFBOOK,IS_ISSUED) values (?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,books.get(i).getID());
            stmt.setString(2,books.get(i).getTitle());
            stmt.setString(3, books.get(i).getAuthor());
            stmt.setString(4, books.get(i).getCategoryOfBook());
            stmt.setBoolean(5, books.get(i).getIssuedStatus());
            stmt.executeUpdate();
            
        }
         
        /* Filling Loan Book's Table*/
        for(int i=0;i<bookLoans.size();i++)
        {
            template = "INSERT INTO LIBRARY.LOAN(L_ID,BORROWER,BOOK,ISSUER,ISS_DATE,RECEIVER,RET_DATE,FINE_PAID) values (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,i+1);
            stmt.setInt(2,bookLoans.get(i).getBorrower().getID());
            stmt.setInt(3,bookLoans.get(i).getBook().getID());
            stmt.setInt(4,bookLoans.get(i).getIssuerOfBook().getID());
            stmt.setTimestamp(5,new java.sql.Timestamp(bookLoans.get(i).getIssuedDateOfBook().getTime()));
            stmt.setBoolean(8,bookLoans.get(i).getFineStatus());
            if(bookLoans.get(i).getReceiver()==null)
            {
                stmt.setNull(6,Types.INTEGER); 
                stmt.setDate(7,null);
            }
            else
            {
                stmt.setInt(6,bookLoans.get(i).getReceiver().getID());  
                stmt.setTimestamp(7,new java.sql.Timestamp(bookLoans.get(i).getReturnDate().getTime()));
            }
                
            stmt.executeUpdate(); 
   
        }
       
        /* Filling On_Hold_ Table*/
        
        int x=1;
        for(int i=0;i<lib.getBooks().size();i++)
        {
            for(int j=0;j<lib.getBooks().get(i).getHoldRequests().size();j++)
            {
            template = "INSERT INTO LIBRARY.ON_HOLD_BOOK(REQ_ID,BOOK,BORROWER,REQ_DATE) values (?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,x);
            stmt.setInt(3,lib.getBooks().get(i).getHoldRequests().get(j).getBorrower().getID());
            stmt.setInt(2,lib.getBooks().get(i).getHoldRequests().get(j).getBook().getID());
            stmt.setDate(4,new java.sql.Date(lib.getBooks().get(i).getHoldRequests().get(j).getHoldRequestFiledDate().getTime()));
                    
            stmt.executeUpdate(); 
            x++;
            
            }
        }
        /*for(int i=0;i<lib.getBooks().size();i++)
        {
            for(int j=0;j<lib.getBooks().get(i).getHoldRequests().size();j++)
            {
            template = "INSERT INTO LIBRARY.ON_HOLD_BOOK(REQ_ID,BOOK,BORROWER,REQ_DATE) values (?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(template);
            
            stmt.setInt(1,i+1);
            stmt.setInt(3,lib.getBooks().get(i).getHoldRequests().get(j).getBorrower().getID());
            stmt.setInt(2,lib.getBooks().get(i).getHoldRequests().get(j).getBook().getID());
            stmt.setDate(4,new java.sql.Date(lib.getBooks().get(i).getHoldRequests().get(j).getRequestDate().getTime()));
                    
            stmt.executeUpdate(); 
            }
        }*/
            
        /* Filling Borrowed Book Table*/
        for(int i=0;i<lib.getBooks().size();i++)
          {
              if(lib.getBooks().get(i).getIssuedStatus()==true)
              {
                  boolean set=true;
                  for(int j=0;j<bookLoans.size() && set ;j++)
                  {
                      if(lib.getBooks().get(i).getID()==bookLoans.get(j).getBook().getID())
                      {
                          if(bookLoans.get(j).getReceiver()==null)
                          {
                            template = "INSERT INTO LIBRARY.BORROWED_BOOK(BOOK,BORROWER) values (?,?)";
                            PreparedStatement stmt = con.prepareStatement(template);
                            stmt.setInt(1,bookLoans.get(j).getBook().getID());
                            stmt.setInt(2,bookLoans.get(j).getBorrower().getID());
                  
                            stmt.executeUpdate();
                            set=false;
                          }
                      }
                      
                  }
                  
              }
          }   
    } // Filling Done!  
    
    
    
    
    
    
} 
        
    