
package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @Test
    void shouldTransferFromFirstToSecond() {
        val amount = 300;
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val balanceOfFirstCardBefore = DashboardPage.getCurrentBalanceOfFirstCard();
        val balanceOfSecondCardBefore = DashboardPage.getCurrentBalanceOfSecondCard();
        val transferPage = dashboardPage.secondCard();
        val cardInfo = DataHelper.getFirstCardInfo();
        transferPage.transferCard(cardInfo, amount);
        val balanceAfterTransferFirstCard = DataHelper.balanceOfSecondCardAfterTransfer(balanceOfSecondCardBefore, amount);
        val balanceAfterTransferSecondCard = DataHelper.balanceOfFirstCardAfterTransfer(balanceOfFirstCardBefore, amount);
        val balanceOfFirstCardAfter = DashboardPage.getCurrentBalanceOfSecondCard();
        val balanceOfSecondCardAfter = DashboardPage.getCurrentBalanceOfFirstCard();
        assertEquals(balanceAfterTransferFirstCard, balanceOfFirstCardAfter);
        assertEquals(balanceAfterTransferSecondCard, balanceOfSecondCardAfter);
    }

    @Test
    void shouldTransferFromSecondToFirst() {
        val amount = 600;
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val balanceOfFirstCardBefore = DashboardPage.getCurrentBalanceOfFirstCard();
        val balanceOfSecondCardBefore = DashboardPage.getCurrentBalanceOfSecondCard();
        val transferPage = dashboardPage.firstCard();
        val cardInfo = DataHelper.getSecondCardInfo();
        transferPage.transferCard(cardInfo, amount);
        val balanceAfterTransferFirstCard = DataHelper.balanceOfSecondCardAfterTransfer(balanceOfFirstCardBefore, amount);
        val balanceAfterTransferSecondCard = DataHelper.balanceOfFirstCardAfterTransfer(balanceOfSecondCardBefore, amount);
        val balanceOfFirstCardAfter = DashboardPage.getCurrentBalanceOfFirstCard();
        val balanceOfSecondCardAfter = DashboardPage.getCurrentBalanceOfSecondCard();
        assertEquals(balanceAfterTransferFirstCard, balanceOfFirstCardAfter);
        assertEquals(balanceAfterTransferSecondCard, balanceOfSecondCardAfter);
    }

    @Test
    void shouldNotTransferMoreThanRestOfBalance() {
        val amount = 15000;
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val transferPage = dashboardPage.firstCard();
        val cardInfo = DataHelper.getSecondCardInfo();
        transferPage.transferCard(cardInfo, amount);
        transferPage.getNotification();
    }
}