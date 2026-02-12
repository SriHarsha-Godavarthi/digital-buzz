-- Insert sample users (password is 'password123' for all users, hashed with BCrypt)
INSERT INTO users (username, email, password, role, status, first_name, last_name, created_by, updated_by) VALUES
('admin', 'admin@digitalbuzz.com', '$2a$10$XWPPfU7P7Y0Y7kf3yN3EqOhpZxJ4i6VZ3x1D3ZLHjhfHOJ7L8Oj3W', 'ADMIN', 'ACTIVE', 'Admin', 'User', 'system', 'system'),
('editor1', 'editor1@digitalbuzz.com', '$2a$10$XWPPfU7P7Y0Y7kf3yN3EqOhpZxJ4i6VZ3x1D3ZLHjhfHOJ7L8Oj3W', 'EDITOR', 'ACTIVE', 'Jane', 'Editor', 'system', 'system'),
('author1', 'author1@digitalbuzz.com', '$2a$10$XWPPfU7P7Y0Y7kf3yN3EqOhpZxJ4i6VZ3x1D3ZLHjhfHOJ7L8Oj3W', 'AUTHOR', 'ACTIVE', 'John', 'Author', 'system', 'system'),
('reader1', 'reader1@digitalbuzz.com', '$2a$10$XWPPfU7P7Y0Y7kf3yN3EqOhpZxJ4i6VZ3x1D3ZLHjhfHOJ7L8Oj3W', 'READER', 'ACTIVE', 'Mary', 'Reader', 'system', 'system');

-- Insert sample categories
INSERT INTO categories (name, description, slug, created_by, updated_by) VALUES
('Technology', 'Latest technology news and trends', 'technology', 'system', 'system'),
('Business', 'Business and finance news', 'business', 'system', 'system'),
('Sports', 'Sports news and updates', 'sports', 'system', 'system'),
('Entertainment', 'Entertainment and celebrity news', 'entertainment', 'system', 'system'),
('Health', 'Health and wellness articles', 'health', 'system', 'system');

-- Insert sample tags
INSERT INTO tags (name, slug, created_by, updated_by) VALUES
('AI', 'ai', 'system', 'system'),
('Machine Learning', 'machine-learning', 'system', 'system'),
('Blockchain', 'blockchain', 'system', 'system'),
('Cloud Computing', 'cloud-computing', 'system', 'system'),
('Cybersecurity', 'cybersecurity', 'system', 'system'),
('Startup', 'startup', 'system', 'system'),
('Football', 'football', 'system', 'system'),
('Basketball', 'basketball', 'system', 'system'),
('Movies', 'movies', 'system', 'system'),
('Music', 'music', 'system', 'system');

-- Insert sample articles
INSERT INTO news_articles (title, content, summary, author_id, category_id, status, published_at, view_count, deleted, created_by, updated_by) VALUES
('The Rise of Artificial Intelligence in 2024', 
 'Artificial Intelligence continues to transform industries across the globe. From healthcare to finance, AI is making significant impacts on how businesses operate and deliver value to customers. This comprehensive article explores the latest trends in AI technology and its future implications.',
 'An overview of AI trends and their impact on various industries',
 3, 1, 'PUBLISHED', CURRENT_TIMESTAMP, 150, FALSE, 'author1', 'author1'),
 
('Blockchain Technology: Beyond Cryptocurrency',
 'While blockchain is often associated with cryptocurrency, its applications extend far beyond digital currency. This article explores how blockchain is revolutionizing supply chain management, healthcare records, and digital identity verification.',
 'Exploring blockchain applications beyond crypto',
 3, 1, 'PUBLISHED', CURRENT_TIMESTAMP, 89, FALSE, 'author1', 'author1'),
 
('Startup Funding Trends in 2024',
 'Venture capital funding has seen significant shifts in recent years. This article analyzes the latest trends in startup funding, including which sectors are attracting the most investment and what investors are looking for in early-stage companies.',
 'Analysis of current startup funding landscape',
 3, 2, 'PUBLISHED', CURRENT_TIMESTAMP, 65, FALSE, 'author1', 'author1'),
 
('The Future of Cloud Computing',
 'Cloud computing continues to evolve with new technologies and services. This article examines emerging trends such as edge computing, serverless architecture, and multi-cloud strategies that are shaping the future of cloud infrastructure.',
 'Exploring the evolution of cloud computing',
 3, 1, 'DRAFT', NULL, 0, FALSE, 'author1', 'author1');

-- Insert article tags
INSERT INTO article_tags (article_id, tag_id) VALUES
(1, 1), (1, 2),
(2, 3),
(3, 6),
(4, 4);

-- Insert sample comments
INSERT INTO comments (article_id, user_id, content, status, created_by, updated_by) VALUES
(1, 4, 'Great article! Very informative about AI trends.', 'APPROVED', 'reader1', 'reader1'),
(1, 4, 'I learned a lot from this. Thank you!', 'APPROVED', 'reader1', 'reader1'),
(2, 4, 'Blockchain has so much potential beyond crypto. Thanks for highlighting this!', 'APPROVED', 'reader1', 'reader1');
