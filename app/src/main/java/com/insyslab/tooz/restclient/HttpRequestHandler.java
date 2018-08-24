package com.insyslab.tooz.restclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.utils.ToozApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.insyslab.tooz.utils.ConstantClass.API_KEY;

class HttpRequestHandler {

    private static final String TAG = HttpRequestHandler.class.getSimpleName() + " ==>";

    private static ConnectivityManager mCM;

    static void makeJsonObjectRequest(JSONObject jsonObject, String url, final String token,
                                      Integer method, final SuccessListener listener,
                                      final Type responseType, Context context) {
        if (hasInternetAccess(context)) {
//            Log.d(TAG, "Make JSON Object Request -");
//            Log.d(TAG, "Token: " + token);
//            Log.d(TAG, "Request URL: " + url);
//            Log.d(TAG, "Request Object: " + getObjectInStringFormat(jsonObject));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    method,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "Response: " + getObjectInStringFormat(response));
                            listener.onSuccess(response, null, null, responseType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int errorCode = -1;
                            String errorMsg = "Error!";

                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                errorCode = response.statusCode;
                                if (errorCode == 500) {
                                    errorMsg = "Error #500: Server Error!";
                                } else if (errorCode == 503) {
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        errorMsg = "NoConnectionError / TimeoutError!";
                                    } else if (error instanceof AuthFailureError) {
                                        errorMsg = "AuthFailureError!";
                                    } else if (error instanceof ServerError) {
                                        errorMsg = "ServerError!";
                                    } else if (error instanceof NetworkError) {
                                        errorMsg = "NetworkError!";
                                    } else if (error instanceof ParseError) {
                                        errorMsg = "ParseError!";
                                    }
                                } else {
                                    errorMsg = getErrorMessage(new String(response.data));
                                }
                            } else if (error instanceof NoConnectionError) {
                                errorMsg = "Error: No Internet Connection!";
                            }

                            Error customError = new Error(errorMsg, errorCode);
                            listener.onError(customError);
                        }
                    }) {
                public java.util.Map<String, String> getHeaders()
                        throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("token", token);
                    headers.put("apikey", API_KEY);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            ToozApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
        } else {
            Error customError = new Error("No internet connection!", 0);
            listener.onError(customError);
        }
    }

    static void makeJsonArrayRequest(String url, final String token, final SuccessListener listener,
                                     final Type responseType, Context context) {
        if (hasInternetAccess(context)) {
//            Log.d(TAG, "Make JSON Array Request -");
//            Log.d(TAG, "Token: " + token);
//            Log.d(TAG, "Request URL: " + url);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "Response: " + getObjectInStringFormat(response));
                            listener.onSuccess(null, response, null, responseType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int errorCode = -1;
                            String errorMessage = "Error!";

                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                errorCode = response.statusCode;
                                if (errorCode == 500) {
                                    errorMessage = "Error #500: Server Error!";
                                } else if (errorCode == 503) {
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        errorMessage = "NoConnectionError / TimeoutError!";
                                    } else if (error instanceof AuthFailureError) {
                                        errorMessage = "AuthFailureError!";
                                    } else if (error instanceof ServerError) {
                                        errorMessage = "ServerError!";
                                    } else if (error instanceof NetworkError) {
                                        errorMessage = "NetworkError!";
                                    } else if (error instanceof ParseError) {
                                        errorMessage = "ParseError!";
                                    }
                                } else {
                                    errorMessage = getErrorMessage(new String(response.data));
                                }
                            } else if (error instanceof NoConnectionError) {
                                errorMessage = "Error: No Internet Connection!";
                            }

                            Error customError = new Error(errorMessage, errorCode);
                            listener.onError(customError);
                        }
                    }) {
                public java.util.Map<String, String> getHeaders()
                        throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("token", token);
                    headers.put("apikey", API_KEY);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            ToozApplication.getInstance().addToRequestQueue(jsonArrayRequest);
        } else {
            Error customError = new Error("No internet connection!", 0);
            listener.onError(customError);
        }
    }

    static void makeCustomRequest(JSONObject jObject, String url, final String token, Integer method,
                                  final SuccessListener listener, final Type responseType, Context context) {
        if (hasInternetAccess(context)) {
//            Log.d(TAG, "Make Custom Request -");
//            Log.d(TAG, "Token: " + token);
//            Log.d(TAG, "Request Object: " + getObjectInStringFormat(jObject));
//            Log.d(TAG, "Request URL: " + url);

            CustomRequest customRequest = new CustomRequest(
                    method,
                    url,
                    jObject,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray responseArray) {
                            Log.d(TAG, "Response: " + getObjectInStringFormat(responseArray));
                            listener.onSuccess(null, responseArray, null, responseType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int errorCode = -1;
                            String errorMessage = "Unknown Error!";

                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                errorCode = response.statusCode;
                                if (errorCode == 500) {
                                    errorMessage = "Error #500: Server Error!";
                                } else if (errorCode == 503) {
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        errorMessage = "NoConnectionError / TimeoutError!";
                                    } else if (error instanceof AuthFailureError) {
                                        errorMessage = "AuthFailureError!";
                                    } else if (error instanceof ServerError) {
                                        errorMessage = "ServerError!";
                                    } else if (error instanceof NetworkError) {
                                        errorMessage = "NetworkError!";
                                    } else if (error instanceof ParseError) {
                                        errorMessage = "ParseError!";
                                    }
                                } else {
                                    errorMessage = getErrorMessage(new String(response.data));
                                }
                            } else if (error instanceof NoConnectionError) {
                                errorMessage = "Error: No Internet Connection!";
                            }

                            Error customError = new Error(errorMessage, errorCode);
                            listener.onError(customError);
                        }
                    }) {
                public java.util.Map<String, String> getHeaders()
                        throws com.android.volley.AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("token", token);
                    headers.put("apikey", API_KEY);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            customRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            ToozApplication.getInstance().addToRequestQueue(customRequest);
        } else {
            Error customError = new Error("No internet connection!", 0);
            listener.onError(customError);
        }
    }

