--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: avatars; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE avatars (
    id integer NOT NULL,
    user_id integer,
    created_at timestamp without time zone,
    data bytea,
    name character varying,
    ext character varying
);


ALTER TABLE avatars OWNER TO postgres;

--
-- Name: avatars_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE avatars_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE avatars_id_seq OWNER TO postgres;

--
-- Name: avatars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE avatars_id_seq OWNED BY avatars.id;


--
-- Name: conv_participants; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE conv_participants (
    id integer NOT NULL,
    conv_id integer,
    user_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE conv_participants OWNER TO postgres;

--
-- Name: conv_participants_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE conv_participants_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE conv_participants_id_seq OWNER TO postgres;

--
-- Name: conv_participants_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE conv_participants_id_seq OWNED BY conv_participants.id;


--
-- Name: conversations; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE conversations (
    id integer NOT NULL,
    ride_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    user_id integer,
    deleted_at timestamp without time zone
);


ALTER TABLE conversations OWNER TO postgres;

--
-- Name: conversations_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE conversations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE conversations_id_seq OWNER TO postgres;

--
-- Name: conversations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE conversations_id_seq OWNED BY conversations.id;


--
-- Name: devices; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE devices (
    id integer NOT NULL,
    user_id integer,
    token character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    enabled boolean,
    platform character varying(255),
    language character varying(255),
    device_expires timestamp without time zone NOT NULL
);


ALTER TABLE devices OWNER TO postgres;

--
-- Name: devices_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE devices_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE devices_id_seq OWNER TO postgres;

--
-- Name: devices_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE devices_id_seq OWNED BY devices.id;


--
-- Name: feedbacks; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE feedbacks (
    id integer NOT NULL,
    user_id integer,
    title character varying(255),
    content character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE feedbacks OWNER TO postgres;

--
-- Name: feedbacks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE feedbacks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE feedbacks_id_seq OWNER TO postgres;

--
-- Name: feedbacks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE feedbacks_id_seq OWNED BY feedbacks.id;


--
-- Name: location_aliases; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE location_aliases (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    location_entry_id integer
);


ALTER TABLE location_aliases OWNER TO postgres;

--
-- Name: location_aliases_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE location_aliases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE location_aliases_id_seq OWNER TO postgres;

--
-- Name: location_aliases_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE location_aliases_id_seq OWNED BY location_aliases.id;


--
-- Name: location_entries; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE location_entries (
    id integer NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    name character varying(64) NOT NULL
);


ALTER TABLE location_entries OWNER TO postgres;

--
-- Name: location_entries_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE location_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE location_entries_id_seq OWNER TO postgres;

--
-- Name: location_entries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE location_entries_id_seq OWNED BY location_entries.id;


--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE messages (
    id integer NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    is_seen boolean,
    content character varying(255),
    conversation_id integer,
    sender_id integer
);


ALTER TABLE messages OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE messages_id_seq OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;


--
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE notifications (
    id integer NOT NULL,
    user_id integer,
    ride_id integer,
    message_type character varying(255),
    date_time timestamp without time zone,
    status character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    message character varying(255),
    extra integer
);


ALTER TABLE notifications OWNER TO postgres;

--
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE notifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE notifications_id_seq OWNER TO postgres;

--
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE notifications_id_seq OWNED BY notifications.id;


--
-- Name: push_configurations; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE push_configurations (
    id integer NOT NULL,
    type character varying(255) NOT NULL,
    app character varying(255) NOT NULL,
    properties text,
    enabled boolean DEFAULT false NOT NULL,
    connections integer DEFAULT 1 NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE push_configurations OWNER TO postgres;

--
-- Name: push_configurations_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE push_configurations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE push_configurations_id_seq OWNER TO postgres;

--
-- Name: push_configurations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE push_configurations_id_seq OWNED BY push_configurations.id;


--
-- Name: push_feedback; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE push_feedback (
    id integer NOT NULL,
    app character varying(255) NOT NULL,
    device character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    follow_up character varying(255) NOT NULL,
    failed_at timestamp without time zone NOT NULL,
    processed boolean DEFAULT false NOT NULL,
    processed_at timestamp without time zone,
    properties text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE push_feedback OWNER TO postgres;

--
-- Name: push_feedback_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE push_feedback_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE push_feedback_id_seq OWNER TO postgres;

--
-- Name: push_feedback_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE push_feedback_id_seq OWNED BY push_feedback.id;


--
-- Name: push_messages; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE push_messages (
    id integer NOT NULL,
    app character varying(255) NOT NULL,
    device character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    properties text,
    delivered boolean DEFAULT false NOT NULL,
    delivered_at timestamp without time zone,
    failed boolean DEFAULT false NOT NULL,
    failed_at timestamp without time zone,
    error_code integer,
    error_description character varying(255),
    deliver_after timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE push_messages OWNER TO postgres;

--
-- Name: push_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE push_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE push_messages_id_seq OWNER TO postgres;

--
-- Name: push_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE push_messages_id_seq OWNED BY push_messages.id;


--
-- Name: ratings; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE ratings (
    id integer NOT NULL,
    to_user_id integer,
    from_user_id integer,
    rating_type integer,
    ride_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE ratings OWNER TO postgres;

--
-- Name: ratings_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ratings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ratings_id_seq OWNER TO postgres;

--
-- Name: ratings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ratings_id_seq OWNED BY ratings.id;


--
-- Name: relationships; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE relationships (
    id integer NOT NULL,
    user_id integer,
    ride_id integer,
    is_driving boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE relationships OWNER TO postgres;

--
-- Name: relationships_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE relationships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE relationships_id_seq OWNER TO postgres;

--
-- Name: relationships_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE relationships_id_seq OWNED BY relationships.id;


--
-- Name: requests; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE requests (
    id integer NOT NULL,
    ride_id integer,
    passenger_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    answered_at timestamp without time zone
);


ALTER TABLE requests OWNER TO postgres;

--
-- Name: requests_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE requests_id_seq OWNER TO postgres;

--
-- Name: requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE requests_id_seq OWNED BY requests.id;


--
-- Name: ride_searches; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE ride_searches (
    id integer NOT NULL,
    user_id integer,
    departure_place character varying(255),
    destination character varying(255),
    departure_time timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    ride_type integer,
    departure_latitude double precision,
    departure_longitude double precision,
    destination_latitude double precision,
    destination_longitude double precision,
    departure_threshold double precision,
    destination_threshold double precision,
    departure_time_offset_before integer NOT NULL,
    departure_time_offset_after integer NOT NULL
);


ALTER TABLE ride_searches OWNER TO postgres;

--
-- Name: ride_searches_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ride_searches_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ride_searches_id_seq OWNER TO postgres;

--
-- Name: ride_searches_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ride_searches_id_seq OWNED BY ride_searches.id;


--
-- Name: rides; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE rides (
    id integer NOT NULL,
    departure_place character varying(255),
    destination character varying(255),
    departure_time timestamp without time zone,
    free_seats integer,
    user_id integer,
    meeting_point character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    realtime_km double precision,
    price double precision,
    realtime_departure_time timestamp without time zone,
    duration double precision,
    realtime_arrival_time timestamp without time zone,
    is_finished boolean,
    ride_type integer,
    departure_latitude double precision,
    departure_longitude double precision,
    destination_latitude double precision,
    destination_longitude double precision,
    car character varying(255),
    rating_id integer,
    last_cancel_time timestamp without time zone,
    regular_ride_id integer,
    deleted_at timestamp without time zone,
    title character varying(255)
);


ALTER TABLE rides OWNER TO postgres;

--
-- Name: rides_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rides_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rides_id_seq OWNER TO postgres;

--
-- Name: rides_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rides_id_seq OWNED BY rides.id;


--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE schema_migrations (
    version character varying(255) NOT NULL
);


ALTER TABLE schema_migrations OWNER TO postgres;

--
-- Name: schema_version; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE schema_version (
    version_rank integer NOT NULL,
    installed_rank integer NOT NULL,
    version character varying(50) NOT NULL,
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE schema_version OWNER TO postgres;

--
-- Name: team; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE team (
    id integer NOT NULL,
    name character varying(128) NOT NULL,
    role character varying(128),
    avatar text,
    text text,
    team character varying(32),
    avatar_format character varying(32) DEFAULT 'png'::character varying NOT NULL
);


ALTER TABLE team OWNER TO postgres;

--
-- Name: team_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE team_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE team_id_seq OWNER TO postgres;

--
-- Name: team_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE team_id_seq OWNED BY team.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE users (
    id integer NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    email character varying(255),
    phone_number character varying(255),
    department character varying(255),
    car character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    admin boolean,
    api_key character varying(255),
    is_student boolean,
    rating_avg double precision,
    enabled boolean DEFAULT true NOT NULL,
    password character varying,
    salt character varying,
    avatar_id integer,
    api_key_expires timestamp without time zone,
    department_legacy character varying(255),
    deleted_at timestamp without time zone,
    intended_use character(32) DEFAULT 'UNSPECIFIED'::bpchar NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avatars ALTER COLUMN id SET DEFAULT nextval('avatars_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conv_participants ALTER COLUMN id SET DEFAULT nextval('conv_participants_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conversations ALTER COLUMN id SET DEFAULT nextval('conversations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY devices ALTER COLUMN id SET DEFAULT nextval('devices_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feedbacks ALTER COLUMN id SET DEFAULT nextval('feedbacks_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location_aliases ALTER COLUMN id SET DEFAULT nextval('location_aliases_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location_entries ALTER COLUMN id SET DEFAULT nextval('location_entries_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notifications ALTER COLUMN id SET DEFAULT nextval('notifications_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY push_configurations ALTER COLUMN id SET DEFAULT nextval('push_configurations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY push_feedback ALTER COLUMN id SET DEFAULT nextval('push_feedback_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY push_messages ALTER COLUMN id SET DEFAULT nextval('push_messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ratings ALTER COLUMN id SET DEFAULT nextval('ratings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relationships ALTER COLUMN id SET DEFAULT nextval('relationships_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requests ALTER COLUMN id SET DEFAULT nextval('requests_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ride_searches ALTER COLUMN id SET DEFAULT nextval('ride_searches_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rides ALTER COLUMN id SET DEFAULT nextval('rides_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY team ALTER COLUMN id SET DEFAULT nextval('team_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: avatars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY avatars
    ADD CONSTRAINT avatars_pkey PRIMARY KEY (id);


--
-- Name: conv_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY conv_participants
    ADD CONSTRAINT conv_participants_pkey PRIMARY KEY (id);


--
-- Name: conversations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY conversations
    ADD CONSTRAINT conversations_pkey PRIMARY KEY (id);


--
-- Name: devices_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY devices
    ADD CONSTRAINT devices_pkey PRIMARY KEY (id);


--
-- Name: feedbacks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY feedbacks
    ADD CONSTRAINT feedbacks_pkey PRIMARY KEY (id);


--
-- Name: location_aliases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY location_aliases
    ADD CONSTRAINT location_aliases_pkey PRIMARY KEY (id);


--
-- Name: location_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY location_entries
    ADD CONSTRAINT location_entries_pkey PRIMARY KEY (id);


--
-- Name: messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: push_configurations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY push_configurations
    ADD CONSTRAINT push_configurations_pkey PRIMARY KEY (id);


--
-- Name: push_feedback_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY push_feedback
    ADD CONSTRAINT push_feedback_pkey PRIMARY KEY (id);


--
-- Name: push_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY push_messages
    ADD CONSTRAINT push_messages_pkey PRIMARY KEY (id);


--
-- Name: ratings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY ratings
    ADD CONSTRAINT ratings_pkey PRIMARY KEY (id);


--
-- Name: relationships_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY relationships
    ADD CONSTRAINT relationships_pkey PRIMARY KEY (id);


--
-- Name: requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY requests
    ADD CONSTRAINT requests_pkey PRIMARY KEY (id);


--
-- Name: ride_searches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY ride_searches
    ADD CONSTRAINT ride_searches_pkey PRIMARY KEY (id);


--
-- Name: rides_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT rides_pkey PRIMARY KEY (id);


--
-- Name: schema_version_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (version);


--
-- Name: team_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- Name: unique$location_aliases$name; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY location_aliases
    ADD CONSTRAINT "unique$location_aliases$name" UNIQUE (name);


--
-- Name: unique$location_entries$name; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY location_entries
    ADD CONSTRAINT "unique$location_entries$name" UNIQUE (name);


--
-- Name: unique$requests$ride_id$passenger_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY requests
    ADD CONSTRAINT "unique$requests$ride_id$passenger_id" UNIQUE (ride_id, passenger_id);


--
-- Name: unique_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY team
    ADD CONSTRAINT unique_id UNIQUE (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: devices_user$id_token_uindex; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE UNIQUE INDEX "devices_user$id_token_uindex" ON devices USING btree (user_id, token);


--
-- Name: fki_avatars$user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_avatars$user_id" ON avatars USING btree (user_id);


--
-- Name: fki_conv_participants$conv_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_conv_participants$conv_id" ON conv_participants USING btree (conv_id);


--
-- Name: fki_conv_participants$user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_conv_participants$user_id" ON conv_participants USING btree (user_id);


--
-- Name: fki_conversations$ride_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_conversations$ride_id" ON conversations USING btree (ride_id);


--
-- Name: fki_devices$user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_devices$user_id" ON devices USING btree (user_id);


--
-- Name: fki_feedbacks$user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_feedbacks$user_id" ON feedbacks USING btree (user_id);


--
-- Name: fki_location_aliases$name; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_location_aliases$name" ON location_aliases USING btree (name);


--
-- Name: fki_location_entries$name; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_location_entries$name" ON location_entries USING btree (name);


--
-- Name: fki_messages$conversation_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_messages$conversation_id" ON messages USING btree (conversation_id);


--
-- Name: fki_messages$sender_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_messages$sender_id" ON messages USING btree (sender_id);


--
-- Name: fki_notifications$ride_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_notifications$ride_id" ON notifications USING btree (ride_id);


--
-- Name: fki_notifications$user_ud; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_notifications$user_ud" ON notifications USING btree (user_id);


--
-- Name: fki_ratings$from_user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_ratings$from_user_id" ON ratings USING btree (from_user_id);


--
-- Name: fki_ratings$ride_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_ratings$ride_id" ON ratings USING btree (ride_id);


--
-- Name: fki_ratings$to_user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_ratings$to_user_id" ON ratings USING btree (to_user_id);


--
-- Name: fki_requests$passenger_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_requests$passenger_id" ON requests USING btree (passenger_id);


--
-- Name: fki_requests$ride_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_requests$ride_id" ON requests USING btree (ride_id);


--
-- Name: fki_ride_searches$user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_ride_searches$user_id" ON ride_searches USING btree (user_id);


--
-- Name: fki_rides$rating_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_rides$rating_id" ON rides USING btree (rating_id);


--
-- Name: fki_uers$pk_avatars; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX "fki_uers$pk_avatars" ON users USING btree (avatar_id);


--
-- Name: index_conv_participants_on_conv_id_and_user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX index_conv_participants_on_conv_id_and_user_id ON conv_participants USING btree (conv_id, user_id);


--
-- Name: index_push_feedback_on_processed; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX index_push_feedback_on_processed ON push_feedback USING btree (processed);


--
-- Name: index_push_messages_on_delivered_and_failed_and_deliver_after; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX index_push_messages_on_delivered_and_failed_and_deliver_after ON push_messages USING btree (delivered, failed, deliver_after);


--
-- Name: index_relationships_on_ride_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX index_relationships_on_ride_id ON relationships USING btree (ride_id);


--
-- Name: index_relationships_on_user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX index_relationships_on_user_id ON relationships USING btree (user_id);


--
-- Name: index_relationships_on_user_id_and_ride_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE UNIQUE INDEX index_relationships_on_user_id_and_ride_id ON relationships USING btree (user_id, ride_id);


--
-- Name: index_rides_on_user_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX index_rides_on_user_id ON rides USING btree (user_id);


--
-- Name: index_users_on_email; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE UNIQUE INDEX index_users_on_email ON users USING btree (email);


--
-- Name: schema_version_ir_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX schema_version_ir_idx ON schema_version USING btree (installed_rank);


--
-- Name: schema_version_s_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX schema_version_s_idx ON schema_version USING btree (success);


--
-- Name: schema_version_vr_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE INDEX schema_version_vr_idx ON schema_version USING btree (version_rank);


--
-- Name: unique_schema_migrations; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE UNIQUE INDEX unique_schema_migrations ON schema_migrations USING btree (version);


--
-- Name: avatars$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avatars
    ADD CONSTRAINT "avatars$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: conv_participants$conv_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conv_participants
    ADD CONSTRAINT "conv_participants$conv_id" FOREIGN KEY (conv_id) REFERENCES conversations(id);


--
-- Name: conv_participants$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conv_participants
    ADD CONSTRAINT "conv_participants$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: conversations$ride_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conversations
    ADD CONSTRAINT "conversations$ride_id" FOREIGN KEY (ride_id) REFERENCES rides(id);


--
-- Name: conversations$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conversations
    ADD CONSTRAINT "conversations$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: devices$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY devices
    ADD CONSTRAINT "devices$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: feedbacks$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feedbacks
    ADD CONSTRAINT "feedbacks$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: fk_uers$pk_avatars; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT "fk_uers$pk_avatars" FOREIGN KEY (avatar_id) REFERENCES avatars(id);


--
-- Name: location_aliases$location_entry_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location_aliases
    ADD CONSTRAINT "location_aliases$location_entry_id" FOREIGN KEY (location_entry_id) REFERENCES location_entries(id);


--
-- Name: messages$conversation_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT "messages$conversation_id" FOREIGN KEY (conversation_id) REFERENCES conversations(id);


--
-- Name: messages$sender_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT "messages$sender_id" FOREIGN KEY (sender_id) REFERENCES users(id);


--
-- Name: notifications$ride_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notifications
    ADD CONSTRAINT "notifications$ride_id" FOREIGN KEY (ride_id) REFERENCES rides(id);


--
-- Name: notifications$user_ud; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notifications
    ADD CONSTRAINT "notifications$user_ud" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: ratings$from_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ratings
    ADD CONSTRAINT "ratings$from_user_id" FOREIGN KEY (from_user_id) REFERENCES users(id);


--
-- Name: ratings$ride_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ratings
    ADD CONSTRAINT "ratings$ride_id" FOREIGN KEY (ride_id) REFERENCES rides(id);


--
-- Name: ratings$to_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ratings
    ADD CONSTRAINT "ratings$to_user_id" FOREIGN KEY (to_user_id) REFERENCES users(id);


--
-- Name: relationships$ride_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relationships
    ADD CONSTRAINT "relationships$ride_id" FOREIGN KEY (ride_id) REFERENCES rides(id);


--
-- Name: relationships$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relationships
    ADD CONSTRAINT "relationships$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: requests$passenger_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requests
    ADD CONSTRAINT "requests$passenger_id" FOREIGN KEY (passenger_id) REFERENCES users(id);


--
-- Name: requests$ride_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requests
    ADD CONSTRAINT "requests$ride_id" FOREIGN KEY (ride_id) REFERENCES rides(id);


--
-- Name: ride_searches$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ride_searches
    ADD CONSTRAINT "ride_searches$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: rides$rating_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT "rides$rating_id" FOREIGN KEY (rating_id) REFERENCES ratings(id);


--
-- Name: rides$user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rides
    ADD CONSTRAINT "rides$user_id" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

