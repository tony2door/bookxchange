package com.bookxchange.repositories;

import com.bookxchange.dto.MarketBookDto;
import com.bookxchange.enums.BookState;
import com.bookxchange.enums.BookStatus;
import com.bookxchange.model.MarketBook;
import utils.JdbcConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MarketBookRepo {

    public List<MarketBookDto> getAllMarketBook() throws SQLException, IOException {

        String sql = "SELECT books.title," + " Authors.name, Authors.surname, BookMarket.forSell, BookMarket.sellPrice, BookMarket.forRent, BookMarket.rentPrice, BookMarket.bookState, BookMarket.bookStatus, Members.username FROM BookMarket JOIN Members ON Members.userID = BookMarket.userID JOIN books ON books.isbn = BookMarket.bookID JOIN  Authors ON Authors.id = books.author";
        List<MarketBookDto> marketBooks = new ArrayList<>();
        try (Connection con = JdbcConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    marketBooks.add(new MarketBookDto(rs.getString("books.title"), rs.getString("Authors.name"), rs.getString("Authors.surname"), rs.getBoolean("BookMarket.forSell"), rs.getDouble("BookMarket.sellPrice"), rs.getBoolean("BookMarket.forRent"), rs.getDouble("BookMarket.rentPrice"), BookState.valueOf(rs.getString("BookMarket.bookState")), BookStatus.valueOf(rs.getString("BookMarket.bookStatus")), rs.getString("Members.username")));
                }
            }
        }
        return marketBooks;
    }

    public List<MarketBookDto> getAllMarketBookByBookStatus(BookStatus bookStatus) throws SQLException {
        String sql = "SELECT books.title, Authors.name, Authors.surname, BookMarket.forSell, BookMarket.sellPrice, BookMarket.forRent, BookMarket.rentPrice, BookMarket.bookState, BookMarket.bookStatus, Members.username FROM\n" + "    BookMarket\n" + "        JOIN\n" + "    Members ON Members.userID = BookMarket.userID\n" + "        JOIN\n" + "    books ON books.isbn = BookMarket.bookID\n" + "        JOIN\n" + "    Authors ON Authors.id = books.author\n" + "WHERE\n" + "\tBookMarket.bookStatus ='" + bookStatus + "'";
        List<MarketBookDto> marketBooks = new ArrayList<>();
        try (Connection con = JdbcConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    marketBooks.add(new MarketBookDto(rs.getString("books.title"), rs.getString("Authors.name"), rs.getString("Authors.surname"), rs.getBoolean("BookMarket.forSell"), rs.getDouble("BookMarket.sellPrice"), rs.getBoolean("BookMarket.forRent"), rs.getDouble("BookMarket.rentPrice"), BookState.valueOf(rs.getString("BookMarket.bookState")), BookStatus.valueOf(rs.getString("BookMarket.bookStatus")), rs.getString("Members.username")));
                }
            }
        }
        return marketBooks;
    }

    public MarketBookDto getAllMarketBookById(UUID id) throws SQLException {
        String sql = "SELECT books.title, Authors.name, Authors.surname, BookMarket.forSell, BookMarket.sellPrice, BookMarket.forRent, BookMarket.rentPrice, BookMarket.bookState, BookMarket.bookStatus, Members.username FROM\n" + "    BookMarket\n" + "        JOIN\n" + "    Members ON Members.userID = BookMarket.userID\n" + "        JOIN\n" + "    books ON books.isbn = BookMarket.bookID\n" + "        JOIN\n" + "    Authors ON Authors.id = books.author\n" + "WHERE\n" + "\tBookMarket.id ='" + id + "'";
        MarketBookDto marketBookDto = null;
        try (Connection con = JdbcConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    marketBookDto = new MarketBookDto(rs.getString("books.title"), rs.getString("Authors.name"), rs.getString("Authors.surname"), rs.getBoolean("BookMarket.forSell"), rs.getDouble("BookMarket.sellPrice"), rs.getBoolean("BookMarket.forRent"), rs.getDouble("BookMarket.rentPrice"), BookState.valueOf(rs.getString("BookMarket.bookState")), BookStatus.valueOf(rs.getString("BookMarket.bookStatus")), rs.getString("Members.username"));
                }
            }
        }
        return marketBookDto;
    }

    public void changeBookStatusInDb(MarketBook marketBook) {
//        System.out.println("book" + marketBook.getId().toString());
        String sql;
        if(marketBook.isForRent()){
             sql = String.format("UPDATE BookMarket SET bookStatus = \"RENTED\" WHERE id = '%s' ", marketBook.getId().toString());
        }else {
            sql = String.format("UPDATE BookMarket SET bookStatus = \"SOLD\" WHERE id = '%s' ", marketBook.getId().toString());
        }
        try (Connection con = JdbcConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MarketBook getMarketBook(UUID id) throws SQLException, IOException {

        String sql = "SELECT * FROM book_market WHERE\n" + "\tbook_market_id ='" + id + "'";

        MarketBook marketBook = new MarketBook();

        try (Connection con = JdbcConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    marketBook.setId(UUID.fromString(rs.getString("book_market_id")));
                    marketBook.setUserId(UUID.fromString(rs.getString("user_id")));
                    marketBook.setBookId(rs.getString("book_id"));
                    marketBook.setBookState(BookState.valueOf(rs.getString("book_state")));
                    marketBook.setForSell(rs.getBoolean("for_sell"));
                    marketBook.setSellPrice(rs.getDouble("sell_price"));
                    marketBook.setForRent(rs.getBoolean("for_rent"));
                    marketBook.setRentPrice(rs.getDouble("rent_price"));
                    marketBook.setBookStatus(BookStatus.valueOf(rs.getString("book_status")));
                }
            }
        }
        return marketBook;
    }
}
