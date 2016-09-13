package news.demosisto.demosistonewspro.Utils;

import android.support.annotation.VisibleForTesting;

import java.io.UnsupportedEncodingException;

/**
 * Created by len on 9/12/16.
 */
public class StringUtils {
    public String cutString(String string, String startKey, String endKey){
        int tempInt = string.toString().indexOf(startKey);
        String temp1 = string.toString().substring(tempInt + startKey.length());
        int tempInt1 = temp1.toString().indexOf(endKey);
        String result = temp1.substring(0, tempInt1);
        return result;
    }

    public String cutQuotationMark(String string) {
        string = string.replace('\"', ' ').trim();
        return string;
    }
    public String deleteString(String string,String startKey,String endKey){
        String deleteString = cutString(string,startKey,endKey);
        int i1 = string.indexOf(deleteString);
        String result;
        if (i1 == -1){
            return string;
        }else{
            return result =
                    string.substring(0,i1+1)
                    + string.substring(i1+deleteString.length());

        }
    }
    private String JsonFinder(String jsonString,String jsonObjId){
        String startKey = "\"" + jsonObjId + "\":";
        String endKey = ",\"";
        String result = jsonString;
        jsonString = deleteString(jsonString,"\"author\":{\"","},\"");
        int tempInt = jsonString.toString().indexOf(startKey);
        if(tempInt == -1) {
        result = jsonString;
        }else {
            String temp1 = jsonString.toString().substring(tempInt + startKey.length());
            int tempInt1 = temp1.toString().indexOf(endKey);
            if(tempInt1 != -1) {

                result = temp1.substring(0, tempInt1);
            }else{}
        }
        if(result.substring(0,1) == "\'" && result.substring(result.length() - 1,1) == "\"") {
        result = result.substring(1,result.length()-1);
            return result;
        }else {
            return result;
        }

    }

    public String jsonFinder(String jsonString,String jsonObjId){
        String result = cutQuotationMark(JsonFinder(jsonString,jsonObjId));
        return result;
    }


}
