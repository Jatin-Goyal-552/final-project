
package LMS;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class Loan 
{
    private Borrower borrower;      
    private Book book;
    
    private Staff issuerOfBook;
    private Date issuedDateOfBook;
    
    private Date returnDateOfBook;
    private Staff receiver;
    
    private boolean isFinePaid;
       
    public Loan(Borrower bor, Book b, Staff i, Staff r, Date iDate, Date rDate, boolean fPaid)  
    {
        borrower = bor;
        book = b;
        issuerOfBook = i;
        receiver = r;
        issuedDateOfBook = iDate;
        returnDateOfBook = rDate;
        
        isFinePaid = fPaid;
    }
    
    
    
    public Book getBook()      
    {
        return book;
    }
    
    public Staff getIssuerOfBook()     
    {
        return issuerOfBook;
    }
    
    public Staff getReceiver()  
    {
        return receiver;
    }
    
    public Date getIssuedDateOfBook()     
    {
        return issuedDateOfBook;
    } 

    public Date getReturnDate()     
    {
        return returnDateOfBook;
    }
    
    public Borrower getBorrower()   
    {
        return borrower;
    }
    
    public boolean getFineStatus()  
    {
        return isFinePaid;
    }
    
    
    
    
    public void setReturnedDate(Date dReturned)
    {
        returnDateOfBook = dReturned;
    }
    
    public void setFineStatus(boolean fStatus)
    {
        isFinePaid = fStatus;
    }    
    
    public void setReceiver(Staff r)
    {
        receiver = r;
    }
    
    



    
    public double computeFine1()
    {
      
        double totalFine = 0;
        
        if (!isFinePaid)
        {    
            Date iDate = issuedDateOfBook;
            Date rDate = new Date();                

            long days =  ChronoUnit.DAYS.between(rDate.toInstant(), iDate.toInstant());        
            days=0-days;

            days = days - Library.getInstance().bookReturnDeadLine;

            if(days>0)
                totalFine = days * Library.getInstance().lateFinePerDay;
            else
                totalFine=0;
        }
        return totalFine;
    }
    
    
    public void payFine()
    {
        
        
        double totalFine = computeFine1();
                
        if (totalFine > 0)
        {
            System.out.println("\nAbsolute Fine created: Rs " + totalFine);

            System.out.println("Would you like to pay? (y/n)");
            
            Scanner input = new Scanner(System.in); 
            
            String choice = input.next();
            
            if(choice.toLowerCase().equals("y"))
                isFinePaid = true;
            
            if(choice.toLowerCase().equals("n"))
                isFinePaid = false;
        }
        else
        {
            System.out.println("\nNo Absolute fine is created.");
            isFinePaid = true;
        }        
    }


    
    public void renewIssuedBook(Date iDate)
    {        
        issuedDateOfBook = iDate;
        
        System.out.println("\nBook's Deadline " + getBook().getTitle() + " has been extended.");
        System.out.println(" Woohoo!successfully able to renewed the book! \n");
    }












    
}  
