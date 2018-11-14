package dyg.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface LoveFamousMp3FileDownload {
    @GET
    Call<ResponseBody> downloadMp3(@Url String url);
}
