package com.test.commentdialog.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.commentdialog.CommentApplication;
import com.test.commentdialog.R;

/*** @author ganhuanhui
 * Time:2019/12/11 0011
 * Description:https://blog.csdn.net/qq_32518491/article/details/85000421
 */
public class InputTextMsgDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText messageTextView;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private TextView tvNumber;
    private int maxNumber = 100;

    public interface OnTextSendListener {

        void onTextSend(String msg);

        void dismiss();
    }

    private OnTextSendListener mOnTextSendListener;

    public InputTextMsgDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        init();
        setLayout();
    }

    /**
     * Maximum input word count default 100
     */
    @SuppressLint("SetTextI18n")
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        tvNumber.setText("0/" + maxNumber);
    }

    /**
     * Set input prompt text
     */
    public void setHint(String text) {
        messageTextView.setHint(text);
    }

    private void init() {
        setContentView(R.layout.dialog_input_text_msg);
        messageTextView = (EditText) findViewById(R.id.et_input_message);
        tvNumber = (TextView) findViewById(R.id.tv_test);
        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        messageTextView.requestFocus();
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvNumber.setText(editable.length() + "/" + maxNumber);
            }
        });

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        ImageView iv_confirm = findViewById(R.id.iv_confirm);
        iv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (msg.length() > maxNumber) {
                    Toast.makeText (mContext, "exceeds maximum word limit" + maxNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg);
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    InputTextMsgDialog.this.dismiss();
                } else {
                    Toast.makeText(CommentApplication.getApplication(), "please enter text", Toast.LENGTH_LONG).show();
//                Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > maxNumber) {
                            Toast.makeText (mContext, "exceeds maximum word limit", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (messageTextView.getText().length() > 0) {
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            InputTextMsgDialog.this.dismiss();
                        } else {
                            Toast.makeText (mContext, "please enter text", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        InputTextMsgDialog.this.dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        rlDlg = findViewById(R.id.rl_outside_view);
        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view)
                    InputTextMsgDialog.this.dismiss();
            }
        });

        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                // Get the current visual part of the interface
                InputTextMsgDialog.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                // Get the height of the screen
                int screenHeight = InputTextMsgDialog.this.getWindow().getDecorView().getRootView().getHeight();
                //Here is used to get the height of the keyboard, when the keyboard does not pop up this height is 0 when the keyboard pops up for a positive number
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    InputTextMsgDialog.this.dismiss();
                }
                mLastDiff = heightDifference;
            }
        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                InputTextMsgDialog.this.dismiss();
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0)
                    InputTextMsgDialog.this.dismiss();
                return false;
            }
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // dismiss before resetting the mLastDiff value to avoid not opening next time
        mLastDiff = 0;
        if (mOnTextSendListener!=null) mOnTextSendListener.dismiss();

    }

    @Override
    public void show() {
        super.show();
    }
}
