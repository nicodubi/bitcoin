package nicolasdubiansky.bitcoin.web_services.retrofit;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nicolas on 25/09/2017.
 */

public class HeaderInterceptor implements Interceptor {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String AUTHOTIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String accessToken = "";
        //accessToken = User.getInstance().getAccessToken();
        if (needAuthorization()) {
            request = request.newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .addHeader(AUTHOTIZATION, BEARER + accessToken).build();
        } else {
            request = request.newBuilder().addHeader(CONTENT_TYPE, APPLICATION_JSON).build();
        }

        Response response = chain.proceed(request);
        return response;
    }

    private boolean needAuthorization() {
        //TODO for this coding challenge is not necessary authorization. If in the future we need it, change return to true
        return false;
    }
}


