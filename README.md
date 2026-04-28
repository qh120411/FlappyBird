# Flappy Bird Edu - Trò Chơi Học Tập Tương Tác

Một trò chơi giáo dục dạng desktop kết hợp cơ chế Flappy Bird kinh điển với hệ thống câu hỏi để học tập trở nên tương tác và hấp dẫn.

## Tổng Quan

Flappy Bird Edu là một ứng dụng Java Swing thách thức người chơi điều hướng qua các ống dẫn trong khi trả lời các câu hỏi trắc nghiệm. Trò chơi theo dõi điểm số trong cơ sở dữ liệu MySQL và có ba mức độ khó khác nhau ảnh hưởng đến vật lý trò chơi và quy tắc tính điểm.

**Tính Năng Chính:**
- Lối chơi tương tác với cơ chế vật lý dựa trên phiên bản chim
- Hệ thống câu hỏi trắc nghiệm tích hợp với hỗ trợ danh mục
- Ba mức độ khó (Dễ, Trung bình, Khó) với vật lý và tính điểm riêng biệt
- Theo dõi điểm số liên tục qua cơ sở dữ liệu MySQL
- Hệ thống bảng xếp hạng
- Giao diện quản lý câu hỏi với các phép toán CRUD
- Nhập/xuất câu hỏi từ tệp CSV

## Công Nghệ Sử Dụng

| Thành Phần | Công Nghệ |
|-----------|-----------|
| Framework Giao Diện | Java Swing |
| Cơ Sở Dữ Liệu | MySQL 8.0+ |
| JDBC Driver | MySQL Connector/J 9.6.0 |
| Ngôn Ngữ | Java 11+ |

## Cấu Trúc Dự Án

```
FlappyBird/
├── src/                              # Mã nguồn Java
│   ├── App.java                      # Điểm nhập của ứng dụng
│   ├── GamePanel.java                # Vòng lặp render & sự kiện
│   ├── Game.java                     # Công cụ trò chơi và vật lý cốt lõi
│   ├── Bird.java                     # Thực thể chim với mô phỏng trọng lực
│   ├── Pipe.java                     # Chướng ngại vật
│   ├── Difficulty.java               # Cấu hình độ khó
│   ├── GameState.java                # Liệt kê trạng thái trò chơi
│   ├── MainMenu.java                 # Giao diện Menu
│   ├── GameOverDialog.java           # Màn hình kết thúc trò chơi
│   ├── LeaderboardPanel.java         # Hiển thị bảng xếp hạng
│   ├── QuizDialog.java               # Hiển thị câu hỏi & tương tác
│   ├── QuizManager.java              # Quản lý câu hỏi trắc nghiệm
│   ├── Question.java                 # Mô hình dữ liệu câu hỏi
│   ├── QuestionAdminDialog.java      # Giao diện quản lý câu hỏi
│   ├── QuestionFileManager.java      # Nhập/xuất CSV
│   ├── GameInputBinder.java          # Ràng buộc phím nhập
│   ├── Keyboard.java                 # Trình xử lý nhập từ bàn phím
│   ├── DatabaseConnection.java       # Quản lý kết nối MySQL
│   ├── PlayerDAO.java                # Đối tượng truy cập dữ liệu người chơi
│   ├── ScoreDAO.java                 # Đối tượng truy cập dữ liệu điểm
│   ├── AssetManager.java             # Hệ thống bộ nhớ đệm hình ảnh
│   ├── Render.java                   # Cấu trúc dữ liệu render
│   ├── Util.java                     # Các hàm tiện ích
│   ├── CsvUtil.java                  # Tiện ích phân tích CSV
│   └── ThemeConstants.java           # Chủ đề & kiểu dáng giao diện
├── lib/                              # Thư viện bên ngoài & tài sản
│   ├── mysql-connector-j-9.6.0.jar
│   ├── bird.png
│   ├── background.png
│   ├── pipe-north.png
│   └── pipe-south.png
├── sql/
│   └── schema.sql                    # Lược đồ cơ sở dữ liệu
├── data/
│   ├── questions.csv                 # Câu hỏi trắc nghiệm mặc định
│   └── questions_java.csv            # Câu hỏi lập trình Java
├── LICENSE.txt
└── README.md
```

## Cài Đặt & Thiết Lập

### Yêu Cầu
- Java Development Kit (JDK) 11 hoặc mới hơn
- MySQL Server 8.0 hoặc mới hơn (đang chạy và có thể truy cập)
- MySQL JDBC Driver (có sẵn trong thư mục `lib/`)

### Thiết Lập Cơ Sở Dữ Liệu

