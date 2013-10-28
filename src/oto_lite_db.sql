/*conventions: always use lower_case for table name for os compatibility purpose*/
create database oto_lite;
use oto_lite;

/*order category*/
drop table if exists order_categories;
create table order_categories(
	categoryID BIGINT not null auto_increment unique,
	uploadID BIGINT not null,
	categoryName varchar(100),
	baseTerm varchar(100),
	primary key (categoryID)
) ENGINE=INNODB;

insert into order_categories (categoryID, uploadID, categoryName) values 
(1, 1, 'Color'),
(2, 1, 'Shape'),
(3, 1, 'Pubescence'),
(4, 1, 'Orientation');

/*base terms in an order category*/
drop table if exists terms_in_order_category;
create table terms_in_order_category(
	termID BIGINT not null auto_increment unique,
	categoryID BIGINT not null,
	termName varchar(100),
	primary key (termID)
) ENGINE=INNODB;

insert into terms_in_order_category (categoryID, termName) values 
(1, 'pink'),
(1, 'white'),
(1, 'red'),
(1, 'green'),
(1, 'blue'),
(1, 'purple'),
(1, 'black'),
(1, 'yellow'),
(2, 'cylindric'),
(2, 'ovoid'),
(2, 'hemispheric'),
(2, 'flat'),
(2, 'convex'),
(2, 'conic'),
(2, 'columnar'),
(2, 'ovate'),
(2, 'lanceolate'),
(2, 'linear'),
(3, 'papillate'),
(3, 'hirsute'),
(3, 'glabrous'),
(3, 'hairy'),
(3, 'bald'),
(3, 'balding'),
(3, 'barbate'),
(3, 'bearded'),
(3, 'bristly'),
(3, 'glabrous'),
(4, 'flat'),
(4, 'erect'),
(4, 'prostrate'),
(4, 'ascending'),
(4, 'spreading'),
(4, 'reflexed'),
(4, 'appressed'),
(4, 'deflexed');

/*orders in an order category*/
drop table if exists orders_in_order_category;
create table orders_in_order_category (
	orderID BIGINT not null auto_increment unique,
	categoryID BIGINT not null, 
	orderName varchar(200),
	orderDescription varchar(500) default '',
	primary key (orderID)
) ENGINE=INNODB;

insert into orders_in_order_category(orderID, categoryID, orderName, orderDescription) 
values
(1, 1, 'Color Order', 'Default Order of Color'),
(2, 2, 'Pubescence-Density Order', 'description of Pubescence-Density Order'),
(3, 3, 'Shape Order', 'explanation of Shape Order'),
(4, 4, 'Shape Order', 'explanation of Orientation Order'),
(5, 1, 'Color Order #2', 'for test only')
;

/*term's position in an order*/
drop table if exists term_position_in_order;
create table term_position_in_order(
	ID BIGINT not null auto_increment unique,
	orderID BIGINT not null,
	termName varchar(100) not null,
	position int not null,
	primary key (ID)
) ENGINE=INNODB;


/*to ontologies page*/
/*table: term_category_pair*/
drop table if exists term_category_pair;
create table term_category_pair (
	ID BIGINT not null auto_increment unique,
	term varchar(100) not null,
	category varchar(100) not null,
	synonyms varchar(200) default '', 
	uploadID BIGINT not null,
	removed boolean default false,
	primary key (ID), 
	index uploadIDIndex (uploadID)
) ENGINE=INNODB;

/*table: ontology_matches*/
drop table if exists ontology_matches;
create table ontology_matches(
	ID BIGINT not null auto_increment unique,
	term varchar(100) not null,
	ontologyID varchar(100) not null,
	permanentID varchar(100) not null,
	parentTerm varchar(100),
	definition text,
	
	primary key (ID)
) ENGINE=INNODB;

/*table: ontology_matches*/
drop table if exists ontology_submissions;
create table ontology_submissions(
	ID BIGINT not null auto_increment unique,
	term varchar(100) not null,
	category varchar(100) not null,
	ontologyID varchar(100) not null,
	submittedBy varchar(50),
	localID varchar(50),
	tmpID varchar(100) not null,
	permanentID varchar(100),
	superClassID varchar(100) not null,
	synonyms varchar(500),
	definition text not null,
	source varchar(100) not null,
	sampleSentence varchar(500) not null,
	accepted boolean default false,
	
	primary key (ID)
) ENGINE=INNODB;

/*test data for to ontology page*/
insert into term_category_pair (ID, term, category, synonyms, uploadID, removed)
values 
(1, 'petal', 'structure', '', 1, false),
(2, 'stem', 'structure', '', 1, false),
(3, 'flower', 'structure', '', 1, false),
(4, 'red', 'color', 'pink, reddish', 1, false),
(5, 'blue', 'color', '', 1, false),
(6, 'round', 'shape', '', 1, false),
(7, 'round', 'shape-2', '', 1, false);

insert into ontology_matches(term, ontologyID, permanentID, parentTerm, definition) 
values 
('red', 'PATO', 'http://PATO:red0001', 'parent of red', 'the color is red'),
('red', 'PATO', 'http://PATO:red0002', 'parent #2 of red', 'the color is still red');


insert into ontology_submissions(term, category, ontologyID, submittedBy, localID, 
tmpID, superClassID, definition, source, sampleSentence)
values 
('red', 'color', 'PATO', 'Fengqiong Huang', "local uuid", 'PATO:tmp_red0001', 'Color', 
'a new submission of color red', 'TEST Volume 1', 'sample sentence of red');

/*add glossaryType field in table uploads*/
alter table uploads add glossaryType int default 1;
alter table uploads add bioportalUserId varchar(50);
alter table uploads add bioportalApiKey varchar(100);
alter table uploads add EtcUser varchar(100);
alter table uploads add source varchar(100);

/*selected match - glossary global*/
drop table if exists selected_ontology_records;
create table selected_ontology_records (
	ID BIGINT not null auto_increment unique,
	term varchar(100) not null,
	category varchar(100) not null,
	glossaryType int not null,
	recordType int not null,
	recordID BIGINT not null,
	
	primary key (ID)
) ENGINE=InnoDB;

insert into selected_ontology_records
(term, category, glossaryType, recordType, recordID)
values ('red', 'color', 1, 1, 1);


/*heirarchy page*/
drop table if exists structures;
create table structures (
	ID BIGINT not null auto_increment unique,
	uploadID BIGINT not null,
	term varchar(100) not null,
	isMannuallyCreated boolean default false,

	primary key (ID)
) ENGINE=InnoDB;

drop table if exists trees;
create table trees (
	ID BIGINT not null auto_increment unique,
	uploadID BIGINT not null,
	termID BIGINT not null,
	pID BIGINT,	

	primary key (ID)
) ENGINE=InnoDB;