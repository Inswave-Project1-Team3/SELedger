package service;

import DTO.CreateAccountBookDTO;
import model.DayAccountBook;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.App.dayAccountBookMap;

public class AccountBookService {

    public void createAccountBook(CreateAccountBookDTO dto) {
        int day = LocalDateTime.now().getDayOfMonth();
        getToFile(); // 파일에서 데이터를 불러옴

        // 기존 데이터가 없을 경우를 대비하여 안전한 방식으로 가져오기
        List<DayAccountBook> list = dayAccountBookMap.getOrDefault(day, new ArrayList<>());

        // 새로운 데이터 추가
        list.add(new DayAccountBook(dto.isBenefitCheck(), dto.getPrice(), dto.getMemo()));

        // 맵 업데이트
        dayAccountBookMap.put(day, list);

        // 변경된 데이터를 다시 저장
        saveToFile();

        // 출력
        list = dayAccountBookMap.get(day);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(
                    list.get(i).getCreateDate() + ", " +
                    list.get(i).getMemo() + ", " +
                    list.get(i).isBenefitCheck() +", " +
                    list.get(i).getMoney());
        }
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
            dayAccountBookMap = new HashMap<>();
            return;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            // 데이터를 읽어온 후 null 체크
            Object obj = in.readObject();
            dayAccountBookMap = (obj instanceof Map) ? (Map<Integer, List<DayAccountBook>>) obj : new HashMap<>();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
