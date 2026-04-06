package com.example.kidsfashion.controller;

import com.example.kidsfashion.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final VNPayService vnPayService;

    public PaymentController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @GetMapping("/create")
    public RedirectView createPayment(HttpServletRequest request,
                                      @RequestParam("amount") long amount) throws Exception {
        String paymentUrl = vnPayService.createPaymentUrl(request, amount, "Thanh toan don hang KidFashion");
        return new RedirectView(paymentUrl);
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, Map<String, Object> model) throws Exception {
        Map<String, String> fields = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> fields.put(key, value[0]));

        boolean validSignature = vnPayService.verifyReturnUrl(new HashMap<>(fields));
        String responseCode = request.getParameter("vnp_ResponseCode");

        if (validSignature && "00".equals(responseCode)) {
            model.put("message", "Thanh toán thành công");
        } else {
            model.put("message", "Thanh toán thất bại");
        }

        return "payment-result";
    }
}