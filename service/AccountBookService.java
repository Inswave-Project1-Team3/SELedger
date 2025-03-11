package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import model.*;

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
                                  int month, int day) {

        // 저장된 파일 가져오기
        Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = getToFile();
        // 저장된 월이 존재하지 않는다면 monthAccountBook 생성
        if (!monthAccountBook.containsKey(month)) monthAccountBook.put(month, new HashMap<>());

        Map<Integer, DayAccountBook> dayAccountBookMap = monthAccountBook.get(month);


        TransactionAccountBook transactionAccountBook = new TransactionAccountBook(
                transactionAccountBookDTO.isBenefit(),
                transactionAccountBookDTO.getMoney(),
                transactionAccountBookDTO.getAccountCategory());

        List<TransactionAccountBook> list = (dayAccountBookMap.containsKey(day)) ?
                dayAccountBookMap.get(day).getTransactionAccountBooks() :
                new ArrayList<>();

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
                        dayAccountBook.getTransactionAccountBooks().get(i).isBenefit() + ", " +
                        dayAccountBook.getTransactionAccountBooks().get(i).getAccountCategory().getDescription());
            }
            System.out.println("메모내용 : " + dayAccountBook.getMemo());
        } else {
            monthAccountBook.get(month).put(day, new DayAccountBook());
        }
    }

    public Map<Integer, DayAccountBook> getMonthAccountBook(int month) {
        Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = getToFile();


        return (monthAccountBook.containsKey(month)) ?
                monthAccountBook.get(month) : new HashMap<>();
        }
    }


