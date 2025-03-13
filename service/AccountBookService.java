package service;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import DTO.UpdateTransactionAccountBookDTO;
import DTO.VO.GetMonthDataVO;
import model.*;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import static app.App.*;

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
                                  int day) {

        Map<Integer, DayAccountBook> monthAccountBook = (visitUserNickname.isEmpty()) ?
            getToFile(userNickName) :
            getToFile(visitUserNickname);

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

        saveToFile(monthAccountBook);
    }

    private void saveToFile (Map<Integer, DayAccountBook> monthAccountBook) {
        // 저장할 경로 설정
        String directoryPath = USER_DATA_FOLDER + File.separator + userNickName + File.separator + "calendar";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // 상위 디렉터리까지 포함해 전체 생성
        }

        try (FileOutputStream fileOut = new FileOutputStream(directoryPath + File.separator + month + ".ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(monthAccountBook);
            System.out.println("등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지정된 위치의 거래내역 가져오기
    public  Map<Integer, DayAccountBook> getToFile(String userNickName) {
        File file = new File(USER_DATA_FOLDER + File.separator + userNickName + File.separator + "calendar" + File.separator + month + ".ser");
        Map<Integer, DayAccountBook> monthAccountBook = new HashMap<>();

        if (file.exists()) {
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

    public DayAccountBook getDayAccountBook(int day, String userNickName) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);

        return (monthAccountBook.containsKey(day)) ? monthAccountBook.get(day) : new DayAccountBook();
    }
  
    public GetMonthDataVO getMonthMoney(String userNickName){

        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName); // 월별 거래내역
        Map<Integer, DayMoney> daysMoney = new HashMap<>();             // key : 일수, DayMoney : 일일 총 수익 및 지출 
        Map<AccountCategory, Long> categoryMoneyCheck = new HashMap<>();     // 가장

        long income = 0;                    // 월별 총 수익
        long expense = 0;                   // 월별 총 지출
        long money;                     //가계부 건당 수익 및 지출비용
        AccountCategory category = null;    //월별 가장 많이 지출한 카테고리
        long maxCategoryMoney = 0;          //category 에서 사용한 금액
        long monthTotalMoney = 0;           //이번달 총 지출 민 수익내역// 돈을 많이 쓰는 카테고리를 반한하기 위한 map

        // 모든 일자의 데이터를 합산
        for(Entry<Integer, DayAccountBook> dayMoney : monthAccountBook.entrySet()){
            long dailyIncome= 0;            //일수별 수익
            long dailyExpense = 0;          //일수별 지출

            for(TransactionAccountBook transactionAccountBook : dayMoney.getValue().getTransactionAccountBooks()) {
                money = transactionAccountBook.getMoney();
                if(transactionAccountBook.isBenefit()){
                    dailyIncome += money;
                    income += money;
                }
                else {
                    dailyExpense += money;
                    expense += money;
                    category = transactionAccountBook.getAccountCategory();
                    categoryMoneyCheck.put(category, categoryMoneyCheck.getOrDefault(category, 0L) + money);
                }
            }
          
            monthTotalMoney = income - expense;

            if (!categoryMoneyCheck.isEmpty()) {
                Entry<AccountCategory, Long> maxEntry = Collections.max(categoryMoneyCheck.entrySet(), Entry.comparingByValue());
                category = maxEntry.getKey();
                maxCategoryMoney = maxEntry.getValue();
            }
            daysMoney.put(dayMoney.getKey(), new DayMoney(dailyIncome, dailyExpense));
        }
        return new GetMonthDataVO(daysMoney, category, maxCategoryMoney, monthTotalMoney);
    }

    public void updateDayAccountBook(UpdateTransactionAccountBookDTO dto, int transactionNumber, int day) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);
        Optional.ofNullable(monthAccountBook.get(day))
                .map(DayAccountBook::getTransactionAccountBooks)
                .filter(list -> transactionNumber - 1 < list.size())
                .ifPresent(list -> list.get(transactionNumber - 1)
                        .UpdateTransactionAccountBook(dto.isBenefit(), dto.getMoney(), dto.getAccountCategory()));
        saveToFile(monthAccountBook);
    }

    public void deleteDayAccountBook(int transactionNumber, int day) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);
        Optional.ofNullable(monthAccountBook.get(day))
                .map(DayAccountBook::getTransactionAccountBooks)
                .filter(list -> transactionNumber - 1 < list.size())
                .ifPresent(list -> list.remove(transactionNumber - 1));
        saveToFile(monthAccountBook);
    }
}



