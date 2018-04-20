create database if not exists festivals_project;
use festivals_project;

create table Festival(
 festID varchar(36) primary key,
 user_name varchar(50),
 location varchar(50),
 production_comp bool,
 start_date date,
 end_date date,
 price decimal(9,2)
);

create table Users(
 userID         varchar(36) primary key,
 username       varchar(50),
 birthdate      date,
 user_location  varchar(50) references Location(city, state, streetAddress),
 is_company     bool
);

create table Friends(
  user1         varchar(36) references Users(userID),
  user2         varchar(36) references Users(userID),
  primary key (user1, user2)
);

create table Bookmarks(
  userID       varchar(36) references Users(userID),
  festID       varchar(36) references Festival(festID),
  primary key (userID, festID)
);

create table Location(
 city           varchar(20),
 state          varchar(20),
 streetAddress  varchar(50),
 address        varchar(20),
 primary key (city, state, streetaddress)
);

create table Providers(
  festID       varchar(36) references Festival(festID),
  name         varchar(50),
  primary key (festID, name)
);

create table Music(
  festID       varchar(36) NOT NULL,
  musicians    varchar(20) references Providers(festID, name),
  type_fest    char(10),
  genre        varchar(20),
  outdoor      bool,
  camping      bool,
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check_music before insert on Music
  for each row set NEW.type_fest = 'Music'
;

create table Comedy(
  festID       varchar(36) NOT NULL,
  type_fest    char(10),
  comedians    varchar(50)  references Providers(festID, name),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check_comedy before insert on Comedy
  for each row set NEW.type_fest = 'Comedy'
;

create table Art(
  festID       varchar(36) NOT NULL,
  type_fest    char(10),
  artist       varchar(20) references Providers(festID, name),
  genre        varchar(20),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check_art before insert on Art
  for each row set NEW.type_fest = 'Art'
;

create table Beer(
  festID       varchar(36) NOT NULL,
  type_fest    char(10),
  breweries    varchar(50) references Providers(festID, name),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check before insert on Beer
  for each row set NEW.type_fest = 'Beer'
;