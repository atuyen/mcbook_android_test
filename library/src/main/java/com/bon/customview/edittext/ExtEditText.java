package com.bon.customview.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.bon.customview.edittext.validators.AlphaNumericValidator;
import com.bon.customview.edittext.validators.AlphaValidator;
import com.bon.customview.edittext.validators.CreditCardValidator;
import com.bon.customview.edittext.validators.DomainValidator;
import com.bon.customview.edittext.validators.EmailValidator;
import com.bon.customview.edittext.validators.IpValidator;
import com.bon.customview.edittext.validators.NumericValidator;
import com.bon.customview.edittext.validators.OrValidator;
import com.bon.customview.edittext.validators.PhoneValidator;
import com.bon.customview.edittext.validators.RegExpValidator;
import com.bon.customview.edittext.validators.Validator;
import com.bon.customview.edittext.validators.WebUrlValidator;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.bon.util.TypefacesUtils;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class ExtEditText extends EditText {
    private static final String TAG = ExtEditText.class.getSimpleName();

    public static final int TEST_REGEXP = 0x0001;
    public static final int TEST_NUMERIC = 0x0002;
    public static final int TEST_ALPHA = 0x0004;
    public static final int TEST_ALPHANUMERIC = 0x0008;
    public static final int TEST_EMAIL = 0x0010;
    public static final int TEST_CREDITCARD = 0x0020;
    public static final int TEST_PHONE = 0x0040;
    public static final int TEST_DOMAINNAME = 0x0080;
    public static final int TEST_IPADDRESS = 0x0100;
    public static final int TEST_WEBURL = 0x0200;
    public static final int TEST_ALL = 0x07ff;
    public static final int TEST_NOCHECK = 0;

    /**
     * @var Validators heap
     */
    protected boolean isEmpty = false;
    protected String errorString = null;
    protected String emptyErrorString = null;
    protected ArrayList<Validator> validators = new ArrayList<>();

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public ExtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initControl(context, attrs);
    }

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExtEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initControl(context, attrs);
    }

    /**
     * Init control params
     *
     * @param context
     * @param attrs
     */
    protected void initControl(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtEditText);

            // valid
            this.errorString = typedArray.getString(R.styleable.ExtEditText_errorString);
            this.emptyErrorString = typedArray.getString(R.styleable.ExtEditText_emptyErrorString);
            this.isEmpty = typedArray.getBoolean(R.styleable.ExtEditText_isEmpty, false);

            // empty string
            if (StringUtils.isEmpty(this.emptyErrorString)) {
                this.emptyErrorString = getResources().getString(R.string.error_field_must_not_be_empty);
            }

            // valid date
            addValidator(typedArray.getInt(R.styleable.ExtEditText_validate, TEST_REGEXP | TEST_NOCHECK),
                    this.errorString, typedArray.getString(R.styleable.ExtEditText_validatorRegexp));

            // font
            String fontPath = typedArray.getString(R.styleable.ExtEditText_editTextFontAssetName);
            if (StringUtils.isEmpty(fontPath)) fontPath = TypefacesUtils.FONT_DEFAULT;
            this.setTypeface(TypefacesUtils.get(getContext(), fontPath));
            this.setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            typedArray.recycle();
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * Add validator by code
     *
     * @param code
     * @param error
     * @param regExp
     * @return
     */
    public boolean addValidator(int code, String error, String regExp) {
        if (0 == (code | TEST_ALL))
            return false;

        OrValidator v = new OrValidator();
        if ((0 == code || (code & TEST_REGEXP) != 0) && null != regExp) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_regexp_not_valid);
            }
            v.addValidator(new RegExpValidator(regExp, error));
        }

        if ((code & TEST_NUMERIC) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_this_field_cannot_contain_special_character);
            }
            v.addValidator(new NumericValidator(error));
        }

        if ((code & TEST_ALPHA) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_only_standard_letters_are_allowed);
            }
            v.addValidator(new AlphaValidator(error));
        }

        if ((code & TEST_ALPHANUMERIC) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_this_field_cannot_contain_special_character);
            }
            v.addValidator(new AlphaNumericValidator(error));
        }

        if ((code & TEST_EMAIL) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_email_address_not_valid);
            }
            v.addValidator(new EmailValidator(error));
        }

        if ((code & TEST_CREDITCARD) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_creditcard_number_not_valid);
            }
            v.addValidator(new CreditCardValidator(error));
        }

        if ((code & TEST_PHONE) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_phone_not_valid);
            }
            v.addValidator(new PhoneValidator(error));
        }

        if ((code & TEST_DOMAINNAME) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_domain_not_valid);
            }
            v.addValidator(new DomainValidator(error));
        }

        if ((code & TEST_IPADDRESS) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_ip_not_valid);
            }
            v.addValidator(new IpValidator(error));
        }

        if ((code & TEST_WEBURL) != 0) {
            if (StringUtils.isEmpty(error)) {
                error = getResources().getString(R.string.error_url_not_valid);
            }
            v.addValidator(new WebUrlValidator(error));
        }

        if (0 < v.getCount()) {
            validators.add(v);
        }

        return true;
    }

    /**
     * Add validator object
     *
     * @param v
     */
    public void addValidator(Validator v) {
        if (null != v) {
            validators.add(v);
        }
    }

    /**
     * Check validate
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isEmpty(getText().toString())) {
            if (this.isEmpty) {
                return true;
            } else {
                setError(this.emptyErrorString);
                requestFocus();
                return false;
            }
        }

        boolean b = false;
        if (null != this.validators) {
            for (Validator v : this.validators) {
                try {
                    b = v.check(this);
                } catch (Exception e) {
                    b = false;
                }

                if (!b) {
                    String error = v.getErrorMessage(this);
                    if (StringUtils.isEmpty(error)) {
                        error = this.errorString;
                    }

                    if (!StringUtils.isEmpty(error)) {
                        setError(error);
                        requestFocus();
                    }

                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @param empty
     * @return
     */
    public ExtEditText setEmpty(boolean empty) {
        this.isEmpty = empty;
        return this;
    }

    /**
     * @param emptyErrorString
     * @return
     */
    public ExtEditText setEmptyErrorString(String emptyErrorString) {
        this.emptyErrorString = emptyErrorString;
        return this;
    }

    /**
     * @param errorString
     * @return
     */
    public ExtEditText setErrorString(String errorString) {
        this.errorString = errorString;
        return this;
    }
}
