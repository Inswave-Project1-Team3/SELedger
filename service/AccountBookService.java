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

}

