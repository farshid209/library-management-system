package org.example;

import org.example.entity.Book;
import org.example.entity.Member;
import org.example.entity.MemberDetail;
import org.example.entity.Reserve;
import org.example.jdbc.DBManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private DBManager dbManager;
    private Connection connection;
    private Statement statement;

    //ایجاد ارتباط با دیتابیس همزمان با ایجاد شی از این کلاس
    public LibraryManager() {
        dbManager = DBManager.getInstance();
        try {
            connection = dbManager.getConnection();
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // جهت ایجاد جداول و مقدار دهی اولیه
    public void initialize() {
        try {
            dbManager.initializeTables(statement);
            dbManager.initializeTablesData(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //جهت بستن ارتباط با دیتابیس
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // چاپ کلیه اطلاعات کاربر
    public void printMemberDataWithNationalCode(String nationalCode) {
        String getMemberQuery = "select * from member where national_code = '" + nationalCode + "';";
        ResultSet resultSet = null;
        Member member = null;
        try {
            resultSet = statement.executeQuery(getMemberQuery);
            if (resultSet.next()) {
                member = new Member();
                member.setId(resultSet.getLong("id"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setNationalCode(resultSet.getString("national_code"));
                member.setBirthDate(resultSet.getString("birth_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (member != null) {
            String getMemberDetailQuery = "select * from member_detail where member_id = " + member.getId() + ";";
            ResultSet resultSet2 = null;
            MemberDetail memberDetail = null;
            try {
                resultSet2 = statement.executeQuery(getMemberDetailQuery);
                if (resultSet2.next()) {
                    memberDetail = new MemberDetail();
                    memberDetail.setId(resultSet2.getLong("id"));
                    memberDetail.setMemberId(resultSet2.getLong("member_id"));
                    memberDetail.setProvince(resultSet2.getString("province"));
                    memberDetail.setCity(resultSet2.getString("city"));
                    memberDetail.setAddress(resultSet2.getString("address"));
                    memberDetail.setPhoneNumber(resultSet2.getString("phone_number"));
                    memberDetail.setMobileNumber(resultSet2.getString("mobile_number"));
                    memberDetail.setMembershipDate(resultSet2.getString("membership_date"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String getReserveQuery = "select * from reserve where member_id = " + member.getId() + " and is_returned = 0;";
            ResultSet resultSet3 = null;
            List<Reserve> reserveList = new ArrayList<>();
            try {
                resultSet3 = statement.executeQuery(getReserveQuery);
                while (resultSet3.next()) {
                    Reserve reserve = new Reserve();
                    reserve.setId(resultSet3.getLong("id"));
                    reserve.setBookId(resultSet3.getLong("book_id"));
                    reserve.setMemberId(resultSet3.getLong("member_id"));
                    reserve.setReserveDate(resultSet3.getString("reserve_date"));
                    reserve.setReturned(resultSet3.getBoolean("is_returned"));

                    reserveList.add(reserve);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (!reserveList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < reserveList.size(); i++) {
                    sb.append(reserveList.get(i).getBookId());
                    if (i < reserveList.size() - 1) {
                        sb.append(",");
                    }
                }

                String getBookQuery = "select * from book where id in (" + sb.toString() + ");";
                ResultSet resultSet4 = null;
                List<Book> bookList = new ArrayList<>();
                try {
                    resultSet4 = statement.executeQuery(getBookQuery);
                    while (resultSet4.next()) {
                        Book book = new Book();
                        book.setId(resultSet4.getLong("id"));
                        book.setTitle(resultSet4.getString("title"));
                        book.setAuthor(resultSet4.getString("author"));
                        book.setPublicDate(resultSet4.getString("public_date"));

                        bookList.add(book);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Member: " + member + " " + memberDetail + " loan books: " + bookList);
            } else {
                System.out.println("Member: " + member + " " + memberDetail + " with no books lend!");
            }
        } else {
            System.out.println("There is no such a member with this national code: " + nationalCode);
        }
    }

    //جستجوی عضو با استفاده از اسم کتاب در اختیارش
    public Member searchLendBook(String bookName) {
        String query = "SELECT * FROM member WHERE id = (select member_id as id from reserve where book_id = (select id from book where title = '" + bookName + "') and is_returned = 0);";
        ResultSet resultSet = null;
        Member member = new Member();
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                member.setId(resultSet.getLong("id"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setNationalCode(resultSet.getString("national_code"));
                member.setBirthDate(resultSet.getString("birth_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return member;
    }

    // تخصیص کتاب به عضو کتابخانه
    public boolean lendingBook(Long memberId, Long bookId) {
        String readQuery = "select * from reserve where book_id = " + bookId + " and is_returned = 0;";
        String query = "INSERT INTO reserve (book_id, member_id, reserve_date, is_returned) VALUES (" + bookId + ", " + memberId + ", now(), false);";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            int count = resultSet.getRow();
            if (count == 0) {
                statement.executeUpdate(query);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //جستجوی عضو با استفاده از کدملی
    public Member searchMemberByNationalCode(String nationalCode) {
        String query = "select * from member where national_code = '" + nationalCode + "';";
        ResultSet resultSet = null;
        Member member = new Member();
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                member.setId(resultSet.getLong("id"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setNationalCode(resultSet.getString("national_code"));
                member.setBirthDate(resultSet.getString("birth_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return member;
    }

    //جستجوی کتاب با استفاده از اسمش
    public Book searchBookByTitle(String bookName) {
        String query = "select * from book where title = '" + bookName + "';";
        ResultSet resultSet = null;
        Book book = new Book();
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublicDate(resultSet.getString("public_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
}
