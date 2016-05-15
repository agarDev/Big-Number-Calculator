package info.mayuragarkar.calculator;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.viewpagerindicator.CirclePageIndicator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    /** local variables here */
    BigInteger first_data = null, second_data = null;
    BigDecimal first_dataDec = null, second_dataDec = null;
    private EditText numOne, numTwo, result;
    private TextView operationApplied;
    private String operationAppliedString = "Operation Applied: ";
    private Button addBtn, subBtn, divBtn, mulBtn, clearBtn, raisedto, factorial, mod, modinverse, isPrime, copyBtn;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private SQLiteDatabase calcDB = null;
    private String tableName = "bigNumCalcHistory", dbName = "bigNumCalcHistory";
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** CREATE DATABASE FOR HISTORY HERE */
        calcDB = getBaseContext().openOrCreateDatabase(dbName + ".db", MODE_PRIVATE, null);
        calcDB.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+" (firstNum TEXT, secondNum TEXT, answerNum TEXT)");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /** Button Logics */
        numOne = (EditText) findViewById(R.id.numOne);
        numTwo = (EditText) findViewById(R.id.numTwo);
        result = (EditText) findViewById(R.id.result);
        addBtn = (Button) findViewById(R.id.plus);
        subBtn = (Button) findViewById(R.id.minus);
        divBtn = (Button) findViewById(R.id.divide);
        mulBtn = (Button) findViewById(R.id.multiply);
        clearBtn = (Button) findViewById(R.id.clearAll);
        //raisedto = (Button) findViewById(R.id.raisedto);
        mod = (Button) findViewById(R.id.mod);
        modinverse = (Button) findViewById(R.id.modinverse);
        isPrime = (Button) findViewById(R.id.isPrime);
        copyBtn = (Button) findViewById(R.id.copyBtn);
        operationApplied = (TextView) findViewById(R.id.operationAppliedText);

        /** make operation applied empty on run time */
        operationApplied.setText("");


        /** Make View Pager Initialized Here */
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        //Bind the title indicator to the adapter
        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        titleIndicator.setViewPager(viewPager);


        /** clear all button */
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearall();
                clearText();
            }
        });



        /** copy contents button */
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result.length()>0){
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(result.getText());
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Big Number History",result.getText());
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(getApplicationContext(), "Result Copied.", Toast.LENGTH_LONG).show();
                }else if(result.length()<=0){
                    Toast.makeText(getApplicationContext(), "Nothing to Copy." , Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /** Handling Onclick Event of All Buttons */
    public void onClickCalcButtons(View v){
        minKeybrd();
        //mInterstitialAdCounter++;
        switch(v.getId()) {
            case R.id.plus:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_dataDec = new BigDecimal(String.valueOf(numOne.getText()));
                    second_dataDec = new BigDecimal(String.valueOf(numTwo.getText()));
                    result.setText(String.valueOf(first_dataDec.add(second_dataDec)));
                    addtoDBDec(first_dataDec, second_dataDec, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Addition");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.minus:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_dataDec = new BigDecimal(String.valueOf(numOne.getText()));
                    second_dataDec = new BigDecimal(String.valueOf(numTwo.getText()));
                    result.setText((first_dataDec.subtract(second_dataDec)).toString());
                    addtoDBDec(first_dataDec, second_dataDec, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Subtraction");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;


            case R.id.multiply:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_dataDec = new BigDecimal(String.valueOf(numOne.getText()));
                    second_dataDec = new BigDecimal(String.valueOf(numTwo.getText()));
                    result.setText(String.valueOf(first_dataDec.multiply(second_dataDec)));
                    addtoDBDec(first_dataDec, second_dataDec, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Multiply");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;


            case R.id.divide:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_dataDec = new BigDecimal(String.valueOf(numOne.getText()));
                    second_dataDec = new BigDecimal(String.valueOf(numTwo.getText()));
                    result.setText(String.valueOf(first_dataDec.divide(second_dataDec, 100, RoundingMode.HALF_UP)));
                    addtoDBDec(first_dataDec, second_dataDec, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Division");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;


            case R.id.mod:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    second_data = new BigDecimal(String.valueOf(numTwo.getText())).toBigInteger();
                    result.setText(String.valueOf(first_data.mod(second_data)));
                    addtoDBInt(first_data, second_data, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Mod");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;


            case R.id.modinverse:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    second_data = new BigDecimal(String.valueOf(numTwo.getText())).toBigInteger();
                    if (first_data.gcd(second_data).equals(BigInteger.ONE)) {
                        result.setText(String.valueOf(first_data.modInverse(second_data)));
                        addtoDBInt(first_data, second_data, result.getText().toString());
                    } else {
                        result.setText("Given numbers are not prime to each other.");
                    }
                    operationApplied.setText(operationAppliedString + "Mod Inverse");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;


            case R.id.isPrime:
                if (!(numOne.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    //certainty hardcoded to large number
                    if (first_data.isProbablePrime(250)) {
                        result.setText("Number is a Prime Number.");
                    } else {
                        result.setText("Number is not a Prime Number.");
                    }
                    operationApplied.setText(operationAppliedString + "Is Prime ?");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.compareTo:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_dataDec = new BigDecimal(String.valueOf(numOne.getText()));
                    second_dataDec = new BigDecimal(String.valueOf(numTwo.getText()));
                    int resultFinal = first_dataDec.compareTo(second_dataDec);
                    String resultText = (resultFinal == 0 ? "Both Numbers are Equal" : (resultFinal > 0 ? "First Number is Greater than Second Number" : "Second Number is Greater than First Number"));
                    result.setText(resultText);
                    addtoDBDec(first_dataDec, second_dataDec, String.valueOf(resultFinal));
                    operationApplied.setText(operationAppliedString + "Compare to");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.lShift:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    second_data = new BigDecimal(String.valueOf(numTwo.getText())).toBigInteger();
                    result.setText(String.valueOf(first_data.shiftLeft(second_data.intValue())));
                    addtoDBDec(first_dataDec, second_dataDec, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Left Shift");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.rShift:
                if (!(numOne.getText().toString()).equals("") && !(numTwo.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    second_data = new BigDecimal(String.valueOf(numTwo.getText())).toBigInteger();
                    result.setText(String.valueOf(first_data.shiftRight(second_data.intValue())));
                    addtoDBDec(first_dataDec, second_dataDec, result.getText().toString());
                    operationApplied.setText(operationAppliedString + "Right Shift");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.logOf:
                if (!(numOne.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    result.setText(String.valueOf(logBigInteger(first_data)));
                    operationApplied.setText(operationAppliedString + "Logarithm");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.squareOf:
                if (!(numOne.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    result.setText(String.valueOf(first_data.multiply(first_data)));
                    operationApplied.setText(operationAppliedString + "Square");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
            case R.id.cubeOf:
                if (!(numOne.getText().toString()).equals("")) {
                    first_data = new BigDecimal(String.valueOf(numOne.getText())).toBigInteger();
                    result.setText(String.valueOf((first_data.multiply(first_data)).multiply(first_data)));
                    operationApplied.setText(operationAppliedString + "Cube");
                    clearall();
                } else {
                    result.setText("Numbers cannot be empty !");
                }
                break;
        }
    }


    /** Add each calc to the db */
    void addtoDBDec(BigDecimal fNum, BigDecimal sNum, String ansNum){
        calcDB.execSQL("INSERT INTO " + tableName + " VALUES(" + fNum + ", " + sNum + ", " + ansNum + ")");
    }



    void addtoDBInt(BigInteger fNum, BigInteger sNum, String ansNum){
        calcDB.execSQL("INSERT INTO " + tableName + " VALUES(" + fNum + ", " + sNum + ", " + ansNum + ")");
    }


    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_about:

                //Setting THe Dialog Box Here
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = pInfo.versionName;
                new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("About")
                        .setContentText("Big Number Calculator v"+version+"\n\nDesigned & Developed By:\n\nMayur Agarkar (Z-Day Apps)")
                        .setCustomImage(R.mipmap.ic_launcher)
                        .setConfirmText("Sweet!")
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        return result;
    }


    /**
     * Computes the natural logarithm of a BigInteger. Works for really big
     * integers (practically unlimited)
     *
     * @param val Argument, positive integer
     * @return Natural logarithm, as in <tt>Math.log()</tt>
     */
    private static final double LOG2 = Math.log(2.0);
    public static double logBigInteger(BigInteger val) {
        int blex = val.bitLength() - 1022; // any value in 60..1023 is ok
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }



    /** reset the numbers and memory allocations */
    public void clearall(){
        first_data = null;
        second_data = null;
        first_dataDec = null;
        second_dataDec = null;
    }



    /** clear text from text views */
    private void clearText() {
        result.setText("");
        numOne.setText("");
        numTwo.setText("");
        operationApplied.setText("");
    }



    /** minimize the on sceen keyboard */
    private void minKeybrd(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }



    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }



    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }



    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
