package org.example.jdbc;

import java.sql.*;

public class DBManager {
    private static DBManager instance;
    static final String DB_URL = "jdbc:mysql://localhost:3306/mapsa";
    static final String USER = "root";
    static final String PASS = "1q2w3e4r5";

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    //متد ایجاد ارتباط با دیتابیس
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    //متد ایجاد جداول در دیتابیس
    public void initializeTables(Statement stmt) throws SQLException {
        String dropTablesQuery = "DROP TABLE IF EXISTS book, member, reserve, member_detail;";
        stmt.executeUpdate(dropTablesQuery);
        System.out.println("All tables dropped by sql statement: " + dropTablesQuery);

        String createBookQuery = """
                CREATE TABLE book
                (
                    id          bigint auto_increment primary key,
                    title       varchar(255) not null,
                    author      varchar(255) not null,
                    public_date varchar(255) not null
                );""";
        stmt.executeUpdate(createBookQuery);
        System.out.println("Create book table by sql statement: " + createBookQuery);

        String createMemberQuery = """
                CREATE TABLE member
                (
                    id            bigint auto_increment primary key,
                    first_name    varchar(255) not null,
                    last_name     varchar(255) not null,
                    national_code varchar(255) not null,
                    birth_date     varchar(255) not null,
                    constraint UK_national_code unique (national_code)
                );""";
        stmt.executeUpdate(createMemberQuery);
        System.out.println("Create member table by sql statement: " + createMemberQuery);

        String createMemberDetailQuery = """
                CREATE TABLE member_detail
                (
                    id              bigint auto_increment primary key,
                    province        varchar(255) not null,
                    city            varchar(255) not null,
                    address         varchar(255) not null,
                    phone_number    varchar(255) not null,
                    mobile_number   varchar(255) not null,
                    membership_date varchar(255) not null,
                    member_id       bigint       not null,
                    constraint UK_mobile_number unique (mobile_number),
                    constraint FK_member_detail_to_member foreign key (member_id) references member (id)
                );""";
        stmt.executeUpdate(createMemberDetailQuery);
        System.out.println("Create member_detail table by sql statement: " + createMemberDetailQuery);

        String createReserveQuery = """
                CREATE TABLE reserve
                (
                    id           bigint auto_increment primary key,
                    book_id      bigint       not null,
                    member_id    bigint       not null,
                    reserve_date varchar(255) not null,
                    is_returned  bit          not null default 0,
                    constraint FK_reserve_to_book foreign key (book_id) references book (id),
                    constraint FK_reserve_to_member foreign key (member_id) references member (id)
                );""";
        stmt.executeUpdate(createReserveQuery);
        System.out.println("Create reserve table by sql statement: " + createReserveQuery);
    }

    //متد وارد کردن دیتاهای تستی در جداول ایجاد شده
    public void initializeTablesData(Statement stmt) throws SQLException {
        String insertMember1 = "INSERT INTO member (first_name, last_name, national_code, birth_date) VALUES ('Ali', 'Etemadi', '0017609854', '1375-05-08');";
        String insertMember2 = "INSERT INTO member (first_name, last_name, national_code, birth_date) VALUES ('Farshid', 'HosseinPour', '0012755320', '1370-03-23');";
        String insertMember3 = "INSERT INTO member (first_name, last_name, national_code, birth_date) VALUES ('Ehsan', 'Soheilian', '3920874351', '1373-05-22');";
        String insertMember4 = "INSERT INTO member (first_name, last_name, national_code, birth_date) VALUES ('Zahra', 'Akbari', '4560092345', '1380-09-12');";
        String insertMember5 = "INSERT INTO member (first_name, last_name, national_code, birth_date) VALUES ('Elham', 'Moradi', '8700165432', '1365-11-01');";

        stmt.executeUpdate(insertMember1);
        stmt.executeUpdate(insertMember2);
        stmt.executeUpdate(insertMember3);
        stmt.executeUpdate(insertMember4);
        stmt.executeUpdate(insertMember5);
        System.out.println("5 Member inserted.");

        String insertBook1 = "INSERT INTO book (title, author, public_date) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', '1925');";
        String insertBook2 = "INSERT INTO book (title, author, public_date) VALUES ('To Kill a Mockingbird', 'Harper Lee', '1960');";
        String insertBook3 = "INSERT INTO book (title, author, public_date) VALUES ('Nineteen Eighty-Four', 'George Orwell', '1949');";
        String insertBook4 = "INSERT INTO book (title, author, public_date) VALUES ('The Catcher in the Rye', 'J. D. Salinger', '1951');";
        String insertBook5 = "INSERT INTO book (title, author, public_date) VALUES ('One Hundred Years of Solitude', 'Gabriel Garcia Marquez', '1967');";
        String insertBook6 = "INSERT INTO book (title, author, public_date) VALUES ('Wuthering Heights', 'Emily Bronte', '1847');";
        String insertBook7 = "INSERT INTO book (title, author, public_date) VALUES ('The Brothers Karamazov', 'Fyodor Dostoevsky', '1879');";
        String insertBook8 = "INSERT INTO book (title, author, public_date) VALUES ('Lolita', 'Vladimir Nabokov', '1955');";
        String insertBook9 = "INSERT INTO book (title, author, public_date) VALUES ('Beloved', 'Toni Morrison', '1987');";
        String insertBook10 = "INSERT INTO book (title, author, public_date) VALUES ('Crime and Punishment', 'Fyodor Dostoevsky', '1866');";

        stmt.executeUpdate(insertBook1);
        stmt.executeUpdate(insertBook2);
        stmt.executeUpdate(insertBook3);
        stmt.executeUpdate(insertBook4);
        stmt.executeUpdate(insertBook5);
        stmt.executeUpdate(insertBook6);
        stmt.executeUpdate(insertBook7);
        stmt.executeUpdate(insertBook8);
        stmt.executeUpdate(insertBook9);
        stmt.executeUpdate(insertBook10);
        System.out.println("10 Books inserted.");

        String insertMemberDetail1 = "INSERT INTO member_detail (province, city, address, phone_number, mobile_number, membership_date, member_id) VALUES ('Tehran', 'Tehran', 'Narmak, Haft Hoz', '02187654321', '09120000000', '1400-12-01',(select id from member where national_code = '0012755320'));";
        stmt.executeUpdate(insertMemberDetail1);
        System.out.println("Insert Detail for member.");
    }
}
