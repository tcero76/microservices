DROP SCHEMA IF EXISTS "catalogo" CASCADE;

CREATE SCHEMA "catalogo";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "catalogo".ts (
    id_ts uuid NOT NULL,
    idpagina numeric NOT NULL,
    edad numeric NOT NULL,
    idjob numeric NOT NULL,
    fecharegistro numeric NOT NULL,
    sexo varchar(55),
    nombre varchar(75) NOT NULL,
    medidas varchar(35) NOT NULL,
    seccion varchar(255),
    depilacion varchar(55) NOT NULL,
    ubicacion varchar(55) NOT NULL,
    horario varchar(55) NOT NULL,
    descripcion varchar(1024) NOT NULL,
    atencion varchar(55) NOT NULL,
    telefono varchar(55),
    estatura varchar(55) NOT NULL,
    valor varchar(55),
    video varchar(255),
    imagen varchar(255) NOT NULL,
    sitio varchar(55) NOT NULL,
    cartel varchar(55) NOT NULL,
    ciudad varchar(55) NOT NULL,
    failure_messages character varying COLLATE pg_catalog."default",
    UNIQUE (id_ts),
    CONSTRAINT ts_pkey PRIMARY KEY (id_ts)
);

CREATE TABLE "catalogo".imagen (
    id_imagen uuid NOT NULL,
    url varchar(255) NOT NULL,
    id_ts uuid NOT NULL,
    CONSTRAINT imagen_pkey PRIMARY KEY (id_imagen),
    UNIQUE (id_imagen),
    CONSTRAINT imagen_ts_fkey FOREIGN KEY (id_ts) REFERENCES "catalogo".ts(id_ts)
);

CREATE TABLE "catalogo".servicio (
    id_servicio uuid NOT NULL,
    adicional boolean NOT NULL,
    nombre varchar(75) NOT NULL,
    id_ts uuid NOT NULL,
    CONSTRAINT servicio_pkey PRIMARY KEY (id_servicio),
    UNIQUE (id_servicio),
    CONSTRAINT servicio_ts_fkey FOREIGN KEY (id_ts) REFERENCES "catalogo".ts(id_ts)
);

DROP TYPE IF EXISTS "catalogo".outbox_status;

CREATE TYPE "catalogo".outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

DROP TABLE IF EXISTS "catalogo".outbox CASCADE;

CREATE TABLE "catalogo".outbox (
    id_outbox uuid NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,
--    type character varying COLLATE pg_catalog."default" NOT NULL,
    payload jsonb NOT NULL,
    outbox_status "catalogo".outbox_status NOT NULL,
    version integer NOT NULL,
    CONSTRAINT outbox_pkey PRIMARY KEY (id_outbox)
);