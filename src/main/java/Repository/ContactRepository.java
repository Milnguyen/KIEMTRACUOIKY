package Repository;

import Model.Contact;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    private static final String FILE_PATH = "data/contacts.csv";

    // Đọc danh bạ từ CSV
    public List<Contact> readContacts() {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = br.readLine(); // Bỏ qua header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) {
                    contacts.add(new Contact(values[0], values[1], values[2], values[3],
                            values[4], values[5], values[6]));
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc file CSV: " + e.getMessage());
        }
        return contacts;
    }

    // Ghi danh bạ ra CSV
    public void writeContacts(List<Contact> contacts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("PhoneNumber,Group,FullName,Gender,Address,DOB,Email\n");
            for (Contact c : contacts) {
                bw.write(c.getPhoneNumber() + "," + c.getGroup() + "," +
                        c.getFullName() + "," + c.getGender() + "," +
                        c.getAddress() + "," + c.getDob() + "," + c.getEmail() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Không thể ghi file CSV: " + e.getMessage());
        }
    }
}
