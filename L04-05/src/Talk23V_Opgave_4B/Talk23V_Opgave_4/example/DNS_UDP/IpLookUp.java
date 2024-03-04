package Talk23V_Opgave_4B.Talk23V_Opgave_4.example.DNS_UDP;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class IpLookUp {
    private  Map<String,String> ipMap =  new HashMap();
    public IpLookUp(){
        ipMap.put("nikolaj","10.10.130.80");
        ipMap.put("mark","10.10.131.122");

    }

    public String ipLookUp(String hostName){
        String hn = hostName.toLowerCase().trim();
        System.out.println(hn);
        if(ipMap.containsKey(hn)){
            return ipMap.get(hn);
        }else{
        throw  new NoSuchElementException("Det host name eksister ikke");
        }
    }

    public void upDateIPCache(String hostName,String ipAdresse){
       String hn = hostName.toLowerCase();
        if(!ipMap.containsKey(hn)){
            ipMap.put(hn,ipAdresse);
        }else {
            ipMap.keySet().add(ipAdresse);
        }
    }
}
