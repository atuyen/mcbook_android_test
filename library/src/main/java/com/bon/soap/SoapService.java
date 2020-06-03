package com.bon.soap;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.bon.logger.Logger;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dang on 6/2/2016.
 */
public class SoapService {
    private static final String TAG = SoapService.class.getSimpleName();

    // time out
    private static final int DEFAULT_TIMEOUT = 2 * 60 * 1000; // 2 minutes

    // SoapEnvelope version
    private static int soapEnvelopeVersion = SoapEnvelope.VER12;

    private String nameSpace = "";
    private String url = "";
    private String methodName = "";
    private int timeOut = 0;

    private SoapListener soapListener = null;
    private SoapSerializationEnvelope soapEnvelope = null;
    private SoapObject soapObject = null;
    private List<HeaderProperty> headers = null;

    /**
     * @param nameSpace  (http://www.abc.com/API/PublicJSON)
     * @param url        (http://www.abc.com/API/PublicJSON.asmx)
     * @param methodName (InsertAddress)
     */
    public static SoapService getInstance(String nameSpace, String url, String methodName) {
        return new SoapService(nameSpace, url, methodName, DEFAULT_TIMEOUT);
    }

    /**
     * @param nameSpace  (http://www.abc.com/API/PublicJSON)
     * @param url        (http://www.abc.com/API/PublicJSON.asmx)
     * @param methodName (InsertAddress)
     * @param timeOut    (seconds)
     */
    public static SoapService getInstance(String nameSpace, String url, String methodName, int timeOut) {
        return new SoapService(nameSpace, url, methodName, timeOut);
    }

    /**
     * @param nameSpace  (http://www.abc.com/API/PublicJSON)
     * @param url        (http://www.abc.com/API/PublicJSON.asmx)
     * @param methodName (InsertAddress)
     */
    public SoapService(String nameSpace, String url, String methodName) {
        this(nameSpace, url, methodName, DEFAULT_TIMEOUT);
    }

    /**
     * @param nameSpace  (http://www.abc.com/API/PublicJSON)
     * @param url        (http://www.abc.com/API/PublicJSON.asmx)
     * @param methodName (InsertAddress)
     * @param timeOut    (seconds)
     */
    public SoapService(String nameSpace, String url, String methodName,
                       int timeOut) {
        this.nameSpace = nameSpace;
        this.url = url;
        this.methodName = methodName;
        this.timeOut = timeOut;
        // init value SoapEnvelope, SoapObjectEntity
        initSoapEnvelope();
        initSoapObject();
    }

    /**
     * init soap envelope
     */
    private void initSoapEnvelope() {
        try {
            this.soapEnvelope = new SoapSerializationEnvelope(soapEnvelopeVersion);
            new MarshalBase64().register(this.soapEnvelope);   //serialization
            this.soapEnvelope.encodingStyle = SoapEnvelope.ENC;
            this.soapEnvelope.implicitTypes = true;
            this.soapEnvelope.dotNet = true;
            this.soapEnvelope.setAddAdornments(false);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * init soap object
     */
    private void initSoapObject() {
        try {
            this.soapObject = new SoapObject(this.nameSpace, this.methodName);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * string
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, String value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(String.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * int
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Integer value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Integer.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * long
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Long value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Long.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * double
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Double value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Double.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * float
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Float value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Float.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * boolean
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Boolean value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Boolean.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * byte
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Byte value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Byte.class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * byte[]
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Byte[] value) {
        try {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(propertyName);
            propertyInfo.setValue(value);
            propertyInfo.setType(Byte[].class);
            this.soapObject.addProperty(propertyInfo);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * object
     *
     * @param propertyName
     * @param value
     * @return
     */
    public SoapService addPropertySoap(String propertyName, Object value) {
        try {
            this.soapObject.addProperty(propertyName, value);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * @param propertyName (BillingAddress)
     * @param clazz        Address.Class
     * @return
     */
    public SoapService addMappingSoapEnvelope(String propertyName, Class clazz) {
        try {
            this.soapEnvelope.addMapping(this.nameSpace, propertyName, clazz);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * add header
     *
     * @param header
     * @return
     */
    public SoapService addHeader(HeaderProperty header) {
        try {
            if (this.headers == null) {
                this.headers = new ArrayList<>();
            }
            this.headers.add(header);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * add headers
     *
     * @param headers
     */
    public SoapService addHeader(List<HeaderProperty> headers) {
        try {
            this.headers = headers;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * @param soapListener
     * @return
     */
    public SoapService setSoapListener(SoapListener soapListener) {
        try {
            this.soapListener = soapListener;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get soap action
     *
     * @return
     */
    private String getSoapAction() {
        try {
            return this.nameSpace.endsWith("/") ? this.nameSpace + this.methodName : this.nameSpace + "/" + this.methodName;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * execute service
     */
    public void execute() {
        try {
            AsyncTask<Void, Void, Object> asyncTask = new AsyncTask<Void, Void, Object>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (soapListener != null) {
                        soapListener.onStarted();
                    }
                }

                @Override
                protected Object doInBackground(Void... params) {
                    try {
                        Log.i("nameSpace", "nameSpace:: " + nameSpace);
                        Log.i("url", "url:: " + url);
                        Log.i("methodName", "methodName:: " + methodName);
                        Log.i("soapObject", "soapObject:: " + soapObject);

                        soapEnvelope.setOutputSoapObject(soapObject);
                        HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);

                        if (headers != null) {
                            httpTransport.call(getSoapAction(), soapEnvelope, headers);
                        } else {
                            httpTransport.call(getSoapAction(), soapEnvelope);
                        }

                        return soapEnvelope.getResponse();
                    } catch (IOException | XmlPullParserException ex) {
                        Logger.e(TAG, ex);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Object resultObject) {
                    super.onPostExecute(resultObject);
                    try {
                        if (soapListener != null) {
                            soapListener.onFinished();
                        }

                        if (resultObject instanceof SoapFault) {
                            SoapFault soupFault = (SoapFault) resultObject;
                            System.out.println("Fault string:: " + soupFault.faultstring);

                            Exception ex = new Exception(soupFault.faultstring);
                            if (soapListener != null) {
                                soapListener.onFail(ex);
                            }
                        } else if (resultObject instanceof SoapObject) {
                            String result = ((SoapObject) resultObject).getInnerText();
                            System.out.println("Result:: " + result);

                            if (soapListener != null) {
                                soapListener.onSuccess(result);
                            }
                        } else if (resultObject instanceof SoapPrimitive) {
                            String result = ((SoapPrimitive) resultObject).getValue().toString();
                            System.out.println("Result:: " + result);

                            if (soapListener != null) {
                                soapListener.onSuccess(result);
                            }
                        } else {
                            if (soapListener != null) {
                                soapListener.onSuccess(null);
                            }
                        }
                    } catch (Exception ex) {
                        if (soapListener != null) {
                            soapListener.onFail(ex);
                        }
                        Logger.e(TAG, ex);
                    }
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                asyncTask.execute();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public interface SoapListener {
        void onStarted();

        void onSuccess(String result);

        void onFail(Exception ex);

        void onFinished();
    }
}
