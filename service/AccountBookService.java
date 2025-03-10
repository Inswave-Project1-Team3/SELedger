package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import model.DayAccountBook;
import model.TransactionAccountBook;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.App.dayAccountBookMap;

public class AccountBookService {

    public void createAccountBook(CreateAccountBookDTO accountBookDTO, CreateTransactionAccountBookDTO transactionAccountBookDTO) {
        getToFile();

        int day = LocalDateTime.now().getDayOfMonth();

        TransactionAccountBook transactionAccountBook = new TransactionAccountBook(
                transactionAccountBookDTO.isBenefit(),
                transactionAccountBookDTO.getMoney()
        );

        List<TransactionAccountBook> list = dayAccountBookMap.get(day).getTransactionAccountBooks();
        list.add(transactionAccountBook);
        DayAccountBook dayAccountBook = new DayAccountBook(accountBookDTO.getMemo(), list);
        dayAccountBookMap.put(day, dayAccountBook);

        // 출력
        for(int i = 0; i < dayAccountBookMap.get(day).getTransactionAccountBooks().size(); i++){
            System.out.println(dayAccountBookMap.get(day).getTransactionAccountBooks().get(i).getMoney() +
            ", " + dayAccountBookMap.get(day).getTransactionAccountBooks().get(i).getCreateDate() + ", " +
                    dayAccountBookMap.get(day).getTransactionAccountBooks().get(i).isBenefit());
        }

        System.out.println("메모내용 : " + dayAccountBookMap.get(day).getMemo());

        saveToFile();
}


    private void saveToFile() {
        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\day_account_book.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(dayAccountBookMap);  // Map 전체를 직렬화하여 저장
            System.out.println("등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getToFile() {
        File file = new File("C:\\Temp\\day_account_book.ser");
        if (!file.exists()) {
            System.out.println("거래내역이 존재하지 않습니다");
            return;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            // 데이터를 읽어온 후 null 체크
            Object obj = in.readObject();
            dayAccountBookMap = (obj instanceof Map) ? (Map<Integer, DayAccountBook>) obj : new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
