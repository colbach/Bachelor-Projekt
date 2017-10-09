package commandline.functions.getterandsetter;

import settings.GeneralSettings;

public class GeneralSettingsGetterSetter extends GetterSetter {

    public final String key;
    public final Object defaultValue;

    public GeneralSettingsGetterSetter(String key, Object defaultValue) {
        if (defaultValue instanceof Boolean || defaultValue instanceof String || defaultValue instanceof Integer) {
            this.key = key;
            this.defaultValue = defaultValue;
        } else {
            throw new IllegalArgumentException("GeneralSettingsGetterSetter ist nur Kompatibel mit Boolean, String oder Integer.");
        }
    }

    @Override
    public String get() {
        GeneralSettings generalSettings = GeneralSettings.getInstance();
        if (defaultValue instanceof Boolean) {
            return String.valueOf(generalSettings.getBoolean(key, (boolean) (Boolean) defaultValue));
        } else if(defaultValue instanceof String) {
            return generalSettings.getString(key, (String) defaultValue);
        } else if(defaultValue instanceof Integer) {
            return String.valueOf(generalSettings.getInt(key, (Integer) defaultValue));
        }
        throw new RuntimeException("GeneralSettingsGetterSetter ist nur Kompatibel mit Boolean, String oder Integer.");
    }

    @Override
    public void set(String value) {
        GeneralSettings generalSettings = GeneralSettings.getInstance();
        if (defaultValue instanceof Boolean) {
            boolean newValue = GetterAndSetterResource.stringToBoolean(value);
            generalSettings.setAndTryToWrite(key, newValue);
        } else if(defaultValue instanceof String) {
            generalSettings.setAndTryToWrite(key, value);
        } else if(defaultValue instanceof Integer) {
            int newValue = Integer.parseInt(key);
            generalSettings.setAndTryToWrite(key, newValue);
        }
        throw new RuntimeException("GeneralSettingsGetterSetter ist nur Kompatibel mit Boolean, String oder Integer.");
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

}
