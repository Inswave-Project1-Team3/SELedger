package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import model.DayAccountBook;
import model.MonthAccountBook;
import model.TransactionAccountBook;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.App.dayAccountBookMap;
import static app.App.monthAccountBook;

public class AccountBookService implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 월별 가계부 저장소
     */
    private Map<YearMonth, MonthAccountBook> accountBookRecords = new HashMap<>();

    /**
     * 특정 월의 가계부 가져오기 (없으면 생성)
     *
     * @param month 조회할 월
     * @return 해당 월의 가계부
     */
    private MonthAccountBook getOrCreateMonthAccountBook(YearMonth month) {
        return accountBookRecords.computeIfAbsent(month, MonthAccountBook::new);
    }

    /**
     * 월별 가계부를 파일로 저장 (직렬화 방식)
     */
    public void saveMonthAccountBook(MonthAccountBook monthAccountBook) {
        File file = getMonthAccountBookFile(monthAccountBook.getMonth());
        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(monthAccountBook);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("파일 저장 오류: " + file.getAbsolutePath());
        }
    }

    /**
     * 월별 가계부를 파일에서 로드 (역직렬화 방식)
     */
    public MonthAccountBook loadMonthAccountBook(YearMonth month) {
        File file = getMonthAccountBookFile(month);
        if (!file.exists()) {
            return new MonthAccountBook(month);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (MonthAccountBook) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new MonthAccountBook(month);
        }
    }

    /**
     * 특정 월의 가계부 파일 경로 반환
     */
    private File getMonthAccountBookFile(YearMonth month) {
        return new File("data/" + month + ".ser");
    }




    // 선택한 날의 가계부 생성
    public void createAccountBook(CreateAccountBookDTO accountBookDTO,
                                  CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                  int day) {
        getToFile();

        TransactionAccountBook transactionAccountBook = new TransactionAccountBook(
                transactionAccountBookDTO.isBenefit(),
                transactionAccountBookDTO.getMoney()
        );

        List<TransactionAccountBook> list = (dayAccountBookMap.get(day) == null) ?
                        new ArrayList<>() : dayAccountBookMap.get(day).getTransactionAccountBooks();
        list.add(transactionAccountBook);

        DayAccountBook dayAccountBook = new DayAccountBook(accountBookDTO.getMemo(), list);

        dayAccountBookMap.put(day, dayAccountBook);
        monthAccountBook.put(3, dayAccountBookMap);

        saveToFile();
    }


    private void saveToFile() {
        try (FileOutputStream fileOut = new FileOutputStream("C:\\Temp\\day_account_book.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(dayAccountBookMap);
            out.writeObject(monthAccountBook);
            System.out.println("등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지정된 위치의 일일 거래내역 가져오기
    // 저장위치 바뀌면 파라미터 값으로 전달 후 조절하면 될듯합니다
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

    public void getDayAccountBook(int number){
        getToFile();

        System.out.println("📅 " + number + "일 가계부");
        if(!(dayAccountBookMap.get(number) == null)) {
            for (int i = 0; i < dayAccountBookMap.get(number).getTransactionAccountBooks().size(); i++) {
                System.out.println(dayAccountBookMap.get(number).getTransactionAccountBooks().get(i).getMoney() +
                        ", " + dayAccountBookMap.get(number).getTransactionAccountBooks().get(i).getCreateDate() + ", " +
                        dayAccountBookMap.get(number).getTransactionAccountBooks().get(i).isBenefit());
            }
        System.out.println("메모내용 : " + dayAccountBookMap.get(number).getMemo());
        }

        else{
            dayAccountBookMap = new HashMap<>();
        }
    }

    public void getMonthAccountBook(){
        getToFile();
        System.out.println("📅 " + "3월 가계부");
        for (Map.Entry<Integer, Map<Integer, DayAccountBook>> entry : monthAccountBook.entrySet()) {
            Integer month = entry.getKey();
            Map<Integer, DayAccountBook> dailyAccountBook = entry.getValue();

            // 월별 값
            for (Map.Entry<Integer, DayAccountBook> dayEntry : dailyAccountBook.entrySet()) {
                Integer day = dayEntry.getKey();
                List<TransactionAccountBook> transactionAccountBooks = dayEntry.getValue().getTransactionAccountBooks();
                for(TransactionAccountBook transactionAccountBook : transactionAccountBooks){
                    System.out.println("Month: " + month +
                            ", Day: " + day +
                            ", money: " + transactionAccountBook.getMoney() +
                            ", benefit : " + transactionAccountBook.isBenefit());
                }
            }
        }
    }
}

