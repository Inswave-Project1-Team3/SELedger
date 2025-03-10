package service;

import lombok.RequiredArgsConstructor;
import model.DayAccountBook;
import model.MonthAccountBook;
import model.TransactionAccountBook;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import DTO.AccountBookDTO;

@RequiredArgsConstructor
public class AccountBookService implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 월별 가계부 저장소 */
    private Map<YearMonth, MonthAccountBook> accountBookRecords = new HashMap<>();

    /**
     * 특정 월의 가계부 가져오기 (없으면 생성)
     * @param month 조회할 월
     * @return 해당 월의 가계부
     */
    private MonthAccountBook getOrCreateMonthAccountBook(YearMonth month) {
        return accountBookRecords.computeIfAbsent(month, MonthAccountBook::new);
    }

    /**
     * 거래 내역 추가
     * @param dto 거래 정보 DTO
     */
    public void addTransaction(AccountBookDTO dto) {
        MonthAccountBook monthBook = getOrCreateMonthAccountBook(YearMonth.from(dto.getDate()));
        TransactionAccountBook transaction = new TransactionAccountBook(0, false, null, null, null, null, false, 0);
        monthBook.addTransaction(dto.getDate(), transaction);
        saveMonthAccountBook(monthBook);
    }

    /**
     * 특정 날짜의 가계부 조회
     */
    public DayAccountBook getDailyAccountBookInfo(LocalDate date) {
        MonthAccountBook monthBook = accountBookRecords.get(YearMonth.from(date));
        return (monthBook != null) ? monthBook.getDailyAccountBookInfo(date) : null;
    }

    /**
     * 특정 날짜의 총 수입 반환
     */
    public double getTotalIncomeByDate(LocalDate date) {
        MonthAccountBook monthBook = accountBookRecords.get(YearMonth.from(date));
        return (monthBook != null) ? monthBook.getTotalIncomeByDate(date) : 0;
    }

    /**
     * 특정 날짜의 총 지출 반환
     */
    public double getTotalExpenseByDate(LocalDate date) {
        MonthAccountBook monthBook = accountBookRecords.get(YearMonth.from(date));
        return (monthBook != null) ? monthBook.getTotalExpenseByDate(date) : 0;
    }
    
    /**
     * 특정 월의 가계부 조회
     * @param month 조회할 월
     * @return 해당 월의 가계부 정보
     */
    public MonthAccountBook getMonthAccountBook(YearMonth month) {
        File file = getMonthAccountBookFile(month);
        if (!file.exists()) {
            return new MonthAccountBook(month);
        }
        return loadMonthAccountBook(month);
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

