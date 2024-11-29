-- dialect: MySQL
ALTER TABLE notice_table ADD CONSTRAINT unique_title UNIQUE (title);