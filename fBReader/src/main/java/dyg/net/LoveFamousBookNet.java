package dyg.net;

import java.util.HashMap;

import dyg.beans.CiBaWordBeanJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface LoveFamousBookNet {
    @GET("/api/dictionary.php")
    Call<CiBaWordBeanJson> getWords(@QueryMap HashMap<String, String> map);

}
