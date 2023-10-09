package org.example;

import org.example.entity.Book;
import org.example.entity.Member;

public class Main {
    public static void main(String[] args) {
        //ایجاد شی کلاس مدیریت کتابخانه به همراه ایجاد ارتباط با دیتابیس
        LibraryManager lm = new LibraryManager();

        //ایجاد جداول و مقادیر اولیه
        lm.initialize();

        //پرینت کلیه اطلاعات کاربر
        lm.printMemberDataWithNationalCode("0012755320");


        Member member = lm.searchMemberByNationalCode("0012755320");
        Book book = lm.searchBookByTitle("The Brothers Karamazov");
        //تخصیص دادن کتاب فوق به کاربر فوق
        boolean check = lm.lendingBook(member.getId(), book.getId());

        System.out.println("The book: " + book.getTitle() + " lend to Member: " + member.getFirstName() + " " + member.getLastName() + " with result: " + check);

        //پرینت مجدد کلیه اطلاعات کاربر
        lm.printMemberDataWithNationalCode("0012755320");

        //جستجوی وضعیت امانت کتاب
        String bookName = "The Brothers Karamazov";
        Member searchedMember = lm.searchLendBook(bookName);
        if (searchedMember.getId() != null) {
            System.out.println("The book: " + book + " lend to member: " + searchedMember);
        } else {
            System.out.println("The book: " + book + " lend to no one!");
        }

        //قطع اتصال ایجاد شده به دیتابیس
        lm.closeConnection();
    }
}