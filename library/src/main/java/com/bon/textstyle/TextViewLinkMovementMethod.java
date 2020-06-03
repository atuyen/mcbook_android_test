package com.bon.textstyle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.bon.logger.Logger;

public class TextViewLinkMovementMethod extends LinkMovementMethod {
    private static final String TAG = TextViewLinkMovementMethod.class.getSimpleName();
    private Activity activity = null;

    public TextViewLinkMovementMethod(Activity activity) {
        this.activity = activity;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        try {
            // get action
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();
                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);

                if (link != null && link.length != 0) {
                    String url = link[0].getURL();
                    if (url != null) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }

                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static MovementMethod getInstance(Activity activity) {
        return new TextViewLinkMovementMethod(activity);
    }
}
