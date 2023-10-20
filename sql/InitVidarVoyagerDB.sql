CREATE EXTENSION pgcrypto;

CREATE TABLE vidar_voyager.users
(
    id serial NOT NULL,
    username varchar(32) NOT NULL UNIQUE,
    first_name varchar(32) NOT NULL,
	last_name varchar(32) NOT NULL,
    middle_initial varchar(32),
    date_of_birth date,
    email varchar(32) NOT NULL UNIQUE,
	created_on time with time zone NOT NULL,
	updated_on time with time zone NOT NULL,
	deleted bit,
    PRIMARY KEY (id)
);

CREATE TABLE vidar_voyager.accounts
(
	id serial NOT NULL,
    user_id bigInt NOT NULL UNIQUE,
    password text NOT NULL,
	deleted bit,
	locked bit,
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id),
	PRIMARY KEY (id)
);

CREATE TABLE vidar_voyager.login_events
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	ip_address inet,
	logged_in_on time with time zone NOT NULL,
	logged_out_on time with time zone NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);

CREATE TABLE vidar_voyager.login_attempt
(
	id serial NOT NULL,
	user_id bigInt,
	ip_address inet,
	login_attempt_on time with time zone NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);

CREATE TABLE vidar_voyager.flagged_inets
(
	id serial NOT NULL,
	ip_address inet,
	PRIMARY KEY (id),
);

CREATE TABLE vidar_voyager.high_scores_minesweeper
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	seconds INTEGER NOT NULL,
	created_on time with time zone NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);
