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
    private List<Function> functions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!logon) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }

        // Recycler
        setUpFunctions();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        // Adapter
//        FunctionAdapter adapter = new FunctionAdapter(this);
        IconAdapter adapter = new IconAdapter();
        recyclerView.setAdapter(adapter);


    }

    private void setUpFunctions() {
        functions = new ArrayList<>();
        String[] funcs = getResources().getStringArray(R.array.functions);
        functions.add(new Function(funcs[0], R.drawable.func_transaction));
        functions.add(new Function(funcs[1], R.drawable.func_balance));
        functions.add(new Function(funcs[2], R.drawable.func_invesment));
        functions.add(new Function(funcs[3], R.drawable.func_contacts));
        functions.add(new Function(funcs[4], R.drawable.func_exit));
    }


    public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {


        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(
                    R.layout.item_icon, parent, false);
            return new IconHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder holder, int position) {
            Function function = functions.get(position);
            holder.nameText.setText(function.getName());
            holder.iconImage.setImageResource(functions.get(position).getIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(function);
                }
            });

        }

        @Override
        public int getItemCount() {
            return functions.size();
        }

        public class IconHolder extends RecyclerView.ViewHolder {
            ImageView iconImage;
            TextView nameText;

            public IconHolder(@NonNull View itemView) {
                super(itemView);
                iconImage = itemView.findViewById(R.id.item_icon);
                nameText = itemView.findViewById(R.id.item_name);

            }
        }
    }

    private void itemClicked(Function function) {
        Log.d(TAG, "itemClicked: " + function.getName());

        switch (function.getIcon()) {
            case R.drawable.func_transaction:
                break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_invesment:
                break;
            case R.drawable.func_contacts:
                break;
            case R.drawable.func_exit:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Exit?").setMessage("Sure to Exit?")
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 結束應用程序
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_LOGIN == requestCode) {
            if (RESULT_OK != resultCode) {
                finish();
            }
        }
    }
}
