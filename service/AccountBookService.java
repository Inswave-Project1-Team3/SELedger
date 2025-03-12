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
            getToFile(month, userNickName) :
            getToFile(month, visitUserNickname);

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

    /**
     * 저장된 가계부 데이터 불러오기
     */
    public  Map<Integer, DayAccountBook> getToFile(int month, String userNickName) {
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
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);

        return (monthAccountBook.containsKey(day)) ? monthAccountBook.get(day) : new DayAccountBook();
    }
  
    public GetMonthDataVO getMonthMoney(String userNickName){

        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName); // 월별 거래내역
        Map<Integer, DayMoney> daysMoney = new HashMap<>();             // key : 일수, DayMoney : 일일 총 수익 및 지출 
        Map<AccountCategory, Long> categoryMoneyCheck = new HashMap<>();     // 가장

        long income = 0;                    //일수별 수익
        long expense = 0;                   //일수별 지출
        long money;                     //가계부 건당 수익 및 지출비용
        AccountCategory category = null;    //월별 가장 많이 지출한 카테고리
        long maxCategoryMoney = 0;          //category 에서 사용한 금액
        long monthTotalMoney = 0;           //이번달 총 지출 민 수익내역// 돈을 많이 쓰는 카테고리를 반한하기 위한 map

        // 모든 일자의 데이터를 합산
        for(Entry<Integer, DayAccountBook> dayMoney : monthAccountBook.entrySet()){
            long dayIncome = 0;
            long dayExpense = 0;

            for(TransactionAccountBook transactionAccountBook : dayMoney.getValue().getTransactionAccountBooks()) {
                money = transactionAccountBook.getMoney();
                if(transactionAccountBook.isBenefit()){
                    dayIncome += money;
                    income += money;
                }
                else {
                    dayExpense += money;
                    expense += money;
                    category = transactionAccountBook.getAccountCategory();
                    categoryMoneyCheck.put(category, categoryMoneyCheck.getOrDefault(category, 0L) + money);
                }
            }
            
            // 각 일자별 수입/지출 정보 저장
            daysMoney.put(dayMoney.getKey(), new DayMoney(dayIncome, dayExpense));
        }
        
        // 월 전체 총액 계산
        monthTotalMoney = income - expense;
        
        // 카테고리별 지출 맵이 비어있지 않은 경우에만 최대값 계산
        if (!categoryMoneyCheck.isEmpty()) {
            Entry<AccountCategory, Long> maxEntry = Collections.max(categoryMoneyCheck.entrySet(), Entry.comparingByValue());
            category = maxEntry.getKey();
            maxCategoryMoney = maxEntry.getValue();
        }

        return new GetMonthDataVO(daysMoney, category, maxCategoryMoney, monthTotalMoney);
    }

    public void updateDayAccountBook(UpdateTransactionAccountBookDTO dto, int transactionNumber, int day){
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);
        DayAccountBook dayAccountBook= monthAccountBook.get(day);
        TransactionAccountBook transactionAccountBook = dayAccountBook.getTransactionAccountBooks().get(transactionNumber-1);
        transactionAccountBook.UpdateTransactionAccountBook(dto.isBenefit(), dto.getMoney(), dto.getAccountCategory());

        saveToFile(monthAccountBook, month, userNickName);
    }

    public void deleteDayAccountBook(int transactionNumber, int day){
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(month, userNickName);
        DayAccountBook dayAccountBook = monthAccountBook.get(day);
        TransactionAccountBook transactionAccountBook = dayAccountBook.getTransactionAccountBooks().remove(transactionNumber-1);

        saveToFile(monthAccountBook, month, userNickName);
    }
}



