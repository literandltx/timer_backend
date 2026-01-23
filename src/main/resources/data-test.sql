-- Init User table
INSERT INTO users (id, password, email) VALUES (1, 'password123', 'test@example.com');

-- Init Label table
INSERT INTO labels (name, color, user_id) VALUES ('Test Label', '#CCCCCC', 1);
