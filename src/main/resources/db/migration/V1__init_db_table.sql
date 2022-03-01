BEGIN;
CREATE DATABASE IF NOT EXISTS bookOLX ;

USE bookOLX;


CREATE TABLE Authors (
                         id varchar(36) NOT NULL unique,
                         name VARCHAR(25),
                         surname VARCHAR (25),
                         birthDate DATE,
                         PRIMARY KEY (id)
);



CREATE TABLE books (
                       isbn varchar(20) NOT NULL UNIQUE,
                       title VARCHAR(100),
                       author varchar(36),
                       description TEXT,
                       quantity INT,
                       PRIMARY KEY (isbn),
                       FOREIGN KEY (author) REFERENCES Authors(id)
);

CREATE TABLE author_book_mtm (
                             id varchar(36) NOT NULL unique ,
                             book_isbn varchar(13) NOT NULL,
                             author_id varchar (36) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (author_id) REFERENCES Authors(id),
                             FOREIGN KEY (book_isbn) REFERENCES books(isbn)
);



CREATE TABLE Members (
                         userID varchar(36) NOT NULL,
                         username TEXT,
                         points INT,
                         emailAddress varchar(100),
                         primary key (userID)
);


CREATE TABLE BookMarket (
                            id VARCHAR(36) not null unique,
                            userID VARCHAR(36) not null,
                            bookID VARCHAR(36) not null,
                            bookState TEXT,
                            forSell boolean,
                            sellPrice double,
                            forRent boolean,
                            rentPrice double,
                            bookStatus text,
                            primary key(id),
                            foreign key (userID) references Members(userID),
                            foreign key (bookID) references books(isbn)
);

CREATE TABLE Transaction (
                            id bigint not null unique auto_increment,
                            marketBookId VARCHAR(36) not null unique,
                            memberIdFrom VARCHAR(36) not null,
                            memberIdTo VARCHAR(36) not null,
                            transactionType VARCHAR(10),
                            transactionDate date,
                            expectedReturnDate date,
                            primary key(id),
                            foreign key (memberIdFrom) references Members(userID),
                            foreign key (marketBookId) references BookMarket(id)
);


CREATE TABLE Rating (
                        ratingId INT not null unique AUTO_INCREMENT,
                        grade INT,
                        description VARCHAR(200),
                        leftBy VARCHAR(36),
                        userID VARCHAR(36),
                        bookID varchar(36),
                        primary key (ratingId),
                        foreign key (userID) references Members(userID)
);

CREATE TABLE Emails (
                        ID int AUTO_INCREMENT,
                        content varchar(150),
                        whensent DATE,
                        status varchar(20),
                        memberID varchar(36) NOT NULL,
                        primary key(ID),
                        foreign key (memberID) references Members(userID)
);
CREATE TABLE Notifications (
                               ID int AUTO_INCREMENT,
                               MARKETBOOKID varchar(36) not null,
                               TYPE varchar(20),
                               SENT tinyint default 0,
                               MEMBERID varchar(36) NOT NULL,
                               primary key (ID),
                               foreign key (MARKETBOOKID) references BookMarket(id),
                               foreign key (MEMBERID) references Members(MEMBERUSERID)
);

CREATE TABLE EmailTemplates (
                                ID int not null AUTO_INCREMENT,
                                templateName varchar (50),
                                subject varchar (100),
                                contentBody varchar (300),
                                primary key(ID)
);


