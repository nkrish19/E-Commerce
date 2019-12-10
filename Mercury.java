import java.util.*;



class Mercury {

    interface Property
    {
    double GetReward();
    void GetDetails();
    }



    private static class Customer implements Property {

        final String C_Name;
        final int C_ID;
        final String C_Address;
        private double Main_ACCBalance;
        private double Rew_ACCBalance;
        private double Reward;
        private int Num_Order;
        private Queue<Product> Cart;
        private ArrayList<Integer> Cart_qty;
        private ArrayList<Product> OrderHistory;
    
        public Customer(String name, int id, String address) {
    
            this.C_Name = name;
            this.C_ID = id;
            this.C_Address = address;
            this.Main_ACCBalance = 100;
            this.Rew_ACCBalance = 0;
            this.Reward = 0;
            this.Num_Order = 0;
            this.Cart = new LinkedList<Product>();
            this.Cart_qty = new ArrayList<Integer>(); 
            this.OrderHistory = new ArrayList<Product>();
        }
    
        // To be used when asked to Display Details of the Customer
        public void GetDetails() {
            System.out.println("Name: "+this.C_Name);
            System.out.println("Address: "+this.C_Address);
            System.out.println("Number of Orders Placed: "+this.Num_Order);
        }
    
        // To be used while displaying list of Customers in Menu
        public void GetMenuDetails() {
            System.out.println(this.C_ID+" "+this.C_Name);
        }

        public void ViewOrderHistory() {
            
            for (int i=0; i<this.GetOrderHistory().size(); i++) {

                Product p = this.GetOrderHistory().get(i);
                System.out.println("Bought item "+p.GetName());
                System.out.println("Item quantity "+p.GetSQ());

                if (p.GetOffer() == 2){
                    
                    // p.GetSQ() is even
                    if (p.GetSQ()%2 == 0){
                        System.out.println("Item price "+(p.GetSP()*(p.GetSQ()/2)));
                    }
                    // it is odd
                    else {
                        System.out.println("Item price "+(p.GetSP()*((p.GetSQ()/2)+1 )));
                    }
                }
                else {
                    System.out.println("Item price "+(p.GetSP()*p.GetSQ()));
                }

                System.out.println("Merchant name " +p.GetMerchant().GetName());
            }
        }

        public ArrayList<Product> GetOrderHistory() {
            return this.OrderHistory;
        }

        public void AddIteminHistory(Product p) {

            if (this.GetOrderHistory().size() < 10){
                this.OrderHistory.add(p);
            }
            else {
                this.GetOrderHistory().remove(0);
                this.OrderHistory.add(p);
            }
        }

        public void AddItemtoCart(Product p, int q) {
            this.Cart.add(p);
            this.Cart_qty.add(q);
        }

        public void ClearCart() {
            this.Cart = new LinkedList<>();
            this.Cart_qty = new ArrayList<>();
        }

        public void CartCheckout() {
            
            int i=0;

            while (this.Cart.size()>0) {
                this.BuyItem(this.Cart_qty.get(i), this.Cart.remove());
                i++;
            }
            this.ClearCart();
        }
    
        public String GetName() {
            return this.C_Name;
        }
    
        public void GetID() {
            System.out.println(this.C_ID);
        }
    
        public void GetAddress() {
            System.out.println(this.C_Address);
        }
    
        public double GetMain_ACCBalance() {
            return this.Main_ACCBalance;
        }
    
        public double GetRew_ACCBalance() {
            return this.Rew_ACCBalance;
        }
    
        public int GetNum_Order() {
            return this.Num_Order;
        }

        public double GetReward() {
            return this.Reward;
        }

        public void PrintReward() {
            System.out.println("Reward won is "+GetReward());
        }

        public void SetReward(double p) {
            this.Reward = this.GetReward() + p;
        }

        public void SetMain_ACCBalance(double p) {
            this.Main_ACCBalance = p;
        }

        public void SetRew_ACCBalance(double p) {
            this.Rew_ACCBalance = this.GetRew_ACCBalance() + p;
            this.SetReward(p);
        }

        public void SetNum_Order() {
            this.Num_Order++;
        }

