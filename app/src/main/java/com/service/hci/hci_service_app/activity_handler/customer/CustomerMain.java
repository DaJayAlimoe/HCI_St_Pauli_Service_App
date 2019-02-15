package com.service.hci.hci_service_app.activity_handler.customer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ShoppingCartItemListAdapter;
import com.service.hci.hci_service_app.activity_handler.customer.fragments.MenuFragment;
import com.service.hci.hci_service_app.activity_handler.customer.fragments.OrdersFragment;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_types.Cart;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CustomerMain extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main);

        Toolbar toolbar = findViewById(R.id.toolbar_customer);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewPager_customer);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new MenuFragment(), "Menu Karte");
        adapter.addFragment(new OrdersFragment(), "Meine Bestellungen");
        viewPager.setAdapter(adapter);

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabLayout tabLayout = findViewById(R.id.tabLayout_customer);
        tabLayout.setupWithViewPager(viewPager);

        Cart.initInstance();
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton__customer_cart_menu);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());

                dialog.setContentView(R.layout.customer_shoppingcart);

                TextView cartHeader = (TextView) dialog.findViewById(R.id.textView_customer_cart_header);
                cartHeader.setText("Warenkorb");

                ListView listViewCart = (ListView) dialog.findViewById(R.id.listView_customer_shoppingCartList);
                Button btnOrder;
                Button btnCancel;

                ShoppingCartItemListAdapter shoppingCartItemListAdapter = new ShoppingCartItemListAdapter(dialog.getContext(), R.layout.customer_shopping_list_view, Cart.getInstance().getCart());
                listViewCart.setAdapter(shoppingCartItemListAdapter);

                btnCancel = dialog.findViewById(R.id.btn_customer_cart_back);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnOrder = (Button) dialog.findViewById(R.id.btn_customer_order);
                btnOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Api api = Api.getInstance(v.getContext());
                        Cart cart = Cart.getInstance();
                        if (cart.getCart() != null && (!cart.getCart().isEmpty())) {
                            JSONArray orders = cart.getOrders(api.getSession().getUserId());

                            if (api.placeOrder(orders)) {
                                Toast.makeText(v.getContext(), "Bestellung erfolgreich abgeschickt", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext(), "Bestellung konnte nicht abgeschickt werden", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        } else {
                                Toast.makeText(v.getContext(), "Fügen Sie dem Warenkorb erst etwas hinzu!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}