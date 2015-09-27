package com.example.shilo90.ringandfind;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shilo90 on 08/06/15.
 */

public class fragmentIntro extends Fragment {

    public TextView txt;
    public View layout;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        layout = inflater.inflate(R.layout.activity_introduction, container, false);
        txt = ((TextView) layout.findViewById(R.id.textIntro));
        txt.setMaxLines(50);
        txt.setMovementMethod(new ScrollingMovementMethod());

        String AllText = "";
        String faq_title = getResources().getString(R.string.faq_title);
        //String faq = getResources().getString(R.string.faq);

        String intro1 = getResources().getString(R.string.intro1);
        String intro22 = getResources().getString(R.string.intro22);
        String remember = getResources().getString(R.string.remember);
        String remember2 = getResources().getString(R.string.remember2);

        String question = getResources().getString(R.string.question);
        String question1 = getResources().getString(R.string.faq_q1);
        String answer = getResources().getString(R.string.answer);
        String answer1 = getResources().getString(R.string.faq_a1);

        String question2 = getResources().getString(R.string.faq_q2);
        String option1 = getResources().getString(R.string.option1);
        String option2 = getResources().getString(R.string.option2);
        String answer2_1 = getResources().getString(R.string.faq_a2_1);
        String answer2_2 = getResources().getString(R.string.faq_a2_2);
        String answer2_3 = getResources().getString(R.string.faq_a2_3);




        AllText = intro1 + "<br>" + "<br>" +
                "<b>" + remember + "</b> " + intro22 + "<br>" + "<br>" +

                "<b>" + faq_title + "<br>" + "<br>" +
                "</b>" + "<b>" +question + "</b> " + question1 +"<br>" +
                "<b>" +answer + "</b> " + answer1 + "<br>" + "<br>" +

                "<b>" +question + "</b> " + question2 +"<br>" +
                "<b>" +answer + "</b> " + answer2_1 + "<br>" +
                "&emsp<b>" +option1 + "</b> " + answer2_2 + "<br>" +
                "&emsp<b>" +option2 + "</b> " + answer2_3 + "<br>" + "<br>";

        txt.setText(Html.fromHtml(AllText));

        return layout;
    }
}