1. **Khởi Động Dịch Vụ MySQL**
   ```bash
   # Windows
   net start MySQL80
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. **Khởi Tạo Tự Động**
   - Ứng dụng tự động tạo cơ sở dữ liệu và bảng khi kết nối lần đầu
   - Cơ sở dữ liệu: `flappy_bird_edu`
   - Bảng: `players`, `scores`
   - Lược đồ được định nghĩa trong `sql/schema.sql`

3. **Thông Tin Đăng Nhập Mặc Định** (sửa trong `DatabaseConnection.java` nếu khác)
   ```properties
   Host: localhost:3306
   Tên người dùng: root
   Mật khẩu: (trống)
   Cơ sở dữ liệu: flappy_bird_edu
   ```

### Biên Dịch

```bash
# Điều hướng đến thư mục gốc dự án
cd FlappyBird

# Biên dịch tất cả các tệp Java
javac -cp "lib/mysql-connector-j-9.6.0.jar" -d bin src/*.java
```

### Chạy Ứng Dụng

```bash
# Trên Linux/Mac
java -cp "bin:lib/mysql-connector-j-9.6.0.jar" App

# Trên Windows
java -cp "bin;lib\mysql-connector-j-9.6.0.jar" App
```

## Cơ Chế Trò Chơi

### Mức Độ Khó

Ba mức độ khó với các thách thức gia tăng:

| Mức Độ | Trọng Lực | Vận Tốc Cánh | Tốc Độ Rơi Max | Khoảng Ống | Quy Tắc Tính Điểm |
|--------|----------|-------------|----------------|-----------|------------------|
| **Dễ** | 0.35 | -8.0 | 8.0 | 140 | +1 điểm mỗi câu trả lời đúng |
| **Trung bình** | 0.45 | -8.8 | 8.5 | 120 | +1 điểm mỗi câu trả lời đúng |
| **Khó** | 0.55 | -9.6 | 9.0 | 100 | +1 đúng, -3 sai |

### Dòng Chảy Trò Chơi

1. **Menu Chính**
   - Nhập tên người chơi
   - Chọn mức độ khó
   - Truy cập bảng xếp hạng hoặc quản lý câu hỏi
   - Bắt đầu trò chơi

2. **Lối Chơi**
   - Nhấn SPACE để bắt đầu
   - Điều hướng chim qua các ống bằng phím SPACE
   - Mỗi lần vượt ống thành công sẽ kích hoạt một câu hỏi

3. **Hệ Thống Câu Hỏi**
   - Câu hỏi trắc nghiệm hiển thị sau mỗi ống
   - Dễ/Trung bình: Câu trả lời sai không có điểm cho ống đó
   - Khó: Câu trả lời sai bị trừ 3 điểm
   - Trả lời và tiếp tục hoặc kết thúc trò chơi

4. **Kết Thúc Trò Chơi**
   - Tóm tắt điểm với thống kê
   - Tùy chọn chơi lại, xem bảng xếp hạng hoặc quay về menu

### Điều Khiển

| Phím | Chức Năng |
|-----|----------|
| **SPACE** | Bắt đầu trò chơi / Cánh chim |
| **P** | Tạm dừng/Tiếp tục |
| **R** | Chơi lại nhanh từ bất kỳ trạng thái nào |
| **ESC** | Quay về menu chính |

## Lược Đồ Cơ Sở Dữ Liệu

### Bảng Người Chơi
```sql
CREATE TABLE players (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Bảng Điểm
```sql
CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player_id INT NOT NULL,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    score INT NOT NULL,
    correct_answers INT DEFAULT 0,
    total_questions INT DEFAULT 0,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(player_id) REFERENCES players(id)
);
```

## Định Dạng Câu Hỏi (CSV)

Câu hỏi phải ở định dạng CSV có cấu trúc sau:

```csv
category,question_text,option_a,option_b,option_c,option_d,correct_answer
Java Basics,"Kết quả của: System.out.println(5/2) là gì?","2","2.5","3","Lỗi","A"
Java OOP,"Từ khóa nào cho phép kế thừa?","extends","implements","inherits","super","A"
```

**Giá trị hợp lệ cho `correct_answer`**: A, B, C, D (không phân biệt chữ hoa/thường)

### Quản Lý Câu Hỏi
- Sử dụng bảng **Quản Lý Câu Hỏi** (có thể truy cập từ Menu Chính)
- Nhập câu hỏi từ tệp CSV
- Thêm, chỉnh sửa hoặc xóa câu hỏi
- Xuất câu hỏi để sao lưu

## Khắc Phục Sự Cố

### Lỗi "Không Thể Kết Nối Với Cơ Sở Dữ Liệu"
**Nguyên nhân**: Dịch vụ MySQL không chạy hoặc thông tin đăng nhập không chính xác
- Đảm bảo MySQL đang chạy: `mysql -u root -p` phải hoạt động
- Kiểm tra thông tin đăng nhập trong `DatabaseConnection.java`
- Kiểm tra xem cổng 3306 có thể truy cập được không

### Tài Sản Trò Chơi Bị Thiếu (không hiển thị chim/ống)
**Nguyên nhân**: Không tìm thấy tệp PNG trong thư mục `lib/`
- Xác minh tất cả tệp hình ảnh tồn tại:
  - `bird.png`
  - `background.png`
  - `pipe-north.png`
  - `pipe-south.png`

### Lỗi "Không Thể Tải MySQL JDBC Driver"
**Nguyên nhân**: Trình kết nối JDBC không trong classpath
- Đảm bảo `mysql-connector-j-9.6.0.jar` tồn tại trong `lib/`
- Xác minh tệp jar được bao gồm trong classpath biên dịch và thời gian chạy

### Câu Hỏi Không Tải Trong Trò Chơi
**Nguyên nhân**: Vấn đề định dạng hoặc vị trí tệp CSV
- Xác minh tệp CSV tồn tại trong thư mục `data/`
- Kiểm tra định dạng CSV khớp với đặc tả (yêu cầu tiêu đề)
- Đảm bảo mã hóa tệp là UTF-8
- Tải lại câu hỏi qua bảng Quản Lý Câu Hỏi nếu cần

## Tổ Chức Mã

Cơ sở mã được tổ chức thành bốn khu vực chức năng:

**Công Cụ Trò Chơi Cốt Lõi**
- `Game.java` - Logic vật lý và va chạm chính
- `Bird.java` - Nhân vật người chơi với mô phỏng trọng lực
- `Pipe.java` - Tạo và phát hiện va chạm chướng ngại vật
- `GameState.java` - Quản lý trạng thái trò chơi
- `Difficulty.java` - Cài đặt độ khó và tham số vật lý

**Giao Diện Người Dùng**
- `App.java` - Cửa sổ ứng dụng và điều hướng dựa trên thẻ
- `GamePanel.java` - Render và vòng lặp trò chơi (60 FPS)
- `MainMenu.java` - Màn hình menu và thiết lập người chơi
- `GameOverDialog.java` - Màn hình kết thúc trò chơi
- `LeaderboardPanel.java` - Hiển thị xếp hạng điểm

**Hệ Thống Câu Hỏi**
- `QuizManager.java` - Lựa chọn và quản lý câu hỏi trắc nghiệm
- `QuizDialog.java` - Hiển thị câu hỏi và xử lý câu trả lời
- `Question.java` - Mô hình dữ liệu câu hỏi
- `QuestionAdminDialog.java` - Giao diện quản trị câu hỏi
- `QuestionFileManager.java` - Chức năng nhập/xuất CSV
- `CsvUtil.java` - Tiện ích phân tích CSV

**Dữ Liệu & Tài Sản**
- `DatabaseConnection.java` - Quản lý nhóm kết nối MySQL
- `PlayerDAO.java` - Tính bền vững bản ghi người chơi
- `ScoreDAO.java` - Tính bền vững bản ghi điểm và truy vấn bảng xếp hạng
- `AssetManager.java` - Tải và bộ nhớ đệm hình ảnh
- `Keyboard.java` - Quản lý trạng thái nhập với tiêu thụ phím

## Ghi Chú Hiệu Suất

- **Vòng Lặp Trò Chơi**: Chạy ở ~60 FPS (khoảng thời gian cập nhật 16ms) để lối chơi mượt mà
- **Bộ Nhớ Đệm Hình Ảnh**: AssetManager ngăn chặn I/O đĩa dư thừa
- **Tái Sử Dụng Ống**: Các đối tượng ống được tái chế thay vì tạo các phiên bản mới
- **Cơ Sở Dữ Liệu**: Khởi tạo chậm khi kết nối lần đầu; tạo lược đồ một lần

## Ghi Chú Phát Triển

### Chất Lượng Mã
- JavaDoc toàn diện cho tất cả các phương thức công khai
- Quy ước đặt tên nhất quán và tổ chức mã
- Số ma thuật được trích xuất thành các hằng số được đặt tên
- Quản lý tài nguyên thích hợp với tài nguyên thử
- Mẫu Singleton cho các tài nguyên được chia sẻ (Keyboard, AssetManager)

### Thêm Câu Hỏi Tùy Chỉnh
1. Chỉnh sửa `data/questions.csv` bằng các câu hỏi của bạn
2. Đảm bảo định dạng CSV chính xác (được phân tách bằng dấu phẩy, tiêu đề thích hợp)
3. Lưu với mã hóa UTF-8
4. Khởi động lại ứng dụng hoặc sử dụng bảng Quản Lý Câu Hỏi để tải lại

### Sửa Đổi Cài Đặt Độ Khó
- Chỉnh sửa enum `Difficulty.java` để thay đổi tham số vật lý
- Điều chỉnh trọng lực, vận tốc cánh, khoảng ống và quy tắc tính điểm
- Thay đổi áp dụng ngay khi khởi động lại trò chơi

## Giấy Phép

Dự án này được cung cấp như tài liệu giáo dục. Xem LICENSE.txt để biết chi tiết.

---

**Phiên Bản**: 1.0  
**Cập Nhật Lần Cuối**: Tháng 4 năm 2026  
**Trạng Thái**: Ổn Định & Sẵn Sàng Sản Xuất
