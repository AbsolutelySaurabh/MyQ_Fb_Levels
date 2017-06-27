package dd.com.myq.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import dd.com.myq.App.Config;
import dd.com.myq.R;
import dd.com.myq.Util.Questions.Question;
import dd.com.myq.Util.Questions.QuestionAdapter;
import dd.com.myq.Util.SessionManager;

public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "flag";
    private static final String ARG_PARAM2 = "levels";
    private static final String ARG_PARAM3 = "categories";

    private  ArrayList<String> al = new ArrayList<String>();
    private ArrayList<String> al_id = new ArrayList<String>();
    private ArrayList<String> al_correctAns = new ArrayList<String>();

    private static int index;

    private  static int flag=0;

    private static int another_flag=0;

    int j = 0;

    ArrayAdapter<String> arrayAdapter;


    private int i;

    List<Question> questions;

    public  SwipeFlingAdapterView flingContainer;

    private LoaderManager.LoaderCallbacks<List<Question>> mCallbacks;

    private  LayoutInflater inflater=null;

    Context mContext;

    public  QuestionAdapter questionAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;


    SessionManager currentSession;

    private static final int QUESTION_LOADER_ID = 1;

    private TextView emptyTextView;

    private static final String LOG_TAG = HomeFragment.class.getName();

    private String QUESTIONS_REQUEST_URL = "http://myish.com:10011/api/questions/";

    private String REPORT_REQUEST_URL = "http://myish.com:10011/api/questions/report";

    private TextView mEmptyStateTextView;

    private OnFragmentInteractionListener mListener;

    View view;

    public HomeFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2, String param3) {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
 }
        currentSession = new SessionManager(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame);

        HashMap<String, String> user_details = currentSession.getUserDetails();

        final String user_id = user_details.get(SessionManager.KEY_UID);
        QUESTIONS_REQUEST_URL = QUESTIONS_REQUEST_URL + user_id;

        //////////////////////////////////////rishabh

        if(flag==0&&mParam2==null&&mParam3==null) {

            CallAPI call = new CallAPI();
            call.execute();
            Log.e("First call flag = 0 ", "FIRST");


        }
        else  if (mParam2 != null && !mParam2.isEmpty())
             {

                 Log.e("second call flag = 0 ", "SECOND");


                 Log.e("mParam2 : ", mParam2);
            String qapi = "http://myish.com:10011/api/questions_levels?";
            if (mParam2.equals("E")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&level=E";
            }
            else if (mParam2.equals("M")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&level=M";
           }
            else if (mParam2.equals("H")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&level=H";
            }
            else {
                QUESTIONS_REQUEST_URL = QUESTIONS_REQUEST_URL + user_id;
                }
                 mParam2=null;

                 if (flag == 0)
            {
                CallAPI call = new CallAPI();
                call.execute();
            }
             }

        else  if (mParam3 != null && !mParam3.isEmpty())
             {

                 Log.e("send2222nd call flag=0 ", "SECOND");

                 Log.e("mParam3 : ", mParam3);

                 String qapi = "http://myish.com:10011/api/questions_category?";
            if (mParam3.equals("PHP")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=PHP";
                }
            else if (mParam3.equals("C")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=C";
            }
            else if (mParam3.equals("HADOOP")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=HADOOP";
            }
//            else if (mParam3.equals("AUTOMATION SYSTEM")) {
//                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=AUTOMATION%20SYSTEM";
//            }
            else if (mParam3.equals("CLOUD COMPUTING")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=CLOUD%20COMPUTING";
            }
            else if (mParam3.equals("ROBOTICS AND AI")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=ROBOTICS%20AND%20AI";
            }

            else if (mParam3.equals("CYBER SECURITY")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=%20CYBER%20SECURITY";
            }
            else if (mParam3.equals("C++")) {
                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=C%2B%2B";
            }

            else {
                QUESTIONS_REQUEST_URL = QUESTIONS_REQUEST_URL + user_id;
            }
                 mParam3=null;

                 if (flag == 0)
            {
                CallAPI call = new CallAPI();
                call.execute();
           }
        }
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.helloText, al);
        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                al.remove(0);
                al_id.remove(0);
                al_correctAns.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                String correctness;

                if (al_correctAns.get(index).equals("NO")) {
            correctness = "Correct";
                }
                else {
                    correctness = "incorrect";
                    }

                Log.d("getTop() : ", String.valueOf(flingContainer.getTop()));
                Log.d(" al_id : ", al_id.get(index));
                Log.d(" Questions : ", al.get(index));
                Log.d(" correctA : ", al_correctAns.get(index));

                AddQuestion(user_id, al_id.get(index), al.get(index), "", al_correctAns.get(index), "2", correctness);
                AddUserToQuestion(user_id, al_id.get(index), al_correctAns.get(index));

                Log.d("al in LeftCard: ", String.valueOf(al.size()));

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                String correctness;

                if (al_correctAns.get(index).equals("YES")) {

                    correctness = "Correct";
//                        Toast.makeText(getActivity(), correctness, Toast.LENGTH_SHORT).show();

                } else {

                    correctness = "incorrect";
//                       Toast.makeText(getActivity(), correctness, Toast.LENGTH_SHORT).show();
                }

