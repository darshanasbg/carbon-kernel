CREATE TABLE UM_USERS (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			USER_NAME VARCHAR(255) NOT NULL,
			USER_PASSWORD VARCHAR(255) NOT NULL,
			PRIMARY KEY (ID),
			UNIQUE(USER_NAME));

CREATE TABLE IF NOT EXISTS UM_SYSTEM_USER (
			UM_ID INTEGER NOT NULL AUTO_INCREMENT,
			UM_USER_NAME VARCHAR(255) NOT NULL,
			UM_USER_PASSWORD VARCHAR(255) NOT NULL,
			UM_SALT_VALUE VARCHAR(31),
			UM_REQUIRE_CHANGE BOOLEAN DEFAULT FALSE,
            UM_CHANGED_TIME TIMESTAMP NOT NULL,
			UM_TENANT_ID INTEGER DEFAULT 0,
			PRIMARY KEY (UM_ID, UM_TENANT_ID),
			UNIQUE(UM_USER_NAME, UM_TENANT_ID));

CREATE TABLE UM_USER_ATTRIBUTES (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			ATTR_NAME VARCHAR(255) NOT NULL,
			ATTR_VALUE VARCHAR(255),
			USER_ID INTEGER,
			FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE,
			PRIMARY KEY (ID));

CREATE TABLE UM_ROLES (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			ROLE_NAME VARCHAR(255) NOT NULL,
			PRIMARY KEY (ID),
			UNIQUE(ROLE_NAME));

CREATE TABLE UM_ROLE_ATTRIBUTES (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			ATTR_NAME VARCHAR(255) NOT NULL,
			ATTR_VALUE VARCHAR(255),
			ROLE_ID INTEGER,
			FOREIGN KEY (ROLE_ID) REFERENCES UM_ROLES(ID) ON DELETE CASCADE,
			PRIMARY KEY (ID));

CREATE TABLE UM_PERMISSIONS (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			RESOURCE_ID VARCHAR(255) NOT NULL,
			ACTION VARCHAR(255) NOT NULL,
			PRIMARY KEY (ID));

CREATE INDEX INDEX_UM_PERMISSIONS_RESOURCE_ID_ACTION ON UM_PERMISSIONS (RESOURCE_ID, ACTION);

CREATE TABLE UM_ROLE_PERMISSIONS (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			PERMISSION_ID INTEGER NOT NULL,
			ROLE_ID INTEGER NOT NULL,
			IS_ALLOWED SMALLINT NOT NULL,
			UNIQUE (PERMISSION_ID, ROLE_ID),
			FOREIGN KEY (PERMISSION_ID) REFERENCES UM_PERMISSIONS(ID) ON DELETE  CASCADE,
			FOREIGN KEY (ROLE_ID) REFERENCES UM_ROLES(ID) ON DELETE CASCADE,
			PRIMARY KEY (ID));

CREATE TABLE UM_USER_PERMISSIONS (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			PERMISSION_ID INTEGER NOT NULL,
			USER_ID INTEGER NOT NULL,
			IS_ALLOWED SMALLINT NOT NULL,
			UNIQUE (PERMISSION_ID, USER_ID),
			FOREIGN KEY (PERMISSION_ID) REFERENCES UM_PERMISSIONS(ID) ON DELETE CASCADE,
			FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE,
			PRIMARY KEY (ID));

CREATE TABLE UM_USER_ROLES (
			ID INTEGER GENERATED ALWAYS AS IDENTITY,
			ROLE_ID INTEGER NOT NULL,
			USER_ID INTEGER NOT NULL,
			UNIQUE (USER_ID, ROLE_ID),
			FOREIGN KEY (ROLE_ID) REFERENCES UM_ROLES(ID) ON DELETE CASCADE,
			FOREIGN KEY (USER_ID) REFERENCES UM_USERS(ID) ON DELETE CASCADE,
			PRIMARY KEY (ID));

CREATE TABLE HYBRID_ROLES (
            ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
            ROLE_ID VARCHAR(255) NOT NULL,
            PRIMARY KEY (ID),
            UNIQUE(ROLE_ID));

CREATE TABLE HYBRID_USER_ROLES (
            ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
            USER_ID VARCHAR(255),
            ROLE_ID VARCHAR(255) NOT NULL,
            PRIMARY KEY (ID));

CREATE TABLE HYBRID_PERMISSIONS (
            ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
            RESOURCE_ID VARCHAR(255),
            ACTION VARCHAR(255) NOT NULL,
            PRIMARY KEY (ID));

CREATE TABLE HYBRID_ROLE_PERMISSIONS (
            ID INTEGER GENERATED BY DEFAULT AS IDENTITY,  
            PERMISSION_ID INTEGER NOT NULL,
            ROLE_ID VARCHAR(255) NOT NULL,
            IS_ALLOWED SMALLINT NOT NULL,
            UNIQUE (PERMISSION_ID, ROLE_ID),
            FOREIGN KEY (PERMISSION_ID) REFERENCES HYBRID_PERMISSIONS(ID) ON DELETE  CASCADE,
            PRIMARY KEY (ID));

CREATE TABLE HYBRID_USER_PERMISSIONS (
            ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
            PERMISSION_ID INTEGER NOT NULL,
            USER_ID VARCHAR(255) NOT NULL,
            IS_ALLOWED SMALLINT NOT NULL,
            UNIQUE (PERMISSION_ID, USER_ID),
            FOREIGN KEY (PERMISSION_ID) REFERENCES HYBRID_PERMISSIONS(ID) ON DELETE CASCADE,
            PRIMARY KEY (ID));

CREATE TABLE IF NOT EXISTS UM_SYSTEM_ROLE(
       UM_ID INTEGER NOT NULL AUTO_INCREMENT,
       UM_ROLE_NAME VARCHAR(255),
       UM_TENANT_ID INTEGER DEFAULT 0,
       PRIMARY KEY (UM_ID, UM_TENANT_ID));

CREATE TABLE IF NOT EXISTS UM_SYSTEM_USER_ROLE(
       UM_ID INTEGER NOT NULL AUTO_INCREMENT,
       UM_USER_NAME VARCHAR(255),
       UM_ROLE_ID INTEGER NOT NULL,
       UM_TENANT_ID INTEGER DEFAULT 0,
       UNIQUE (UM_USER_NAME, UM_ROLE_ID, UM_TENANT_ID),
       FOREIGN KEY (UM_ROLE_ID, UM_TENANT_ID) REFERENCES UM_SYSTEM_ROLE(UM_ID, UM_TENANT_ID),
       PRIMARY KEY (UM_ID, UM_TENANT_ID));