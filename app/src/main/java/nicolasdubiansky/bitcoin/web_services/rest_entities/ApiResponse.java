package nicolasdubiansky.bitcoin.web_services.rest_entities;

/**
 * Created by Nicolas on 17/5/2017.
 */

//TODO if in the future response structure of api request change, use this response template. For this coding challenge is not necessary
public class ApiResponse<T> {

    private T response;
    private String status;
    private Integer count;

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T data) {
        this.response = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
