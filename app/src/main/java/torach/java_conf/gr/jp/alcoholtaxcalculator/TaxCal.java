package torach.java_conf.gr.jp.alcoholtaxcalculator;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class TaxCal extends Activity implements View.OnClickListener {

    //ボタン宣言
    Button cal_btn;
    Button clear_btn;

    //スピナー宣言
    Spinner alcSpinner;

    //計算変数宣言
    //購入価格
    int price;

    //容量 ml
    int amount;

    //酒税変数
    int tax;

    //計算式変数
    TextView tax_show;
    TextView rate_show;

    EditText edit_price;
    EditText edit_amount;
    String price_str;
    String amount_str;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_cal);

        //ボタン取得
        cal_btn = (Button) findViewById(R.id.cal_btn);
        clear_btn = (Button) findViewById(R.id.clear_btn);

        //リスナー設定
        cal_btn.setOnClickListener((View.OnClickListener) this);
        clear_btn.setOnClickListener((View.OnClickListener) this);

        alcSpinner = (Spinner) findViewById(R.id.alc_spinner);
        alcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                TypedArray array = getResources().obtainTypedArray(R.array.alc_list);
                int itemId = array.getResourceId(position, R.string.beer);
                switch (itemId) {
                    case R.string.beer:
                        tax = 220000;
                        break;
                    case R.string.lowMaltBeer:
                        tax = 134250;
                        break;
                    case R.string.newGenre:
                        tax = 80000;
                        break;
                }
                array.recycle();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.cal_btn:
                    calculate_result();
                    break;
                case R.id.clear_btn:
                    clear_data();
                    break;
            }
        }

        //酒税の金額と税率の計算
        public  void calculate_result() {
            //入力データ取得
            edit_amount = (EditText) findViewById(R.id.val_amount);
            edit_price = (EditText) findViewById(R.id.val_price);

            //入力値取得
            amount_str = edit_amount.getText().toString();
            price_str = edit_price.getText().toString();


            try {

                //整数値変換
                amount = Integer.parseInt(amount_str);
                price = Integer.parseInt(price_str);

                //酒税の金額の計算
                BigDecimal numTax = new BigDecimal(tax);
                BigDecimal numAmount = new BigDecimal(amount);
                BigDecimal numThousand = new BigDecimal(1000);

                BigDecimal f1 = numAmount.multiply(numTax);
                BigDecimal f2 = f1.divide(numThousand);
                BigDecimal f3 = f2.divide(numThousand);
                BigDecimal tax_result = f3.setScale(0,BigDecimal.ROUND_HALF_UP);

                //税率の計算
                //BigDecimal tax_result2 = tax_result;
                BigDecimal numPrice = new BigDecimal(price);
                BigDecimal numHundred = new BigDecimal(100);

                BigDecimal r1 = tax_result.divide(numPrice,2,BigDecimal.ROUND_DOWN);
                BigDecimal r2 = r1.multiply(numHundred);
                BigDecimal tax_rate = r2.setScale(0,BigDecimal.ROUND_HALF_UP);

                //結果表示
                tax_show = (TextView) findViewById(R.id.textView);
                tax_show.setText(String.valueOf(tax_result));

                rate_show = (TextView) findViewById(R.id.textView2);
                rate_show.setText(String.valueOf(tax_rate));

            } catch (NumberFormatException e) {
                String pop1 = getResources().getString(R.string.exist_non_imp);
                Toast toast = Toast.makeText(TaxCal.this, pop1, Toast.LENGTH_SHORT);
                toast.show();
            }

        }

        public void clear_data() {
            try
            {
                edit_amount.setText("");
                edit_price.setText("");
            } catch (NullPointerException e) {
                String pop2 = getResources().getString(R.string.no_imp_data);
                Toast toast = Toast.makeText(TaxCal.this, pop2, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


}