        public void BuyItem(int q, Product prod) {

            double p = prod.GetPrice();
            int actualqtybought = q;

            // Applying offer
            int o = prod.GetOffer();
            if (o==1) {
                ;
            }
            else if (o==2) {
                q = q/2;
            }
            else if (o==3) {
                p = p*(0.75);
            }

            // If only main account needed
            if (this.GetMain_ACCBalance() >= (p*q)) {
                this.SetMain_ACCBalance(this.GetMain_ACCBalance() - (p*q));
                this.SetNum_Order();
                prod.SetQuantity(prod.GetQuantity()-actualqtybought);
                prod.GetMerchant().SetCommission(p*q);
                prod.SetBill(p,actualqtybought);

                // Adding reward money
                if (this.GetNum_Order()%5 == 0) {
                    this.SetRew_ACCBalance(10);
                }
                if (prod.GetMerchant().GetCommission()%100 == 0) {
                    prod.GetMerchant().SetMaxUnique();
                }

                // Updating order history
                this.AddIteminHistory(prod);
    
                System.out.println("Item Successfully Bought");
            }

            // Reward account needed
            else if (this.GetMain_ACCBalance() + this.GetRew_ACCBalance() >= (p*q)) {
                this.SetMain_ACCBalance(0);
                this.SetRew_ACCBalance(-(p*q));
                this.SetNum_Order();
                prod.SetQuantity(prod.GetQuantity()-actualqtybought);
                prod.GetMerchant().SetCommission(p*q);
                prod.SetBill(p,actualqtybought);

                // Adding reward money
                if (this.GetNum_Order()%5 == 0) {
                    this.SetRew_ACCBalance(10);
                }
                if (prod.GetMerchant().GetCommission()%100 == 0) {
                    prod.GetMerchant().SetMaxUnique();
                }

                // Updating order history
                this.AddIteminHistory(prod);

                System.out.println("Item Successfully Bought");
            }
            
            // Insufficient funds
            else {
                System.out.println("Not enough money");
            }
        }
    }
    
    
    
    private static class Merchant {
    
        final String M_Name;
        final int M_ID;
        final String M_Address;
        private double Commission;     // Contribution to the company
        private Map Item_List;
        private int UniqueCount;
        private int MaxUnique;
    
        public Merchant(String name, int id, String add) {
    
            this.M_Name = name;
            this.M_ID = id;
            this.M_Address = add;
            this.Commission = 0; 
            this.UniqueCount = 0; 
            this.MaxUnique = 10;  
    
            Map<Integer, Product> il = new HashMap<>();
            this.Item_List = il;
        }
    
        // To be used when asked to Display Details of the Merchant
        public void GetDetails() {
            System.out.println("Name: "+this.M_Name);
            System.out.println("Address: "+this.M_Address);
            System.out.println("Contribution to the company: "+this.Commission);
        }
    
        // To be used while displaying list of Customers in Menu
        public void GetMenuDetails() {
            System.out.println(this.M_ID+" "+this.M_Name);
        }
    
        public String GetName() {
            return this.M_Name;
        }
    
        public void GetID() {
            System.out.println(this.M_ID);
        }
    
        public void GetAddress() {
            System.out.println(this.M_Address);
        }
    
        public double GetCommission() {
            return this.Commission;
        }

        public int GetUniqueCount() {
            return this.UniqueCount;
        }

        public int GetMaxUnique() {
            return this.MaxUnique;
        }

        public void PrintReward() {
            int extra_slot = this.GetMaxUnique();
            extra_slot = extra_slot - 10;
            System.out.println("Rewards won "+extra_slot);
        }

        public Map GetItemList() {
            return this.Item_List;
        }

        public void SetMaxUnique() {
            this.MaxUnique++;
        }

        public void SetUniqueCount() {
            this.UniqueCount++;
        }

        public void SetCommission(double p) {
            this.Commission = this.GetCommission() + (p*(0.0005));
        }

        public void AddItemList (Product item) {

            int id = item.GetID();
            Map<Integer, Product> replace = this.Item_List;
            
            boolean check = false;
            for (Map.Entry<Integer, Product> entry : replace.entrySet()) {
                if (entry.getValue().GetCategory() == item.GetCategory()){
                    check = true;
                }
            }
            if (!(check)) {
                this.SetUniqueCount();
            }

            this.Item_List.put(id, item);
        }
    
    }
    
    
    
    private static class Product {
    
        final String I_Name;
        final int I_ID;
        final String Category;
        private Merchant Merch;
        private double Price;
        private int Offer;      // 1:No Offer; 2:B1G1; 3:25% Off
        private int Quantity;
        private static int id = 1;

        private double SP;
        private int SQ;
    
