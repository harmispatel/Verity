package com.certified.verityscanningOne.Chat.utils.configs;

import com.certified.verityscanningOne.Chat.models.SampleConfigs;
import com.google.gson.Gson;
import com.quickblox.sample.core.utils.configs.ConfigParser;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;

import java.io.IOException;



public class ConfigUtils extends CoreConfigUtils {

    public static SampleConfigs getSampleConfigs(String fileName) throws IOException {
        ConfigParser configParser = new ConfigParser();
        Gson gson = new Gson();
        return gson.fromJson(configParser.getConfigsAsJsonString(fileName), SampleConfigs.class);
    }
}
