# 💰 Finance Manager - OOP final project

## 🧩 Giới thiệu
**Finance Manager** là một ứng dụng Java được xây dựng theo mô hình **Lập trình hướng đối tượng (OOP)**, giúp người dùng **quản lý tài chính cá nhân** một cách dễ dàng.  
Người dùng có thể theo dõi **thu nhập**, **chi tiêu**, **khoản vay**, **tài khoản ngân hàng**, và **giao dịch** trong cùng một nơi.

Dự án được phát triển phục vụ mục đích **học tập môn OOP** và thực hành các khái niệm:
- Kế thừa (Inheritance)
- Đa hình (Polymorphism)
- Trừu tượng (Abstraction)
- Bao đóng (Encapsulation)
- Sử dụng `enum`, `exception`, `package`, `interface`, `final`…

---

## 🚀 Tính năng chính
### 1. Quản lý tài khoản (`Account`)
- Tạo, sửa, xóa tài khoản (ngân hàng, ví, tiết kiệm, tiền mặt, tín dụng)
- Lưu trữ số dư, loại tài khoản và lịch sử giao dịch

### 2. Quản lý giao dịch (`Transaction`)
- Ghi nhận **thu nhập** và **chi tiêu**
- Hỗ trợ phân loại giao dịch theo loại (`TransactionType`)
- Tự động cập nhật số dư tài khoản khi có giao dịch mới

### 3. Quản lý khoản vay (`Loan`, `Lending`)
- Ghi nhận khoản vay, số tiền, lãi suất, ngày vay và hạn trả
- Theo dõi lịch sử hoàn trả

### 4. Phân loại & báo cáo
- Lưu trữ danh mục thu nhập / chi tiêu trong `Constants`
- Xuất báo cáo tổng quan thu – chi theo thời gian

### 5. Xử lý ngoại lệ (`exception`)
- Xây dựng hệ thống ngoại lệ riêng như `InvalidAmountException`, `AccountNotFoundException`
- Giúp ứng dụng chạy an toàn và rõ ràng khi xảy ra lỗi

---

## 🏗️ Cấu trúc thư mục

Xem tại [đây](/tree.txt)

---

## ⚙️ Cách chạy dự án

### 🧱 Yêu cầu
- **JDK 17** trở lên  
- IDE: IntelliJ IDEA / VS Code / Eclipse  
- Cấu trúc project theo chuẩn Maven hoặc Gradle

### ▶️ Chạy chương trình
```bash
# Di chuyển vào thư mục src/main/java
cd src/main/java

# Biên dịch
javac com/financemanager/Main.java

# Chạy chương trình
java com.financemanager.Main
```
