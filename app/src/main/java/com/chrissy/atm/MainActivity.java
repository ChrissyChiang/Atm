package com.chrissy.atm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    boolean logon = false;
    public static int REQUEST_LOGIN = 100;
    private List<Function> functionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!logon) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }

        setupRecycler();

    }//end of onCreate

    private void setupRecycler() {
        //Recycler
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        String[] funcs = getResources().getStringArray(R.array.functions);
        functionsList = new ArrayList<>();
        functionsList.add(new Function(funcs[0], R.drawable.func_transaction));
        functionsList.add(new Function(funcs[1], R.drawable.func_balance));
        functionsList.add(new Function(funcs[2], R.drawable.func_finance));
        functionsList.add(new Function(funcs[3], R.drawable.func_contacts));
        functionsList.add(new Function(funcs[4], R.drawable.func_exit));

        //Adapter
        IconAdapter adapter = new IconAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_LOGIN == requestCode) {
            if (RESULT_OK != resultCode) {
                finish();
            }
        }
    }//end of onActivityResult

    public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {

        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_icon, parent, false);
            return new IconHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder holder, int position) {
            Function func = functionsList.get(position);
            holder.itemName.setText(func.getName());
            holder.itemIcon.setImageResource(func.getIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItem(func);
                }
            });
        }

        @Override
        public int getItemCount() {
            return functionsList.size();
        }

        public class IconHolder extends RecyclerView.ViewHolder {

            TextView itemName;
            ImageView itemIcon;

            public IconHolder(@NonNull View itemView) {
                super(itemView);
                itemIcon = itemView.findViewById(R.id.item_icon);
                itemName = itemView.findViewById(R.id.item_name);


            }
        }//end of IconHolder

    }//end of IconAdapter

    private void clickItem(Function func) {
        Log.d(TAG, "clickItem: "+func.getName());
        switch(func.getIcon()){
            case R.drawable.func_transaction:
                break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_finance:
                break;
            case R.drawable.func_contacts:
                break;
            case R.drawable.func_exit:
                new AlertDialog.Builder(this)
                        .setTitle("Exit")
                        .setMessage("Sure to leave?")
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
        }
    }


}//end of Main
