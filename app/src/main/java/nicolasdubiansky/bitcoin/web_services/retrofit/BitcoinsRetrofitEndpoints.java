package nicolasdubiansky.bitcoin.web_services.retrofit;



import java.util.ArrayList;

import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Balance;
import nicolasdubiansky.bitcoin.web_services.rest_entities.FullBalance;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Nicolas on 25/09/2017.
 */

public interface BitcoinsRetrofitEndpoints {
    public static final String HTTP_PREFIX = "https://";
    public static final String IP = "api.blockcypher.com/v1/btc/test3/";
    public static final String BASE_URL = HTTP_PREFIX + IP;


    @POST("addrs")
    Call<Address> createBitcoinAddress();

    @GET("addrs/{address}/balance")
    Call<Balance> getBalance(@Path("address") String address);


    //can set parameters ?before=300000
    @GET("addrs/{address}/full")
    Call<FullBalance> getFullBalance(@Path("address") String address, @Query("before") Integer height);


    @POST
    /**
     * urlToSendMoney: https://faucet.eordano.com/{address}
     */
    Call<String> sendBitcoinsToAddress(@Url String urlToSendMoney);

}
