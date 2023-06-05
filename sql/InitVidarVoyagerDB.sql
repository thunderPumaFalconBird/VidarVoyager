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
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id),
	PRIMARY KEY (id)
);

CREATE TABLE vidar_voyager.login_events
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	logged_in_on time with time zone NOT NULL,
	logged_out_on time with time zone NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);

CREATE TABLE vidar_voyager.inventory
(
	id serial NOT NULL,
	object1 varchar(32),
	object2 varchar(32),
	object3 varchar(32),
	object4 varchar(32),
	object5 varchar(32),
	PRIMARY KEY (id)
);


CREATE TABLE vidar_voyager.game_states_rescue_mission
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	level_number INTEGER NOT NULL,
	player_position_x INTEGER NOT NULL,
	player_position_y INTEGER NOT NULL,
	oxygen_level float NOT NULL,
	inventory_id bigInt NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id),
	FOREIGN KEY(INVENTORY_ID) REFERENCES vidar_voyager.inventory(id)
);

CREATE TABLE vidar_voyager.doors
(
	id serial NOT NULL,
	door_position_x INTEGER NOT NULL,
	door_position_y INTEGER NOT NULL,
	puzzle_type varchar(32) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE vidar_voyager.open_doors
(
	game_state_id bigInt NOT NULL,
	door_id bigInt NOT NULL,
	FOREIGN KEY(game_state_id) REFERENCES vidar_voyager.game_state_rescue_mission(id),
	FOREIGN KEY(door_id) REFERENCES vidar_voyager.doors(id)
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

CREATE TABLE vidar_voyager.high_scores_madplanets
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	points INTEGER NOT NULL,
	level_NUMBER INTEGER NOT NULL,
	created_on time with time zone NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);

CREATE TABLE vidar_voyager.game_states_madplanets
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	level_number INTEGER NOT NULL,
	player_position_x INTEGER NOT NULL,
	player_position_y INTEGER NOT NULL,
	num_of_ships INTEGER NOT NULL,
	points INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);

CREATE TABLE vidar_voyager.planets
(
	id serial NOT NULL,
	type varchar(32) NOT NULL,
	num_of_moons INTEGER NOT NULL,
	position_x INTEGER NOT NULL,
	position_y INTEGER NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE vidar_voyager.active_planets
(
	game_state_id bigInt NOT NULL,
	planet_id bigInt NOT NULL,
	FOREIGN KEY(game_state_id) REFERENCES vidar_voyager.game_state_madplanets(id),
	FOREIGN KEY(planet_id) REFERENCES vidar_voyager.planets(id)
);

CREATE TABLE vidar_voyager.moons
(
	id serial NOT NULL,
	position_x INTEGER NOT NULL,
	position_y INTEGER NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE vidar_voyager.active_moons
(
	game_state_id bigInt NOT NULL,
	moon_id bigInt NOT NULL,
	FOREIGN KEY(game_state_id) REFERENCES vidar_voyager.game_state_madplanets(id),
	FOREIGN KEY(moon_id) REFERENCES vidar_voyager.moons(id)
);

CREATE TABLE vidar_voyager.last_played_game
(
	id serial NOT NULL,
	user_id bigInt NOT NULL,
	game_type varchar(32) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(user_id) REFERENCES vidar_voyager.users(id)
);
