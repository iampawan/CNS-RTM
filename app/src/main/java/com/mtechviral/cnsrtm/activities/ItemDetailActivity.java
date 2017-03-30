package com.mtechviral.cnsrtm.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mtechviral.cnsrtm.R;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView numItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        numItem = (TextView) findViewById(R.id.numItem);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnApplyChanges:
                Toast.makeText(this, "Button Apply Changes clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonMinus:
                int num = Integer.parseInt(numItem.getText().toString());
                if (num > 1) {
                    num--;
                }
                numItem.setText(Integer.toString(num));
                break;
            case R.id.buttonPlus:
                int num2 = Integer.parseInt(numItem.getText().toString());
                num2++;
                numItem.setText(Integer.toString(num2));
                break;

        }
    }
}
