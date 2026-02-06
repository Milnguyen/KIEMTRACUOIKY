package main;

import Service.ContactService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ContactService service = new ContactService();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("\n--- CHƯƠNG TRÌNH QUẢN LÝ DANH BẠ ---");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Thêm mới");
            System.out.println("3. Cập nhật");
            System.out.println("4. Xóa");
            System.out.println("5. Tìm kiếm");
            System.out.println("6. Đọc từ file");
            System.out.println("7. Lưu vào file");
            System.out.println("8. Thoát");
            System.out.print("Lựa chọn: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số từ 1 đến 8!");
                continue;
            }

            switch (choice) {
                case 1: service.showContacts(); break;
                case 2: service.addContact(); break;
                case 3: service.updateContact(); break;
                case 4: service.deleteContact(); break;
                case 5: service.searchContact(); break;
                case 6: service.readFromFile(); break;
                case 7: service.saveToFile(); break;
                case 8: System.out.println("Thoát chương trình."); break;
                default: System.out.println("Lựa chọn không hợp lệ!"); break;
            }
        } while (choice != 8);
    }
}
