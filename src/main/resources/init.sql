use kotlin;

CREATE TABLE test_user(
    id bigint generated always as identity,
    name varchar(50),
    age int,
    employee_type varchar(30),
    PRIMARY KEY(id)
);