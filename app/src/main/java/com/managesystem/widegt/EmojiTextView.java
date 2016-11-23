package com.managesystem.widegt;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.managesystem.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 自动转换表情的TextView
 * Author: zhuxiaohong
 * Date: 2016/4/12 15:05
 */
public class EmojiTextView extends TextView {

    private Context context;
    private int mEmojiconSize = 60;
    private int replayLength = 0;

    public EmojiTextView(Context context) {
        super(context);
        init(null,context);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        this.context = context;
        if (attrs == null) {
            mEmojiconSize = (int) getTextSize();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon,0,R.style.styleEmojicon);
            mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
            a.recycle();
        }
        setText(getText());
    }

    public void setText(String text, int replayLength){
        this.replayLength = replayLength;
        setText(text, BufferType.NORMAL);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        SpannableStringBuilder builder=new SpannableStringBuilder();
        SpannableString content = parseString(builder, String.valueOf(text));
        super.setText(content,type);
    }

    /**
     * 将表情对应的描述转换为表情显示
     * @param inputStr 输入的内容
     * @return
     */
    private SpannableString parseString(SpannableStringBuilder spb, String inputStr){
        Pattern mPattern= Pattern.compile("\\[.{1,3}\\]");
        Matcher mMatcher=mPattern.matcher(inputStr);
        String tempStr=inputStr;

        while(mMatcher.find()){
            int start=mMatcher.start();
            int end=mMatcher.end();
            spb.append(tempStr.substring(0,start));
            String faceName=mMatcher.group();
            setFace(spb, faceName);
            tempStr=tempStr.substring(end, tempStr.length());
            /**
             * 更新查找的字符串
             */
            mMatcher.reset(tempStr);
        }
        spb.append(tempStr);
        SpannableString content = new SpannableString(spb);
        if (replayLength > 0){
            content.setSpan(new TextAppearanceSpan(context, R.style.black_fourteen_text), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            content.setSpan(new TextAppearanceSpan(context, R.style.a_child_text), 2, replayLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return content;
    }

    private void setFace(SpannableStringBuilder spb, String faceName) {
        EmojiRuls.getruls(context);
        Integer faceId = EmojiRuls.map.get(faceName);
        if (faceId != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), faceId);
            bitmap = Bitmap.createScaledBitmap(bitmap, mEmojiconSize, mEmojiconSize, true);
            ImageSpan imageSpan = new ImageSpan(context, bitmap);
            SpannableString spanStr = new SpannableString(faceName);
            spanStr.setSpan(imageSpan, 0, faceName.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spb.append(spanStr);
        } else {
            spb.append(faceName);
        }

    }

    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
    }
}
