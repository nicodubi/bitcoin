package nicolasdubiansky.bitcoin.web_services.retrofit;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import nicolasdubiansky.bitcoin.events.CreateAddressEvent;
import nicolasdubiansky.bitcoin.events.CreateAddressEventSuccess;
import nicolasdubiansky.bitcoin.events.ErrorResponseEvent;
import nicolasdubiansky.bitcoin.events.GetBalanceEvent;
import nicolasdubiansky.bitcoin.events.GetBalanceEventSuccess;
import nicolasdubiansky.bitcoin.events.GetFullBalanceEvent;
import nicolasdubiansky.bitcoin.events.GetFullBalanceEventSuccess;
import nicolasdubiansky.bitcoin.events.SendBitcoinsEvent;
import nicolasdubiansky.bitcoin.events.SendBitcoinsEventSuccess;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Balance;
import nicolasdubiansky.bitcoin.web_services.rest_entities.FullBalance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nicolas on 25/09/2017.
 */

public class RetroiftServiceExecutor {

    private static final int UNAUTHORIZED_CODE = 401;
    private static RetroiftServiceExecutor instance;
    private RetrofitInstance retrofitInstance;

    private RetroiftServiceExecutor() {
        retrofitInstance = new RetrofitInstance();
        EventBus.getDefault().register(this);
    }

    public static RetroiftServiceExecutor getInstance() {
        if (instance == null) {
            instance = new RetroiftServiceExecutor();
        }
        return instance;
    }


    @Subscribe
    public void createBitcoinAddress(final CreateAddressEvent event) {
        Call<Address> call = retrofitInstance.getService().createBitcoinAddress();
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.body() != null) {
                    EventBus.getDefault().post(new CreateAddressEventSuccess(response.body()));
                    //sendResponseFromApiResponse(response, new CreateAddressEventSuccess(response.body()), null);
                } else {
                    verificateToken(response.code(), event, null);
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                sendDefaultError();
            }
        });
    }

    @Subscribe
    public void getBalance(final GetBalanceEvent event) {
        Call<Balance> call = retrofitInstance.getService().getBalance(event.getAddress());
        call.enqueue(new Callback<Balance>() {
            @Override
            public void onResponse(Call<Balance> call, Response<Balance> response) {
                if (response.body() != null) {
                    //TODO sendResponseFromApiResponse(response, new GetBalanceEventSuccess(response.body()), null);
                } else {
                    verificateToken(response.code(), event, null);
                }
            }

            @Override
            public void onFailure(Call<Balance> call, Throwable t) {
                sendDefaultError();
            }
        });
    }

    @Subscribe
    public void getFullBalance(final GetFullBalanceEvent event) {
        Call<FullBalance> call = retrofitInstance.getService().getFullBalance(event.getAddress(), event.getHeight());
        call.enqueue(new Callback<FullBalance>() {
            @Override
            public void onResponse(Call<FullBalance> call, Response<FullBalance> response) {
                if (response.body() != null) {
                    //TODO  sendResponseFromApiResponse(response, new GetFullBalanceEventSuccess(response.body()), null);
                } else {
                    verificateToken(response.code(), event, null);
                }
            }

            @Override
            public void onFailure(Call<FullBalance> call, Throwable t) {
                sendDefaultError();
            }
        });
    }


    @Subscribe
    public void sendBitcoinsToAddress(final SendBitcoinsEvent event) {
        Call<String> call = retrofitInstance.getService().sendBitcoinsToAddress(event.getUrlToSendMoney(), event.getAddress());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    //TODO sendResponseFromApiResponse(response, new SendBitcoinsEventSuccess(response.body()), null);
                } else {
                    verificateToken(response.code(), event, null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendDefaultError();
            }
        });
    }

    /*

    private <T> void sendResponseFromApiResponse(Response<ApiResponse<T>> response, Object eventSuccess, String msgError) {
        ApiResponse<T> responseBody = response.body();
        if (response.isSuccessful() && responseBody.getResponse() != null) {
            if (responseBody.getResponse() != null) {
                EventBus.getDefault().post(eventSuccess);
            } else {
                //TODO ACA HABRA QUE PASARLE EL ATRIBUTO DE LA RESPONSE msgError CUANDO VENGA POR POSTMAN por ahora se pasa por parametro y se pregunta si es null, despues sacar parametro y el if else
                if (msgError != null) {
                    EventBus.getDefault().post(new ErrorResponseEvent(msgError));
                } else {
                    EventBus.getDefault().post(new ErrorResponseEvent());
                }
                // EventBus.getDefault().post(new ErrorResponseEvent(responseBody.getMsgError()));
            }
        } else {
            sendDefaultError();
        }
    }
*/

    private void sendDefaultError() {
        EventBus.getDefault().post(new ErrorResponseEvent());

    }

    private void sendDefaultError(String errorMsg) {
        EventBus.getDefault().post(new ErrorResponseEvent(errorMsg));

    }

    private void sendResponse(Response response, Object eventSuccess) {
        if (response.isSuccessful() && response.body() != null) {
            EventBus.getDefault().post(eventSuccess);
        } else {
            //TODO ACA HABRA QUE PASARLE EL ATRIBUTO DE LA RESPONSE msgError CUANDO VENGA POR POSTMAN
            sendDefaultError();
            // EventBus.getDefault().post(new ErrorResponseEvent(responseBody.getMsgError()));
        }
    }

    private void sendResponse(Response response, Object eventSuccess, String msgError) {
        if (response.isSuccessful() && response.body() != null) {
            EventBus.getDefault().post(eventSuccess);
        } else {
            if (msgError != null) {
                EventBus.getDefault().post(new ErrorResponseEvent(msgError));
            } else {
                sendDefaultError();
            }
        }
    }


    //TODO in case we have a expired token or a null token for an OS KILL we have to get a new one.
    //TODO for this challenge code it´s enough getting the saved address.
    /*
    @Subscribe
    public void refreshTokenRequest(TokenRefreshEventRequest event) {
        Call<UserLoginResponse> call = retrofitInstance.getService().login(event.getRequest());
        call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.body() != null) {
                    sendResponse(response, new TokenRefreshEventSuccess(response.body()));
                } else {
                    EventBus.getDefault().post(new TokenRefreshEventError());
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                EventBus.getDefault().post(new TokenRefreshEventError());
            }
        });
    }
*/


    private void verificateToken(int code, final Object event, Object errorEvent) {
       /* if (code == UNAUTHORIZED_CODE) {
            Call<UserLoginResponse> call = retrofitInstance.getService().login(User.getInstance().getCredentials());
            call.enqueue(new Callback<UserLoginResponse>() {
                @Override
                public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        User.getInstance().setUserLoginResponse(response.body());
                        EventBus.getDefault().post(event);
                    } else {
                        EventBus.getDefault().post(new TokenRefreshEventError());
                    }
                }

                @Override
                public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                    sendDefaultError();
                }
            });
        } else if (errorEvent == null) {
            sendDefaultError();
        } else {
            EventBus.getDefault().post(errorEvent);
        }
    */
    }
}