        public Product(String name, String cat, double price, int qty, Merchant m) {
    
            this.I_Name = name;
            this.I_ID = id;
            this.Category = cat;
            this.Price = price;
            this.Quantity = qty;
            this.Offer = 1;
            this.Merch = m;
            id ++;
        }
        
        // Display details in the required format in query 1 of merchant
        public void GetDetails() {
    
            if (this.Offer==1) {
                System.out.println(this.I_ID+" "+this.I_Name+" "+this.Quantity+" None "+this.Category);
            }
            else if (this.Offer==2) {
                System.out.println(this.I_ID+" "+this.I_Name+" "+this.Quantity+" Buy 1 Get 1 Free "+this.Category);
            }
            else if (this.Offer==3) {
                System.out.println(this.I_ID+" "+this.I_Name+" "+this.Quantity+" 25% Off "+this.Category);
            }
        }
    
        public String GetName() {
            return this.I_Name;
        }
    
        public int GetID() {
            return this.I_ID;
        }
    
        public String GetCategory() {
            return this.Category;
        }
    
        public double GetPrice() {
            return this.Price;
        }
    
        public int GetQuantity() {
            return this.Quantity;
        }
    
        public int GetOffer() {
            return this.Offer;
        }

        public Merchant GetMerchant() {
            return this.Merch;
        }

        public double GetSP() {
            return this.SP;
        }

        public int GetSQ() {
            return this.SQ;
        }
    
        public void SetPrice(double p) {
            this.Price = p;
        }
    
        public void SetQuantity(int q) {
            this.Quantity = q;
        }
    
        public void SetOffer(int o) {
            this.Offer = o;
        }

        public void SetBill(double p, int q) {
            this.SP = p;
            this.SQ = q;
        }
    }
    
    
    
    private static class Menu {
    
        public Menu() {
    
        }
    
        public void DisplayHomeMenu() {
            
            System.out.println("Welcome to Mercury");
            System.out.println("1) Enter as Merchant");
            System.out.println("2) Enter as Customer");
            System.out.println("3) See user Details");
            System.out.println("4) Company account balance");
            System.out.println("5) Exit");
        }
    
        public void DisplayMerchantMenu() {
    
            System.out.println("Choose Merchant");
            System.out.println("1) Jack");
            System.out.println("2) John");
            System.out.println("3) James");
            System.out.println("4) Jeff");
            System.out.println("5) Joseph");
        }
    
        public void DisplayCustomerMenu() {
    
            System.out.println("Choose Customer");
            System.out.println("1) Ali");
            System.out.println("2) Nobby");
            System.out.println("3) Bruno");
            System.out.println("4) Borat");
            System.out.println("5) Aladeen");
        }
    
        public void DisplayCustomerOptions(String name) {
    
            System.out.println("Welcome "+name);
            System.out.println("Customer Menu");
            System.out.println("1) Search Item");
            System.out.println("2) Checkout Cart");
            System.out.println("3) Reward won");
            System.out.println("4) Print Lates Orders");
            System.out.println("5) Exit");
        }
    
        public void DisplayMerchantOptions(String name) {
    
            System.out.println("Welcome "+name);
            System.out.println("Merchant Menu");
            System.out.println("1) Add item");
            System.out.println("2) Edit item");
            System.out.println("3) Search by category");
            System.out.println("4) Add offer");
            System.out.println("5) Rewards won");
            System.out.println("6) Exit");
        }
    }



