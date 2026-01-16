package domain.service;

import aplication.HardCashService;
import domain.entity.platform.HardCashCreator;

public class ItemCreatorFactory {
    public static ItemCreator getItemCreator(HardCashCreator platform) {
        if (HardCashService.isEnableCustomHead()){
            return new ItemWithTexture(platform);
        }
        return new ItemWithoutTexture(platform);
    }
}
