package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import model.DayAccountBook;
import model.MonthAccountBook;
import model.TransactionAccountBook;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountBookService implements Serializable {
    private static final long serialVersionUID = 1L;


    // 선택한 날의 가계부 생성
    public void createAccountBook(CreateAccountBookDTO accountBookDTO,
                                  CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                  int day) {
        int month = LocalDateTime.now().getMonthValue();

        Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = getToFile();

        if (!monthAccountBook.containsKey(month)) monthAccountBook.put(month, new HashMap<>());

        Map<Integer, DayAccountBook> dayAccountBookMap = monthAccountBook.get(month);

        TransactionAccountBook transactionAccountBook = new TransactionAccountBook(
                transactionAccountBookDTO.isBenefit(),
                transactionAccountBookDTO.getMoney()
        );

        List<TransactionAccountBook> list;

        if (!dayAccountBookMap.containsKey(day)) {
            list = new ArrayList<>();
        } else {
            DayAccountBook dayAccountBook = dayAccountBookMap.get(day);
            list = (dayAccountBook.getTransactionAccountBooks() != null) ?
                    dayAccountBook.getTransactionAccountBooks() : new ArrayList<>();
        }

        list.add(transactionAccountBook);

        DayAccountBook dayAccountBook = new DayAccountBook(accountBookDTO.getMemo(), list);

        monthAccountBook.get(month).put(day, dayAccountBook);

        saveToFile(monthAccountBook);
    }

    private void saveToFile(Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook) {
        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\day_account_book.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(monthAccountBook);
            System.out.println("등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지정된 위치의 거래내역 가져오기
    public Map<Integer, Map<Integer, DayAccountBook>> getToFile() {
        File file = new File("C:\\Temp\\day_account_book.ser");
        Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = new HashMap<>();

        if (!file.exists()) {
            System.out.println("거래내역이 존재하지 않습니다");
        } else {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {

                Object obj = in.readObject();

                if (obj instanceof Map) monthAccountBook = (Map<Integer, Map<Integer, DayAccountBook>>) obj;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monthAccountBook;
    }

    public void getDayAccountBook(int month, int day) {
        Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = getToFile();

        System.out.println("📅 " + day + "일 가계부");
        DayAccountBook dayAccountBook;
        if (monthAccountBook.containsKey(month) && monthAccountBook.get(month).containsKey(day)) {
            dayAccountBook = monthAccountBook.get(month).get(day);
        } else {
            dayAccountBook = new DayAccountBook();
        }
        if (dayAccountBook != null) {
            for (int i = 0; i < dayAccountBook.getTransactionAccountBooks().size(); i++) {
                System.out.println(dayAccountBook.getTransactionAccountBooks().get(i).getMoney() +
                        ", " + dayAccountBook.getTransactionAccountBooks().get(i).getCreateDate() + ", " +
                        dayAccountBook.getTransactionAccountBooks().get(i).isBenefit());
            }
            System.out.println("메모내용 : " + dayAccountBook.getMemo());
        } else {
            monthAccountBook.get(month).put(day, new DayAccountBook());
        }
    }

    public void getMonthAccountBook() {
        Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = getToFile();
        int currentMonth = LocalDateTime.now().getMonthValue();
        System.out.println("📅 " + currentMonth +"월 가계부");

        for (Map.Entry<Integer, Map<Integer, DayAccountBook>> entry : monthAccountBook.entrySet()) {
            Integer month = entry.getKey();
            Map<Integer, DayAccountBook> dayAccountBookMap = entry.getValue();  // Map<Integer, DayAccountBook> 추출

            System.out.println(month + "월:");

            // 일별로 DayAccountBook을 출력
            for (Map.Entry<Integer, DayAccountBook> dayEntry : dayAccountBookMap.entrySet()) {
                Integer day = dayEntry.getKey();
                DayAccountBook dayAccountBook = dayEntry.getValue();

                System.out.println("  " + day + "일 메모: " + dayAccountBook.getMemo());
                for (TransactionAccountBook transactionAccountBook : dayAccountBook.getTransactionAccountBooks()) {
                    System.out.println("    가격: " + transactionAccountBook.getMoney());
                    System.out.println("         수익여부: " + transactionAccountBook.isBenefit());
                }
            }
        }
    }
}

