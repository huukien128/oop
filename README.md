# 🧱 Akanoid — Bài tập lớn lập trình hướng đối tượng OOP (JAVA)

## I. 👨‍💻 Thông tin nhóm
- **Họ và tên:**
    - Phan Huy Quang — MSSV: 24022827
    - Nguyễn Hữu Kiên — MSSV: 24022807
    - Nguyễn Đức Thành — MSSV: 24022833
- **Lớp:** K69I-CN1
- **Môn học:** Lập trình hướng đối tượng (OOP) — INT2204 15

---

## II. 🌐 Tổng quan
- **Tên game:** AKANOID
- **Thể loại:** Arcade / Brick Breaker (Arkanoid-style)
- **Ngôn ngữ lập trình:** Java (Swing + AWT)
- **Thư viện / Công cụ:** Java SE (`javax.swing`, `java.awt`, `ImageIO`…), `Audio/SoundManager` (module nội bộ)
- **Tài nguyên âm thanh:**  
  `/sound/music.wav`, `/sound/electric.wav`, `/sound/recover.wav`, `/sound/hurt.wav`, `/sound/lose.wav`, `/sound/powerup.wav`
- **Tài nguyên ảnh:**  
  `/images/background.png`, `/images/menu_main_bg.png` (và nhiều sprite khác đặt cùng thư mục `/images`)

---

## III. 🎮 Mô tả trò chơi & cơ chế
**Akanoid** là game phá gạch cổ điển được phát triển bằng Java/Swing trong khuôn khổ bài tập lớn môn **Lập trình hướng đối tượng (OOP)**.  
Người chơi điều khiển thanh đỡ để đỡ bóng, phá vỡ các loại gạch và thu thập vật phẩm. Game có nhiều màn, hiệu ứng bắt mắt và âm thanh sống động.

### 🌟 Tính năng nổi bật:
- **Menu:** hỗ trợ cả chuột và bàn phím
- **Các trạng thái game:** `menu`, `ready`, `playing`, `paused`, `gameover`
- **Hiển thị:** Điểm (Score), Mạng (Lives), Cấp độ (Level), Trạng thái vật phẩm (Power-up)
- **Lưu lại điểm :** Điểm số của mỗi lần chơi sẽ được lưu lại vĩnh viễn trong file 
- **Power-up:** Rơi ngẫu nhiên từ brick gồm tăng kích thước paddle, nhân ba bóng, laser, tăng tốc độ bóng, v.v.
- **Laser Beam:** Cho phép bắn phá gạch (âm thanh riêng `/sound/electric.wav`)
- **Hiệu ứng chuyển cảnh:** Fade-in / fade-out khi đổi trạng thái hoặc mất mạng
- **Âm thanh:** Nhạc nền, hiệu ứng va chạm, thu power-up, mất mạng, thua cuộc
- **Tăng độ khó theo level:** brick bố trí dày đặc hơn và khó phá vỡ hơn

---

## IV. 🎮 Điều khiển (Controls)
- **← / → (Left / Right arrow):** Di chuyển paddle sang trái / phải
- **Enter:** Chọn tính năng
- **Space:** Phát bóng, tiếp tục game hoặc tạm dừng
- **ESC:** Thoát game
- **Chuột:** Click vào menu để chọn tùy chọn

---

## V. 🚀 Hướng dẫn Cài đặt & Chạy Game

### 1. Cài đặt (Setup)

**Cách 1 – Clone qua Git:**
```bash
git clone https://github.com/huukien128/oop.git
cd oop
```

