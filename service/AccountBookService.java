package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountBookService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String USER_DATA_FOLDER = "data";

    // 선택한 날의 가계부 생성
    public void createAccountBook(CreateAccountBookDTO accountBookDTO,
                                  CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                  int month, int day, String userNickName) {


        // 저장된 파일 가져오기
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);

        TransactionAccountBook transactionAccountBook = new TransactionAccountBook(
                transactionAccountBookDTO.isBenefit(),
                transactionAccountBookDTO.getMoney(),
                transactionAccountBookDTO.getAccountCategory());

        List<TransactionAccountBook> list = (monthAccountBook.containsKey(day)) ?
                monthAccountBook.get(day).getTransactionAccountBooks() :
                new ArrayList<>();

        list.add(transactionAccountBook);

        DayAccountBook dayAccountBook = new DayAccountBook(accountBookDTO.getMemo(), list);

        monthAccountBook.put(day, dayAccountBook);

        saveToFile(monthAccountBook, month, userNickName);
    }

    private void saveToFile (Map<Integer, DayAccountBook> monthAccountBook, int month, String userNickName) {
        // 저장할 경로 설정
//        String directoryPath = (USER_DATA_FOLDER + File.separator + userNickName + File.separator + "calendar" + File.separator + month + ".ser");
//        File directory = new File(directoryPath);
//
//        if (!directory.exists()) {
//            directory.mkdirs(); // 상위 디렉터리까지 포함해 전체 생성
//        }



        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\day_account_book.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(monthAccountBook);
            System.out.println("등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지정된 위치의 거래내역 가져오기
    public  Map<Integer, DayAccountBook> getToFile(int month, String userNickName) {
//        String directoryPath = (USER_DATA_FOLDER + File.separator + userNickName + File.separator + "calendar" + File.separator + month + ".ser");
        File file = new File("C:\\Temp\\day_account_book.ser");

//        File file = new File(directoryPath);
//        if (!file.exists()) {
//            file.mkdirs(); // 상위 디렉터리까지 포함해 전체 생성
//        }

        Map<Integer, DayAccountBook> monthAccountBook = new HashMap<>();

        if (!file.exists()) {
            System.out.println("거래내역이 존재하지 않습니다");
        } else {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {

                Object obj = in.readObject();

                if (obj instanceof Map) monthAccountBook = (Map<Integer, DayAccountBook>) obj;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monthAccountBook;
    }


    public void getDayAccountBook(int month, int day, String userNickName) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);

        System.out.println("📅 " + month +" 월 "+ day + "일 가계부");
        DayAccountBook dayAccountBook = (monthAccountBook.containsKey(day)) ?
                monthAccountBook.get(day) : new DayAccountBook();

        if (dayAccountBook != null) {
            for (int i = 0; i < dayAccountBook.getTransactionAccountBooks().size(); i++) {
                System.out.println(dayAccountBook.getTransactionAccountBooks().get(i).getMoney() +
                        ", " + dayAccountBook.getTransactionAccountBooks().get(i).getCreateDate() + ", " +
                        dayAccountBook.getTransactionAccountBooks().get(i).isBenefit() + ", " +
                        dayAccountBook.getTransactionAccountBooks().get(i).getAccountCategory().getDescription());
            }
            System.out.println("메모내용 : " + dayAccountBook.getMemo());
        } else {
            monthAccountBook.put(day, new DayAccountBook());
        }
    }
}


