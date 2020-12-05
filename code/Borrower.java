
package LMS;

import java.io.*;
import java.util.*;

public class Borrower extends Person 
{    
    private ArrayList<Loan> borrowedBooks;         
    private ArrayList<HoldRequest> onHoldBooks;    
    
    public Borrower(int id,String n, String a, int p) 
    {
        super(id,n,a,p);
        
        borrowedBooks = new ArrayList();
        onHoldBooks = new ArrayList();        
    }

    
    
    @Override
    public void printInfo()
    {
        super.printInfo();
               
        printBorrowedBooks();
        printOnHoldBooks();
    }
   
    
    public void printBorrowedBooks()
    {
        if (!borrowedBooks.isEmpty())
        { 
            System.out.println("\nBorrowed Books are as follows: ");
            
            System.out.println("------------------------------------------------------------------------------");            
            System.out.println("Number.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");
            
            for (int i = 0; i < borrowedBooks.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                borrowedBooks.get(i).getBook().printInfo();
                System.out.print("\n");
            }
        }
        else
            System.out.println("\nApologies, No  borrowed books.");                
    }
    
    
    public void printOnHoldBooks()
    {
        if (!onHoldBooks.isEmpty())
        { 
            System.out.println("\n The On Hold Books are as follows: ");
            
            System.out.println("------------------------------------------------------------------------------");            
            System.out.println("Number.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");
            
            for (int i = 0; i < onHoldBooks.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                onHoldBooks.get(i).getBook().printInfo();
                System.out.print("\n");
            }
        }
        else
            System.out.println("\nThere are no On Hold books.");                
    }
   
    
    public void updateBorrowerInfo() throws IOException
    {
        String choice;
        
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        
        System.out.println("\nWould you like to update " + getName() + " Name ? (y/n)");  
        choice = sc.next();  

        if(choice.equals("y"))
        {
            System.out.println("\nPlease Type a  New Name: ");
            setName(reader.readLine());
            System.out.println("Woohoo! Successfully able to update name.");         
        }    

               
        System.out.println("\nWoud you like to update " + getName() + "'s Address ? (y/n)");  
        choice = sc.next();  

        if(choice.equals("y"))
        {
            System.out.println("\nPlease Type New Address: ");
            setAddress(reader.readLine());
            System.out.println("\nWoohoo!  Successfully able to update the address");          
        }    

        System.out.println("\nWould you like to update " + getName() + "'s Phone Number ? (y/n)");  
        choice = sc.next();  

        if(choice.equals("y"))
        {
            System.out.println("\nPlease Type New Phone Number: ");
            setMobileNumber(sc.nextInt());
            System.out.println("\nWoohoo! Successfully able to update a phone number.");
        }
        
        System.out.println("\nInformation Regarding Borrower is successfully updated.");
        
    }
    
   
    public void addBorrowedBook(Loan iBook)
    {
        borrowedBooks.add(iBook);
    }
    
    public void removeBorrowedBook(Loan iBook)
    {
        borrowedBooks.remove(iBook);
    }    
    
    
    
    
    public void addHoldRequest(HoldRequest hr)
    {
        onHoldBooks.add(hr);
    }
    
    public void removeHoldRequest(HoldRequest hr)
    {
        onHoldBooks.remove(hr);
    }
    
    
    
    public ArrayList<Loan> getBorrowedBooks()
    {
        return borrowedBooks;
    }
    
    public ArrayList<HoldRequest> getOnHoldBooks()
    {
        return onHoldBooks;
    }
    
}
