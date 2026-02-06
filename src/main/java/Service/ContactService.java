package Service;

import Model.Contact;
import Repository.ContactRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ContactService {

    private List<Contact> contacts;
    private ContactRepository repository = new ContactRepository();
    private Scanner scanner = new Scanner(System.in);

    public ContactService() {
        contacts = repository.readContacts();
    }

    // ================== Hiển thị danh bạ 5/lần ==================
    public void showContacts() {
        int index = 0;
        if (contacts.isEmpty()) {
            System.out.println("Danh bạ trống!");
            return;
        }
        while (index < contacts.size()) {
            for (int i = 0; i < 5 && index < contacts.size(); i++, index++) {
                System.out.println(contacts.get(index));
            }
            if (index < contacts.size()) {
                System.out.println("Nhấn Enter để xem tiếp...");
                scanner.nextLine();
            }
        }
    }

    // ================== Thêm danh bạ ==================
    public void addContact() {
        System.out.println("----- Thêm mới danh bạ -----");
        String phone = inputPhoneNumber();
        String group = inputRequired("Nhập nhóm: ");
        String name = inputRequired("Nhập họ tên: ");
        String gender = inputRequired("Nhập giới tính: ");
        String address = inputRequired("Nhập địa chỉ: ");
        String dob = inputDob();
        String email = inputEmail();

        contacts.add(new Contact(phone, group, name, gender, address, dob, email));
        System.out.println("Đã thêm danh bạ thành công!");
    }

    // ================== Sửa danh bạ ==================
    public void updateContact() {
        System.out.println("----- Cập nhật danh bạ -----");
        if (contacts.isEmpty()) {
            System.out.println("Danh bạ trống!");
            return;
        }

        Contact contact = null;
        while (true) {
            System.out.print("Nhập số điện thoại muốn sửa (Enter để thoát): ");
            String phone = scanner.nextLine();
            if (phone.isEmpty()) return;

            contact = findByPhone(phone);
            if (contact == null) System.out.println("Không tìm được danh bạ với số điện thoại trên.");
            else break;
        }

        String newPhone = inputOptionalPhone("Số điện thoại (" + contact.getPhoneNumber() + "): ");
        if (!newPhone.isEmpty()) contact.setPhoneNumber(newPhone);

        String group = inputOptional("Nhóm (" + contact.getGroup() + "): ");
        if (!group.isEmpty()) contact.setGroup(group);

        String name = inputOptional("Họ tên (" + contact.getFullName() + "): ");
        if (!name.isEmpty()) contact.setFullName(name);

        String gender = inputOptional("Giới tính (" + contact.getGender() + "): ");
        if (!gender.isEmpty()) contact.setGender(gender);

        String address = inputOptional("Địa chỉ (" + contact.getAddress() + "): ");
        if (!address.isEmpty()) contact.setAddress(address);

        String dob = inputOptionalDob("Ngày sinh (" + contact.getDob() + "): ");
        if (!dob.isEmpty()) contact.setDob(dob);

        String email = inputOptionalEmail("Email (" + contact.getEmail() + "): ");
        if (!email.isEmpty()) contact.setEmail(email);

        System.out.println("Đã cập nhật danh bạ thành công!");
    }

    // ================== Xóa danh bạ ==================
    public void deleteContact() {
        System.out.println("----- Xóa danh bạ -----");
        if (contacts.isEmpty()) {
            System.out.println("Danh bạ trống!");
            return;
        }

        Contact contact = null;
        while (true) {
            System.out.print("Nhập số điện thoại muốn xóa (Enter để thoát): ");
            String phone = scanner.nextLine();
            if (phone.isEmpty()) return;

            contact = findByPhone(phone);
            if (contact == null) System.out.println("Không tìm thấy danh bạ!");
            else break;
        }

        System.out.print("Xác nhận xóa (Y/N): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            contacts.remove(contact);
            System.out.println("Đã xóa danh bạ!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }

    // ================== Tìm kiếm ==================
    public void searchContact() {
        System.out.print("Nhập số điện thoại hoặc họ tên cần tìm: ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.getFullName().toLowerCase().contains(keyword) || c.getPhoneNumber().contains(keyword)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) System.out.println("Không tìm thấy danh bạ nào!");
    }

    // ================== Đọc từ file CSV ==================
    public void readFromFile() {
        System.out.print("Cảnh báo: sẽ xóa toàn bộ danh bạ hiện tại! Bạn có muốn tiếp tục (Y/N)? ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            contacts = repository.readContacts();
            System.out.println("Đã tải danh bạ từ file CSV!");
        } else System.out.println("Hủy thao tác.");
    }

    // ================== Lưu vào file CSV ==================
    public void saveToFile() {
        System.out.print("Cảnh báo: sẽ ghi đè nội dung file CSV! Bạn có muốn tiếp tục (Y/N)? ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            repository.writeContacts(contacts);
            System.out.println("Đã lưu danh bạ vào file CSV!");
        } else System.out.println("Hủy thao tác.");
    }

    // ================== Hỗ trợ kiểm tra dữ liệu ==================
    private Contact findByPhone(String phone) {
        for (Contact c : contacts) {
            if (c.getPhoneNumber().equals(phone)) return c;
        }
        return null;
    }

    private String inputRequired(String msg) {
        String value;
        while (true) {
            System.out.print(msg);
            value = scanner.nextLine();
            if (!value.trim().isEmpty()) break;
            System.out.println("Trường này bắt buộc nhập!");
        }
        return value;
    }

    private String inputOptional(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    // ================== Kiểm tra số điện thoại ==================
    private String inputPhoneNumber() {
        String phone;
        while (true) {
            System.out.print("Nhập số điện thoại (10 số, bắt đầu bằng 0): ");
            phone = scanner.nextLine().trim();
            if (phone.isEmpty()) System.out.println("Số điện thoại bắt buộc nhập!");
            else if (!phone.matches("0\\d{9}")) System.out.println("Số điện thoại không hợp lệ! Phải 10 chữ số và bắt đầu bằng 0.");
            else break;
        }
        return phone;
    }

    private String inputOptionalPhone(String msg) {
        String phone;
        while (true) {
            System.out.print(msg);
            phone = scanner.nextLine().trim();
            if (phone.isEmpty()) return "";
            else if (!phone.matches("0\\d{9}")) System.out.println("Số điện thoại không hợp lệ! Phải 10 chữ số và bắt đầu bằng 0.");
            else return phone;
        }
    }

    // ================== Kiểm tra email ==================
    private String inputEmail() {
        String email;
        while (true) {
            System.out.print("Nhập email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) return "";
            else if (!Pattern.matches("^\\S+@\\S+\\.\\S+$", email)) System.out.println("Email không hợp lệ!");
            else break;
        }
        return email;
    }

    private String inputOptionalEmail(String msg) {
        String email;
        while (true) {
            System.out.print(msg);
            email = scanner.nextLine().trim();
            if (email.isEmpty()) return "";
            else if (!Pattern.matches("^\\S+@\\S+\\.\\S+$", email)) System.out.println("Email không hợp lệ!");
            else return email;
        }
    }

    // ================== Kiểm tra ngày sinh ==================
    private String inputDob() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String dob;
        while (true) {
            System.out.print("Nhập ngày sinh (yyyy-MM-dd): ");
            dob = scanner.nextLine().trim();
            if (dob.isEmpty()) return "";
            try {
                Date date = sdf.parse(dob);
                break;
            } catch (ParseException e) {
                System.out.println("Ngày sinh không hợp lệ! Nhập đúng định dạng yyyy-MM-dd.");
            }
        }
        return dob;
    }

    private String inputOptionalDob(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String dob;
        while (true) {
            System.out.print(msg);
            dob = scanner.nextLine().trim();
            if (dob.isEmpty()) return "";
            try {
                sdf.parse(dob);
                break;
            } catch (ParseException e) {
                System.out.println("Ngày sinh không hợp lệ! Nhập đúng định dạng yyyy-MM-dd.");
            }
        }
        return dob;
    }
}
