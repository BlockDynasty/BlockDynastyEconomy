package EngineTest;

import EngineTest.mocks.MinecraftServer;
import EngineTest.mocks.Player;
import api.EconomyResponse;
import api.IApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApiTest {
    private static MinecraftServer server;
    private static IApi api;
    private static Player nullplague = new Player(UUID.fromString("55e72bac-6481-3abe-9c9b-94cefed85271"), "Nullplague");
    private static Player fede = new Player(UUID.fromString("51a888b7-5a59-3f3d-9922-08746bcd8cd6"), "Fede");

    @BeforeAll
    public static void setup(){
        MinecraftServer.start();
        api = MinecraftServer.getApi();
        MinecraftServer.connectPlayer(nullplague);
        MinecraftServer.connectPlayer(fede);
    }

    @Test
    public void testInitialization() {
        assert(MinecraftServer.getEconomy() != null);
    }

    @Test
    public void testApiAccess(){
        assertNotNull(api);
    }

    @Test
    public void testGetBalancePlayer(){
        BigDecimal balance = api.getBalance(fede.getName(),"Money");
        assertNotNull(balance);
    }

    @Test
    public void testAddBalancePlayer(){
        BigDecimal balanceAct = api.getBalance(fede.getName(),"Money");
        EconomyResponse response = api.deposit(fede.getName(), BigDecimal.valueOf(5000),"Money");
        BigDecimal balanceNew = api.getBalance(fede.getName(),"Money");
        assertTrue(response.isSuccess());
        assertEquals(balanceAct, balanceNew.subtract(BigDecimal.valueOf(5000)));
    }

    @Test
    public void testSubtractBalancePlayer(){
        BigDecimal balanceAct = api.getBalance(fede.getName(),"Money");
        EconomyResponse response = api.withdraw(fede.getName(), BigDecimal.valueOf(5000),"Money");
        BigDecimal balanceNew = api.getBalance(fede.getName(),"Money");
        assertTrue(response.isSuccess());
        assertEquals(balanceAct, balanceNew.add(BigDecimal.valueOf(5000)));
    }

    @Test
    public void TestTransferBalancePlayer(){
        api.deposit(fede.getName(),BigDecimal.valueOf(2000),"Money");
        BigDecimal balanceAct1 = api.getBalance(nullplague.getName(),"Money");
        BigDecimal balanceAct2 = api.getBalance(fede.getName(),"Money");
        EconomyResponse response = api.transfer(fede.getName(), nullplague.getName(),"Money", BigDecimal.valueOf(2000));
        BigDecimal balanceNew1 = api.getBalance(nullplague.getName(),"Money");
        BigDecimal balanceNew2 = api.getBalance(fede.getName(),"Money");
        assertTrue(response.isSuccess());
        assertEquals(balanceNew1, balanceAct1.add(BigDecimal.valueOf(2000)));
        assertEquals(balanceNew2, balanceAct2.subtract(BigDecimal.valueOf(2000)));
    }
}
