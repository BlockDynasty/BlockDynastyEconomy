package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.account.*;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.*;
import BlockDynasty.Economy.aplication.useCase.currency.*;
import BlockDynasty.Economy.aplication.useCase.offer.*;
import BlockDynasty.Economy.aplication.useCase.transaction.*;
import BlockDynasty.Economy.aplication.useCase.transaction.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.*;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

public class UseCaseFactory {
    private final ServicesManager servicesManager;
    private final IRepository repository;
    private final Courier courier;
    private final Log log;

    public UseCaseFactory (ServicesManager servicesManager, IRepository repository, Courier courier, Log log){
        this.servicesManager = servicesManager;
        this.repository = repository;
        this.courier = courier;
        this.log = log;
    }

    @Deprecated
    public SearchAccountUseCase searchAccount(){
        return new SearchAccountUseCase(servicesManager.getAccountService(), repository);
    }
    public PayUseCase pay(){
        return new PayUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public CreateAccountUseCase createAccount(){
        return new CreateAccountUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository);
    }
    public DeleteAccountUseCase deleteAccount(){
        return new DeleteAccountUseCase(repository, servicesManager.getAccountService());
    }
    public EditAccountUseCase editAccount(){
        return new EditAccountUseCase(servicesManager.getAccountService(), repository, courier);
    }
    public GetAccountByNameUseCase searchAccountByName(){
        return new GetAccountByNameUseCase(servicesManager.getAccountService());
    }
    public GetAccountByUUIDUseCase searchAccountByUUID(){
        return new GetAccountByUUIDUseCase(servicesManager.getAccountService());
    }

    public GetAccountByPlayerUseCase searchAccountByPlayer(){
        return new GetAccountByPlayerUseCase(servicesManager.getAccountService());
    }

    public GetOfflineAccountsUseCase searchOfflineAccounts(){
        return new GetOfflineAccountsUseCase(servicesManager.getAccountService());
    }
    public GetTopAccountsUseCase topAccounts(){
        return new GetTopAccountsUseCase(servicesManager.getAccountService(), repository);
    }
    public DeleteAccountUseCase deleteBalance(){
        return new DeleteAccountUseCase(repository, servicesManager.getAccountService());
    }
    public GetBalanceUseCase getBalance(){
        return new GetBalanceUseCase(servicesManager.getAccountService());
    }
    public CreateCurrencyUseCase createCurrency(){
        return new CreateCurrencyUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), courier, repository);
    }
    public DeleteCurrencyUseCase deleteCurrency(){
        return new DeleteCurrencyUseCase( servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier);
    }
    public EditCurrencyUseCase editCurrency(){
        return new EditCurrencyUseCase(servicesManager.getCurrencyService(), courier, repository);
    }
    public SearchCurrencyUseCase searchCurrency(){
        return new SearchCurrencyUseCase(servicesManager.getCurrencyService(),repository);
    }

    public IWithdrawUseCase withdraw(){
        return new WithdrawUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public IWithdrawUseCase withdraw(Log log){
        return new WithdrawUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public IDepositUseCase deposit(){
        return new DepositUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository, courier, log, servicesManager.getEventManager());
    }
    public IDepositUseCase deposit(Log log){
        return new DepositUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ISetBalanceUseCase setBalance(){
        return new SetBalanceUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ISetBalanceUseCase setBalance(Log log){
        return new SetBalanceUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public IExchangeUseCase exchange(){
        return new ExchangeUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public IExchangeUseCase exchange(Log log){
        return new ExchangeUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ITradeUseCase tradeCurrencies(){
        return new TradeCurrenciesUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ITradeUseCase tradeCurrencies(Log log){
        return new TradeCurrenciesUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ITransferUseCase transferFunds(){
        return new TransferFundsUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ITransferUseCase transferFunds(Log log){
        return new TransferFundsUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }

    public CreateOfferUseCase createOffer(){
        return new CreateOfferUseCase(servicesManager.getOfferService(), servicesManager.getAccountService(),
                courier, servicesManager.getEventManager(), servicesManager.getCurrencyService(), repository);
    }
    public CancelOfferUseCase cancelOffer(){
        return new CancelOfferUseCase(  servicesManager.getOfferService(), courier, servicesManager.getEventManager());
    }
    public AcceptOfferUseCase acceptOffer() {
        return new AcceptOfferUseCase( servicesManager.getOfferService(), courier, servicesManager.getEventManager(), tradeCurrencies());
    }
    public SearchOfferUseCase searchOffer() {
        return new SearchOfferUseCase( servicesManager.getOfferService());
    }
}
