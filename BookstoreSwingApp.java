import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookstoreSwingApp extends JFrame {
    private Store store;
    private User currentUser = null;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JPanel loginPanel;
    private JPanel adminPanel;
    private JPanel vendorPanel;
    private JPanel customerPanel;

    private JLabel userStatusLabel;
    private JButton logoutBtn;

    public static final String[] CATEGORIES = {"Fiction", "Crime", "Horror", "Comedy"};

    public BookstoreSwingApp() {
        super("Code Shelf - Desktop App (SQLite)");
        initStore();
        initUI();
    }

    private void initStore() {
        store = new Store("Code Shelf ");
        // Only seed initial sample data if SQLite database is completely fresh
        if (store.getVendors().isEmpty()) {
            try {
                store.addVendor("City Books Supply", "vendor1", "vendor123", "9876543210", "Thrissur");
                store.addVendor("Readers Hub", "vendor2", "vendor123", "9123456780", "Kochi");
                Vendor v1 = store.getVendors().get(0);
                Vendor v2 = store.getVendors().get(1);
                store.addBook(v1, "Fiction", "The Lost Island", "A. Kumar", 300, 10);
                store.addBook(v2, "Crime", "Dark Alley", "R. Nair", 250, 6);
                store.addBook(v1, "Horror", "Midnight House", "S. Paul", 200, 4);
                store.addBook(v2, "Comedy", "Laugh Out Loud", "M. Das", 150, 8);
            } catch (StoreException e) {
                System.err.println("Sample data load error: " + e.getMessage());
            }
        }
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topBar.setBackground(new Color(240, 243, 246));

        JLabel titleLabel = new JLabel("Code Shelf ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);
        userStatusLabel = new JLabel("Please log in");
        logoutBtn = new JButton("Logout");
        logoutBtn.setVisible(false);
        logoutBtn.addActionListener(e -> logout());

        rightTop.add(userStatusLabel);
        rightTop.add(logoutBtn);
        topBar.add(rightTop, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = createLoginPanel();
        adminPanel = new JPanel(new BorderLayout());
        vendorPanel = new JPanel(new BorderLayout());
        customerPanel = new JPanel(new BorderLayout());

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(adminPanel, "ADMIN");
        cardPanel.add(vendorPanel, "VENDOR");
        cardPanel.add(customerPanel, "CUSTOMER");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "LOGIN");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Login Card
        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBorder(BorderFactory.createTitledBorder("User Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtLoginId = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        loginCard.add(new JLabel("Login ID / Email:"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtLoginId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginCard.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        loginCard.add(btnLogin, gbc);

        JLabel infoLbl = new JLabel("<html><i>Admin ,Vendors or Customers can log in here.</i></html>");
        gbc.gridx = 1; gbc.gridy = 3;
        loginCard.add(infoLbl, gbc);

        btnLogin.addActionListener(e -> {
            try {
                User u = store.login(txtLoginId.getText(), new String(txtPassword.getPassword()));
                txtLoginId.setText("");
                txtPassword.setText("");
                onLoginSuccess(u);
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register Card
        JPanel regCard = new JPanel(new GridBagLayout());
        regCard.setBorder(BorderFactory.createTitledBorder("New Customer? Register"));
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(6, 6, 6, 6);
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtRegName = new JTextField(15);
        JTextField txtRegEmail = new JTextField(15);
        JPasswordField txtRegPass = new JPasswordField(15);
        JButton btnRegister = new JButton("Register");

        gbcR.gridx = 0; gbcR.gridy = 0;
        regCard.add(new JLabel("Full Name:"), gbcR);
        gbcR.gridx = 1;
        regCard.add(txtRegName, gbcR);

        gbcR.gridx = 0; gbcR.gridy = 1;
        regCard.add(new JLabel("Email Address:"), gbcR);
        gbcR.gridx = 1;
        regCard.add(txtRegEmail, gbcR);

        gbcR.gridx = 0; gbcR.gridy = 2;
        regCard.add(new JLabel("Password (min 4):"), gbcR);
        gbcR.gridx = 1;
        regCard.add(txtRegPass, gbcR);

        gbcR.gridx = 1; gbcR.gridy = 3;
        regCard.add(btnRegister, gbcR);

        btnRegister.addActionListener(e -> {
            try {
                store.registerCustomer(txtRegName.getText(), txtRegEmail.getText(), new String(txtRegPass.getPassword()));
                JOptionPane.showMessageDialog(this, "Registration successful. Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtRegName.setText("");
                txtRegEmail.setText("");
                txtRegPass.setText("");
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginCard);
        panel.add(regCard);
        return panel;
    }

    private void onLoginSuccess(User u) {
        this.currentUser = u;
        userStatusLabel.setText("Logged in as: " + u.getName() + " (" + u.getRole() + ")");
        logoutBtn.setVisible(true);

        if (u instanceof Admin) {
            setupAdminPanel();
            cardLayout.show(cardPanel, "ADMIN");
        } else if (u instanceof Vendor) {
            setupVendorPanel((Vendor) u);
            cardLayout.show(cardPanel, "VENDOR");
        } else if (u instanceof Customer) {
            setupCustomerPanel((Customer) u);
            cardLayout.show(cardPanel, "CUSTOMER");
        }
    }

    private void logout() {
        currentUser = null;
        userStatusLabel.setText("Please log in");
        logoutBtn.setVisible(false);
        cardLayout.show(cardPanel, "LOGIN");
    }

    private void setupAdminPanel() {
        adminPanel.removeAll();
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Books and Stock Tab
        JPanel booksTab = new JPanel(new BorderLayout(5, 5));
        booksTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] bookCols = {"ID", "Title", "Author", "Category", "Vendor", "Price (Rs.)", "Stock"};
        DefaultTableModel bookModel = new DefaultTableModel(bookCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bookTable = new JTable(bookModel);
        Runnable refreshBooks = () -> {
            bookModel.setRowCount(0);
            for (Book b : store.search("", "")) {
                bookModel.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(),
                    b.getVendor().getName(), String.format("%.2f", b.getPrice()), b.getQuantity()
                });
            }
        };
        refreshBooks.run();

        JPanel bookActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteBookBtn = new JButton("Delete Selected Book");
        deleteBookBtn.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.");
                return;
            }
            int id = (int) bookModel.getValueAt(row, 0);
            try {
                store.deleteBook(id);
                refreshBooks.run();
                JOptionPane.showMessageDialog(this, "Book deleted.");
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        bookActionPanel.add(deleteBookBtn);

        booksTab.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        booksTab.add(bookActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Books & Stock", booksTab);

        // 2. Vendors Tab
        JPanel vendorTab = new JPanel(new BorderLayout(10, 10));
        vendorTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel addVendorForm = new JPanel(new GridLayout(6, 2, 6, 6));
        addVendorForm.setBorder(BorderFactory.createTitledBorder("Add New Vendor"));
        JTextField vName = new JTextField();
        JTextField vLogin = new JTextField();
        JPasswordField vPass = new JPasswordField();
        JTextField vPhone = new JTextField();
        JTextField vCity = new JTextField();
        JButton btnAddV = new JButton("Add Vendor");

        addVendorForm.add(new JLabel("Vendor Name:")); addVendorForm.add(vName);
        addVendorForm.add(new JLabel("Login ID:")); addVendorForm.add(vLogin);
        addVendorForm.add(new JLabel("Password:")); addVendorForm.add(vPass);
        addVendorForm.add(new JLabel("Phone (digits):")); addVendorForm.add(vPhone);
        addVendorForm.add(new JLabel("City:")); addVendorForm.add(vCity);
        addVendorForm.add(new JLabel("")); addVendorForm.add(btnAddV);

        String[] vCols = {"Name", "Login ID", "Phone", "City", "Books Added"};
        DefaultTableModel vendorModel = new DefaultTableModel(vCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable vendorTable = new JTable(vendorModel);
        Runnable refreshVendors = () -> {
            vendorModel.setRowCount(0);
            for (Vendor v : store.getVendors()) {
                vendorModel.addRow(new Object[]{
                    v.getName(), v.getLoginId(), v.getPhone(), v.getCity(), store.booksOf(v).size()
                });
            }
        };
        refreshVendors.run();

        btnAddV.addActionListener(e -> {
            try {
                store.addVendor(vName.getText(), vLogin.getText(), new String(vPass.getPassword()),
                                vPhone.getText(), vCity.getText());
                refreshVendors.run();
                vName.setText(""); vLogin.setText(""); vPass.setText(""); vPhone.setText(""); vCity.setText("");
                JOptionPane.showMessageDialog(this, "Vendor added successfully.");
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        vendorTab.add(addVendorForm, BorderLayout.NORTH);
        vendorTab.add(new JScrollPane(vendorTable), BorderLayout.CENTER);
        tabbedPane.addTab("Vendors", vendorTab);

        // 3. Orders Tab
        JPanel ordersTab = new JPanel(new BorderLayout());
        ordersTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] orderCols = {"Order ID", "Customer", "Email", "Book", "Qty", "Total (Rs.)", "Vendor"};
        DefaultTableModel orderModel = new DefaultTableModel(orderCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Order o : store.getOrders()) {
            orderModel.addRow(new Object[]{
                o.getId(), o.getCustomer().getName(), o.getCustomer().getEmail(),
                o.getBook().getTitle(), o.getQuantity(), String.format("%.2f", o.getTotal()),
                o.getBook().getVendor().getName()
            });
        }
        ordersTab.add(new JScrollPane(new JTable(orderModel)), BorderLayout.CENTER);
        tabbedPane.addTab("Customer Orders", ordersTab);

        // 4. Logs & Customers Tab
        JPanel logCustPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        logCustPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultListModel<String> logListModel = new DefaultListModel<>();
        for (String l : store.getLog()) logListModel.addElement(l);
        JList<String> logList = new JList<>(logListModel);
        JPanel logBox = new JPanel(new BorderLayout());
        logBox.setBorder(BorderFactory.createTitledBorder("Activity Logs"));
        logBox.add(new JScrollPane(logList), BorderLayout.CENTER);

        String[] custCols = {"Name", "Email"};
        DefaultTableModel custModel = new DefaultTableModel(custCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Customer c : store.getCustomers()) {
            custModel.addRow(new Object[]{ c.getName(), c.getEmail() });
        }
        JPanel custBox = new JPanel(new BorderLayout());
        custBox.setBorder(BorderFactory.createTitledBorder("Registered Customers"));
        custBox.add(new JScrollPane(new JTable(custModel)), BorderLayout.CENTER);

        logCustPanel.add(logBox);
        logCustPanel.add(custBox);
        tabbedPane.addTab("Logs & Customers", logCustPanel);

        adminPanel.add(tabbedPane, BorderLayout.CENTER);
        adminPanel.revalidate();
        adminPanel.repaint();
    }

    private void setupVendorPanel(Vendor v) {
        vendorPanel.removeAll();
        vendorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel mainLayout = new JPanel(new BorderLayout(10, 10));

        JPanel addForm = new JPanel(new GridLayout(3, 4, 8, 8));
        addForm.setBorder(BorderFactory.createTitledBorder("Add a New Book"));

        JTextField txtTitle = new JTextField();
        JTextField txtAuthor = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtQty = new JTextField();
        JComboBox<String> cmbCat = new JComboBox<>(CATEGORIES);
        JButton btnAdd = new JButton("Add Book");

        addForm.add(new JLabel("Title:")); addForm.add(txtTitle);
        addForm.add(new JLabel("Author:")); addForm.add(txtAuthor);
        addForm.add(new JLabel("Price (Rs.):")); addForm.add(txtPrice);
        addForm.add(new JLabel("Quantity:")); addForm.add(txtQty);
        addForm.add(new JLabel("Category:")); addForm.add(cmbCat);
        addForm.add(new JLabel("")); addForm.add(btnAdd);

        String[] cols = {"ID", "Title", "Author", "Category", "Price (Rs.)", "Stock"};
        DefaultTableModel vendorBookModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(vendorBookModel);

        Runnable refreshVendorBooks = () -> {
            vendorBookModel.setRowCount(0);
            for (Book b : store.booksOf(v)) {
                vendorBookModel.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(),
                    String.format("%.2f", b.getPrice()), b.getQuantity()
                });
            }
        };
        refreshVendorBooks.run();

        btnAdd.addActionListener(e -> {
            try {
                double price = Double.parseDouble(txtPrice.getText().trim());
                int qty = Integer.parseInt(txtQty.getText().trim());
                store.addBook(v, (String) cmbCat.getSelectedItem(), txtTitle.getText(),
                              txtAuthor.getText(), price, qty);
                refreshVendorBooks.run();
                txtTitle.setText(""); txtAuthor.setText(""); txtPrice.setText(""); txtQty.setText("");
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        stockPanel.setBorder(BorderFactory.createTitledBorder("Add Stock to Selected Book"));
        JTextField txtAddQty = new JTextField("1", 5);
        JButton btnAddStock = new JButton("Add Stock");

        stockPanel.add(new JLabel("Quantity to add:"));
        stockPanel.add(txtAddQty);
        stockPanel.add(btnAddStock);

        btnAddStock.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a book from the table first.");
                return;
            }
            int bookId = (int) vendorBookModel.getValueAt(row, 0);
            try {
                int addQty = Integer.parseInt(txtAddQty.getText().trim());
                store.addStock(v, bookId, addQty);
                refreshVendorBooks.run();
                JOptionPane.showMessageDialog(this, "Stock added successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid integer quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("My Books & Inventory"));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        centerPanel.add(stockPanel, BorderLayout.SOUTH);

        mainLayout.add(addForm, BorderLayout.NORTH);
        mainLayout.add(centerPanel, BorderLayout.CENTER);

        vendorPanel.add(mainLayout, BorderLayout.CENTER);
        vendorPanel.revalidate();
        vendorPanel.repaint();
    }

    private void setupCustomerPanel(Customer c) {
        customerPanel.removeAll();
        customerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.65);

        JPanel catalogPanel = new JPanel(new BorderLayout(5, 5));
        catalogPanel.setBorder(BorderFactory.createTitledBorder("Browse & Order Books"));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JTextField txtSearch = new JTextField(15);
        JComboBox<String> cmbFilterCat = new JComboBox<>();
        cmbFilterCat.addItem("All Categories");
        for (String cat : CATEGORIES) cmbFilterCat.addItem(cat);
        JButton btnSearch = new JButton("Search");
        JButton btnClear = new JButton("Show All");

        searchBar.add(new JLabel("Keyword:"));
        searchBar.add(txtSearch);
        searchBar.add(new JLabel("Category:"));
        searchBar.add(cmbFilterCat);
        searchBar.add(btnSearch);
        searchBar.add(btnClear);

        catalogPanel.add(searchBar, BorderLayout.NORTH);

        String[] bookCols = {"ID", "Title", "Author", "Category", "Price", "Discount", "Final Price", "Stock", "Note"};
        DefaultTableModel catalogModel = new DefaultTableModel(bookCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable catalogTable = new JTable(catalogModel);

        Runnable refreshCatalog = () -> {
            catalogModel.setRowCount(0);
            String kw = txtSearch.getText().trim();
            String cat = (String) cmbFilterCat.getSelectedItem();
            if ("All Categories".equals(cat)) cat = "";

            for (Book b : store.search(kw, cat)) {
                catalogModel.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(),
                    String.format("Rs.%.2f", b.getPrice()),
                    (int) b.getDiscountPercent() + "%",
                    String.format("Rs.%.2f", b.getFinalPrice()),
                    b.getQuantity(), b.getNote()
                });
            }
        };
        refreshCatalog.run();

        btnSearch.addActionListener(e -> refreshCatalog.run());
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            cmbFilterCat.setSelectedIndex(0);
            refreshCatalog.run();
        });

        JPanel ordersPanel = new JPanel(new BorderLayout(5, 5));
        ordersPanel.setBorder(BorderFactory.createTitledBorder("My Orders"));
        String[] orderCols = {"Order ID", "Book Title", "Quantity", "Total"};
        DefaultTableModel myOrderModel = new DefaultTableModel(orderCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable myOrdersTable = new JTable(myOrderModel);

        Runnable refreshOrders = () -> {
            myOrderModel.setRowCount(0);
            for (Order o : store.getOrdersOf(c)) {
                myOrderModel.addRow(new Object[]{
                    o.getId(), o.getBook().getTitle(), o.getQuantity(), String.format("Rs.%.2f", o.getTotal())
                });
            }
        };
        refreshOrders.run();
        ordersPanel.add(new JScrollPane(myOrdersTable), BorderLayout.CENTER);

        JPanel orderActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JTextField txtOrderQty = new JTextField("1", 5);
        JButton btnPlaceOrder = new JButton("Place Order");

        orderActionPanel.add(new JLabel("Qty to purchase:"));
        orderActionPanel.add(txtOrderQty);
        orderActionPanel.add(btnPlaceOrder);

        btnPlaceOrder.addActionListener(e -> {
            int row = catalogTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to order.");
                return;
            }
            int bookId = (int) catalogModel.getValueAt(row, 0);
            try {
                int qty = Integer.parseInt(txtOrderQty.getText().trim());
                String msg = store.placeOrder(c, bookId, qty);
                JOptionPane.showMessageDialog(this, msg, "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
                refreshCatalog.run();
                refreshOrders.run();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (StoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        catalogPanel.add(new JScrollPane(catalogTable), BorderLayout.CENTER);
        catalogPanel.add(orderActionPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(catalogPanel);
        splitPane.setBottomComponent(ordersPanel);

        customerPanel.add(splitPane, BorderLayout.CENTER);
        customerPanel.revalidate();
        customerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new BookstoreSwingApp().setVisible(true);
        });
    }
}