//                flingContainer.getTopCardListener().selectRight();

                Log.d(" al_id : ", al_id.get(index));

                Log.d(" Questions : ", al.get(index));

                Log.d(" correctA : ", al_correctAns.get(index));

                AddQuestion(user_id, al_id.get(index), al.get(index), "", al_correctAns.get(index), "2", correctness);
                AddUserToQuestion(user_id, al_id.get(index), al_correctAns.get(index));

                Log.d("al in Right Card : ", String.valueOf(al.size()));

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

                Log.d("al EMpty Adapter 1 : ", String.valueOf(al.size()));

                TextView report = (TextView) view.findViewById(R.id.report_question);
                report.setEnabled(false);


                Button button_left = (Button) view.findViewById(R.id.left);
                button_left.setBackgroundResource(R.drawable.false_btn);
                button_left.setEnabled(false);

                Button button_right = (Button) view.findViewById(R.id.right);
                button_right.setBackgroundResource(R.drawable.true_btn);
                button_right.setEnabled(false);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(flag==10&&mParam2==null&&mParam3==null)
                {

                    Log.e("second 111st0", "1");
                    flingContainer.setAdapter(null);
                    CallAPI call = new CallAPI();
                    call.execute();
                    flag=0;

                }
                else
                {
                    if(flag==10)
                    {
                        if (mParam3 != null && !mParam3.isEmpty()&&mParam2 == null && mParam2.isEmpty())
                        {

                            Log.e("EMPTYY mParam3 : ", mParam3);

                            String qapi = "http://myish.com:10011/api/questions_category?";
                            if (mParam3.equals("PHP")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=PHP";
                            }
                            else if (mParam3.equals("C")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=C";
                            }

                            else if (mParam3.equals("HADOOP")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=HADOOP";
                            }
//                            else if (mParam3.equals("AUTOMATION SYSTEM")) {
//                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=AUTOMATION%20SYSTEM";
//                            }
                            else if (mParam3.equals("CLOUD COMPUTING")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=CLOUD%20COMPUTING";
                            }
                            else if (mParam3.equals("ROBOTICS AND AI")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=ROBOTICS%20AND%20AI";
                            }
                            else if (mParam3.equals("CYBER SECURITY")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=%20CYBER%20SECURITY";
                            }
                            else if (mParam3.equals("C++")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&category=C%2B%2B";
                            }

                            else {
                                QUESTIONS_REQUEST_URL = QUESTIONS_REQUEST_URL + user_id;
                            }
                        }
                    else if (mParam3 == null && mParam3.isEmpty()&&mParam2 != null && !mParam2.isEmpty())
                        {

                            Log.e("EMPTYY  mParam2 : ", mParam2);

                            String qapi = "http://myish.com:10011/api/questions_levels?";
                            if (mParam2.equals("E")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&level=E";
                            }
                            else if (mParam2.equals("M")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&level=M";
                            }
                            else if (mParam2.equals("H")) {
                                QUESTIONS_REQUEST_URL = qapi + "userId=" + user_id + "&level=H";
                            }
                            else {
                                QUESTIONS_REQUEST_URL = QUESTIONS_REQUEST_URL + user_id;
                            }
                        }


                        flingContainer.setAdapter(null);
                        CallAPI call = new CallAPI();
                        call.execute();

                    }

                    }
                Log.d("al EMpty Adapter 2 : ", String.valueOf(al.size()));
            }
            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //makeToast(getA "Clicked!");
            }
        });

        Button button_left = (Button) view.findViewById(R.id.left);
        button_left.setBackgroundResource(R.drawable.false_btn);

        button_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                flingContainer.getTopCardListener().selectLeft();

            }
        });

        Button button_right = (Button) view.findViewById(R.id.right);

        button_right.setBackgroundResource(R.drawable.true_btn);

        button_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                flingContainer.getTopCardListener().selectRight();

            }
        });

        TextView report = (TextView) view.findViewById(R.id.report_question);
        Log.d("TEXTTTT", String.valueOf(report));
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        ReportAPI(user_id, al_id.get(index));
                Toast.makeText(getActivity(), "Question Reported", Toast.LENGTH_SHORT).show();
           }
        });
        ButterKnife.inject(getActivity());
        return view;
    }


    public void ReportAPI(String userid, String questionid){


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userid);
        requestParams.put("qid", questionid);

        client.post(getActivity(),REPORT_REQUEST_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("REPORT Success",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("REPORT Error",errorResponse.toString());
            }

        });

    }


    public void AddQuestion(String userid,String questionid, String questiontext, String questionimage, String questioncorrectanswer, String questionpoints, String answercorrectness){


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userid);
        requestParams.put("questionid", questionid);
        requestParams.put("questiontext", questiontext);
        requestParams.put("questionimage", questionimage);
        requestParams.put("questioncorrectanswer", questioncorrectanswer);
        requestParams.put("questionpoints", questionpoints);
        requestParams.put("answercorrectness", answercorrectness);

        client.post(getActivity(), Config.AddQuestionUrl , requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.e("ResponsePoint Success",response.toString());            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("ResponsePoint Error",errorResponse.toString());
            }

        });
    }

    public void AddUserToQuestion(String uid,String qid, String questioncorrectanswer){

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", uid);
        requestParams.put("qid", qid);
        requestParams.put("answer", questioncorrectanswer);

        client.post(getActivity(), Config.AddQuestionToUser, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("ResponsePoint Error",errorResponse.toString());

            }

        });
    }


    public class CallAPI extends AsyncTask<URL, Integer, List<Question>> {

        public CallAPI(){
            super();

        }

        @Override
        protected List<Question> doInBackground(URL... params) {

            URL url = createUrl(QUESTIONS_REQUEST_URL);

            Log.e("Get Ques Req URL: ", QUESTIONS_REQUEST_URL);

            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            questions = extractFeatureFromJson(jsonResponse);

            return questions;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        private URL  createUrl(String stringUrl){

            URL url = null;
            try{
                url = new URL(stringUrl);

            }catch(MalformedURLException e){

                Log.e(LOG_TAG,"Error with creating URL",e);
            }
            return url;
        }

        private String makeHttpRequest(URL url)throws IOException{

            String jsonResponse = "";

            if(url==null){

                return jsonResponse;
            }
            HttpURLConnection urlConnection =null;
            InputStream inputStream = null;
            try{

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                if(urlConnection.getResponseCode()==200){

                    inputStream = urlConnection.getInputStream();

                    jsonResponse = readFromStream(inputStream);
                    Log.d("Response",""+jsonResponse);
                }else{

                    Log.e(LOG_TAG,"Error Response code: "+urlConnection.getResponseCode());

                }

            }catch(IOException e){
                Log.e(LOG_TAG,"Problem retrieving the news JSON results. ",e);

            }finally{

                if(urlConnection!=null){

                    urlConnection.disconnect();;
                }
                if(inputStream!=null){
                    inputStream.close();
                }
            }
            return jsonResponse;

        }

        private String readFromStream(InputStream inputStream) throws IOException{

            StringBuilder output = new StringBuilder();
            if(inputStream!=null){

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line!=null){
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();

        }

        private List<Question> extractFeatureFromJson(String questionsJSON) {


            if (TextUtils.isEmpty(questionsJSON)) {
                return null;
            }

            // Create an empty ArrayList that we can start adding questions to
            List<Question> questions = new ArrayList<>();

            try {

                JSONArray questionsArray = new JSONArray(questionsJSON);

                for(int i=0;i<questionsArray.length();i++){

                    JSONObject currentNews = questionsArray.getJSONObject(i);

                    String text = currentNews.getString("text");

                    String level = currentNews.getString("level");

                    String id = currentNews.getString("_id");

                    String correctAnswer = currentNews.getString("correctAnswere");
                    // String imageUrl = currentNews.getString("imageUrl");

                    Question newquestion = new Question(text, level, correctAnswer, id);
                    questions.add(newquestion);

                }

            } catch (JSONException e) {

                Log.e("QueryUtils", "Problem parsing the Questions JSON results", e);
            }

            return questions;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {

            Question question;
            String text;
            String id;
            String correctAns;

            mParam2=null;
            mParam3=null;

            if(questions!=null) {

                for (int i = 0; i < questions.size(); i++) {

                    question = questions.get(i);

                    text = question.getText();
                    //level = question.getLevel();
                    id = question.getId();

                    correctAns = question.getCorrectAnswere();

                    al_id.add(id);

                    al.add(text);

                    al_correctAns.add(correctAns);

                    Log.d("Add To List", "al_id: "+id+"  al: "+ text+ "  al_correctAns "+correctAns);

                }

                Log.d(" IDs size : ",String.valueOf(al_id.size()));
                Log.d(" Questions size : ",String.valueOf(al.size()));
                Log.d(" CorrectAns size : ",String.valueOf(al_correctAns.size()));

                flag=10;

            }

            flingContainer.setAdapter(arrayAdapter);

            Button button_left = (Button) view.findViewById(R.id.left);
            button_left.setBackgroundResource(R.drawable.false_btn);
            button_left.setEnabled(true);

            Button button_right = (Button) view.findViewById(R.id.right);
            button_right.setBackgroundResource(R.drawable.true_btn);
            button_right.setEnabled(true);

            TextView report = (TextView) view.findViewById(R.id.report_question);
            report.setEnabled(true);

            arrayAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
   public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}