package view;

public class AccountBookPage {
    public void categoryView(boolean benefitCheck) {
        System.out.println("카테고리");
        if(benefitCheck) {
            System.out.println("SALARY : 월급");
            System.out.println("BONUS : 보너스");
            System.out.println("INVESTMENT : 투자수익");
            System.out.println("ALLOWANCE : 용돈");
        } else {
            System.out.println("FOOD : 식비");
            System.out.println("TRANSPORT : 교통비");
            System.out.println("ENTERTAINMENT : 문화생활");
            System.out.println("DAILY_NECESSITIES : 생필품");
            System.out.println("CLOTHING : 의류");
        }
    }

    public void addAccount(){
        System.out.println("아래의 값을 순서대로 입력해주세요");
        System.out.println("1. 가격");
        System.out.println("2. 메모내용");
    }
}
