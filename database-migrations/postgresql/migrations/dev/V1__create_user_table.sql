CREATE TABLE "Country"(
    "countryCode" VARCHAR(20) PRIMARY KEY,
    "countryName" VARCHAR(200) NOT NULL
);

CREATE TABLE "State"(
    "stateCode" VARCHAR(20) PRIMARY KEY,
    "stateName" VARCHAR(200) NOT NULL,
    "countryCode" VARCHAR(20) NOT NULL,
    CONSTRAINT fk_countryCode FOREIGN KEY("countryCode") REFERENCES "Country"("countryCode")
);

CREATE TABLE "AMCRequest"(
    "amcId" UUID PRIMARY KEY,
    "amcName" VARCHAR(500) NOT NULL,
    "amcManagerName" VARCHAR(500) NOT NULL,
    "status" VARCHAR(50) NOT NULL,
    "hoAddress1" VARCHAR(200),
    "hoAddress2" VARCHAR(200),
    "hoAddress3" VARCHAR(200),
    "hoCountry" VARCHAR(20),
    "hoState" VARCHAR(20),
    "remarks" VARCHAR(1000),
    "createdAt" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    "modifiedAt" TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_countryCode FOREIGN KEY("hoCountry") REFERENCES "Country"("countryCode"),
    CONSTRAINT fk_stateCode FOREIGN KEY("hoState") REFERENCES "State"("stateCode")
);




