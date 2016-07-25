package info.mayuragarkar.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.viewpagerindicator.CirclePageIndicator;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    /** local variables here */
    BigInteger firstIntegerNum = null, secondIntegerNum = null;
    BigDecimal firstDecimalNum = null, secondDecimalNum = null;
    private EditText firstNumberEditText, secondNumberEditText, resultEditText;
    private TextView operationApplied;
    private String operationAppliedString = "Operation Applied: ";
    private Button addBtn, subBtn, divBtn, mulBtn, clearBtn, raisedto, factorial, mod, modinverse, isPrime, copyBtn;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private String tableName = "bigNumCalcHistory", dbName = "bigNumCalcHistory";
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** CREATE DATABASE FOR HISTORY HERE */
        DatabaseHandler.calcDB = getBaseContext().openOrCreateDatabase(dbName + ".db", MODE_PRIVATE, null);
        DatabaseHandler.calcDB.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+" (firstNum TEXT, secondNum TEXT, answerNum TEXT)");

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
        firstNumberEditText = (EditText) findViewById(R.id.firstNumberEditText);
        secondNumberEditText = (EditText) findViewById(R.id.secondNumberEditText);
        resultEditText = (EditText) findViewById(R.id.resultEditText);
        addBtn = (Button) findViewById(R.id.plus);
        factorial = (Button) findViewById(R.id.factorial);
        subBtn = (Button) findViewById(R.id.minus);
        divBtn = (Button) findViewById(R.id.divide);
        mulBtn = (Button) findViewById(R.id.multiply);
        clearBtn = (Button) findViewById(R.id.clearAll);
        raisedto = (Button) findViewById(R.id.raisedto);
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
                if(resultEditText.length()>0){
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(resultEditText.getText());
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Big Number History",resultEditText.getText());
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(getApplicationContext(), "Result Copied.", Toast.LENGTH_LONG).show();
                }else if(resultEditText.length()<=0){
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
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    secondDecimalNum = new BigDecimal(String.valueOf(secondNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.addOperation(firstDecimalNum, secondDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.minus:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    secondDecimalNum = new BigDecimal(String.valueOf(secondNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.subOperation(firstDecimalNum, secondDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.multiply:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    secondDecimalNum = new BigDecimal(String.valueOf(secondNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.mulOperation(firstDecimalNum, secondDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                    clearall();
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;


            case R.id.divide:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    secondDecimalNum = new BigDecimal(String.valueOf(secondNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.divOperation(firstDecimalNum, secondDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;


            case R.id.mod:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    secondIntegerNum = new BigDecimal(String.valueOf(secondNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.modOperation(firstIntegerNum, secondIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;


            case R.id.modinverse:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    secondIntegerNum = new BigDecimal(String.valueOf(secondNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.modInverseOperation(firstIntegerNum, secondIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;


            case R.id.isPrime:
                if (validateInput(firstNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.isPrimeOperation(firstIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.compareTo:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    secondDecimalNum = new BigDecimal(String.valueOf(secondNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.compareOperation(firstDecimalNum, secondDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.lShift:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    secondIntegerNum = new BigDecimal(String.valueOf(secondNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.lShiftOperation(firstIntegerNum, secondIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.rShift:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    secondIntegerNum = new BigDecimal(String.valueOf(secondNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.rShiftOperation(firstIntegerNum, secondIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.logOf:
                if (validateInput(firstNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.logOperation(firstIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.squareOf:
                if (validateInput(firstNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.squareOperation(firstIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.cubeOf:
                if (validateInput(firstNumberEditText.getText().toString())) {
                    firstIntegerNum = new BigDecimal(String.valueOf(firstNumberEditText.getText())).toBigInteger();
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.cubeOperation(firstIntegerNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.raisedto:
                if (validateInput(firstNumberEditText.getText().toString(), secondNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    secondDecimalNum = new BigDecimal(String.valueOf(secondNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.powOperation(firstDecimalNum, secondDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
            case R.id.factorial:
                if (validateInput(firstNumberEditText.getText().toString())) {
                    firstDecimalNum = new BigDecimal(String.valueOf(firstNumberEditText.getText()));
                    ResultSet rs = new ResultSet();
                    setMessage(resultEditText, (calculateOperation.factorialOperation(firstDecimalNum, firstDecimalNum, rs).getValue()));
                    setMessage(operationApplied, rs.getMessage());
                } else {
                    setErrorMessage_1(resultEditText);
                }
                break;
        }
        clearall();
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
                //Setting The Dialog Box Here
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = pInfo.versionName;
                new LovelyInfoDialog(this)
                        .setTopColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.ic_info_white_18dp)
                        //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                        .setTitle("About")
                        .setMessage("\nBig Number Calculator v"+version+"\n\nDesigned & Developed By:\nMayur Agarkar (Z-Day Apps)")
                        .show();
                break;
            case R.id.action_libs:
                /*Intent intent = new Intent(this, LibsCards.class);
                startActivity(intent);*/
                new LovelyCustomDialog(this)
                        .setView(R.layout.activity_libs_cards)
                        .setTopColorRes(R.color.colorAccent)
                        .setTitle("Licenses and Libraries Used")
                        .setIcon(R.drawable.ic_info_white_18dp)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private static Boolean validateInput(String firstParam, String secondParam){
        return (!(firstParam).equals("") && !(secondParam).equals("")) ? true : false;
    }


    private static Boolean validateInput(String firstParam){
        return (!(firstParam).equals("")) ? true : false;
    }


    private static void setErrorMessage_1(EditText resultEditText){
        resultEditText.setText("Numbers cannot be empty !");
    }


    private static void setMessage(EditText resultEditText, String message){
        resultEditText.setText(message);
    }


    private static void setMessage(TextView resultEditText, String message){
        resultEditText.setText(message);
    }


    /** reset the numbers and memory allocations */
    public void clearall(){
        firstIntegerNum = null;
        secondIntegerNum = null;
        firstDecimalNum = null;
        secondDecimalNum = null;
    }


    /** clear text from text views */
    private void clearText() {
        resultEditText.setText("");
        firstNumberEditText.setText("");
        secondNumberEditText.setText("");
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
