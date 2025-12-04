package lib.abstractions;

import java.util.Map;

public interface IConfiguration {
    void saveButtonConfig(int slot, boolean value);
    Map<Integer, Boolean> getButtonsConfig();
}
