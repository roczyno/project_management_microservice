CREATE TABLE forgot_password_token
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token        INTEGER                                  NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    expired_at   TIMESTAMP WITHOUT TIME ZONE,
    validated_at TIMESTAMP WITHOUT TIME ZONE,
    user_id      INTEGER                                  NOT NULL,
    CONSTRAINT pk_forgotpasswordtoken PRIMARY KEY (id)
);

ALTER TABLE forgot_password_token
    ADD CONSTRAINT FK_FORGOTPASSWORDTOKEN_ON_USERID FOREIGN KEY (user_id) REFERENCES _user (id);
