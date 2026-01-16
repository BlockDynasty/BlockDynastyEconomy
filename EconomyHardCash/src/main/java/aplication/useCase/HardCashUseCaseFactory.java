package aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IDepositUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import aplication.HardCashService;
import domain.entity.platform.HardCashCreator;

public class HardCashUseCaseFactory {
    private static HardCashCreator hardCashCreator;
    private static IDepositUseCase depositUseCase;
    private static IWithdrawUseCase withdrawUseCase;
    private static SearchCurrencyUseCase searchCurrencyUseCase;

    public static void init(HardCashCreator hardCashCreator,IDepositUseCase depositUseCase,IWithdrawUseCase withdrawUseCase,SearchCurrencyUseCase searchCurrencyUseCase) {
        HardCashUseCaseFactory.hardCashCreator = hardCashCreator;
        HardCashUseCaseFactory.depositUseCase =depositUseCase;
        HardCashUseCaseFactory.withdrawUseCase = withdrawUseCase;
        HardCashUseCaseFactory.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    public static IDepositItemUseCase getDepositItemUseCase() {
        if(HardCashService.isEnabled()){
            return new DepositItemUseCase(hardCashCreator, depositUseCase,searchCurrencyUseCase);
        }
        return new DepositItemUseCaseDisable();
    }

    public static IExtractItemUseCase getExtractItemUseCase() {
        if (HardCashService.isEnabled()) {
            return new ExtractItemUseCase( hardCashCreator, withdrawUseCase,searchCurrencyUseCase);
        }
        return new ExtractItemUseCaseDisable();
    }

    public static IGiveItemUseCase getGiveItemUseCase() {
        if (HardCashService.isEnabled()) {
            return new GiveItemUseCase(searchCurrencyUseCase,hardCashCreator);
        }
        return new GiveItemUseCaseDisable();
    }
}
