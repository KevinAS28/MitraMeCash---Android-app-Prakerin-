package com.kevinas.mitramecash

import java.util.HashMap

class Configuration{
    companion object {
        var lib = Lib();
        var serverIP = "192.168.43.171";
        var apiUrl = "https://webhook.site/da26775f-801a-45ec-b0c9-8b3ef5c42011";
        var serverPort = 80;
        var listFile: HashMap<String, String> = Configuration.lib.mapGenerate(
          arrayOf(
              "login",
              "regis",
              "dat"
              ),
            arrayOf(
                "08c11fef",
                "85nv93f2",
                "2y3b89pss"
            )
        );
        var companyEmail = "cv.mitrasoftglobal@gmail.com";

    }
}