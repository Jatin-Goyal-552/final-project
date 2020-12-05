package LMS;

import java.util.Date;

public class HoldRequest {
    
    Borrower borrower;
    Book book;
    Date holdRequestFiledDate;
    
    public HoldRequest(Borrower bor, Book b, Date reqDate)  
    {
        borrower = bor;
        book = b;
        holdRequestFiledDate = reqDate;
    }
    
    
    public Borrower getBorrower()
    {
        return borrower;
    }
    
    public Book getBook()
    {
        return book;
    }
    
    public Date getHoldRequestFiledDate()
    {
        return holdRequestFiledDate;
    }
    
    
   
    public void print()
    {
        System.out.print(book.getTitle() + "\t\t\t\t" + borrower.getName() + "\t\t\t\t"  + holdRequestFiledDate + "\n");
    }
}