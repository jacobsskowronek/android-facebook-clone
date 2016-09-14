package com.jacobskowronek.facebookclone;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimelineFragment extends Fragment implements View.OnClickListener{

    private void createPost() {
        String username = ParseUser.getCurrentUser().getUsername();
        final EditText postEditText = (EditText) getView().findViewById(R.id.post);
        final String post = String.valueOf(postEditText.getText());

        // Get date and time with formatting, and get time in milliseconds for proper ordering on timeline
        String currentDateAndTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String currentTimeInMilis = String.valueOf(System.currentTimeMillis());

        ParseObject timeline = new ParseObject("Timeline");
        timeline.put("username", username);
        timeline.put("post", post);
        timeline.put("postedDateAndTime", currentDateAndTime);
        timeline.put("postedTimeInMilis", currentTimeInMilis);

        if (post.equals("")) {
            Toast.makeText(getContext(), "Title can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            timeline.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getContext(), "Post successfully created!", Toast.LENGTH_SHORT).show();
                        hideKeyboard();
                        postEditText.setText("");
                        updateTimeline();
                    } else {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void updateTimeline() {

        if (ParseUser.getCurrentUser() != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Timeline");
            query.addDescendingOrder("postedTimeInMilis");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {
                        if (objects.size() > 0) {
                            List<List<String>> timeline = new ArrayList<>();
                            ListView timelineListView = (ListView) getView().findViewById(R.id.timelineListView);

                            for (ParseObject object : objects) {
                                List<String> timelineComponents = new ArrayList<String>();

                                timelineComponents.add(String.valueOf(object.get("post")));
                                timelineComponents.add(String.valueOf(object.get("postedDateAndTime")));
                                timelineComponents.add(String.valueOf(object.get("username")));

                                Log.i("PostInMili", String.valueOf(object.get("postedTimeInMilis")));

                                timeline.add(timelineComponents);


                            }

                            List<String> formattedTimeline = new ArrayList<String>();
                            for (List<String> timelineItems : timeline) {
                                formattedTimeline.add(timelineItems.get(0) + "\n" + "Posted by: " + timelineItems.get(2) + "\n" + timelineItems.get(1));
                                //formattedTimeline.add(timelineItems.get(2) + " is at " + timelineItems.get(0));
                            }

                            // Update listview
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_timeline_listview, formattedTimeline);
                            timelineListView.setAdapter(arrayAdapter);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        }

    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) getView().findViewById(R.id.postToTimelineButton);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.postToTimelineButton:
                createPost();

        }
    }
}
