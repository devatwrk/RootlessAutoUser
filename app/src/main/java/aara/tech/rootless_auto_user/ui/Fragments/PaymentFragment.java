package aara.tech.rootless_auto_user.ui.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import aara.tech.rootless_auto_user.MainActivity;
import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.utils.Commonhelper;


public class PaymentFragment extends Fragment implements PaymentResultListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    TextView tvOrderId, tvAmountToPay;
    Button pay;
    String username;
    float amount;
    ScrollView scrollView;
    ProgressBar progressBar;
    Sprite chasingDots;
    WebView webView;
    private Commonhelper commonhelper;


    private void initViews(View view) {
        //TextView
        tvOrderId = view.findViewById(R.id.tv_order_id);
        tvAmountToPay = view.findViewById(R.id.tv_amount);
        webView = view.findViewById(R.id.webview);
        pay = view.findViewById(R.id.btn_pay);
        scrollView = view.findViewById(R.id.payment_scrollView);

        progressBar = view.findViewById(R.id.spin_kit);
        chasingDots = new ChasingDots();
        progressBar.setIndeterminateDrawable(chasingDots);

        commonhelper = new Commonhelper(getContext());
        username = commonhelper.getSharedPreferences("uname", null);

    }

    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvOrderId.getPaint().setShader(textShader);
        tvAmountToPay.getPaint().setShader(textShader);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
        textViewColor();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                paytmBtn();
//                amount = Integer.parseInt(tvAmountToPay.getText().toString().trim());
                amount = 2;
                amount = amount * 100;
                startPayment(amount);
//                Checkout.clearUserData(getActivity());
            }
        });


        return view;
    }

    private void startPayment(float amount) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
//        checkout.setImage(R.drawable.rzp_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", username);
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", amount);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    private void paytmBtn(){

        webView.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        webView.loadUrl("https://paytm.com/");



        /*tvOrderId.setVisibility(View.INVISIBLE);
        tvAmountToPay.setVisibility(View.INVISIBLE);*/

    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getContext(), "Successful payment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(), "Failed payment", Toast.LENGTH_SHORT).show();
    }
}
