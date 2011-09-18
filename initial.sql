
CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    details TEXT NOT NULL,
    author VARCHAR(200) NOT NULL,
    date_created TIMESTAMP
);
