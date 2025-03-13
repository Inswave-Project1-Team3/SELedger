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

public class AccountBookService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String USER_DATA_FOLDER = "data";

    // 선택한 날의 가계부 생성
    public void createAccountBook(CreateAccountBookDTO accountBookDTO,
                                  CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                  int day) {

        // 월별로 저장된 값을 가져오기
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);

        // 가져온 값에서 추가하여야 할 리스트 가져오기
        List<TransactionAccountBook> list = (monthAccountBook.containsKey(day)) ?
                monthAccountBook.get(day).getTransactionAccountBooks() :
                new ArrayList<>();

        // 리스트에 추가할 값 생성
        TransactionAccountBook transactionAccountBook = new TransactionAccountBook(
                transactionAccountBookDTO.isBenefit(),
                transactionAccountBookDTO.getMoney(),
                transactionAccountBookDTO.getAccountCategory());

        // 리스트에 추가
        list.add(transactionAccountBook);


        // 기존 DayAccountBook 객체가 존재하면 수정하고, 없으면 새로 생성
        DayAccountBook dayAccountBook = monthAccountBook.getOrDefault(day, new DayAccountBook(accountBookDTO.getMemo(), list));

        // 리스트에 추가된 값을 넣은 객체를 새로생성
        //DayAccountBook dayAccountBook = new DayAccountBook(accountBookDTO.getMemo(), list);

        // 새로 생성된 값을 넣음
        monthAccountBook.put(day, dayAccountBook);

        // 저장
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

        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);    // 월별 거래내역
        Map<Integer, DayMoney> daysMoney = new HashMap<>();                         // key : 일수, DayMoney : 일일 총 수익 및 지출
        Map<AccountCategory, Long> categoryMoneyCheck = new HashMap<>();            // 지출이 가장 많은 카테고리 및 금액

        long money;                     //가계부 건당 수익 및 지출비용
        AccountCategory category = null;    //월별 가장 많이 지출한 카테고리
        long maxCategoryMoney = 0;          //category 에서 사용한 금액
        long monthTotalMoney = 0;           //이번달 총 지출 민 수익내역// 돈을 많이 쓰는 카테고리를 반한하기 위한 map

        for(Entry<Integer, DayAccountBook> dayMoney : monthAccountBook.entrySet()){
            long dailyIncome = 0;            //일수별 수익
            long dailyexpense = 0;          //일수별 지출

            for(TransactionAccountBook transactionAccountBook : dayMoney.getValue().getTransactionAccountBooks()) {
                money = transactionAccountBook.getMoney();
                if(transactionAccountBook.isBenefit()){
                    dailyIncome += money;
                }
                else {
                    dailyexpense += money;
                    category = transactionAccountBook.getAccountCategory();
                    categoryMoneyCheck.put(category, categoryMoneyCheck.getOrDefault(category, 0L)  + money);
                }
                daysMoney.put(dayMoney.getKey(), new DayMoney(dailyIncome, dailyexpense));
            }
            monthTotalMoney += dailyIncome - dailyexpense;

            if (!categoryMoneyCheck.isEmpty()) {
                Entry<AccountCategory, Long> maxEntry = Collections.max(categoryMoneyCheck.entrySet(), Entry.comparingByValue());
                category = maxEntry.getKey();
                maxCategoryMoney = maxEntry.getValue();
            }
        }
        return new GetMonthDataVO(daysMoney, category, maxCategoryMoney, monthTotalMoney);
    }

    // 가계부 수정
    public void updateDayAccountBook(UpdateTransactionAccountBookDTO dto, int transactionNumber, int day) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);
        Optional.ofNullable(monthAccountBook.get(day))
                .map(DayAccountBook::getTransactionAccountBooks)
                .filter(list -> transactionNumber - 1 < list.size())
                .ifPresent(list -> list.get(transactionNumber - 1)
                        .UpdateTransactionAccountBook(dto.isBenefit(), dto.getMoney(), dto.getAccountCategory()));
        saveToFile(monthAccountBook);
    }

    // 가계부 삭제
    public void deleteDayAccountBook(int transactionNumber, int day) {
        Map<Integer, DayAccountBook> monthAccountBook = getToFile(userNickName);
        Optional.ofNullable(monthAccountBook.get(day))
                .map(DayAccountBook::getTransactionAccountBooks)
                .filter(list -> transactionNumber - 1 < list.size())
                .ifPresent(list -> list.remove(transactionNumber - 1));
        saveToFile(monthAccountBook);
    }
}


