package service;

import DTO.CreateAccountBookDTO;
import model.DayAccountBook;
import util.Timestamped;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AccountBookService extends Timestamped {
    public void createAccountBook(CreateAccountBookDTO dto){
        DayAccountBook dayAccountBook = new DayAccountBook(dto.getMemo());


        saveToFile(dayAccountBook);

    }
    private void saveToFile(DayAccountBook accountBook) {
        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\day_account_book.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(accountBook);
            System.out.println("객체가 C:\\Temp\\day_account_book.ser 에 저장되었습니다.");
            System.out.println(accountBook.getCreatedAt());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getToFile(){
        try (FileInputStream fileIn = new FileInputStream("C:\\Temp\\day_account_book.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            DayAccountBook accountBook = (DayAccountBook) in.readObject();
            System.out.println("Memo: " + accountBook.getMemo());
            System.out.println("Created At: " + accountBook.getCreateDate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}