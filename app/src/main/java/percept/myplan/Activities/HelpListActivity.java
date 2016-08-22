package percept.myplan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;
import percept.myplan.fragments.fragmentContacts;

import static percept.myplan.fragments.fragmentContacts.CONTACT_NAME;
import static percept.myplan.fragments.fragmentContacts.HELP_CONTACT_NAME;
import static percept.myplan.fragments.fragmentContacts.LIST_CONTACTS;
import static percept.myplan.fragments.fragmentContacts.LIST_HELPCONTACTS;

public class HelpListActivity extends AppCompatActivity {

    private TextView TV_ADDHELPLIST, TV_EDITHELPLIST;
    private RecyclerView LST_HELP;
    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;
    private List<ContactDisplay> LIST_ALLCONTACTS;
    private ProgressBar PB;

    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.help_list));

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        TV_ADDHELPLIST = (TextView) findViewById(R.id.tvAddHelpContact);
        TV_EDITHELPLIST = (TextView) findViewById(R.id.tvEditHelpList);
        LST_HELP = (RecyclerView) findViewById(R.id.lstHelpList);
        PB = (ProgressBar) findViewById(R.id.pbHelpList);
        LIST_ALLCONTACTS = new ArrayList<>();
        HELP_CONTACT_NAME = new HashMap<>();
        LIST_HELPCONTACTS = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HelpListActivity.this);
        LST_HELP.setLayoutManager(mLayoutManager);
        LST_HELP.setItemAnimator(new DefaultItemAnimator());
        LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);

        TV_ADDHELPLIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HelpListActivity.this, AddContactActivity.class);
                _intent.putExtra("ADD_TO_HELP", "true");
                startActivity(_intent);
            }
        });

        TV_EDITHELPLIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HelpListActivity.this, HelpListEditActivity.class);
                _intent.putExtra("FROM_HELP", "true");
                startActivity(_intent);
            }
        });
//        getContacts();

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (fragmentContacts.GET_CONTACTS) {
            getContacts();
            fragmentContacts.GET_CONTACTS = false;
        }*/
        getContacts();
    }

    private void getContacts() {
        PB.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            LIST_HELPCONTACTS.clear();
            HELP_CONTACT_NAME.clear();
            new General().getJSONContentFromInternetService(HelpListActivity.this, General.PHPServices.GET_CONTACTS, params, true, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(":::::::::::::: ", response.toString());

                    Gson gson = new Gson();
                    try {
                        LIST_ALLCONTACTS = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<ContactDisplay>>() {
                        }.getType());
                        PB.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (ContactDisplay _obj : LIST_ALLCONTACTS) {


                        if (!_obj.getHelplist().equals("0")) {
                            HELP_CONTACT_NAME.put(_obj.getId(), _obj.getFirst_name());
                            LIST_HELPCONTACTS.add(_obj);
                        }
                    }

                    ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACTS, "HELP");
                    LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            PB.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getContacts();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_help_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            HelpListActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_add_editHelpList) {
            Intent _intent = new Intent(HelpListActivity.this, AddContactActivity.class);
            _intent.putExtra("ADD_TO_HELP", "true");
            startActivity(_intent);
        }
        return false;
    }
}
