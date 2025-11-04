# 🧱 Akanoid — Bài tập lớn lập trình hướng đối tượng OOP (JAVA)

## I. Thông tin nhóm
- **Họ và tên:**  
  - Phan Huy Quang — MSSV: 24022827  
  - Nguyễn Hữu Kiên — MSSV: 24022807
  - Nguyễn Đức Thành — MSSV: 24022833 
- **Lớp:** K69I-CN1  
- **Môn học:** Lập trình hướng đối tượng (OOP) — INT2204 15

## II. Tổng quan
- **Tên game:** AKANOID  
- **Thể loại:** Arcade / Brick Breaker (Arkanoid-style)  
- **Ngôn ngữ lập trình:** Java (Swing + AWT)  
- **Thư viện / Công cụ:** Java SE (javax.swing, java.awt, ImageIO…), Audio/SoundManager (module nội bộ)  
- **Tài nguyên âm thanh:** `/sound/music.wav`, `/sound/electric.wav`, `/sound/recover.wav`, `/sound/hurt.wav`, `/sound/lose.wav`, `/sound/powerup.wav`  
- **Tài nguyên ảnh:** `/images/background.png`, `/images/menu_main_bg.png` (và nhiều sprite khác đặt cùng thư mục `/images`)  

## III. Mô tả trò chơi & cơ chế
**Akanoid** là game phá gạch cổ điển được phát triển bằng Java/Swing trong khuôn khổ bài tập lớn môn **Lập trình hướng đối tượng (OOP)**.  
Người chơi điều khiển thanh đỡ để đỡ bóng, phá vỡ các loại gạch và thu thập vật phẩm. Game có nhiều màn, hiệu ứng đầy bắt mắt và âm thanh rất sống động

### 🎮 Tính năng nổi bật:
- **Menu:** hỗ trợ cả chuột và bàn phím
- **Các trạng thái game:** `menu`, `ready`, `playing`, `paused`, `gameover`
- **Hiển thị:** Điểm (Score), Mạng (Lives), Cấp độ (Level), Trạng thái vật phẩm (Power-up)
- **Power-up:** Rơi ngẫu nhiên từ brick gồm tăng kích thước paddle, nhân ba bóng, laser, tăng tốc độ bóng v.v
- **Laser Beam:** Cho phép bắn phá gạch (âm thanh riêng `/sound/electric.wav`)
- **Hiệu ứng chuyển cảnh:** Fade-in / fade-out khi đổi trạng thái hoặc mất mạng
- **Âm thanh:** Nhạc nền, hiệu ứng va chạm, thu power-up, mất mạng, thua cuộc
- **Tăng độ khó theo level:** brick bố trí dày đặc hơn và khó phá vỡ hơn

## IV. Điều khiển (Controls)
- **← / → (Left / Right arrow):** Di chuyển paddle sang trái / phải
- **Enter:** Chọn tính năng
- **Space:** Phát bóng, tiếp tục game hoặc tạm dừng
- **ESC:** Thoát game  
- **Chuột:** Click vào menu để chọn tùy chọn

🎯 Hãy chơi game và tận hưởng những trải nghiệm tuyệt vời nhất cùng Akanoid!
