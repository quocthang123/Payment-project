package org.example.payment.paymentservice.service;

import com.cybersource.authsdk.core.MerchantConfig;
import org.example.payment.paymentservice.dto.VisaPaymentRequest;
import org.example.payment.paymentservice.utils.XPayTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;


@Service
public class VisaPaymentService {
    @Value("${visa.apiKey}")
    private String apiKey;

    @Value("${visa.apiSharedSecret}")
    private String apiSharedSecret;

    public String processVisaPayment(VisaPaymentRequest paymentRequest) {
        String xPayToken = "";
        try {
            xPayToken = XPayTokenGenerator.generateXpaytoken("/cybersource/payments/v1/authorizations", "", "", apiSharedSecret);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        String paymentApiUrl = "https://sandbox.api.visa.com/cybersource/payments/v1/sale";
        String requestBody = "{\"amount\":100,\"currency\":\"USD\",\"payment\":{\"token\":{\"id\":\"" + xPayToken + "\"}}}";

        // Tạo yêu cầu thanh toán bằng X_PAY_TOKEN
        String request = buildPaymentRequest(xPayToken, "cardNumber", "expiryDate", "cvv", 1d, "USD");

        // Gửi yêu cầu thanh toán và xử lý kết quả
        boolean paymentResult = false;
        try {
            paymentResult = sendPaymentRequest(request);
            if (paymentResult) {
                System.out.println("Thanh toán thành công!");
                return "Thanh toán thành công!";
            } else {
                System.out.println("Thanh toán thất bại!");
                return "Thanh toán thất bại!";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Xây dựng yêu cầu thanh toán bằng X_PAY_TOKEN
    private static String buildPaymentRequest(String xPayToken, String cardNumber, String expiryDate, String cvv, double amount, String currency) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("{");
        requestBuilder.append("\"xPayToken\": \"" + xPayToken + "\",");
        requestBuilder.append("\"cardNumber\": \"" + cardNumber + "\",");
        requestBuilder.append("\"expiryDate\": \"" + expiryDate + "\",");
        requestBuilder.append("\"cvv\": \"" + cvv + "\",");
        requestBuilder.append("\"amount\": " + amount + ",");
        requestBuilder.append("\"currency\": \"" + currency + "\"");
        requestBuilder.append("}");
        return requestBuilder.toString();
    }

    // Gửi yêu cầu thanh toán đến API thanh toán của Visa
    private static boolean sendPaymentRequest(String paymentRequest) throws IOException {
        URL url = new URL("https://sandbox.api.visa.com/cybersource/payments/v1/sale"); // Thay đổi URL tương ứng với môi trường sandbox hoặc production
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        byte[] requestBody = paymentRequest.getBytes(StandardCharsets.UTF_8);
        connection.getOutputStream().write(requestBody);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Xử lý kết quả thành công
            return true;
        } else {
            // Xử lý kết quả thất bại
            return false;
        }
    }
}