**Cách 2 – Tải thủ công:**
Truy cập [https://github.com/huukien128/oop](https://github.com/huukien128/oop)  
→ Chọn **Code → Download ZIP** → Giải nén vào thư mục tùy chọn.

💡 **Lưu ý:** Các thư mục **images/**, **sound/**, **map/** phải nằm trong **src/** (Source Root).

---

### 2. Chạy Game (Run)

**Cách 1 – IDE (Đề xuất):**  
Mở dự án trong **IntelliJ IDEA** hoặc **Eclipse**, chạy `gameView.java` trong package `game/`.

**Cách 2 – Command Line:**
```bash
javac src/game/gameView.java
java -cp src game.gameView
```
## 🧱 VI. Cấu trúc Chính và Nguyên tắc OOP

- Dự án được thiết kế theo các **nguyên tắc lập trình hướng đối tượng (OOP)**, sử dụng mô hình **Manager–Object** để tách biệt rõ giữa logic điều khiển và đối tượng game.

---

### 1. Nguyên tắc OOP Áp dụng

| **Nguyên tắc** | **Mô tả** | **Ứng dụng trong Project** |
|----------------|-----------|-----------------------------|
| **Kế thừa (Inheritance)** | Xây dựng hệ thống phân cấp lớp để tái sử dụng mã nguồn. | `Ball`, `Paddle` kế thừa `MovableObject`; các loại gạch (`NormalBrick`, `StrongBrick`) kế thừa `Brick`. |
| **Trừu tượng (Abstraction)** | Ẩn chi tiết cài đặt, chỉ hiển thị giao diện cần thiết như `render()` và `update()`. | Sử dụng các lớp trừu tượng `GameObject`, `MovableObject`, `Brick`, `PowerUp`. |
| **Đóng gói (Encapsulation)** | Bảo vệ dữ liệu nội bộ, kiểm soát quyền truy cập. | Các thuộc tính (`x`, `y`, `hitPoints`, `speed`) được khai báo `private`, truy cập qua `get/set`. |
| **Đa hình (Polymorphism)** | Các đối tượng phản ứng khác nhau với cùng một lệnh. | Phương thức `render(Graphics g)` và `update()` được ghi đè khác nhau ở từng lớp con. |

---

### 2. Cấu trúc Kế thừa (Inheritance Tree)

- Cấu trúc mô tả mối quan hệ “A là một loại B” (`A extends B`) giữa các đối tượng trong game:

```
GameObject (abstract)
└── LaserBeam
└── MovableObject (abstract)
│   ├── Paddle
│   └── Ball
└── Brick (abstract)
│   ├── NormalBrick
│   ├── StrongBrick
│   └── VeryStrongBrick
└── PowerUp (abstract)
    ├── MultiballPowerUp
    ├── LaserPowerUp
    ├── FastBallPowerUp
    └── ExpandPaddlePowerUp
```

📘 **Giải thích:**
- `GameObject`: lớp cơ sở cho mọi vật thể, có vị trí, kích thước, `render()`, `update()`.
- `MovableObject`: thêm vận tốc (`dx`, `dy`, `speed`), có thể di chuyển.
- `Brick`: có `hitPoints`, đổi ảnh khi bị đánh trúng.
- `PowerUp`: có `applyEffect()` và `removeEffect()`.

---

### 3. Tổ chức Gói (Package Structure)

| **Gói (Package)** | **Lớp / Thành phần** | **Vai trò** |
|------------------|----------------------|--------------|
| `gameController` | `GameManager`, `gameView` | Quản lý vòng lặp, trạng thái, input người chơi. |
| `manager` | `LevelManager`, `MenuManager` | Quản lý logic level, menu, giao diện. |
| `audio` | `SoundManager` | Quản lý âm thanh, nhạc nền, hiệu ứng. |
| `object` | `GameObject`, `MovableObject` | Cung cấp lớp cơ sở cho các đối tượng. |
| `object.ball.*`, `object.brick.*` | Các lớp con cụ thể | Mô tả hành vi và loại đối tượng trong game. |

## VII. 🖼 Hình ảnh minh họa trong game

- Dưới đây là một số hình ảnh minh họa của game

<div style="display: flex; justify-content: space-around;">
  <img src="https://github.com/huukien128/oop/blob/7a9a178094cd222d3c79993b1a6aa0fcd1000e65/menu.png" alt="Menu Screen" width="400"/>
  <img src="https://github.com/huukien128/oop/blob/7a9a178094cd222d3c79993b1a6aa0fcd1000e65/gameplay.png" width="400"/>
</div>

## VIII. 📚 Tài liệu Tham khảo và công cụ hỗ trợ

- [Breakout Game by LuciaMezquida](https://github.com/LuciaMezquida/Breakout-Game)
- [Arkanoid Online by dobkir](https://github.com/dobkir/Arkanoid-Online)
- [Arkanoid-Breakout-Game-site by VladimirSaenko](https://github.com/VladimirSaenko/Arkanoid-Breakout-Game-site)
- Chatgpt, Gemini
