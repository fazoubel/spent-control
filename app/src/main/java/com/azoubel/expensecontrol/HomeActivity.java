package com.azoubel.expensecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.azoubel.expensecontrol.controller.Controller;
import com.azoubel.expensecontrol.model.Address;
import com.azoubel.expensecontrol.model.CreditCard;
import com.azoubel.expensecontrol.model.Expense;
import com.azoubel.expensecontrol.model.Payment;
import com.azoubel.expensecontrol.model.Promotion;
import com.azoubel.expensecontrol.model.Store;
import com.azoubel.expensecontrol.model.User.Person;
import com.azoubel.expensecontrol.model.User.User;
import com.azoubel.expensecontrol.ui.ExpensesActivity;
import com.azoubel.expensecontrol.ui.PaymentsActivity;
import com.azoubel.expensecontrol.ui.PromotionActivity;
import com.azoubel.expensecontrol.ui.StoreActivity;
import com.azoubel.expensecontrol.ui.UserActivity;
import com.azoubel.expensecontrol.ui.view.ExpensesView;
import com.azoubel.expensecontrol.ui.view.PaymentsView;
import com.azoubel.expensecontrol.ui.view.PromotionsView;
import com.azoubel.expensecontrol.ui.view.StoreView;
import com.azoubel.expensecontrol.ui.view.UsersView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SHOW_USERS_VIEW = 0;
    private static final int SHOW_EXPENSES_VIEW = 1;
    private static final int SHOW_PAYMENTS_VIEW = 2;
    private static final int SHOW_STORES_VIEW = 3;
    private static final int SHOW_PROMOTIONS_VIEW = 4;

    private static final int USER_ACTIVITY = 0;
    private static final int EXPENSE_ACTIVITY = 1;
    private static final int PAYMENT_ACTIVITY = 2;
    private static final int STORE_ACTIVITY = 3;
    private static final int PROMOTION_ACTIVITY = 4;

    private static Controller controller;
    private List<User> users;

    private UsersView usersView;
    private ExpensesView expensesView;
    private StoreView storesView;
    private PromotionsView promotionsView;

    private PaymentsView paymentsView;
    private FloatingActionButton addButton;
    private FloatingActionButton editButton;
    private FloatingActionButton openButton;

    private Menu navigationMenu;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        editButton = (FloatingActionButton) findViewById(R.id.edit_button);
        openButton = (FloatingActionButton) findViewById(R.id.open_button);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ConstraintLayout appBarHome = findViewById(R.id.app_bar);

        usersView = appBarHome.findViewById(R.id.users_view_layout);

        expensesView = appBarHome.findViewById(R.id.expenses_view_layout);

        paymentsView = appBarHome.findViewById(R.id.payments_view_layout);

        storesView = appBarHome.findViewById(R.id.stores_view_layout);

        promotionsView = appBarHome.findViewById(R.id.promotions_view_layout);

        if(controller == null) {
            controller = new Controller();
        }

        users = controller.loadAllUsers(this);

        if(users == null || users.isEmpty()) {
            controller.populateDatabase(this);
            users = controller.loadAllUsers(this);
        }

        usersView.setData(users, this);

        changeView(SHOW_USERS_VIEW);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        navigationMenu = navigationView.getMenu();

        addUsersMenu(navigationMenu);
        updateFloatButton();
    }

    private long getStartDate() {
        Calendar calendar = Calendar.getInstance();
        Date now  = new Date();
        now.setDate(calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        long startDate = now.getTime();
        return startDate;
    }

    private long getEndDate() {
        Calendar calendar = Calendar.getInstance();
        Date now  = new Date();
        now.setDate(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long endDate = now.getTime();
        return endDate;
    }

     private void addUsersMenu(Menu naviMenu) {
        if(users != null && !users.isEmpty()) {
            for (User user : this.users) {
                if(user instanceof Person) {
                    Person person = (Person) user;
                    MenuItem usersItem = naviMenu.add( R.id.menuUsers, (int) user.getUserId(), Menu.NONE, person.getFirstName());
                    usersItem.setIcon(R.drawable.ic_menu_users);
                    usersItem.setCheckable(true);
                }
            }
        }
    }

    private void updateFloatButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usersView.getVisibility() == View.VISIBLE) {
                    Intent startUserActivityIntent = new Intent(HomeActivity.this, UserActivity.class);
                    startActivityForResult(startUserActivityIntent, USER_ACTIVITY);
                }
                else if(expensesView.getVisibility() == View.VISIBLE) {
                    Intent startExpensesActivityIntent = new Intent(HomeActivity.this, ExpensesActivity.class);
                    startExpensesActivityIntent.putExtra("buyer", usersView.getSelectedUser().getUserId());
                    startActivityForResult(startExpensesActivityIntent, EXPENSE_ACTIVITY);
                }
                else if(paymentsView.getVisibility() == View.VISIBLE){
                    Intent startPaymentsActivityIntent = new Intent(HomeActivity.this, PaymentsActivity.class);
                    startPaymentsActivityIntent.putExtra("expense_id", paymentsView.getExpense().getExpenseId());
                    startActivityForResult(startPaymentsActivityIntent, PAYMENT_ACTIVITY);
                }
                else if(storesView.getVisibility() == View.VISIBLE){
                    Intent storeActivityIntent = new Intent(HomeActivity.this, StoreActivity.class);
                    startActivityForResult(storeActivityIntent, STORE_ACTIVITY);
                }
                else if(promotionsView.getVisibility() == View.VISIBLE){
                    Intent promotionsActivityIntent = new Intent(HomeActivity.this, PromotionActivity.class);
                    startActivityForResult(promotionsActivityIntent, PROMOTION_ACTIVITY);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(usersView.getVisibility() == View.VISIBLE) {
                User selectedUser = usersView.getSelectedUser();
                if(selectedUser != null) {
                    Intent startNewPersonActivityIntent = new Intent(HomeActivity.this, UserActivity.class);
                    startNewPersonActivityIntent.putExtra("id", selectedUser.getUserId());
                    startActivityForResult(startNewPersonActivityIntent, USER_ACTIVITY);
                }
            }
            else if (expensesView.getVisibility() == View.VISIBLE) {
                Expense selectedExpense = expensesView.getSelectedExpense();
                if(selectedExpense != null) {
                    Intent expensesActivityIntent = new Intent(HomeActivity.this, ExpensesActivity.class);
                    expensesActivityIntent.putExtra("id", selectedExpense.getExpenseId());
                    startActivityForResult(expensesActivityIntent, EXPENSE_ACTIVITY);
                }
            }
            else if (paymentsView.getVisibility() == View.VISIBLE) {
                Payment selectedPayment = paymentsView.getSelectedPayment();
                if(selectedPayment != null) {
                    Intent paymentsActivityIntent = new Intent(HomeActivity.this, PaymentsActivity.class);
                    paymentsActivityIntent.putExtra("id", selectedPayment.getPaymentId());
                    startActivityForResult(paymentsActivityIntent, PAYMENT_ACTIVITY);
                }
            }
            else if(storesView.getVisibility() == View.VISIBLE) {
                Store selectedStore = storesView.getSelectedStore();
                if(selectedStore != null) {
                    Intent storeActivityIntent = new Intent(HomeActivity.this, StoreActivity.class);
                    storeActivityIntent.putExtra("id", selectedStore.getStoreId());
                    startActivityForResult(storeActivityIntent, STORE_ACTIVITY);
                }
            }
            else if(promotionsView.getVisibility() == View.VISIBLE) {
                Promotion selectedPromotion = promotionsView.getSelectedPromotion();
                if(selectedPromotion != null) {
                    Intent promotionActivityIntent = new Intent(HomeActivity.this, PromotionActivity.class);
                    promotionActivityIntent.putExtra("id", selectedPromotion.getPromotionId());
                    startActivityForResult(promotionActivityIntent, STORE_ACTIVITY);
                }
            }

            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(usersView.getVisibility() == View.VISIBLE) {
                User selectedUser = usersView.getSelectedUser();
                if(selectedUser != null) {
                    List<Expense> expenseList = controller.findExpenseByUser(HomeActivity.this, selectedUser.getUserId());

                    expensesView.setData(expenseList, HomeActivity.this);
                    changeView(SHOW_EXPENSES_VIEW);
                }
            }
            else if (expensesView.getVisibility() == View.VISIBLE) {
                Expense selectedExpense = expensesView.getSelectedExpense();
                if(selectedExpense != null) {
                    List<Payment> paymentList = controller.findPaymentsByExpense(HomeActivity.this, selectedExpense.getExpenseId());
                    paymentsView.setData(paymentList, HomeActivity.this);
                    paymentsView.setExpense(expensesView.getSelectedExpense());
                    changeView(SHOW_PAYMENTS_VIEW);
                }
            }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(paymentsView.getVisibility() == View.VISIBLE) {
                changeView(SHOW_EXPENSES_VIEW);
            }
            else if(expensesView.getVisibility() == View.VISIBLE) {
                changeView(SHOW_USERS_VIEW);
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_users) {
            changeView(SHOW_USERS_VIEW);
        }
        else if (id == R.id.nav_stores) {
            List<Store> storeList = controller.getAllStores(this);
            storesView.setData(storeList, this);
            changeView(SHOW_STORES_VIEW);
        }
        else if (id == R.id.nav_promotions) {
            List<Promotion> promotionList = controller.getAllPromotions(this);
            promotionsView.setData(promotionList, this);
            changeView(SHOW_PROMOTIONS_VIEW);
        }
        else {
            for (User user : users) {
                if(id == user.getUserId()) {
                    List<Expense> expenseList = controller.findExpenseByUser(this, user.getUserId());

                    expensesView.setData(expenseList, this);

                    usersView.setSelectedUser(user);

                    changeView(SHOW_EXPENSES_VIEW);
                    break;
                }
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeView(int view) {

        if(view == SHOW_USERS_VIEW) {

            usersView.clearSelected();
            expensesView.clearSelected();
            paymentsView.clearSelected();

            usersView.setVisibility(View.VISIBLE);
            expensesView.setVisibility(View.GONE);
            paymentsView.setVisibility(View.GONE);
            storesView.setVisibility(View.GONE);
            promotionsView.setVisibility(View.GONE);
        }
        else if(view == SHOW_EXPENSES_VIEW) {

            expensesView.clearSelected();
            paymentsView.clearSelected();

            usersView.setVisibility(View.GONE);
            expensesView.setVisibility(View.VISIBLE);
            paymentsView.setVisibility(View.GONE);
            storesView.setVisibility(View.GONE);
            promotionsView.setVisibility(View.GONE);
        }
        else if(view == SHOW_PAYMENTS_VIEW){

            paymentsView.clearSelected();

            usersView.setVisibility(View.GONE);
            expensesView.setVisibility(View.GONE);
            paymentsView.setVisibility(View.VISIBLE);
            storesView.setVisibility(View.GONE);
            promotionsView.setVisibility(View.GONE);
        }
        else if(view == SHOW_STORES_VIEW){

            storesView.clearSelected();
            usersView.clearSelected();
            expensesView.clearSelected();
            paymentsView.clearSelected();
            promotionsView.clearSelected();

            usersView.setVisibility(View.GONE);
            expensesView.setVisibility(View.GONE);
            paymentsView.setVisibility(View.GONE);
            storesView.setVisibility(View.VISIBLE);
            promotionsView.setVisibility(View.GONE);
        }
        else if(view == SHOW_PROMOTIONS_VIEW){
            storesView.clearSelected();
            usersView.clearSelected();
            expensesView.clearSelected();
            paymentsView.clearSelected();
            promotionsView.clearSelected();

            usersView.setVisibility(View.GONE);
            expensesView.setVisibility(View.GONE);
            paymentsView.setVisibility(View.GONE);
            storesView.setVisibility(View.GONE);
            promotionsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //update the views after updating data
        if(requestCode == USER_ACTIVITY) {
            users = controller.loadAllUsers(this);
            usersView.setData(users, this);
        }
        else if(requestCode == EXPENSE_ACTIVITY) {
            long userId = usersView.getSelectedUser().getUserId();
            List<Expense> expenseList = controller.findExpenseByUser(HomeActivity.this, userId);
            expensesView.setData(expenseList, HomeActivity.this);
        }
        else if(requestCode == PAYMENT_ACTIVITY) {
            long expenseId = expensesView.getSelectedExpense().getExpenseId();
            List<Payment> paymentList = controller.findPaymentsByExpense(HomeActivity.this, expenseId);
            paymentsView.setData(paymentList, HomeActivity.this);
        }
        else if(requestCode == STORE_ACTIVITY) {
            List<Store> storeList = controller.getAllStores(this);
            storesView.setData(storeList, this);
        }
        else if(requestCode == PROMOTION_ACTIVITY) {
            List<Promotion> promotionList = controller.getAllPromotions(this);
            promotionsView.setData(promotionList, this);
        }

    }
}