    public static void main(String[] args) {
        

        /*
        Creating Menu object
        Creating Customer and Merchant Objects and 
        Creating a List for Respective Objects and
        Adding them in the list
        */
        Menu menu = new Menu();
        Scanner sc = new Scanner(System.in);
        double Comp_Earning = 0;

        Merchant Jack = new Merchant("Jack", 1, "Rashtrapati Bhavan");
        Merchant John = new Merchant("John", 2, "White House");
        Merchant James = new Merchant("James", 3, "Eiffel Tower");
        Merchant Jeff = new Merchant("Jeff", 4, "Taj Mahal");
        Merchant Joseph = new Merchant("Joseph", 5, "Haveli");

        Customer Ali = new Customer("Ali", 1, "House Number 1");
        Customer Nobby = new Customer("Nobby", 2, "House Number 2");
        Customer Bruno = new Customer("Bruno", 3, "House Number  3");
        Customer Borat = new Customer("Borat", 4, "House Number 4");
        Customer Aladeen = new Customer("Aladeen", 5, "House Number 5");

        ArrayList<Merchant> Merchant_List = new ArrayList<>();
        ArrayList<Customer> Customer_List = new ArrayList<>();
        ArrayList<Product> Product_List = new ArrayList<>();
        ArrayList<String> Category_List = new ArrayList<>();

        Merchant_List.add(Jack);
        Merchant_List.add(John);
        Merchant_List.add(James);
        Merchant_List.add(Jeff);
        Merchant_List.add(Joseph);

        Customer_List.add(Ali);
        Customer_List.add(Nobby);
        Customer_List.add(Bruno);
        Customer_List.add(Borat);
        Customer_List.add(Aladeen);

        

        /*
        Running the main program
        */
        while (true) {

            menu.DisplayHomeMenu();
            int HomeMenuQuery = sc.nextInt();



            // Enter as merchant
            if (HomeMenuQuery==1) {

                menu.DisplayMerchantMenu();
                int userid = sc.nextInt();
                boolean MerchantMenu = false;


                while (!(MerchantMenu)) {

                    menu.DisplayMerchantOptions(Merchant_List.get(userid-1).GetName());
                    int MerchantMenuQuery = sc.nextInt();


                    // Add item
                    if (MerchantMenuQuery==1) {

                        if (Merchant_List.get(userid-1).GetUniqueCount() < Merchant_List.get(userid-1).GetMaxUnique()) {

                            // Taking input
                            System.out.println("Enter item details");
                            System.out.println("Item Name");
                            String itemname = sc.next();
                            System.out.println("Item Price");
                            double itemprice = sc.nextInt();
                            System.out.println("Item Quantity");
                            int itemqtty = sc.nextInt();
                            System.out.println("Item Category");
                            String itemcat = sc.next();

                            // Creating diff structures
                            Product prod = new Product(itemname, itemcat, itemprice, itemqtty, Merchant_List.get(userid-1));
                            Merchant_List.get(userid-1).AddItemList(prod);
                            Product_List.add(prod);
                            prod.GetDetails();
                            if (!(Category_List.contains(prod.GetCategory()))) {
                                Category_List.add(prod.GetCategory());
                            }
                        }

                        else {
                            System.out.println("Maximum Unique item count limit reached.");
                        }

                    }


                    // Edit item
                    else if (MerchantMenuQuery==2) {

                        // Taking input and printing details
                        System.out.println("Choose item by code");

                        Map<Integer, Product> replace = Merchant_List.get(userid-1).GetItemList();
                        for (Map.Entry<Integer, Product> entry : replace.entrySet()){
                            entry.getValue().GetDetails();
                        }

                        int edit_id = sc.nextInt();
                        System.out.println("Enter edit details");
                        System.out.println("Item Price");
                        double edit_price = sc.nextInt();
                        System.out.println("Item Quantity");
                        int edit_qutty = sc.nextInt();

                        // Editing and displaying the details in product
                        for (int i=0; i<Product_List.size(); i++) {
                            if (Product_List.get(i).GetID() == edit_id) {
                                Product_List.get(i).SetPrice(edit_price);
                                Product_List.get(i).SetQuantity(edit_qutty);
                                Product_List.get(i).GetDetails();
                            }
                        }                        
                    }


                    // Serch by category
                    else if (MerchantMenuQuery==3) {

                        // Printing and taking input
                        int index = 0;
                        System.out.println("Choose a category");
                        for (int i=0; i<Category_List.size(); i++) {
                            index = i+1;
                            System.out.println(index+") "+Category_List.get(i));
                        }
                        index = sc.nextInt()-1;
                        
                        // Printing products of this category
                        String search_cat = Category_List.get(index);
                        
                        for (int i=0; i<Product_List.size(); i++) {
                            if (Product_List.get(i).GetCategory().equals(search_cat)) {
                                Product_List.get(i).GetDetails();
                            }
                        }
                    }
                    

                    // Add offer
                    else if (MerchantMenuQuery==4) {

                        // Printing item list and aking input
                        System.out.print("Choose item by code");
                        Map<Integer, Product> replace = Merchant_List.get(userid-1).GetItemList();
                        for (Map.Entry<Integer, Product> entry : replace.entrySet()){
                            entry.getValue().GetDetails();
                        }
                        int offerid = sc.nextInt();
                        System.out.println("Choose offer");
                        System.out.println("1) Buy one Get one");
                        System.out.println("2) 25% off");
                        int offercode = sc.nextInt();

                        // Adding Offer
                        Product_List.get(offerid-1).SetOffer(offercode+1);
                        Product_List.get(offerid-1).GetDetails();
                    }


                    // Rewards won
                    else if (MerchantMenuQuery==5) {
                        Merchant_List.get(userid-1).PrintReward();
                    }


                    // Exit
                    else if (MerchantMenuQuery==6) {
                        MerchantMenu = true;
                    }
                }
            }



            // Enter as customer
            else if (HomeMenuQuery==2) {

                menu.DisplayCustomerMenu();
                int userid = sc.nextInt();
                boolean CustomerMenu = false;


                while (!(CustomerMenu)) {

                    menu.DisplayCustomerOptions(Customer_List.get(userid-1).GetName());
                    int CustomerMenuQuery = sc.nextInt();


                    // Search item
                    if (CustomerMenuQuery==1) {

                        // Printing and taking input
                        int index = 0;
                        System.out.println("Choose a category");
                        for (int i=0; i<Category_List.size(); i++) {
                            index = i+1;
                            System.out.println(index+") "+Category_List.get(i));
                        }
                        index = sc.nextInt()-1;
                        
                        // Printing products of this category
                        String search_cat = Category_List.get(index);
                        
                        for (int i=0; i<Product_List.size(); i++) {
                            if (Product_List.get(i).GetCategory().equals(search_cat)) {
                                Product_List.get(i).GetDetails();
                            }
                        }

                        // Purchasing item
                        System.out.println("Enter item code");
                        int custitemcode = sc.nextInt();
                        System.out.println("Enter item quantity");
                        int custitemqtty = sc.nextInt();
                        System.out.println("Choose method of transaction");
                        System.out.println("1) Buy item");
                        System.out.println("2) Add item to cart");
                        System.out.println("3) Exit");
                        int trnscmethod = sc.nextInt();

                        // Implementing choice of transaction
                        int max_qtty = Product_List.get(custitemcode-1).GetQuantity();
                        if (Product_List.get(custitemcode-1).GetOffer()==2) {
                            if (max_qtty <= (2*custitemqtty)) {
                                custitemqtty = max_qtty;
                            }
                            else {
                                custitemqtty = (2*custitemqtty);
                            }
                        }

                        if (Product_List.get(custitemcode-1).GetQuantity() >= custitemqtty) {

                            if (trnscmethod==1) {
                                Customer_List.get(userid-1).BuyItem(custitemqtty, Product_List.get(custitemcode-1));
                            }

                            else if (trnscmethod==2) {
                                Customer_List.get(userid-1).AddItemtoCart(Product_List.get(custitemcode-1), custitemqtty);
                            }

                            else if (trnscmethod==3) {
                                continue;
                            }
                        }
                        else {
                            System.out.println("Not enough quantity");
                        }
                    }


                    // Checkout cart
                    else if (CustomerMenuQuery==2) {
                        Customer_List.get(userid-1).CartCheckout();
                    }


                    // Reward won
                    else if (CustomerMenuQuery==3) {
                        Customer_List.get(userid-1).PrintReward();
                    }
                    

                    // Print latest orders
                    else if (CustomerMenuQuery==4) {
                        Customer_List.get(userid-1).ViewOrderHistory();
                    }


                    // Exit
                    else if (CustomerMenuQuery==5) {
                        CustomerMenu = true;
                    }
                }
            }



            // See user details
            else if (HomeMenuQuery==3) {

                String MorC = sc.next();
                int userid;
                
                if (MorC.equals("M")) {
                    menu.DisplayMerchantMenu();
                    userid = sc.nextInt();
                    Merchant_List.get(userid-1).GetDetails();
                }
                else if (MorC.equals("C")) {
                    menu.DisplayCustomerMenu();
                    userid = sc.nextInt();
                    Customer_List.get(userid-1).GetDetails();
                }
            }



            // Company account balance
            else if (HomeMenuQuery==4) {
                
                double CompAccBal = 0;
                for (int i=0; i<5; i++) {
                    CompAccBal = Merchant_List.get(i).GetCommission() + CompAccBal;
                }

                System.out.println("Account Balance of the Company is "+(2*CompAccBal));
            }



            // Exit
            else if (HomeMenuQuery==5) {
                sc.close();
                System.exit(0);
            }
        }
    }
}