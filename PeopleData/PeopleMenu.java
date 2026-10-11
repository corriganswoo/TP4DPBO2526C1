import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    // Komponen dari form
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JComboBox<String> genderComboBox;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;
    private JLabel titleLabel;

    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    // CONSTRUCTOR
    public PeopleMenu() {
        listPerson = new ArrayList<>();
        populateList();
        peopleTable.setModel(setTable());

        if (titleLabel != null) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        }

        // Setting pilihan dropdown
        String[] kategoriData = {"--- Pilih Kategori ---", "Mahasiswa", "Dosen", "Tendik"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        String[] genderData = {"--- Pilih Gender ---", "Laki-laki", "Perempuan"};
        genderComboBox.setModel(new DefaultComboBoxModel<>(genderData));

        // Sembunyikan tombol delete awal
        deleteButton.setVisible(false);

        // Tombol Add / Update
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // Tombol Delete (Diberi Konfirmasi Pop-up)
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // Tombol Cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Klik baris di tabel
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();

                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curGender = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                genderComboBox.setSelectedItem(curGender);

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    // Data awal/dummy
    public void populateList() {
        listPerson.add(new Person("P001", "Budi Santoso", 1995, "Mahasiswa", "Laki-laki"));
        listPerson.add(new Person("P002", "Siti Aminah", 1990, "Dosen", "Perempuan"));
    }

    // Mengatur data tabel
    public DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Tahun Lahir", "Kategori", "Gender"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                    i + 1,
                    listPerson.get(i).getId(),
                    listPerson.get(i).getNama(),
                    listPerson.get(i).getTahunLahir(),
                    listPerson.get(i).getKategori(),
                    listPerson.get(i).getGender()
            };
            tmp.addRow(row);
        }
        return tmp;
    }

    // Fungsi Tambah Data
    public void insertData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String gender = genderComboBox.getSelectedItem().toString();

            listPerson.add(new Person(id, nama, tahunLahir, kategori, gender));
            peopleTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fungsi Update Data
    public void updateData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String gender = genderComboBox.getSelectedItem().toString();

            listPerson.get(selectedIndex).setId(id);
            listPerson.get(selectedIndex).setNama(nama);
            listPerson.get(selectedIndex).setTahunLahir(tahunLahir);
            listPerson.get(selectedIndex).setKategori(kategori);
            listPerson.get(selectedIndex).setGender(gender);

            peopleTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fungsi Hapus Data
    public void deleteData() {
        listPerson.remove(selectedIndex);
        peopleTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
    }

    // Bersihkan Form
    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        genderComboBox.setSelectedIndex(0);
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    // MAIN METHOD
    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}