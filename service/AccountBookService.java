package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static app.App.month;

/**
 * 가계부 관련 비즈니스 로직 처리
 */
public class AccountBookService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String USER_DATA_FOLDER = "data";

    /**
     * 선택한 날짜에 가계부 항목 생성
     */
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

    /**
     * 가계부 데이터 파일 저장
     */
    private void saveToFile (Map<Integer, DayAccountBook> monthAccountBook, int month, String userNickName) {
        // 저장할 경로 설정
//        String directoryPath = USER_DATA_FOLDER + File.separator + userNickName + File.separator + "calendar";
        String directoryPath = ("C:\\Temp\\day_account_book.ser");
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // 상위 디렉터리까지 포함해 전체 생성
        }

        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\day_account_book.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(monthAccountBook);
            System.out.println("등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 저장된 가계부 데이터 불러오기
     */
    public  Map<Integer, DayAccountBook> getToFile(int month, String userNickName) {
//        File file = new File(USER_DATA_FOLDER + File.separator + userNickName + File.separator + "calendar" + File.separator + month + ".ser");
        File file = new File("C:\\Temp\\day_account_book.ser");
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

    /**
     * 특정 날짜의 가계부 조회
     */
    public DayAccountBook getDayAccountBook(int month, int day, String userNickName) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);

        return (monthAccountBook.containsKey(day)) ? monthAccountBook.get(day) : new DayAccountBook();

    }
    
    /**
     * 월별 일자별 수입/지출 합계 조회
     */
    public Map<Integer,DayMoney> getMonthMoney(String userNickName){
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);
        Map<Integer, DayMoney> daysMoney = new HashMap<>();

        for (Entry<Integer, DayAccountBook> entry : monthAccountBook.entrySet()) {
            int day = entry.getKey();
            DayAccountBook dayAccountBook = entry.getValue();
            List<TransactionAccountBook> transactionAccountBooks = dayAccountBook.getTransactionAccountBooks();

            int benefit = 0;
            int expenditure = 0;

            for (TransactionAccountBook transactionAccountBook : transactionAccountBooks) {
                if (transactionAccountBook.isBenefit()) {
                    benefit += transactionAccountBook.getMoney();
                } else {
                    expenditure += transactionAccountBook.getMoney();
                }
            }

            daysMoney.put(day, new DayMoney(benefit, expenditure));
        }

        return daysMoney;
    }
}


