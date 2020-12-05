
package LMS;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Book {
   
    private int bookID;           
    private String title;         
    private String categoryOfBook;      
    private String author;        
    private boolean isIssued;        
    private ArrayList<HoldRequest> holdRequests; 
 
    static int currentIdNumber = 0;     
                                       
    
  
    public Book(int id,String t, String s, String a, boolean issued)    
    {
        currentIdNumber++;
        if(id==-1)
        {
            bookID = currentIdNumber;
        }
        else
            bookID=id;
        
        title = t;
        categoryOfBook = s;
        author = a;
        isIssued = issued;
        
        holdRequests = new ArrayList();
    }
    
    
    public void addHoldRequest(HoldRequest hr)
    {
        holdRequests.add(hr);
    }
    
    
    public void removeHoldRequest()
    {
        if(!holdRequests.isEmpty())
        {
            holdRequests.remove(0);
        }
    }
    
    
    public void printHoldRequests()
    {
        if (!holdRequests.isEmpty())
        { 
            System.out.println("\nHold or demand Requests are as follows : ");
            
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");            
            System.out.println("Number\t\tTitle of Book\t\t\tName of Borrower\t\t\tDate of Request");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
            
            for (int i = 0; i < holdRequests.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                holdRequests.get(i).print();
            }
        }
        else
            System.out.println("\nOOPS  No hold Requests are available");                                
    }
    
    
    public void printInfo()
    {
        System.out.println(title + "\t\t\t" + author + "\t\t\t" + categoryOfBook);
    }
    
    
    public void changeBookInfo() throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("\n Do you want to Update Author? (y/n)");
        input = scanner.next();
        
        if(input.toLowerCase().equals("y"))
        {
            System.out.println("\nplease provide the name of the new Author: ");
            author = reader.readLine();
        }

        System.out.println("\nDo you want to Update Subject? (y/n)");
        input = scanner.next();
        
        if(input.toLowerCase().equals("y"))
        {
            System.out.println("\nplease provide  the category of book: ");
            categoryOfBook = reader.readLine();
        }

        System.out.println("\nDo you want to Update Title? (y/n)");
        input = scanner.next();
        
        if(input.toLowerCase().equals("y"))
        {
            System.out.println("\nPlease provide a new Title: ");
            title = reader.readLine();
        }        
        
        System.out.println("\nWoohoo!  successfully able to  update a book.");
        
    }
    
    
    
    public String getTitle()
    {
        return title;
    }

    public String getCategoryOfBook()
    {
        return categoryOfBook;
    }

    public String getAuthor()
    {
        return author;
    }
    
    public boolean getIssuedStatus()
    {
        return isIssued;
    }
    
    public void setIssuedStatus(boolean s)
    {
        isIssued = s;
    }
    
     public int getID()
    {
        return bookID;
    }
     
     public ArrayList<HoldRequest> getHoldRequests()
    {
        return holdRequests;
    }
    
     
    
    public static void setIDCount(int n)
    {
        currentIdNumber = n;
    }
    

    
    
    
    
    
    public void placeBookOnHold(Borrower bor)
    {
        HoldRequest hr = new HoldRequest(bor,this, new Date());
        
        addHoldRequest(hr);        
        bor.addHoldRequest(hr);      
        
        System.out.println("\nThe book " + title + "  has been effectively requires to briefly wait by borrower" + bor.getName() + ".\n");
    }
    
    


   
    public void makeHoldRequest(Borrower borrower)
    {
        boolean makeRequest = true;

        
        for(int i=0;i<borrower.getBorrowedBooks().size();i++)
        {
            if(borrower.getBorrowedBooks().get(i).getBook()==this)
            {
                System.out.println("\n" + "You have just acquired " + title);
                return;                
            }
        }
        
        
        
        for (int i = 0; i < holdRequests.size(); i++)
        {
            if ((holdRequests.get(i).getBorrower() == borrower))
            {
                makeRequest = false;    
                break;
            }
        }

        if (makeRequest)
        {
            placeBookOnHold(borrower);
        }
        else
            System.out.println("\nYou as of now have one hold demand for this book.\n");
    }

    
    
    public void serviceHoldRequest(HoldRequest hr)
    {
        removeHoldRequest();
        hr.getBorrower().removeHoldRequest(hr);
    }

    
        
    
    public void issueBook(Borrower borrower, Staff staff)
    {        
       
        Date today = new Date();        
        
        ArrayList<HoldRequest> hRequests = holdRequests;
        
        for (int i = 0; i < hRequests.size(); i++)
        {
            HoldRequest hr = hRequests.get(i);            
            
            
            long days =  ChronoUnit.DAYS.between(today.toInstant(), hr.getHoldRequestFiledDate().toInstant());
            days = 0-days;
            
            if(days>Library.getInstance().getHoldRequestExpiry())
            {
                removeHoldRequest();
                hr.getBorrower().removeHoldRequest(hr);
            } 
        }
               
        if (isIssued)
        {
            System.out.println("\n book " + title + " is issued in advance.");
            System.out.println("Do you want to place the book on hold? (y/n)");
             
            Scanner sc = new Scanner(System.in);
            String choice = sc.next();
            
            if (choice.toLowerCase().equals("y"))
            {                
                makeHoldRequest(borrower);
            }
        }
        
        else
        {               
            if (!holdRequests.isEmpty())
            {
                boolean hasRequest = false;
                
                for (int i = 0; i < holdRequests.size() && !hasRequest;i++)
                {
                    if (holdRequests.get(i).getBorrower() == borrower)
                        hasRequest = true;
                        
                }
                
                if (hasRequest)
                {
                   
                    if (holdRequests.get(0).getBorrower() == borrower)
                        serviceHoldRequest(holdRequests.get(0));       

                    else
                    {
                        System.out.println("\nSorry some different borrower have mentioned for this book sooner than you. So you need to stand by until their hold demands are handled.");
                        return;
                    }
                }
                else
                {
                    System.out.println("\nA few borrower have just positioned this book on solicitation and you haven't, so the book can't be given to you.");
                    
                    System.out.println("do u want to place the book on hold? (y/n)");

                    Scanner sc = new Scanner(System.in);
                    String choice = sc.next();
                    
                    if (choice.toLowerCase().equals("y"))
                    {
                        makeHoldRequest(borrower); 
                    }                    
                    
                    return;
                }               
            }
                        
                        
            setIssuedStatus(true);
            
            Loan iHistory = new Loan(borrower,this,staff,null,new Date(),null,false);
            
            Library.getInstance().addLoan(iHistory);
            borrower.addBorrowedBook(iHistory);
                                    
            System.out.println("\nThe book " + title + " is successfully issued to " + borrower.getName() + ".");
            System.out.println("\nIssued by: " + staff.getName());            
        }
    }
        
        
    
    public void returnBook(Borrower borrower, Loan l, Staff staff)
    {
        l.getBook().setIssuedStatus(false);        
        l.setReturnedDate(new Date());
        l.setReceiver(staff);        
        
        borrower.removeBorrowedBook(l);
        
        l.payFine();
        
        System.out.println("\nThe book " + l.getBook().getTitle() + " is successfully returned by " + borrower.getName() + ".");
        System.out.println("\nReceived by: " + staff.getName());            
    }
    
}   