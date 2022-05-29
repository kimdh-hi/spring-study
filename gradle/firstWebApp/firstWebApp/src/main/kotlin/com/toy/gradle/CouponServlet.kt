package com.toy.gradle

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/coupon")
class CouponServlet: HttpServlet() {

    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        res.writer.print("SUPER-SALE-COUPON!!!")
    }

    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        val coupon = req.getParameter("coupon")
        req.setAttribute("discount", "discount for coupon $coupon is 50%")
        req.getRequestDispatcher("response.jsp").forward(req, res)
    }
}