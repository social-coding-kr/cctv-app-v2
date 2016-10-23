package com.socialcoding.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yoon on 2016. 10. 23..
 */
public class RawTxtToString {

    public String convert(InputStream inputStream) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        // cut last character
        String resultWithNewLineChar = stringBuilder.toString();
        String result = resultWithNewLineChar;
        if(resultWithNewLineChar.length() != 0)
            result = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();

        return result;

    }

}