//    static void makeStringRequest(String url, final String token, int method, final SuccessListener listener,
//                                  Context context) {
//        if (hasInternetAccess(context)) {
////            Log.d(TAG, "Make String Request -");
////            Log.d(TAG, "Token: " + token);
////            Log.d(TAG, "Request URL: " + url);
//
//            StringRequest stringRequest = new StringRequest(
//                    method,
//                    url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
////                            Log.d(TAG, "Response(class java.lang.String): " + response + "");
//                            listener.onSuccess(null, null, response, String.class);
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            int errorCode = -1;
//                            String errorMessage = "Error!";
//
//                            NetworkResponse response = error.networkResponse;
//                            if (response != null && response.data != null) {
//                                errorCode = response.statusCode;
//                                if (errorCode == 500) {
//                                    errorMessage = "Error #500: Server Error!";
//                                } else if (errorCode == 503) {
//                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                        errorMessage = "NoConnectionError / TimeoutError!";
//                                    } else if (error instanceof AuthFailureError) {
//                                        errorMessage = "AuthFailureError!";
//                                    } else if (error instanceof ServerError) {
//                                        errorMessage = "ServerError!";
//                                    } else if (error instanceof NetworkError) {
//                                        errorMessage = "NetworkError!";
//                                    } else if (error instanceof ParseError) {
//                                        errorMessage = "ParseError!";
//                                    }
//                                } else {
//                                    errorMessage = getErrorMessage(new String(response.data));
//                                }
//                            } else if (error instanceof NoConnectionError) {
//                                errorMessage = "Error: No Internet Connection!";
//                            }
//
//                            Error customError = new Error(errorMessage, errorCode);
//                            listener.onError(customError);
//                        }
//                    }) {
//                public java.util.Map<String, String> getHeaders()
//                        throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<>();
//                    headers.put("token", token);
//                    headers.put("apikey", API_KEY);
//                    return headers;
//                }
//            };
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    15000,
//                    10,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            ));
//            ToozApplication.getInstance().addToRequestQueue(stringRequest, TAG);
//        } else {
//            Error customError = new Error("No internet connection!", 0);
//            listener.onError(customError);
//        }
//    }

    static void makeMultipartRequest(String url, final String token, int method, final SuccessListener listener,
                                     final Map<String, VolleyMultipartRequest.DataPart> partMap,
                                     final Map<String, String> paramsMap, final Type responseType,
                                     Context context, final Boolean isStringResponse) {
        if (hasInternetAccess(context)) {
//            Log.d(TAG, "Make Multipart Request -");
//            Log.d(TAG, "Token: " + token);
//            Log.d(TAG, "Request URL: " + url);
//            Log.d(TAG, "Request Map: " + partMap.toString());
//            Log.d(TAG, "Request Fields: " + paramsMap.toString());

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                    method,
                    url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d(TAG, "Response: " + Arrays.toString(response.data) + " -- " + new String(response.data));
                            if (!isStringResponse)
                                listener.onSuccess(null, null, response.statusCode + "", responseType);
                            else
                                listener.onSuccess(null, null, new String(response.data) + "", responseType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int errorCode = -1;
                            String errorMessage = "Unknown error!";

                            NetworkResponse networkResponse = error.networkResponse;

                            if (networkResponse == null) {
                                if (error.getClass().equals(TimeoutError.class)) {
                                    errorMessage = "Request timeout";
                                } else if (error.getClass().equals(NoConnectionError.class)) {
                                    errorMessage = "Failed to connect server";
                                }
                            } else {
                                errorCode = networkResponse.statusCode;
                                String result = new String(networkResponse.data);
                                try {
                                    JSONObject response = new JSONObject(result);
                                    String status = response.getString("status");
                                    String message = response.getString("message");
//                                    Log.e(TAG, "Error Status: " + status);
//                                    Log.e(TAG, "Error Message: " + message);

                                    if (errorCode == 404) {
                                        errorMessage = "Resource not found";
                                    } else if (errorCode == 401) {
                                        errorMessage = message + "Authentication Error!";
                                    } else if (errorCode == 400) {
                                        errorMessage = message + "Invalid Input!";
                                    } else if (errorCode == 500) {
                                        errorMessage = message + "Server Error!";
                                    } else if (error instanceof NoConnectionError) {
                                        errorMessage = "Error: No Internet Connection!";
                                    } else {
                                        errorMessage = getErrorMessage(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Error customError = new Error(errorMessage, errorCode);
                            listener.onError(customError);
                        }
                    }) {

                public java.util.Map<String, String> getHeaders()
                        throws com.android.volley.AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("token", token);
                    headers.put("apikey", API_KEY);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    return paramsMap;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    return partMap;
                }
            };
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            ToozApplication.getInstance().addToRequestQueue(multipartRequest);
        } else {
            Error customError = new Error("No internet connection!", 0);
            listener.onError(customError);
        }
    }


    /**
     * Util Methods
     */

    private static String getErrorMessage(String jsonStr) {
        ErrorFromServer error = new Gson().fromJson(jsonStr, ErrorFromServer.class);
        return error != null ? error.getMessage() : "Error!";

    }

    private static String getObjectInStringFormat(JSONObject jObject) {
        try {
            if (jObject != null) return jObject.toString(2);
            else return "null!";
        } catch (JSONException e) {
            e.printStackTrace();
            return "JSONException!";
        }
    }

    private static String getObjectInStringFormat(JSONArray jArray) {
        try {
            return jArray.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return "JSONException!";
        }
    }

    private static boolean hasInternetAccess(Context context) {
        if (mCM == null)
            mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (mCM != null) netInfo = mCM.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class ErrorFromServer {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Integer status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }
}
